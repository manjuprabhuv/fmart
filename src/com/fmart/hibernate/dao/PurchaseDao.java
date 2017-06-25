package com.fmart.hibernate.dao;

// Generated Apr 13, 2015 10:25:23 PM by Hibernate Tools 3.4.0.CR1

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.fmart.hibernate.pojos.Expense;
import com.fmart.hibernate.pojos.Inventory;
import com.fmart.hibernate.pojos.InventoryId;
import com.fmart.hibernate.pojos.Notifications;
import com.fmart.hibernate.pojos.Product;
import com.fmart.hibernate.pojos.ProductGrp;
import com.fmart.hibernate.pojos.Purchase;
import com.fmart.hibernate.pojos.PurchaseDetail;
import com.fmart.hibernate.pojos.Sale;
import com.fmart.ui.beans.UserSession;
import com.fmart.ui.utils.SessionInfo;

/**
 * Dao object for domain model class Purchase.
 * 
 * @see com.fmart.hibernate.pojos.Purchase
 * @author Hibernate Tools
 */

public class PurchaseDao {

	private static final Logger log = LoggerFactory
			.getLogger(PurchaseDao.class);

	private Session getSession() {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		return session;
	}

	public void save(Purchase transientInstance) {
		log.debug("persisting Purchase instance");
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

	public void update(Purchase transientInstance, Integer id) {
		log.debug("persisting Purchase instance");
		Transaction tx = null;
		Session session = HibernateUtil.getSession();
		try {
			tx = session.beginTransaction();
			String fromVal = NotificationUtils.getJsonVal(findById(id));
			session.update(transientInstance);
			String toVal = NotificationUtils.getJsonVal(findById(id));
			String username = UserSession.getSession().getUsername();
			NotificationUtils.saveNotification(fromVal, toVal, Purchase.class,
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

	public Purchase findById(int id) {
		log.debug("getting Purchase instance with id: " + id);
		Session session = HibernateUtil.getSession();
		try {
			Purchase instance = (Purchase) session.get(Purchase.class, id);
			log.debug("get successful");
			initializePurchaseIbject(instance);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public List<Purchase> findAll() {
		log.debug("finding all Object instances");
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from " + Purchase.class.getCanonicalName();
			Query queryObject = session.createQuery(queryString);
			List<Purchase> genericList = queryObject.list();
			return genericList;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public List<Purchase> findByProperty(String propertyName, Object value) {
		log.debug("finding Object instance with property: " + propertyName
				+ ", value: " + value);
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from " + Purchase.class.getCanonicalName()
					+ " as model where model." + propertyName + "= ?";
			Query queryObject = session.createQuery(queryString);
			queryObject.setParameter(0, value);
			List<Purchase> genericList = queryObject.list();
			initializeObjects(genericList);
			return genericList;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		} finally {
			session.close();
		}
	}
	
	public List<Purchase> findByProperty(String company, Object companyId,String created_by,Object employeeId,
			int first, int pageSize) {
		log.debug("finding Object instance with property: " + company
				+ ", value: " + companyId+" property:"+created_by+", value: "+employeeId);
		Session session = HibernateUtil.getSession();
		try {
			String queryString = "from " + Purchase.class.getCanonicalName()
					+ " as model where model." + company
					+ "= ? order by created desc";
			if(employeeId!=null)
				queryString = "from " + Purchase.class.getCanonicalName()
					+ " as model where model." + company
					+ "= ? and "+created_by+"=? order by created desc";
			
			Query queryObject = session.createQuery(queryString);
			queryObject.setFirstResult(first);
			queryObject.setMaxResults(pageSize);
			queryObject.setParameter(0, companyId);
			if(employeeId!=null)
				queryObject.setParameter(1, employeeId);
			List<Purchase> genericList = queryObject.list();
			initializeObjects(genericList);
			return genericList;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		} finally {
			session.close();
		}
	}
	
	public int countByProperty(int company_id) {
		log.debug("counting number of records for Purchase with company id:",company_id);
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null;
		String sql = null;
		ResultSet rs = null;
		int records=0;
		try {
			stmt=conn.createStatement();
			sql = "SELECT count(*) count FROM fmart.purchase WHERE company_id="
					+ company_id;
			rs = stmt.executeQuery(sql);
			if(rs.next())
				records= rs.getInt("count");
		} catch (SQLException re) {
			log.error("record count failed for Purchase", re);
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
	
	public <T> List<Purchase> findByCriteria(Map<String, String> properties) {
		Session session = HibernateUtil.getSession();
		try {
			ProjectionList projections = Projections.projectionList();
			for (String property : properties.keySet()) {
				String attribute = properties.get(property);
				projections.add(Projections.property(property), attribute);
			}
			Criteria cr = session
					.createCriteria(Purchase.class)
					.setProjection(projections)
					.setResultTransformer(
							Transformers.aliasToBean(Purchase.class));

			List<Purchase> list = cr.list();

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

	public <T> List<T> findAllNameContains(Class<T> klazz, String query,
			String suffix) {
		log.debug("finding all Object instances");
		Session session = getSession();
		try {
			String className = klazz.getCanonicalName();
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
			initializeObjects(genericList);
			return genericList;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		} finally {
			session.close();
		}
	}
	
	public String savePurchase(Purchase purchase, List<Inventory> inventories,
			String page) throws Exception {

		log.debug("saving Object instance");
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null;
		PreparedStatement pstmt = null;
		String sql = null;
		Session session = getSession();
		ResultSet rs = null;
		Transaction tx = null;
		String result = null;
		String originalVal = null;
		String actualVal = null;
		SessionInfo sessionInfo = UserSession.getSession();
		try {
			tx = session.beginTransaction();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			if ("purchase_update".equalsIgnoreCase(page)) {
				sql = "DELETE FROM fmart.purchase_detail WHERE purchase_id="
						+ purchase.getId() + " and returned=false";
				stmt.execute(sql);
			}
			if ("purchase_update".equalsIgnoreCase(page)
					|| "purchase_return".equalsIgnoreCase(page)) {
				originalVal = getOriginalVal(purchase.getId());
			}

			for (Inventory inventory : inventories) {
				InventoryId inId = inventory.getId();
				sql = "SELECT product_id, company_id, customised, quantity FROM fmart.inventory WHERE product_id="
						+ inId.getProductId()
						+ " and company_id="
						+ inId.getCompanyId()
						+ " and customised="
						+ inId.isCustomised();
				rs = stmt.executeQuery(sql);
				if (rs.next()) {
					if (rs.getInt("quantity") + inventory.getQuantity() < 0)
						throw new SQLException("In-Sufficient Inventory");
					sql = "UPDATE fmart.inventory SET quantity=quantity+"
							+ inventory.getQuantity() + " WHERE product_id="
							+ inId.getProductId() + " and company_id="
							+ inId.getCompanyId() + " and customised="
							+ inId.isCustomised();
					stmt.execute(sql);
				} else {
					sql = "INSERT INTO fmart.inventory(product_id, company_id, customised, quantity) VALUES ("
							+ inId.getProductId()
							+ ","
							+ inId.getCompanyId()
							+ ",'"
							+ inId.isCustomised()
							+ "',"
							+ inventory.getQuantity() + ")";
					stmt.execute(sql);
				}
			}
			if ("purchase_update".equalsIgnoreCase(page)
					|| "purchase_return".equalsIgnoreCase(page)) {
				sql = "UPDATE fmart.purchase SET dealer_id=?, amount=?, updated_by=?, company_id=?, status=?,created=?,updated=? WHERE id=?";
				if ("purchase_return".equalsIgnoreCase(page))
					sql = "UPDATE fmart.purchase SET dealer_id=?, amount=amount-?, updated_by=?, company_id=?, status=?, created=?,updated=? WHERE id=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, purchase.getDealer().getId());
				pstmt.setBigDecimal(2, purchase.getAmount());
				pstmt.setInt(3, sessionInfo.getEmployeeId());
				pstmt.setInt(4, purchase.getCompany().getId());
				pstmt.setString(5, purchase.getStatus());
				pstmt.setDate(6,
						new java.sql.Date(purchase.getCreated().getTime()));				
				pstmt.setTimestamp(7, new java.sql.Timestamp(new Date().getTime()));
				pstmt.setInt(8, purchase.getId());
				pstmt.execute();

				for (PurchaseDetail pd : purchase.getPurchaseDetails()) {
					if (pd.getQuantity() > 0) {
						sql = "INSERT INTO fmart.purchase_detail(purchase_id, product_id, quantity, amount, remarks, returned,customised,rate,created,created_by,updated_by,updated) VALUES (?, ?, ?, ?, ?, ?,?,?,?,?,?,?)";
						pstmt = conn.prepareStatement(sql);
						pstmt.setInt(1, purchase.getId());
						pstmt.setInt(2, pd.getProduct().getId());
						pstmt.setInt(3, pd.getQuantity());
						pstmt.setBigDecimal(4, pd.getAmount());
						pstmt.setString(5, pd.getRemarks());
						pstmt.setBoolean(6, pd.getReturned());
						pstmt.setBoolean(7, pd.getCustomised());
						pstmt.setBigDecimal(8, pd.getRate());
						pstmt.setTimestamp(9, new java.sql.Timestamp(new Date().getTime()));
						pstmt.setInt(10, sessionInfo.getEmployeeId());
						pstmt.setInt(11, sessionInfo.getEmployeeId());
						pstmt.setTimestamp(12, new java.sql.Timestamp(new Date().getTime()));
						pstmt.execute();

						if ("purchase_return".equalsIgnoreCase(page)) {
							sql = "UPDATE fmart.purchase_detail SET quantity=quantity-?,remarks=?,rate=?, amount=amount-?, updated_by=?,updated=? WHERE id=?";
							pstmt = conn.prepareStatement(sql);
							pstmt.setInt(1, pd.getQuantity());
							pstmt.setString(2, pd.getRemarks());
							pstmt.setBigDecimal(3, pd.getRate());
							pstmt.setBigDecimal(4, pd.getAmount());						
							pstmt.setInt(5, sessionInfo.getEmployeeId());
							pstmt.setTimestamp(6, new java.sql.Timestamp(new Date().getTime()));
							pstmt.setInt(7, pd.getId());
							pstmt.execute();
						}
					}
				}

				// Saving Notification
				actualVal = toObjectString(purchase);
				String username = UserSession.getSession().getUsername();
				Notifications notification = new Notifications(originalVal,
						actualVal, page, username, "update");
				session.save(notification);

			} else {
				session.save(purchase);
			}
			conn.commit();
			tx.commit();
			log.debug("Purchase Save Successful");
			if ("purchase_entry".equalsIgnoreCase(page))
				result = "Purchase Entry : Done Successfully";
			else if ("purchase_update".equalsIgnoreCase(page))
				result = "Purchase Update : Done Successfully";
			else
				result = "Purchase Return : Done Successfully";
		} catch (RuntimeException re) {
			log.error("Purchase Save Failed", re);
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

	private <T> void initializeIbject(T t) {
		if (t instanceof Product) {
			Product p = (Product) t;
			Hibernate.initialize(p.getProductGrp());
		} else if (t instanceof Expense) {
			Expense ex = (Expense) t;
			Hibernate.initialize(ex.getEmployeeByCreatedBy());
			Hibernate.initialize(ex.getEmployeeByEmployeeId());
			Hibernate.initialize(ex.getCompany());
			Hibernate.initialize(ex.getExpenseType());
		} else if (t instanceof Purchase) {
			Purchase pur = (Purchase) t;
			Hibernate.initialize(pur.getCompany());
			Hibernate.initialize(pur.getDealer());
			Hibernate.initialize(pur.getEmployeeByCreatedBy());
			Hibernate.initialize(pur.getEmployeeByUpdatedBy());
			/*Hibernate.initialize(pur.getPurchaseDetails());
			for (PurchaseDetail details : pur.getPurchaseDetails()) {
				Hibernate.initialize(details.getProduct());
				if (details.getProduct() != null) {
					Hibernate.initialize(details.getProduct().getProductGrp());
				}
				Hibernate.initialize(details.getPurchase());
			}*/

		} else if (t instanceof PurchaseDetail) {
			PurchaseDetail pd = (PurchaseDetail) t;
			Hibernate.initialize(pd.getProduct());
			if (pd.getProduct() != null) {
				Hibernate.initialize(pd.getProduct().getProductGrp());
			}
			Hibernate.initialize(pd.getPurchase());
		} else if (t instanceof ProductGrp) {
			ProductGrp pg = (ProductGrp) t;
		}
	}
	
	private <T> void initializePurchaseIbject(T t) {
		if (t instanceof Purchase) {
			Purchase pur = (Purchase) t;
			Hibernate.initialize(pur.getCompany());
			Hibernate.initialize(pur.getDealer());
			Hibernate.initialize(pur.getEmployeeByCreatedBy());
			Hibernate.initialize(pur.getEmployeeByUpdatedBy());
			Hibernate.initialize(pur.getPurchaseDetails());
			for (PurchaseDetail details : pur.getPurchaseDetails()) {
				Hibernate.initialize(details.getProduct());
				if (details.getProduct() != null) {
					Hibernate.initialize(details.getProduct().getProductGrp());
				}
				Hibernate.initialize(details.getPurchase());
			}

		} 
	}

	private <T> void initializeObjects(List<T> genericList) {
		for (T t : genericList) {
			initializeIbject(t);
		}

	}
}
