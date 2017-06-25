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
import com.fmart.hibernate.pojos.Roles;
import com.fmart.hibernate.pojos.Roles;
import com.fmart.ui.beans.UserSession;

/**
 * Dao object for domain model class Roles.
 * 
 * @see com.fmart.hibernate.pojos.Roles
 * @author Hibernate Tools
 */

public class RolesDao {

	private static final Logger log = LoggerFactory.getLogger(RolesDao.class);

	public void save(Roles transientInstance) {
		log.debug("persisting Roles instance");
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

	public void update(Roles transientInstance, Integer id) {
		log.debug("persisting Roles instance");
		Transaction tx = null;
		Session session = HibernateUtil.getSession();
		try {
			tx = session.beginTransaction();
			String fromVal = NotificationUtils.getJsonVal(findById(id));
			session.update(transientInstance);
			String toVal = NotificationUtils.getJsonVal(findById(id));
			String username = UserSession.getSession().getUsername();
			NotificationUtils.saveNotification(fromVal, toVal, Roles.class,
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

	public Roles findById(int id) {
		log.debug("getting Roles instance with id: " + id);
		Session session = HibernateUtil.getSession();
		try {

			Roles instance = (Roles) session.get(Roles.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public List<Roles> findAll() {
		log.debug("finding all Object instances");
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from " + Roles.class.getCanonicalName();
			Query queryObject = session.createQuery(queryString);
			List<Roles> genericList = queryObject.list();
			return genericList;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public List<Roles> findByProperty(String propertyName, Object value) {
		log.debug("finding Object instance with property: " + propertyName
				+ ", value: " + value);
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from " + Roles.class.getCanonicalName()
					+ " as model where model." + propertyName + "= ?";
			Query queryObject = session.createQuery(queryString);
			queryObject.setParameter(0, value);
			List<Roles> genericList = queryObject.list();

			return genericList;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public <T> List<Roles> findByCriteria(Map<String, String> properties) {
		Session session = HibernateUtil.getSession();
		try {
			ProjectionList projections = Projections.projectionList();
			for (String property : properties.keySet()) {
				String attribute = properties.get(property);
				projections.add(Projections.property(property), attribute);
			}
			Criteria cr = session
					.createCriteria(Roles.class)
					.setProjection(projections)
					.setResultTransformer(Transformers.aliasToBean(Roles.class));

			List<Roles> list = cr.list();

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
}
