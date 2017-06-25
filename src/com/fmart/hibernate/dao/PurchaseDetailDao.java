package com.fmart.hibernate.dao;

// Generated Apr 13, 2015 10:25:23 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
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
import com.fmart.hibernate.pojos.PurchaseDetail;
import com.fmart.hibernate.pojos.Receipt;
import com.fmart.hibernate.pojos.Sale;
import com.fmart.hibernate.pojos.SaleDetail;
import com.fmart.ui.beans.UserSession;

/**
 * Dao object for domain model class PurchaseDetail.
 * 
 * @see com.fmart.hibernate.pojos.PurchaseDetail
 * @author Hibernate Tools
 */

public class PurchaseDetailDao {

	private static final Logger log = LoggerFactory
			.getLogger(PurchaseDetailDao.class);

	public void save(PurchaseDetail transientInstance) {
		log.debug("persisting PurchaseDetail instance");
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

	public void update(PurchaseDetail transientInstance, Integer id) {
		log.debug("persisting PurchaseDetail instance");
		Transaction tx = null;
		Session session = HibernateUtil.getSession();
		try {
			tx = session.beginTransaction();
			String fromVal = NotificationUtils.getJsonVal(findById(id));
			session.update(transientInstance);
			String toVal = NotificationUtils.getJsonVal(findById(id));
			String username = UserSession.getSession().getUsername();
			NotificationUtils.saveNotification(fromVal, toVal,
					PurchaseDetail.class, username, NotificationType.UPDATE);
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

	public PurchaseDetail findById(int id) {
		log.debug("getting PurchaseDetail instance with id: " + id);
		Session session = HibernateUtil.getSession();

		try {
			PurchaseDetail instance = (PurchaseDetail) session.get(
					PurchaseDetail.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public List<PurchaseDetail> findAll() {
		log.debug("finding all Object instances");
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from "
					+ PurchaseDetail.class.getCanonicalName();
			Query queryObject = session.createQuery(queryString);
			List<PurchaseDetail> genericList = queryObject.list();
			return genericList;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public List<PurchaseDetail> findByProperty(String propertyName, Object value) {
		log.debug("finding Object instance with property: " + propertyName
				+ ", value: " + value);
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from "
					+ PurchaseDetail.class.getCanonicalName()
					+ " as model where model." + propertyName + "= ?";
			Query queryObject = session.createQuery(queryString);
			queryObject.setParameter(0, value);
			List<PurchaseDetail> genericList = queryObject.list();
			initializeObjects(genericList);
			return genericList;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public <T> List<PurchaseDetail> findByCriteria(
			Map<String, String> properties) {
		Session session = HibernateUtil.getSession();
		try {
			ProjectionList projections = Projections.projectionList();
			for (String property : properties.keySet()) {
				String attribute = properties.get(property);
				projections.add(Projections.property(property), attribute);
			}
			Criteria cr = session
					.createCriteria(PurchaseDetail.class)
					.setProjection(projections)
					.setResultTransformer(
							Transformers.aliasToBean(PurchaseDetail.class));
			
			List<PurchaseDetail> list = cr.list();

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
	private <T> void initializeObject(T t) {
		if (t instanceof PurchaseDetail) {
			PurchaseDetail pd = (PurchaseDetail) t;
			Hibernate.initialize(pd.getProduct());
			if (pd.getProduct() != null) {
				Hibernate.initialize(pd.getProduct().getProductGrp());
			}
			Hibernate.initialize(pd.getPurchase());
		}
	}
	
	private <T> void initializeObjects(List<T> genericList) {
		for (T t : genericList) {
			initializeObject(t);
		}
	}
}
