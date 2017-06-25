package com.fmart.hibernate.dao;

// Generated Apr 13, 2015 10:25:23 PM by Hibernate Tools 3.4.0.CR1

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
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
import com.fmart.hibernate.pojos.EmpReport;
import com.fmart.hibernate.pojos.Employee;
import com.fmart.hibernate.pojos.Expense;
import com.fmart.hibernate.pojos.Notifications;
import com.fmart.hibernate.pojos.Payment;
import com.fmart.hibernate.pojos.Product;
import com.fmart.hibernate.pojos.ProductGrp;
import com.fmart.hibernate.pojos.Purchase;
import com.fmart.hibernate.pojos.PurchaseDetail;
import com.fmart.hibernate.pojos.Receipt;
import com.fmart.hibernate.pojos.Receipt;
import com.fmart.hibernate.pojos.Sale;
import com.fmart.hibernate.pojos.SaleDetail;
import com.fmart.ui.beans.UserSession;

/**
 * Dao object for domain model class Receipt.
 * 
 * @see com.fmart.hibernate.pojos.Receipt
 * @author Hibernate Tools
 */

public class ReceiptDao {

	private static final Logger log = LoggerFactory.getLogger(ReceiptDao.class);

	private Session getSession() {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		return session;
	}

	public void save(Receipt transientInstance) {
		log.debug("persisting Receipt instance");
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

	public void update(Receipt transientInstance, Integer id) {
		log.debug("persisting Receipt instance");
		Transaction tx = null;
		Session session = HibernateUtil.getSession();
		try {
			tx = session.beginTransaction();
			String fromVal = NotificationUtils.getJsonVal(findById(id,
					Receipt.class));
			session.update(transientInstance);
			String toVal = NotificationUtils.getJsonVal(findById(id,
					Receipt.class));
			String username = UserSession.getSession().getUsername();
			NotificationUtils.saveNotification(fromVal, toVal, Receipt.class,
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

	/*
	 * public Receipt findById(int id) {
	 * log.debug("getting Receipt instance with id: " + id); Session session =
	 * HibernateUtil.getSession();
	 * 
	 * try { Receipt instance = (Receipt) session.get(Receipt.class, id);
	 * log.debug("get successful"); return instance; } catch (RuntimeException
	 * re) { log.error("get failed", re); throw re; } finally { session.close();
	 * } }
	 * 
	 * public Sale findById(java.lang.Integer id) {
	 * log.debug("getting Object instance with id: " + id); Session session =
	 * getSession(); try { String className = Sale.class.getCanonicalName();
	 * Sale sale = (Sale) session.get(className, id); initializeSale(sale);
	 * return sale; } catch (RuntimeException re) { log.error("get failed", re);
	 * throw re; } finally { session.close(); } }
	 */

	public <T> T findById(java.lang.Integer id, Class<T> persistentInstance) {
		log.debug("getting Object instance with id: " + id);
		Session session = getSession();
		try {
			String className = persistentInstance.getCanonicalName();
			T instance = (T) session.get(className, id);
			initializeObject(instance);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public List<Receipt> findAll() {
		log.debug("finding all Object instances");
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from " + Receipt.class.getCanonicalName();
			Query queryObject = session.createQuery(queryString);
			List<Receipt> genericList = queryObject.list();
			return genericList;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public List<Receipt> findByProperty(String propertyName, Object value,
			int first, int pageSize) {
		log.debug("finding Object instance with property: " + propertyName
				+ ", value: " + value);
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from " + Receipt.class.getCanonicalName()
					+ " as model where model." + propertyName
					+ "= ? order by created desc";
			Query queryObject = session.createQuery(queryString);
			queryObject.setParameter(0, value);
			queryObject.setFirstResult(first);
			queryObject.setMaxResults(pageSize);
			List<Receipt> list = queryObject.list();
			initializeObjects(list);
			return list;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public int countByProperty(int company_id) {
		log.debug("counting number of records for Receipt with company id:",
				company_id);
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null;
		String sql = null;
		ResultSet rs = null;
		int records = 0;
		try {
			stmt = conn.createStatement();
			sql = "SELECT count(*) count FROM fmart.receipt WHERE company_id="
					+ company_id;
			rs = stmt.executeQuery(sql);
			if (rs.next())
				records = rs.getInt("count");
		} catch (SQLException re) {
			log.error("record count failed for Receipt", re);
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

	public <T> List<Receipt> findByCriteria(Map<String, String> properties) {
		Session session = HibernateUtil.getSession();
		try {
			ProjectionList projections = Projections.projectionList();
			for (String property : properties.keySet()) {
				String attribute = properties.get(property);
				projections.add(Projections.property(property), attribute);
			}
			Criteria cr = session
					.createCriteria(Receipt.class)
					.setProjection(projections)
					.setResultTransformer(
							Transformers.aliasToBean(Receipt.class));

			List<Receipt> list = cr.list();

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

	public BigDecimal getPendingAmountForSale(Integer sale_id) {
		BigDecimal totalSale = BigDecimal.ZERO;
		BigDecimal totalReceipt = BigDecimal.ZERO;
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null;
		String sql = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			sql = "SELECT sum(amount) amt from fmart.sale where id=" + sale_id;
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				totalSale = rs.getBigDecimal("amt");
				if (totalSale == null)
					totalSale = BigDecimal.ZERO;
				rs.close();
			}
			sql = "SELECT sum(amount) amt from fmart.receipt where sale_id="
					+ sale_id;
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				totalReceipt = rs.getBigDecimal("amt");
				if (totalReceipt == null)
					totalReceipt = BigDecimal.ZERO;
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
		return totalSale.subtract(totalReceipt);
	}

	public <T> List<T> findAllNameContains(Class<T> klazz, String query,
			String suffix) {
		log.debug("finding all Object instances");
		Session session = getSession();
		try {
			String className = klazz.getCanonicalName();
			String queryString = "from " + className + " where id=" + query;
			Query queryObject = session.createQuery(queryString);
			List<T> list = queryObject.list();
			initializeObjects(list);
			return list;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public void checkDispatchAndReceipts(Sale sale) throws Exception {
		log.debug("Checking Dispatch and Receipts for a sale and changing the status to SALE");
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null, stmt1 = null, stmt2 = null;
		ResultSet rs = null, rs1 = null, rs2 = null;
		String sql = null, sql1 = null, sql2 = null;
		Date date = new Date();
		int dispatchedQuantity=0, returnedQuantity=0;
		BigDecimal saleAmount = BigDecimal.ZERO, receiptAmount = BigDecimal.ZERO;
		boolean amountFlag = false, productFlag = false;
		try {
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			stmt1 = conn.createStatement();
			stmt2 = conn.createStatement();
			sql = "SELECT amount from fmart.sale where id=" + sale.getId();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				saleAmount = rs.getBigDecimal("amount");
				sql1 = "SELECT amount from fmart.receipt where sale_id="
						+ sale.getId();
				rs1 = stmt1.executeQuery(sql1);
				while (rs1.next()) {
					receiptAmount = receiptAmount.add(rs1
							.getBigDecimal("amount"));
				}
				if (saleAmount.compareTo(receiptAmount) == 0)
					amountFlag = true;
				else
					amountFlag = false;
			}
			sql = "SELECT * from fmart.sale_detail where dispatched=false and returned=false and sale_id="
					+ sale.getId();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int productId = rs.getInt("product_id");
				boolean customised = rs.getBoolean("customised");
				int quantity = rs.getInt("quantity");

				sql1 = "SELECT sum(quantity) quantity from fmart.sale_detail where dispatched=true and returned=false and product_id="
						+ productId
						+ " and customised="
						+ customised
						+ " and sale_id=" + sale.getId();
				rs1 = stmt1.executeQuery(sql1);
				sql2 = "SELECT sum(quantity) quantity from fmart.sale_detail where dispatched=false and returned=true and product_id="
						+ productId
						+ " and customised="
						+ customised
						+ " and sale_id=" + sale.getId();
				rs2 = stmt2.executeQuery(sql2);
				if (rs1.next()) {
					dispatchedQuantity = rs1.getInt("quantity");
				}
				if (rs2.next()) {
					returnedQuantity = rs2.getInt("quantity");
				}
				if (quantity == (dispatchedQuantity - returnedQuantity))
					productFlag = true;
				else
					productFlag = false;
			}
			if (amountFlag && productFlag) {
				sql = "UPDATE fmart.sale set status='CLEARED BOOKING' where id="
						+ sale.getId();
				stmt.execute(sql);
			}
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
				throw e;
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw e;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (conn != null) conn.close();
				if (stmt != null) stmt.close();
				if (stmt1 != null) stmt1.close();
				if (stmt2 != null) stmt2.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

	public String saveReceipt(Receipt receipt) throws Exception {
		log.debug("saving Object instance");
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null;
		String sql = null;
		Session session = getSession();
		ResultSet rs = null;
		Transaction tx = null;
		String result = null;
		try {
			tx = session.beginTransaction();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();

			sql = "UPDATE fmart.company  SET cash_in_hand = cash_in_hand +"
					+ receipt.getAmount() + " where id="
					+ receipt.getCompany().getId() + "";
			stmt.executeUpdate(sql);
			session.save(receipt);
			conn.commit();
			tx.commit();
			result = "Receipt Entry : Done Sucessfully!";

			checkDispatchAndReceipts(findById(receipt.getSale().getId(),
					Sale.class));
		} catch (RuntimeException re) {
			log.error("Receipt Save Failed", re);
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

	public String updateReceipt(Receipt receipt, BigDecimal oldAmount)
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
					+ receipt.getCompany().getId() + "";
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				cashInHand = rs.getInt("cash_in_hand");
				if (cashInHand < (oldAmount.subtract(receipt.getAmount()))
						.doubleValue()) {
					throw new Exception("Cash in Hand is less than required");
				}
			}
			sql = "UPDATE fmart.company  SET cash_in_hand = cash_in_hand -("
					+ (oldAmount.subtract(receipt.getAmount())).doubleValue()
					+ ") where id=" + receipt.getCompany().getId() + "";
			stmt.executeUpdate(sql);

			if (receipt.getParticulars().equals("ADVANCE")) {
				sql = "UPDATE fmart.sale  SET advance = advance -("
						+ (oldAmount.subtract(receipt.getAmount()))
								.doubleValue() + ") where id="
						+ receipt.getSale().getId() + "";
				stmt.executeUpdate(sql);
			}
			String originalVal = getOriginalVal(receipt.getId(), Receipt.class);
			session.update(receipt);
			String actualVal = toObjectString(receipt);
			String username = UserSession.getSession().getUsername();
			Notifications notification = new Notifications(originalVal,
					actualVal, "Receipt", username, "update");
			session.save(notification);

			conn.commit();
			tx.commit();
			result = "Receipt Entry : Done Sucessfully!";

			checkDispatchAndReceipts(findById(receipt.getSale().getId(),
					Sale.class));
		} catch (RuntimeException re) {
			log.error("Receipt Save Failed", re);
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

	private String toObjectString(Object obj) {
		try {
			ObjectWriter ow = new ObjectMapper().writer();
			String retString = ow.writeValueAsString(obj);
			retString = retString.replaceAll("  ", "");
			return retString;
		} catch (JsonGenerationException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return "";
	}

	private <T> String getOriginalVal(java.lang.Integer id,
			Class<T> persistentInstance) {
		T instance = (T) findById(id, persistentInstance);
		return toObjectString(instance);
	}

	private <T> void initializeObject(T t) {
		if (t instanceof Sale) {
			Sale s = (Sale) t;
			Hibernate.initialize(s.getCompany());
			Hibernate.initialize(s.getEmployeeByCreatedBy());
			Hibernate.initialize(s.getEmployeeByUpdatedBy());
			Hibernate.initialize(s.getSaleDetails());
			for (SaleDetail details : s.getSaleDetails()) {
				Hibernate.initialize(details.getProduct());
				if (details.getProduct() != null) {
					Hibernate.initialize(details.getProduct().getProductGrp());
				}

			}
		} else if (t instanceof Receipt) {
			Receipt r = (Receipt) t;
			Hibernate.initialize(r.getCompany());
			Hibernate.initialize(r.getSale());
			Hibernate.initialize(r.getEmployeeByCreatedBy());
		}
	}

	private <T> void initializeObjects(List<T> genericList) {
		for (T t : genericList) {
			initializeObject(t);
		}

	}

	/*
	 * public void initializeReceiptList(List<Receipt> list){ for (Receipt
	 * receipt : list) { initializeReceipt(receipt); } }
	 * 
	 * public void initializeReceipt(Receipt r){
	 * Hibernate.initialize(r.getCompany()); Hibernate.initialize(r.getSale());
	 * Hibernate.initialize(r.getEmployee()); }
	 * 
	 * public void initializeSaleList(List<Sale> list){ for (Sale sale : list) {
	 * initializeSale(sale); } }
	 * 
	 * public void initializeSale(Sale s){ Hibernate.initialize(s.getCompany());
	 * Hibernate.initialize(s.getEmployeeByCreatedBy());
	 * Hibernate.initialize(s.getEmployeeByEmployeeId());
	 * Hibernate.initialize(s.getSaleDetails()); for (SaleDetail details :
	 * s.getSaleDetails()) { Hibernate.initialize(details.getProduct()); if
	 * (details.getProduct() != null) {
	 * Hibernate.initialize(details.getProduct().getProductGrp()); }
	 * 
	 * } }
	 */
}
