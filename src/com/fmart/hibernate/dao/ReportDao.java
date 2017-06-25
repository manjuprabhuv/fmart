package com.fmart.hibernate.dao;

// Generated Apr 13, 2015 10:25:23 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fmart.framework.hibernate.HibernateUtil;
import com.fmart.hibernate.dao.NotificationUtils.NotificationType;
import com.fmart.hibernate.pojos.EmpReport;
import com.fmart.hibernate.pojos.Employee;
import com.fmart.hibernate.pojos.Expense;
import com.fmart.hibernate.pojos.Payment;
import com.fmart.hibernate.pojos.Product;
import com.fmart.hibernate.pojos.ProductGrp;
import com.fmart.hibernate.pojos.Purchase;
import com.fmart.hibernate.pojos.PurchaseDetail;
import com.fmart.hibernate.pojos.Receipt;
import com.fmart.hibernate.pojos.Report;
import com.fmart.hibernate.pojos.Report;
import com.fmart.hibernate.pojos.Sale;
import com.fmart.hibernate.pojos.SaleDetail;
import com.fmart.ui.beans.UserSession;

/**
 * Dao object for domain model class Report.
 * 
 * @see com.fmart.hibernate.pojos.Report
 * @author Hibernate Tools
 */

public class ReportDao {

	private static final Logger log = LoggerFactory.getLogger(ReportDao.class);
	
	private Session getSession() {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		return session;
	}

	public void save(Report transientInstance) {
		log.debug("persisting Report instance");
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

	public void update(Report transientInstance, Integer id) {
		log.debug("persisting Report instance");
		Transaction tx = null;
		Session session = HibernateUtil.getSession();
		try {
			tx = session.beginTransaction();
			String fromVal = NotificationUtils.getJsonVal(findById(id));
			session.update(transientInstance);
			String toVal = NotificationUtils.getJsonVal(findById(id));
			String username = UserSession.getSession().getUsername();
			NotificationUtils.saveNotification(fromVal, toVal, Report.class,
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

	public Report findById(int id) {
		log.debug("getting Report instance with id: " + id);
		Session session = HibernateUtil.getSession();

		try {
			Report instance = (Report) session.get(Report.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public List<Report> findAll() {
		log.debug("finding all Object instances");
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from " + Report.class.getCanonicalName();
			Query queryObject = session.createQuery(queryString);
			List<Report> genericList = queryObject.list();
			return genericList;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public List<Report> findByProperty(String propertyName, Object value) {
		log.debug("finding Object instance with property: " + propertyName
				+ ", value: " + value);
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from " + Report.class.getCanonicalName()
					+ " as model where model." + propertyName + "= ?";
			Query queryObject = session.createQuery(queryString);
			queryObject.setParameter(0, value);
			List<Report> genericList = queryObject.list();

			return genericList;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public <T> List<Report> findByCriteria(Map<String, String> properties) {
		Session session = HibernateUtil.getSession();
		try {
			ProjectionList projections = Projections.projectionList();
			for (String property : properties.keySet()) {
				String attribute = properties.get(property);
				projections.add(Projections.property(property), attribute);
			}
			Criteria cr = session
					.createCriteria(Report.class)
					.setProjection(projections)
					.setResultTransformer(
							Transformers.aliasToBean(Report.class));

			List<Report> list = cr.list();

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
	
	public <T> List<T> generateReportsCreated(Class<T> klazz, Integer companyId,
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
					+ companyId + " and created >= '" + new java.sql.Date(fromDate.getTime())
					+ "' and created < '" + new java.sql.Date(toDate.getTime()) + "' "+ appendQuery;
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
	
	public <T> List<T> generateReportsUpdated(Class<T> klazz, Integer companyId,
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
	
	public <T> List<T> generateExpenseReports(Class<T> klazz, Integer companyId,
			Date fromDate, Date toDate, Integer employeeId, Integer expenseTypeId) {
		log.debug("finding all Object instances");
		Session session = getSession();
		try {
			String className = klazz.getCanonicalName();
			String appendQuery = "";
			if(expenseTypeId !=0){
				appendQuery = "and type_id = "+ expenseTypeId;
			}
			if(employeeId != 0){
				appendQuery = appendQuery + " and employee_id ="+ employeeId;
			}
			String queryString = "from " + className + " where company_id ="
					+ companyId + " and created >= '" + new java.sql.Date(fromDate.getTime())
					+ "' and created < '" + new java.sql.Date(toDate.getTime()) + "' "+ appendQuery;
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
}
