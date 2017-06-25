package com.fmart.framework.hibernate;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.internal.SessionImpl;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fmart.hibernate.pojos.Company;
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
import com.fmart.hibernate.pojos.SaleDetail;
import com.fmart.ui.beans.UserSession;
import com.fmart.ui.utils.DbManager;
import com.google.gson.Gson;

/**
 * 
 * @author mvprabhu
 */
public class FMartGenericDAO {
	private static final Logger log = LoggerFactory
			.getLogger(FMartGenericDAO.class);

	private Session getSession() {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		return session;
	}

	public <T> void update(T transientInstance, int id) {
		log.debug("saving Object instance");
		Session session = getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			String originalVal = getOriginalVal(id,
					transientInstance.getClass());
			session.update(transientInstance);
			String actualVal = toObjectString(transientInstance);
			String username = UserSession.getSession().getUsername();
			Notifications notification = new Notifications(originalVal,
					actualVal, transientInstance.getClass().getSimpleName(),
					username, "update");
			session.save(notification);

			tx.commit();
			log.debug("save successful");
		} catch (RuntimeException re) {
			tx.rollback();
			log.error("save failed", re);
			throw re;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public <T> void saveOrUpdate(T transientInstance) {
		log.debug("saving Object instance");
		Session session = getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(transientInstance);
			tx.commit();
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			tx.rollback();
			throw re;
		} finally {
			session.close();
		}
	}

	public <T> void save(T transientInstance) {
		log.debug("saving Object instance");
		Session session = getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(transientInstance);
			tx.commit();
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			tx.rollback();
			throw re;
		} finally {
			session.close();
		}
	}

	/*public void cancelBooking(Sale sale) throws Exception {
		log.debug("Cancelling Booking Object instance");
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Date date = new Date();
		BigDecimal receiptAmount = BigDecimal.ZERO;
		try {
			// set status to 'cancelled booking' in SALE
			stmt = conn.createStatement();
			sql = "UPDATE fmart.sale set status='CANCELLED BOOKING', amount=0, employee_id="
					+ UserSession.getSession().getEmployeeId()
					+ " where id="
					+ sale.getId();
			stmt.execute(sql);

			// set returned to true in SALE_DETAIL and add to INVENTORY
			List<SaleDetail> saleDetails = findByProperty(SaleDetail.class,
					"sale.id", sale.getId());
			for (SaleDetail sd : saleDetails) {
				if (sd.getDispatched()) {
					sql = "INSERT INTO fmart.sale_detail(sale_id, product_id, quantity, amount, returned, remarks,customised, date, rate, dispatched) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, sale.getId());
					pstmt.setInt(2, sd.getProduct().getId());
					pstmt.setInt(3, sd.getQuantity());
					pstmt.setBigDecimal(4, sd.getAmount());
					pstmt.setBoolean(5, true);
					pstmt.setString(6, sd.getRemarks());
					pstmt.setBoolean(7, sd.getCustomised());
					pstmt.setDate(8, new java.sql.Date(date.getTime()));
					pstmt.setBigDecimal(9, sd.getRate());
					pstmt.setBoolean(10, false);
					pstmt.execute();

					sql = "UPDATE fmart.inventory SET quantity=quantity+("
							+ sd.getQuantity() + ") WHERE product_id="
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
			sql = "UPDATE fmart.receipt set particulars=concat(particulars,'"
					+ " - CANCELLED" + "') where sale_id=" + sale.getId();
			stmt.execute(sql);

			// create a negative entry in RECEIPT
			sql = "INSERT INTO fmart.receipt(sale_id, employee_id, company_id, date, amount, particulars) VALUES (?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, sale.getId());
			pstmt.setInt(2, UserSession.getSession().getEmployeeId());
			pstmt.setInt(3, sale.getCompany().getId());
			pstmt.setDate(4, new java.sql.Date(date.getTime()));
			pstmt.setBigDecimal(5, receiptAmount.negate());
			pstmt.setString(6, "CANCELLED BOOKING");
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
					|| "sale_return".equalsIgnoreCase(page)) {
				originalVal = getOriginalVal(sale.getId(), Sale.class);
			}

			if ("sale_update".equalsIgnoreCase(page)
					|| "sale_return".equalsIgnoreCase(page)
					|| "sale_dispatch".equalsIgnoreCase(page)) {
				sql = "UPDATE fmart.sale SET customer_name=?, customer_address=?, amount=?, employee_id=?, company_id=?, status=?, date=?,advance=? WHERE id=?";
				if ("sale_return".equalsIgnoreCase(page))
					sql = "UPDATE fmart.sale SET customer_name=?, customer_address=?, amount=amount-?, employee_id=?, company_id=?, status=?, date=?,advance=? WHERE id=?";

				if ("sale_update".equalsIgnoreCase(page)
						|| "sale_return".equalsIgnoreCase(page)) {
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, sale.getCustomerName());
					pstmt.setString(2, sale.getCustomerAddress());
					pstmt.setBigDecimal(3, sale.getAmount());
					pstmt.setInt(4, sale.getEmployeeByEmployeeId().getId());
					pstmt.setInt(5, sale.getCompany().getId());
					pstmt.setString(6, sale.getStatus());
					pstmt.setDate(7,
							new java.sql.Date(sale.getDate().getTime()));
					pstmt.setBigDecimal(8, sale.getAdvance());
					pstmt.setInt(9, sale.getId());
					pstmt.execute();
				}
				for (SaleDetail sd : sale.getSaleDetails()) {
					if (sd.getQuantity() > 0) {

						// Insert Sale Update/Return/Dispatch records.
						sql = "INSERT INTO fmart.sale_detail(sale_id, product_id, quantity, amount, remarks, returned,customised,dispatched,rate,date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
						pstmt.setDate(10, new java.sql.Date(sale.getDate()
								.getTime()));
						pstmt.execute();

						// If Sale Return we don't delete the old records, we
						// update old records for quantity and amount
						if ("sale_return".equalsIgnoreCase(page)) {
							sql = "UPDATE fmart.sale_detail SET quantity=quantity-?, amount=amount-?, date=? WHERE id=?";
							pstmt = conn.prepareStatement(sql);
							pstmt.setInt(1, sd.getQuantity());
							pstmt.setBigDecimal(2, sd.getAmount());
							pstmt.setDate(3, new java.sql.Date(sale.getDate()
									.getTime()));
							pstmt.setInt(4, sd.getId());
							pstmt.execute();
						}
					}
				}

				// Saving Notification
				actualVal = toObjectString(sale);
				String username = UserSession.getSession().getUsername();
				Notifications notification = new Notifications(originalVal,
						actualVal, page.toUpperCase(), username, "update");
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
*/
	/*public void checkDispatchAndReceipts(Sale sale) throws Exception {
		log.debug("Checking Dispatch and Receipts for a sale and changing the status to SALE");
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null,stmt1=null;
		ResultSet rs = null, rs1 = null;
		String sql = null, sql1 = null;
		Date date = new Date();
		BigDecimal saleAmount = BigDecimal.ZERO, receiptAmount = BigDecimal.ZERO;
		boolean amountFlag = false, productFlag = false;
		try {
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			stmt1 = conn.createStatement();
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
				if (rs1.next()) {
					if (quantity == rs1.getInt("quantity"))
						productFlag = true;
					else
						productFlag = false;
				} else
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
*/


