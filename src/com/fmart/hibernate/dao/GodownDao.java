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
import com.fmart.hibernate.pojos.Godown;
import com.fmart.ui.beans.UserSession;

/**
 * Dao object for domain model class Godown.
 * 
 * @see com.fmart.hibernate.pojos.Godown
 * @author Hibernate Tools
 */

public class GodownDao {

	private static final Logger log = LoggerFactory
			.getLogger(GodownDao.class);

	public void save(Godown transientInstance) {
		log.debug("persisting Godown instance");
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

	public void update(Godown transientInstance, Integer id) {
		log.debug("persisting Godown instance");
		Transaction tx = null;
		Session session = HibernateUtil.getSession();
		try {
			tx = session.beginTransaction();
			String fromVal = NotificationUtils.getJsonVal(findById(id));
			session.update(transientInstance);
			tx.commit();
			String toVal = NotificationUtils.getJsonVal(findById(id));
			String username = UserSession.getSession().getUsername();
			NotificationUtils.saveNotification(fromVal, toVal,
					Godown.class, username, NotificationType.UPDATE);
			
			log.debug("persist successful");
		} catch (RuntimeException re) {
			tx.rollback();
			log.error("persist failed", re);
			throw re;
		} finally {
			session.close();
		}
	}


	public Godown findById(int id) {
		log.debug("getting Godown instance with id: " + id);
		Session session = HibernateUtil.getSession();

		try {
			Godown instance = (Godown) session
					.get(Godown.class, id);
			log.debug("get successful");
			initializeGodown(instance);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public List<Godown> findAll() {
		log.debug("finding all Object instances");
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from " + Godown.class.getCanonicalName();
			Query queryObject = session.createQuery(queryString);
			List<Godown> genericList = queryObject.list();
			initializeGodownList(genericList);
			return genericList;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public List<Godown> findByProperty(String propertyName, Object value) {
		log.debug("finding Object instance with property: " + propertyName
				+ ", value: " + value);
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from " + Godown.class.getCanonicalName()
					+ " as model where model." + propertyName + "= ?";
			Query queryObject = session.createQuery(queryString);
			queryObject.setParameter(0, value);
			List<Godown> genericList = queryObject.list();
			initializeGodownList(genericList);
			return genericList;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public <T> List<Godown> findByCriteria(Map<String, String> properties) {
		Session session = HibernateUtil.getSession();
		try {
			ProjectionList projections = Projections.projectionList();
			for (String property : properties.keySet()) {
				String attribute = properties.get(property);
				projections.add(Projections.property(property), attribute);
			}
			Criteria cr = session
					.createCriteria(Godown.class)
					.setProjection(projections)
					.setResultTransformer(
							Transformers.aliasToBean(Godown.class));

			List<Godown> list = cr.list();
			initializeGodownList(list);
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
	
	private void initializeGodownList(List<Godown> list){
		for (Godown godown : list) {
			initializeGodown(godown);
		}
	}
	
	public void initializeGodown(Godown godown){
		Hibernate.initialize(godown.getCompany());
	
	}
}
