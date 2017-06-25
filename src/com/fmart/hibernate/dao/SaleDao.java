package com.fmart.hibernate.dao;

// Generated Apr 13, 2015 10:25:23 PM by Hibernate Tools 3.4.0.CR1

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
import com.fmart.hibernate.pojos.Inventory;
import com.fmart.hibernate.pojos.InventoryId;
import com.fmart.hibernate.pojos.Notifications;
import com.fmart.hibernate.pojos.Payment;
import com.fmart.hibernate.pojos.Product;
import com.fmart.hibernate.pojos.ProductGrp;
import com.fmart.hibernate.pojos.Purchase;
import com.fmart.hibernate.pojos.PurchaseDetail;
import com.fmart.hibernate.pojos.Receipt;
import com.fmart.hibernate.pojos.Sale;
import com.fmart.hibernate.pojos.Sale;
import com.fmart.hibernate.pojos.SaleDetail;
import com.fmart.ui.beans.UserSession;
import com.fmart.ui.utils.SessionInfo;

/**
 * Dao object for domain model class Sale.
 * 
 * @see com.fmart.hibernate.pojos.Sale
 * @author Hibernate Tools
 */

public class SaleDao {

	private static final Logger log = LoggerFactory.getLogger(SaleDao.class);

	private Session getSession() {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		return session;
	}