/*	public String saveExpense(Expense expense, Company company)
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
*/
	

/*	public String saveReceipt(Receipt receipt) throws Exception {
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
			
			checkDispatchAndReceipts(findById(receipt.getSale().getId(), Sale.class));
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

			checkDispatchAndReceipts(findById(receipt.getSale().getId(), Sale.class));
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
	}*/

/*	public String updateExpense(Expense expense, Company company,
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
			String originalVal = getOriginalVal(expense.getId(), Expense.class);
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
	}*/

	public <T> T findByInventoryId(InventoryId id, T persistentInstance) {
		log.debug("getting Object instance with id: " + id);
		Session session = getSession();
		try {
			String className = persistentInstance.getClass().getCanonicalName();
			T instance = (T) session.get(className, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public <T> void delete(T persistentInstance, int id) {
		log.debug("deleting Object instance");
		Session session = getSession();
		try {
			String originalVal = "";
			Transaction tx = session.beginTransaction();
			if (!(persistentInstance instanceof Notifications)) {
				originalVal = getOriginalVal(id, persistentInstance.getClass());
			}
			session.delete(persistentInstance);
			session.flush();
			if (!(persistentInstance instanceof Notifications)) {
				String actualVal = "{\"msg\":\"value deleted\"}";
				String username = UserSession.getSession().getUsername();
				Notifications notification = new Notifications(originalVal,
						actualVal, persistentInstance.getClass()
								.getSimpleName(), username, "delete");
				session.save(notification);
			}
			tx.commit();

			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		} finally {
			session.close();
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

	public void executeHQL(String hql, Map<String, Object> params) {
		Session session = getSession();
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
		if (t instanceof Employee) {
			Employee e = (Employee) t;
			Hibernate.initialize(e.getEmpReports());
			for (EmpReport report : e.getEmpReports()) {
				Hibernate.initialize(report.getReport());

			}
			Hibernate.initialize(e.getEmpRoles());
			Hibernate.initialize(e.getCompany());
		} else if (t instanceof Product) {
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
			//Hibernate.initialize(pur.getEmployeeByEmployeeId());
			Hibernate.initialize(pur.getPurchaseDetails());
			for (PurchaseDetail details : pur.getPurchaseDetails()) {
				Hibernate.initialize(details.getProduct());
				if (details.getProduct() != null) {
					Hibernate.initialize(details.getProduct().getProductGrp());
				}
				Hibernate.initialize(details.getPurchase());
			}

		} else if (t instanceof PurchaseDetail) {
			PurchaseDetail pd = (PurchaseDetail) t;
			Hibernate.initialize(pd.getProduct());
			if (pd.getProduct() != null) {
				Hibernate.initialize(pd.getProduct().getProductGrp());
			}
			Hibernate.initialize(pd.getPurchase());
		} else if (t instanceof Payment) {
			Payment pay = (Payment) t;
			Hibernate.initialize(pay.getCompany());
			Hibernate.initialize(pay.getDealer());
			Hibernate.initialize(pay.getEmployeeByCreatedBy());
		} else if (t instanceof ProductGrp) {
			ProductGrp pg = (ProductGrp) t;
		} else if (t instanceof Sale) {
			Sale s = (Sale) t;
			Hibernate.initialize(s.getCompany());
			Hibernate.initialize(s.getEmployeeByCreatedBy());
		//	Hibernate.initialize(s.getEmployeeByEmployeeId());
			Hibernate.initialize(s.getSaleDetails());
			for (SaleDetail details : s.getSaleDetails()) {
				Hibernate.initialize(details.getProduct());
				if (details.getProduct() != null) {
					Hibernate.initialize(details.getProduct().getProductGrp());
				}

			}
		} else if (t instanceof SaleDetail) {
			SaleDetail sd = (SaleDetail) t;
			Hibernate.initialize(sd.getProduct());
			Hibernate.initialize(sd.getSale());
		} else if (t instanceof Receipt) {
			Receipt r = (Receipt) t;
			Hibernate.initialize(r.getCompany());
			Hibernate.initialize(r.getSale());
			Hibernate.initialize(r.getEmployeeByCreatedBy());
		} else if (t instanceof EmpReport) {
			EmpReport er = (EmpReport) t;
			Hibernate.initialize(er.getReport());
			Hibernate.initialize(er.getEmployee());
		}
	}

	private <T> void initializeObjects(List<T> genericList) {
		for (T t : genericList) {
			initializeIbject(t);
		}

	}

/*	public BigDecimal getPendingAmountForDealer(Integer dealer_id,
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
	}*/

	public String queryInventory(String product_name, Integer product_id,
			Integer company_id) {
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null;
		String sql = null;
		ResultSet rs = null;
		int customisedInventory = 0;
		int nonCustomisedInventory = 0;
		try {
			stmt = conn.createStatement();
			sql = "SELECT * from fmart.inventory where product_id="
					+ product_id + " and company_id=" + company_id;
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				if (rs.getBoolean("customised"))
					customisedInventory = rs.getInt("quantity");
				if (!rs.getBoolean("customised"))
					nonCustomisedInventory = rs.getInt("quantity");

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
		product_name = product_name + "    [" + nonCustomisedInventory + "/"
				+ customisedInventory + "]";
		return product_name;
	}

	/*public int queryDispatchedQuantity(Integer sale_id, Integer company_id,
			Integer product_id, Boolean customised) {
		Connection conn = ((SessionImpl) getSession()).connection();
		Statement stmt = null;
		String sql = null;
		ResultSet rs = null;
		int dispatchedQuantity = 0;
		try {
			stmt = conn.createStatement();
			sql = "SELECT sum(quantity) dispatchedQuantity from fmart.sale_detail where dispatched=true and product_id="
					+ product_id
					+ " and customised="
					+ customised
					+ " and sale_id=" + sale_id;
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				dispatchedQuantity = rs.getInt("dispatchedQuantity");
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
		return dispatchedQuantity;
	}*/
/*
	public <T> List<T> generateReports(Class<T> klazz, Integer companyId,
			Date fromDate, Date toDate, Integer employeeId) {
		log.debug("finding all Object instances");
		Session session = getSession();
		try {
			String className = klazz.getCanonicalName();
			String appendQuery = "";
			if(employeeId != 0){
				appendQuery = " and created_by ="+ employeeId;
			}
			String queryString = "from " + className + " where company_id ="
					+ companyId + " and updated >= '" + new java.sql.Date(fromDate.getTime())
					+ "' and updated < '" + new java.sql.Date(toDate.getTime()) + "' "+ appendQuery;
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
	
	public <T> List<T> generateReports(Class<T> klazz, Integer companyId,
			Date fromDate, Date toDate) {
		return generateReports(klazz,companyId,fromDate,toDate,0);
	}*/

	public <T> List<T> findByCriteria(Class<T> clazz,
			Map<String, String> properties, List<String> fetchList) {
		Session session = getSession();
		try {
			ProjectionList projections = Projections.projectionList();
			for (String property : properties.keySet()) {
				String attribute = properties.get(property);
				projections.add(Projections.property(property), attribute);
			}
			Criteria cr = session.createCriteria(clazz)
					.setProjection(projections)
					.setResultTransformer(Transformers.aliasToBean(clazz));
			for (String fetch : fetchList) {
				cr.setFetchMode(fetch, FetchMode.EAGER);
			}

			List<T> list = cr.list();
			initializeObjects(list);

			return list;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		} finally {
			session.close();
		}

	}

}
