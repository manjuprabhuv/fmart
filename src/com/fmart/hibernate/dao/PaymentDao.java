package com.fmart.hibernate.dao;

// Generated Apr 13, 2015 10:25:23 PM by Hibernate Tools 3.4.0.CR1

import java.io.IOException;
import java.math.BigDecimal;
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
import com.fmart.hibernate.pojos.Dealer;
import com.fmart.hibernate.pojos.EmpReport;
import com.fmart.hibernate.pojos.Employee;
import com.fmart.hibernate.pojos.Notifications;
import com.fmart.hibernate.pojos.Payment;
import com.fmart.hibernate.pojos.Payment;
import com.fmart.ui.beans.UserSession;

/**
 * Dao object for domain model class Payment.
 * 
 * @see com.fmart.hibernate.pojos.Payment
 * @author Hibernate Tools
 */

public class PaymentDao {

	private static final Logger log = LoggerFactory.getLogger(PaymentDao.class);
	
	private Session getSession() {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		return session;
	}

	public void save(Payment transientInstance) {
		log.debug("persisting Payment instance");
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

	public void update(Payment transientInstance, Integer id) {
		log.debug("persisting Payment instance");
		Transaction tx = null;
		Session session = HibernateUtil.getSession();
		try {
			tx = session.beginTransaction();
			String fromVal = NotificationUtils.getJsonVal(findById(id));
			session.update(transientInstance);
			String toVal = NotificationUtils.getJsonVal(findById(id));
			String username = UserSession.getSession().getUsername();
			NotificationUtils.saveNotification(fromVal, toVal, Payment.class,
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

	public Payment findById(int id) {
		log.debug("getting Payment instance with id: " + id);
		Session session = HibernateUtil.getSession();

		try {
			Payment instance = (Payment) session.get(Payment.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public List<Payment> findAll() {
		log.debug("finding all Object instances");
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from " + Payment.class.getCanonicalName();
			Query queryObject = session.createQuery(queryString);
			List<Payment> list = queryObject.list();
			initializePaymentList(list);
			return list;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public List<Payment> findByProperty(String propertyName, Object value, int first, int pageSize) {
		log.debug("finding Object instance with property: " + propertyName
				+ ", value: " + value);
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from " + Payment.class.getCanonicalName()
					+ " as model where model." + propertyName + "= ? order by created desc";
			Query queryObject = session.createQuery(queryString);
			queryObject.setParameter(0, value);
			queryObject.setFirstResult(first);
			queryObject.setMaxResults(pageSize);
			List<Payment> list = queryObject.list();
			initializePaymentList(list);
			return list;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		} finally {
			session.close();
		}
	}
	
	public int countByProperty(int company_id) {
		log.debug("counting number of records for Payment with company id:",company_id);
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null;
		String sql = null;
		ResultSet rs = null;
		int records=0;
		try {
			stmt=conn.createStatement();
			sql = "SELECT count(*) count FROM fmart.payment WHERE company_id="
					+ company_id;
			rs = stmt.executeQuery(sql);
			if(rs.next())
				records= rs.getInt("count");
		} catch (SQLException re) {
			log.error("record count failed for Payment", re);
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

	public <T> List<Payment> findByCriteria(Map<String, String> properties) {
		Session session = HibernateUtil.getSession();
		try {
			ProjectionList projections = Projections.projectionList();
			for (String property : properties.keySet()) {
				String attribute = properties.get(property);
				projections.add(Projections.property(property), attribute);
			}
			Criteria cr = session
					.createCriteria(Payment.class)
					.setProjection(projections)
					.setResultTransformer(
							Transformers.aliasToBean(Payment.class));

			List<Payment> list = cr.list();

			return list;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}

	}
	
	public List<Dealer> findAllNameContains(String query,
			String suffix) {
		log.debug("finding all Object instances");
		Session session = getSession();
		try {
			String className = Dealer.class.getCanonicalName();
			String queries[] = query.split(" ");
			boolean first = true;
			String name = "name";
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
			List<Dealer> list = queryObject.list();
			return list;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		} finally {
			session.close();
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
	
	public String savePayment(Payment payment) throws Exception {
		log.debug("saving Object instance");
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null;
		String sql = null;
		Session session = getSession();
		ResultSet rs = null;
		Transaction tx = null;
		String result = null;
		double cashInHand = 0;
		try {
			tx = session.beginTransaction();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();

			sql = "SELECT cash_in_hand FROM fmart.company WHERE id="
					+ payment.getCompany().getId() + "";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				cashInHand = rs.getInt("cash_in_hand");
				if (cashInHand < (payment.getAmount().doubleValue())) {
					throw new Exception("Cash in Hand is less than required");
				}
			}
			sql = "UPDATE fmart.company  SET cash_in_hand = cash_in_hand -"
					+ payment.getAmount() + " where id="
					+ payment.getCompany().getId() + "";
			stmt.executeUpdate(sql);
			session.save(payment);
			conn.commit();
			tx.commit();
			result = "Payment Entry : Done Sucessfully!";
		} catch (RuntimeException re) {
			log.error("Payment Save Failed", re);
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

	public String updatePayment(Payment payment, BigDecimal oldAmount)
			throws Exception {
		log.debug("saving Object instance");
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null;
		String sql = null;
		Session session = getSession();
		ResultSet rs = null;
		Transaction tx = null;
		String result = null;
		double cashInHand = 0;
		try {
			tx = session.beginTransaction();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();

			sql = "SELECT cash_in_hand FROM fmart.company WHERE id="
					+ payment.getCompany().getId() + "";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				cashInHand = rs.getInt("cash_in_hand");
				if (cashInHand < (payment.getAmount().subtract(oldAmount))
						.doubleValue()) {
					throw new Exception("Cash in Hand is less than required");
				}
			}
			sql = "UPDATE fmart.company  SET cash_in_hand = cash_in_hand -("
					+ (payment.getAmount().subtract(oldAmount)).doubleValue()
					+ ") where id=" + payment.getCompany().getId() + "";
			stmt.executeUpdate(sql);

			String originalVal = getOriginalVal(payment.getId());
			session.update(payment);
			String actualVal = toObjectString(payment);
			String username = UserSession.getSession().getUsername();
			Notifications notification = new Notifications(originalVal,
					actualVal, "Payment", username, "update");

			session.save(notification);
			conn.commit();
			tx.commit();
			result = "Payment Entry : Done Sucessfully!";
		} catch (RuntimeException re) {
			log.error("Payment Save Failed", re);
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
	
	public BigDecimal getPendingAmountForDealer(Integer dealer_id,
			Integer company_id) {
		BigDecimal totalPurchase = BigDecimal.ZERO;
		BigDecimal totalPayment = BigDecimal.ZERO;
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null;
		String sql = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			sql = "SELECT sum(amount) amt from fmart.purchase where dealer_id="
					+ dealer_id + " and company_id=" + company_id;
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				totalPurchase = rs.getBigDecimal("amt");
				if (totalPurchase == null)
					totalPurchase = BigDecimal.ZERO;
				rs.close();
			}
			sql = "SELECT sum(amount) amt from fmart.payment where dealer_id="
					+ dealer_id + " and company_id=" + company_id;
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				totalPayment = rs.getBigDecimal("amt");
				if (totalPayment == null)
					totalPayment = BigDecimal.ZERO;
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
		return totalPurchase.subtract(totalPayment);
	}
	
	public void initializePaymentList(List<Payment> list){		
		for (Payment payment : list) {
			initializePayment(payment);
		}
	}
	
	public void initializePayment(Payment p){		
		Hibernate.initialize(p.getEmployeeByCreatedBy());
		Hibernate.initialize(p.getDealer());
		Hibernate.initialize(p.getCompany());
	}
	
	private <T> String getOriginalVal(java.lang.Integer id) {
		T instance = (T) findById(id);
		return toObjectString(instance);
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
}
