package com.fmart.hibernate.dao;

// Generated Apr 13, 2015 10:25:23 PM by Hibernate Tools 3.4.0.CR1


import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
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
import com.fmart.hibernate.pojos.EmpReport;
import com.fmart.ui.beans.UserSession;

/**
 * Dao object for domain model class EmpReport.
 * @see com.fmart.hibernate.pojos.EmpReport
 * @author Hibernate Tools
 */

public class EmpReportDao {

	private static final Logger log = LoggerFactory
			.getLogger(EmpReportDao.class);

	public void save(EmpReport transientInstance) {
		log.debug("persisting EmpReport instance");
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
		}finally {
			session.close();
		}
	}
	
	public void update(EmpReport transientInstance,Integer id) {
		log.debug("persisting EmpReport instance");
		Transaction tx = null;
	Session session = HibernateUtil.getSession();
	
		try {
				
			tx = session.beginTransaction();
			String fromVal = NotificationUtils.getJsonVal(findById(id));
			session.update(transientInstance);
			String toVal = NotificationUtils.getJsonVal(findById(id));
			String username = UserSession.getSession().getUsername();
			NotificationUtils.saveNotification(fromVal, toVal, EmpReport.class, username, NotificationType.UPDATE);
			tx.commit();
			log.debug("persist successful");
		} catch (RuntimeException re) {
			tx.rollback();
			log.error("persist failed", re);
			throw re;
		}finally {
			session.close();
		}
	}

	public EmpReport findById(int id) {
		log.debug("getting EmpReport instance with id: " + id);
	Session session = HibernateUtil.getSession();
		try {
			
			EmpReport instance = (EmpReport) session.get(EmpReport.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}finally {
			session.close();
		}
	}

	public List<EmpReport> findAll() {
		log.debug("finding all Object instances");
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from " + EmpReport.class.getCanonicalName();
			Query queryObject = session.createQuery(queryString);
			List<EmpReport> genericList = queryObject.list();
			return genericList;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}finally {
			session.close();
		}
	}
	
	public List<EmpReport> findByProperty(String propertyName,
			Object value) {
		log.debug("finding Object instance with property: " + propertyName
				+ ", value: " + value);
		Session session = HibernateUtil.getSession();
		try {
			
			String queryString = "from " + EmpReport.class.getCanonicalName() + " as model where model."
					+ propertyName + "= ?";
			Query queryObject = session.createQuery(queryString);
			queryObject.setParameter(0, value);
			List<EmpReport> genericList = queryObject.list();
			
			return genericList;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public <T> List<EmpReport> findByCriteria(Map<String, String> properties) {
		Session session = HibernateUtil.getSession();
		try {
			ProjectionList projections = Projections.projectionList();
			for (String property : properties.keySet()) {
				String attribute = properties.get(property);
				projections.add(Projections.property(property), attribute);
			}
			Criteria cr = session
					.createCriteria(EmpReport.class)
					.setProjection(projections)
					.setResultTransformer(
							Transformers.aliasToBean(EmpReport.class));

			List<EmpReport> list = cr.list();

			return list;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}finally {
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
}
