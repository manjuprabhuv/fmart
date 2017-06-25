package com.fmart.hibernate.dao;

// Generated Apr 13, 2015 10:25:23 PM by Hibernate Tools 3.4.0.CR1

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.internal.SessionImpl;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fmart.framework.hibernate.HibernateUtil;
import com.fmart.hibernate.dao.NotificationUtils.NotificationType;
import com.fmart.hibernate.pojos.Company;
import com.fmart.hibernate.pojos.Expense;
import com.fmart.hibernate.pojos.Expense;
import com.fmart.hibernate.pojos.ExpenseType;
import com.fmart.hibernate.pojos.Notifications;
import com.fmart.hibernate.pojos.Receipt;
import com.fmart.hibernate.pojos.Sale;
import com.fmart.ui.beans.UserSession;

/**
 * Dao object for domain model class Expense.
 * 
 * @see com.fmart.hibernate.pojos.Expense
 * @author Hibernate Tools
 */

public class ExpenseDao {

	private static final Logger log = LoggerFactory.getLogger(ExpenseDao.class);
	
	private Session getSession() {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		return session;
	}

	public void save(Expense transientInstance) {
		log.debug("persisting Expense instance");
		Transaction tx = null;
		Session session = HibernateUtil.getSession();
		try {
			tx = session.beginTransaction();
			session.save(transientInstance);
			tx.commit();
			log.debug("persist successful");
		} catch (RuntimeException re) {
			tx.rollback();
			log.error("persist failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public void update(Expense transientInstance, Integer id) {
		log.debug("persisting Expense instance");
		Transaction tx = null;
		Session session = HibernateUtil.getSession();
		try {
			tx = session.beginTransaction();
			String fromVal = NotificationUtils.getJsonVal(findById(id));
			session.update(transientInstance);
			String toVal = NotificationUtils.getJsonVal(findById(id));
			String username = UserSession.getSession().getUsername();
			NotificationUtils.saveNotification(fromVal, toVal, Expense.class,
					username, NotificationType.UPDATE);
			tx.commit();
			log.debug("persist successful");
		} catch (RuntimeException re) {
			tx.rollback();
			log.error("persist failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public Expense findById(int id) {
		log.debug("getting Expense instance with id: " + id);
		Session session = HibernateUtil.getSession();
		try {
			Expense instance = (Expense) session.get(Expense.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public List<Expense> findAll() {
		log.debug("finding all Object instances");
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from " + Expense.class.getCanonicalName();
			Query queryObject = session.createQuery(queryString);
			List<Expense> list = queryObject.list();
			initializeExpenseList(list);
			return list;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		} finally {
			session.close();
		}
	}
	
	public List<Expense> findAll(int first, int pageSize) {
		log.debug("finding all Object instances");
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from " + Expense.class.getCanonicalName()
					+ " order by created desc";
			Query queryObject = session.createQuery(queryString);
			queryObject.setFirstResult(first);
			queryObject.setMaxResults(pageSize);
			List<Expense> genericList = queryObject.list();
			initializeExpenseList(genericList);
			return genericList;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		} finally {
			session.close();
		}
	}
	
	public int countByProperty(int company_id) {
		log.debug("counting number of records for Expense with company id:",company_id);
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null;
		String sql = null;
		ResultSet rs = null;
		int records=0;
		try {
			stmt=conn.createStatement();
			sql = "SELECT count(*) count FROM fmart.expense WHERE company_id="
					+ company_id;
			rs = stmt.executeQuery(sql);
			if(rs.next())
				records= rs.getInt("count");
		} catch (SQLException re) {
			log.error("record count failed for Sale", re);
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return records;
	}
	
	public List<Expense> findByProperty(String propertyName, Object value) {
		log.debug("finding Object instance with property: " + propertyName
				+ ", value: " + value);
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from " + Expense.class.getCanonicalName()
					+ " as model where model." + propertyName + "= ?";
			Query queryObject = session.createQuery(queryString);
			queryObject.setParameter(0, value);
			List<Expense> genericList = queryObject.list();

			return genericList;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public <T> List<Expense> findByCriteria(Map<String, String> properties) {
		Session session = HibernateUtil.getSession();
		try {
			ProjectionList projections = Projections.projectionList();
			for (String property : properties.keySet()) {
				String attribute = properties.get(property);
				projections.add(Projections.property(property), attribute);
			}
			Criteria cr = session
					.createCriteria(Expense.class)
					.setProjection(projections)
					.setResultTransformer(
							Transformers.aliasToBean(Expense.class));

			List<Expense> list = cr.list();

			return list;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}

	}

	public void executeHQLUpdate(String hql, Map<String, Object> params) {
		Session session = HibernateUtil.getSession();
		Query query = session.createQuery(hql);
		for (String param : params.keySet()) {
			query.setParameter(param, params.get(param));
		}
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			session.close();
		}

	}
	
	public <T> List<T> findAllNameContains(String query,
			String suffix) {
		log.debug("finding all Object instances");
		Session session = getSession();
		try {
			String className = ExpenseType.class.getCanonicalName();
			String queries[] = query.split(" ");
			boolean first = true;
			String name = "name";
			if (className.contains("Sale"))
				name = "customer_name";
			String queryString = "from " + className + " where ";
			if (suffix != null)
				queryString += suffix;
			for (String q : queries) {
				if (first && suffix == null) {
					first = false;
					queryString += " upper(" + name + ") like upper('%" + q
							+ "%')";
				}
				queryString += " and upper(" + name + ") like upper('%" + q
						+ "%')";
			}
			Query queryObject = session.createQuery(queryString);
			List<T> genericList = queryObject.list();
			return genericList;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		} finally {
			session.close();
		}
	}
	
	public String saveExpense(Expense expense, Company company)
			throws Exception {
		log.debug("saving Object instance");
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null;
		String sql = null;
		Session session = getSession();
		ResultSet rs = null;
		Transaction tx = null;
		String result = null;
		int cashInHand = 0;
		try {
			tx = session.beginTransaction();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();

			sql = "SELECT cash_in_hand FROM fmart.company WHERE id="
					+ company.getId() + "";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				cashInHand = rs.getInt("cash_in_hand");
				if (cashInHand < expense.getAmount()) {
					throw new Exception("Cash in Hand is less than required");
				}
			}
			sql = "UPDATE fmart.company  SET cash_in_hand = cash_in_hand -"
					+ expense.getAmount() + " where id=" + company.getId() + "";
			stmt.executeUpdate(sql);
			session.save(expense);
			conn.commit();
			tx.commit();
			result = "Expense Entry : Done Sucessfully!";
		} catch (RuntimeException re) {
			log.error("Expense Save Failed", re);
			tx.rollback();
			throw re;
		} catch (SQLException e) {
			try {
				conn.rollback();
				throw e;
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw e;
			}
		} catch (Exception e) {
			tx.rollback();
			throw e;
		} finally {
			try {
				session.close();
				if (conn != null)
					conn.close();
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;

			}
		}
		return result;
	}

	
	public String updateExpense(Expense expense, Company company,
			Integer oldAmount) throws Exception {
		log.debug("Update Object instance");
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null;
		String sql = null;
		Session session = getSession();
		ResultSet rs = null;
		Transaction tx = null;
		String result = null;
		int cashInHand = 0;
		try {
			tx = session.beginTransaction();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();

			sql = "SELECT cash_in_hand FROM fmart.company WHERE id="
					+ company.getId() + "";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				cashInHand = rs.getInt("cash_in_hand");
			}
			if (cashInHand + oldAmount - expense.getAmount() >= 0) {
				sql = "UPDATE fmart.company  SET cash_in_hand = cash_in_hand +"
						+ oldAmount + " - " + expense.getAmount()
						+ " where id=" + company.getId() + "";
				stmt.executeUpdate(sql);
			} else {
				throw new Exception("Cash in Hand is less than required");
			}
			String originalVal = getOriginalVal(expense.getId());
			session.update(expense);
			String actualVal = toObjectString(expense);
			String username = UserSession.getSession().getUsername();
			Notifications notification = new Notifications(originalVal,
					actualVal, "Expense", username, "update");
			session.save(notification);

			conn.commit();
			tx.commit();
			result = "Expense update : Done Sucessfully!";
		} catch (RuntimeException re) {
			log.error(" Failed", re);
			tx.rollback();
			throw re;
		} catch (SQLException e) {
			try {
				conn.rollback();
				result = "SQL Exception";
				throw e;
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw e;
			}
		} catch (Exception e) {
			tx.rollback();
			throw e;
		} finally {
			try {
				session.close();
				if (conn != null)
					conn.close();
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;

			}
		}
		return result;
	}
	
	private String toObjectString(Object obj) {
		try {
			ObjectWriter ow = new ObjectMapper().writer();
			String retString = ow.writeValueAsString(obj);
			retString = retString.replaceAll("  ", "");
			return retString;
			// System.out.println(test);
		} catch (JsonGenerationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return "";
	}
	
	private <T> String getOriginalVal(java.lang.Integer id) {
		T instance = (T) findById(id);
		return toObjectString(instance);
	}
	
	public void initializeExpenseList(List<Expense> list){		
		for (Expense  expense: list) {
			initializeExpense(expense);
		}
	}
	
	public void initializeExpense(Expense ex){		
		Hibernate.initialize(ex.getEmployeeByCreatedBy());
		Hibernate.initialize(ex.getEmployeeByEmployeeId());
		Hibernate.initialize(ex.getCompany());
		Hibernate.initialize(ex.getExpenseType());
	}
}