	public void save(Sale transientInstance) {
		log.debug("persisting Sale instance");
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

	public void update(Sale transientInstance, Integer id) {
		log.debug("persisting Sale instance");
		Transaction tx = null;
		Session session = HibernateUtil.getSession();
		try {
			tx = session.beginTransaction();
			String fromVal = NotificationUtils.getJsonVal(findById(id));
			session.update(transientInstance);
			String toVal = NotificationUtils.getJsonVal(findById(id));
			String username = UserSession.getSession().getUsername();
			NotificationUtils.saveNotification(fromVal, toVal, Sale.class,
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

	public Sale findById(int id) {
		log.debug("getting Sale instance with id: " + id);
		Session session = HibernateUtil.getSession();

		try {
			Sale instance = (Sale) session.get(Sale.class, id);
			log.debug("get successful");
			initializeSaleIbject(instance);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public <T> List<T> findAll(Class<T> klazz) {
		log.debug("finding all Object instances");
		Session session = getSession();
		try {
			String className = klazz.getCanonicalName();
			String queryString = "from " + className;
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

	public <T> List<T> findByProperty(Class<T> klazz, String propertyName,
			Object value) {
		log.debug("finding Object instance with property: " + propertyName
				+ ", value: " + value);
		Session session = getSession();
		try {
			String className = klazz.getCanonicalName();
			String queryString = "from " + className + " as model where model."
					+ propertyName + "= ?";
			Query queryObject = session.createQuery(queryString);
			queryObject.setParameter(0, value);
			List<T> genericList = queryObject.list();
			initializeObjects(genericList);
			return genericList;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public List<Sale> findByProperty(String company, Object companyId,String created_by,Object employeeId,
			int first, int pageSize) {
		log.debug("finding Object instance with property: " + company
				+ ", value: " + companyId+" property:"+created_by+", value: "+employeeId);
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from " + Sale.class.getCanonicalName()
					+ " as model where model." + company
					+ "= ? order by created desc";
			if(employeeId!=null)
				queryString = "from " + Sale.class.getCanonicalName()
					+ " as model where model." + company
					+ "= ? and "+created_by+"=? order by created desc";
			Query queryObject = session.createQuery(queryString);
			queryObject.setFirstResult(first);
			queryObject.setMaxResults(pageSize);
			queryObject.setParameter(0, companyId);
			if(employeeId!=null)
				queryObject.setParameter(1, employeeId);
				
			List<Sale> genericList = queryObject.list();
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
		log.debug("counting number of records for Sale with company id:",
				company_id);
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null;
		String sql = null;
		ResultSet rs = null;
		int records = 0;
		try {
			stmt = conn.createStatement();
			sql = "SELECT count(*) count FROM fmart.sale WHERE company_id="
					+ company_id;
			rs = stmt.executeQuery(sql);
			if (rs.next())
				records = rs.getInt("count");
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

	public <T> List<Sale> findByCriteria(Map<String, String> properties) {
		Session session = HibernateUtil.getSession();
		try {
			ProjectionList projections = Projections.projectionList();
			for (String property : properties.keySet()) {
				String attribute = properties.get(property);
				projections.add(Projections.property(property), attribute);
			}
			Criteria cr = session.createCriteria(Sale.class)
					.setProjection(projections)
					.setResultTransformer(Transformers.aliasToBean(Sale.class));

			List<Sale> list = cr.list();

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

	public String saveSale(Sale sale, String page) throws Exception {

		log.debug("Saving Sale Object instance");
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null, stmt1 = null;
		PreparedStatement pstmt = null;
		String sql = null;
		String pname = null;
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
			stmt1 = conn.createStatement();
			if ("sale_update".equalsIgnoreCase(page)) {
				sql = "DELETE FROM fmart.sale_detail WHERE sale_id="
						+ sale.getId()
						+ " and returned=false and dispatched=false";
				stmt.execute(sql);
			}
			if ("sale_update".equalsIgnoreCase(page)
					|| "sale_return".equalsIgnoreCase(page)
					|| "sale_dispatch".equalsIgnoreCase(page)) {
				originalVal = getOriginalVal(sale.getId(), Sale.class);
			}

			if ("sale_update".equalsIgnoreCase(page)
					|| "sale_return".equalsIgnoreCase(page)
					|| "sale_dispatch".equalsIgnoreCase(page)) {
				sql = "UPDATE fmart.sale SET amount=?, updated_by=?, company_id=?, status=?, created=?,advance=?,updated=? WHERE id=?";

				if ("sale_update".equalsIgnoreCase(page)) {
					pstmt = conn.prepareStatement(sql);
					pstmt.setBigDecimal(1, sale.getAmount());
					pstmt.setInt(2, sessionInfo.getEmployeeId());
					pstmt.setInt(3, sale.getCompany().getId());
					pstmt.setString(4, sale.getStatus());
					pstmt.setDate(5,
							new java.sql.Date(sale.getCreated().getTime()));
					pstmt.setBigDecimal(6, sale.getAdvance());
					pstmt.setTimestamp(7,
							new java.sql.Timestamp(new Date().getTime()));
					pstmt.setInt(8, sale.getId());
					pstmt.execute();
				}
				for (SaleDetail sd : sale.getSaleDetails()) {
					if (sd.getQuantity() > 0) {

						// Insert Sale Update/Return/Dispatch records.
						sql = "INSERT INTO fmart.sale_detail(sale_id, product_id, quantity, amount, remarks, returned,customised,dispatched,rate,created,created_by,updated_by,updated) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
						pstmt = conn.prepareStatement(sql);
						pstmt.setInt(1, sale.getId());
						pstmt.setInt(2, sd.getProduct().getId());
						pstmt.setInt(3, sd.getQuantity());
						pstmt.setBigDecimal(4, sd.getAmount());
						pstmt.setString(5, sd.getRemarks());
						pstmt.setBoolean(6, sd.getReturned());
						pstmt.setBoolean(7, sd.getCustomised());
						pstmt.setBoolean(8, sd.getDispatched());
						pstmt.setBigDecimal(9, sd.getRate());
						pstmt.setTimestamp(10, new java.sql.Timestamp(
								new Date().getTime()));
						pstmt.setInt(11, sessionInfo.getEmployeeId());
						pstmt.setInt(12, sessionInfo.getEmployeeId());
						pstmt.setTimestamp(13, new java.sql.Timestamp(
								new Date().getTime()));
						pstmt.execute();

						// If Sale Return we don't delete the old records, we
						// update old records for quantity and amount
						/*
						 * if ("sale_return".equalsIgnoreCase(page)) { sql =
						 * "UPDATE fmart.sale_detail SET quantity=quantity-?, amount=amount-?, date=?,updated_by=?,updated=? WHERE id=?"
						 * ; pstmt = conn.prepareStatement(sql); pstmt.setInt(1,
						 * sd.getQuantity()); pstmt.setBigDecimal(2,
						 * sd.getAmount()); pstmt.setDate(3, new
						 * java.sql.Date(sale.getDate() .getTime()));
						 * pstmt.setInt(4, sessionInfo.getEmployeeId());
						 * pstmt.setTimestamp(5, new java.sql.Timestamp(new
						 * Date().getTime())); pstmt.setInt(6, sd.getId());
						 * pstmt.execute(); }
						 */
					}
				}

				// Saving Notification
				actualVal = toObjectString(sale);
				String username = UserSession.getSession().getUsername();
				Notifications notification = new Notifications(originalVal,
						actualVal, page, username, "update");
				session.save(notification);

			} else {
				sql = "UPDATE fmart.company  SET cash_in_hand = cash_in_hand +"
						+ sale.getAdvance() + " where id="
						+ sale.getCompany().getId() + "";
				stmt.executeUpdate(sql);
				session.save(sale);
			}
			conn.commit();
			tx.commit();

			if ("sale_entry".equalsIgnoreCase(page))
				result = "Booking Entry : Done Successfully";
			else if ("sale_update".equalsIgnoreCase(page))
				result = "Booking Update : Done Successfully";
			else if ("sale_return".equalsIgnoreCase(page))
				result = "Booking Return : Done Successfully";
			else
				result = "Booking Dispatch : Done Successfully";
			log.debug(result);

			checkDispatchAndReceipts(sale);

		} catch (RuntimeException re) {
			log.error("BOOKING Save Failed", re);
			tx.rollback();
			conn.rollback();
			throw re;
		} catch (SQLException e) {
			try {
				conn.rollback();
				tx.rollback();
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
				if (stmt1 != null)
					stmt1.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;

			}
		}
		return result;

	}

	public void cancelBooking(Sale sale) throws Exception {
		log.debug("Cancelling Booking Object instance");
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Date date = new Date();
		BigDecimal receiptAmount = BigDecimal.ZERO;
		SessionInfo sessionInfo = UserSession.getSession();
		try {
			// set status to 'cancelled booking' in SALE
			stmt = conn.createStatement();
			sql = "UPDATE fmart.sale set status='CANCELLED BOOKING',updated_by=?,updated=? where id=?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(3, sale.getId());
			pStmt.setInt(1, UserSession.getSession().getEmployeeId());
			pStmt.setTimestamp(2, new java.sql.Timestamp(new Date().getTime()));
			pStmt.execute();
			// set returned to true in SALE_DETAIL and add to INVENTORY
			List<SaleDetail> saleDetails = findByProperty(SaleDetail.class,
					"sale.id", sale.getId());
			for (SaleDetail sd : saleDetails) {
				if (sd.getDispatched()) {
					sql = "INSERT INTO fmart.sale_detail(sale_id, product_id, quantity, amount, returned, remarks,customised, rate, dispatched,created_by,updated_by,created,updated) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, sale.getId());
					pstmt.setInt(2, sd.getProduct().getId());
					pstmt.setInt(3, sd.getQuantity());
					pstmt.setBigDecimal(4, sd.getAmount());
					pstmt.setBoolean(5, true);
					pstmt.setString(6, sd.getRemarks());
					pstmt.setBoolean(7, sd.getCustomised());
					pstmt.setBigDecimal(8, sd.getRate());
					pstmt.setBoolean(9, false);
					pstmt.setInt(10, sessionInfo.getEmployeeId());
					pstmt.setInt(11, sessionInfo.getEmployeeId());
					pstmt.setTimestamp(12,
							new java.sql.Timestamp(new Date().getTime()));
					pstmt.setTimestamp(13,
							new java.sql.Timestamp(new Date().getTime()));
					pstmt.execute();

					sql = "UPDATE fmart.inventory SET quantity=quantity+("
							+ (sd.getQuantity()) + ") WHERE product_id="
							+ sd.getProduct().getId() + " and company_id="
							+ sale.getCompany().getId() + " and customised="
							+ sd.getCustomised();
					stmt.execute(sql);
				}
			}

			// find total receipt amount
			sql = "SELECT sum(amount) amount FROM fmart.receipt WHERE sale_id="
					+ sale.getId();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				receiptAmount = rs.getBigDecimal("amount");
			}

			// set old receipts particulars to CANCEL, to avoid from editing in
			// UI
			/*
			 * sql = "UPDATE fmart.receipt set particulars=concat(particulars,'"
			 * + " - CANCELLED" + "') where sale_id=" + sale.getId();
			 * stmt.execute(sql);
			 */

			sql = "UPDATE fmart.receipt set particulars=concat(particulars,'"
					+ " - CANCELLED"
					+ "') ,updated_by=?,updated=? where sale_id=?";
			pStmt = conn.prepareStatement(sql);
			pStmt.setInt(3, sale.getId());
			pStmt.setInt(1, UserSession.getSession().getEmployeeId());
			pStmt.setTimestamp(2, new java.sql.Timestamp(new Date().getTime()));
			pStmt.execute();

			// create a negative entry in RECEIPT
			sql = "INSERT INTO fmart.receipt(sale_id, company_id, date, amount, particulars,created_by,updated_by,created,updated) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, sale.getId());
			pstmt.setInt(2, sale.getCompany().getId());
			pstmt.setDate(3, new java.sql.Date(date.getTime()));
			pstmt.setBigDecimal(4, receiptAmount.negate());
			pstmt.setString(5, "CANCELLED BOOKING");
			pstmt.setInt(6, sessionInfo.getEmployeeId());
			pstmt.setInt(7, sessionInfo.getEmployeeId());
			pstmt.setTimestamp(8, new java.sql.Timestamp(new Date().getTime()));
			pstmt.setTimestamp(9, new java.sql.Timestamp(new Date().getTime()));

			pstmt.execute();

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
				if (conn != null)
					conn.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

	public void updateInventory(List<Inventory> inventories)
			throws SQLException {
		log.debug("Saving Sale Object instance");
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null, stmt1 = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String pname = null;
		try {
			stmt = conn.createStatement();
			stmt1 = conn.createStatement();
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
					sql = "SELECT name FROM fmart.product WHERE id="
							+ inId.getProductId();
					ResultSet rs1 = stmt1.executeQuery(sql);
					if (rs.getInt("quantity") < inventory.getQuantity()) {
						if (rs1.next())
							pname = rs1.getString("name");
						throw new SQLException("In-Sufficient Inventory for "
								+ pname + "  Required Quantity: "
								+ inventory.getQuantity()
								+ ",  Available Quantity: "
								+ rs.getInt("quantity"));
					}
					sql = "UPDATE fmart.inventory SET quantity=quantity-("
							+ inventory.getQuantity() + ") WHERE product_id="
							+ inId.getProductId() + " and company_id="
							+ inId.getCompanyId() + " and customised="
							+ inId.isCustomised();
					stmt.execute(sql);

				} else if (inventory.getQuantity() < 0) {
					sql = "INSERT INTO fmart.inventory(product_id, company_id, customised, quantity) VALUES (?, ?, ?, ?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, inId.getProductId());
					pstmt.setInt(2, inId.getCompanyId());
					pstmt.setBoolean(3, inId.isCustomised());
					pstmt.setInt(4, inventory.getQuantity() * -1);
					pstmt.execute();
				} else {
					throw new SQLException("Inventory not available for "
							+ pname + " Quantity: " + inventory.getQuantity());
				}
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
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (stmt != null)
					stmt.close();
				if (stmt1 != null)
					stmt1.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;

			}
		}
	}

	public void checkDispatchAndReceipts(Sale sale) throws Exception {
		log.debug("Checking Dispatch and Receipts for a sale and changing the status to SALE");
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null, stmt1 = null, stmt2 = null;
		ResultSet rs = null, rs1 = null, rs2 = null;
		String sql = null, sql1 = null;
		Date date = new Date();
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
				int dispatched=0,returned=0;
				int productId = rs.getInt("product_id");
				boolean customised = rs.getBoolean("customised");
				int quantity = rs.getInt("quantity");

				sql1 = "SELECT sum(quantity) quantity from fmart.sale_detail where dispatched=true and product_id="
						+ productId
						+ " and customised="
						+ customised
						+ " and sale_id=" + sale.getId();
				rs1 = stmt1.executeQuery(sql1);
				if (rs1.next()) {
					dispatched= rs1.getInt("quantity");
				} 
				
				sql1 = "SELECT sum(quantity) quantity from fmart.sale_detail where returned=true and product_id="
						+ productId
						+ " and customised="
						+ customised
						+ " and sale_id=" + sale.getId();
				rs2 = stmt1.executeQuery(sql1);
				if (rs2.next()) {
					returned= rs2.getInt("quantity");
				} 

				if (quantity == (dispatched - returned))
					productFlag = true;
				else
					productFlag = false;
			}
			if (amountFlag && productFlag) {
				sql = "UPDATE fmart.sale set status='CLEARED BOOKING',updated_by=?,updated=? where id=?";
				PreparedStatement pStmt = conn.prepareStatement(sql);
				pStmt.setInt(3, sale.getId());
				pStmt.setInt(1, UserSession.getSession().getEmployeeId());
				pStmt.setTimestamp(2,
						new java.sql.Timestamp(new Date().getTime()));
				pStmt.execute();
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
				if (conn != null)
					conn.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

	public List<java.sql.Date> findDateBySaleID(int id) throws Exception {
		List<java.sql.Date> dates = new ArrayList<java.sql.Date>();
		log.debug("saving Object instance");
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null;
		String sql = null;
		Session session = getSession();
		ResultSet rs = null;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();

			sql = "SELECT DISTINCT date FROM fmart.sale_detail WHERE dispatched=true and sale_id="
					+ id + " order by date";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				dates.add(rs.getDate("date"));
			}
			conn.commit();
			tx.commit();
		} catch (RuntimeException re) {
			log.error("Fetching date(s) for ID " + id + " is Failed", re);
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
		return dates;

	}

	public List<Integer> findAllSaleIDs(int company_id, String id)
			throws Exception {
		List<Integer> ids = new ArrayList<Integer>();
		log.debug("saving Object instance");
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null;
		String sql = null;
		Session session = getSession();
		ResultSet rs = null;
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();

			sql = "SELECT id FROM fmart.sale WHERE company_id=" + company_id
					+ " and id=" + id;
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				ids.add(rs.getInt("id"));
			}
			conn.commit();
			tx.commit();
		} catch (RuntimeException re) {
			log.error("Fetching Sale Id  for company ID " + company_id
					+ " is Failed", re);
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
		return ids;

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

	private <T> void initializeIbject(T t) {
		if (t instanceof Sale) {
			Sale s = (Sale) t;
			Hibernate.initialize(s.getCompany());
			Hibernate.initialize(s.getEmployeeByCreatedBy());
			Hibernate.initialize(s.getEmployeeByUpdatedBy());
			Hibernate.initialize(s.getCustomer());
			/*
			 * Hibernate.initialize(s.getSaleDetails()); for (SaleDetail details
			 * : s.getSaleDetails()) {
			 * Hibernate.initialize(details.getProduct()); if
			 * (details.getProduct() != null) {
			 * Hibernate.initialize(details.getProduct().getProductGrp()); }
			 * 
			 * }
			 */
		} else if (t instanceof SaleDetail) {
			SaleDetail sd = (SaleDetail) t;
			Hibernate.initialize(sd.getProduct());
			Hibernate.initialize(sd.getSale());
			if (sd.getProduct() != null)
				Hibernate.initialize(sd.getProduct().getProductGrp());
		} else if (t instanceof Receipt) {
			Receipt r = (Receipt) t;
			Hibernate.initialize(r.getCompany());
			Hibernate.initialize(r.getSale());
			Hibernate.initialize(r.getEmployeeByCreatedBy());
		}
	}

	private <T> void initializeSaleIbject(T t) {
		if (t instanceof Sale) {
			Sale s = (Sale) t;
			Hibernate.initialize(s.getCompany());
			Hibernate.initialize(s.getEmployeeByCreatedBy());
			Hibernate.initialize(s.getEmployeeByUpdatedBy());
			Hibernate.initialize(s.getSaleDetails());
			Hibernate.initialize(s.getCustomer());
			for (SaleDetail details : s.getSaleDetails()) {
				Hibernate.initialize(details.getProduct());
				if (details.getProduct() != null) {
					Hibernate.initialize(details.getProduct().getProductGrp());
				}

			}
		}
	}

	private <T> void initializeObjects(List<T> genericList) {
		for (T t : genericList) {
			initializeIbject(t);
		}
	}

	public <T> T findById(java.lang.Integer id, Class<T> persistentInstance) {
		log.debug("getting Object instance with id: " + id);
		Session session = getSession();
		try {
			String className = persistentInstance.getCanonicalName();
			T instance = (T) session.get(className, id);
			initializeIbject(instance);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
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
			else if (className.contains("Customer"))
				name = "phone_string";
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

	public <T> List<T> findPrintBookingById(int id) {
		log.debug("finding all Object instances");
		Session session = getSession();
		try {
			String className = Sale.class.getCanonicalName();
			String queryString = "from "
					+ className
					+ " where returned=false and dispatched=false and  sale_id = "
					+ id;

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

	public <T> List<T> findPrintDispatchByDate(int id, Date date) {
		log.debug("finding all Object instances");
		java.sql.Date sqlDate = (java.sql.Date) date;
		Session session = getSession();
		try {
			String className = SaleDetail.class.getCanonicalName();
			String queryString = "from " + className
					+ " where dispatched=true and sale_id = " + id
					+ " and Date = '" + sqlDate + "'";

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

	public int queryDispatchedQuantity(Integer sale_id, Integer company_id,
			Integer product_id, Boolean customised) {
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null, stmt1 = null;
		String sql = null;
		ResultSet rs = null, rs1 = null;
		int dispatchedQuantity = 0, returnedQuantity = 0;
		try {
			stmt = conn.createStatement();
			stmt1 = conn.createStatement();
			sql = "SELECT sum(quantity) dispatchedQuantity from fmart.sale_detail where dispatched=true and product_id="
					+ product_id
					+ " and customised="
					+ customised
					+ " and sale_id=" + sale_id;
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				dispatchedQuantity = rs.getInt("dispatchedQuantity");
			}

			sql = "SELECT sum(quantity) returnedQuantity from fmart.sale_detail where returned=true and product_id="
					+ product_id
					+ " and customised="
					+ customised
					+ " and sale_id=" + sale_id;
			rs1 = stmt1.executeQuery(sql);
			if (rs1.next()) {
				returnedQuantity = rs1.getInt("returnedQuantity");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (stmt != null)
					stmt.close();
				if (stmt1 != null)
					stmt1.close();
				if (rs != null)
					rs.close();
				if (rs1 != null)
					rs1.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dispatchedQuantity - returnedQuantity;
	}
}
