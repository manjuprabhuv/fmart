package com.fmart.hibernate.dao;

// Generated Apr 13, 2015 10:25:23 PM by Hibernate Tools 3.4.0.CR1

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
import com.fmart.hibernate.pojos.GodownInventory;
import com.fmart.hibernate.pojos.Product;
import com.fmart.ui.beans.UserSession;

/**
 * Dao object for domain model class GodownInventory.
 * 
 * @see com.fmart.hibernate.pojos.GodownInventory
 * @author Hibernate Tools
 */

public class GodownInventoryDao {

	private static final Logger log = LoggerFactory
			.getLogger(GodownInventoryDao.class);

	public void save(GodownInventory transientInstance) {
		log.debug("persisting GodownInventory instance");
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

	public void update(GodownInventory transientInstance) {
		log.debug("persisting GodownInventory instance");
		Transaction tx = null;
		Session session = HibernateUtil.getSession();
		try {
			tx = session.beginTransaction();
			// String fromVal = NotificationUtils.getJsonVal(findById(id));
			session.update(transientInstance);
			tx.commit();
			// String toVal = NotificationUtils.getJsonVal(findById(id));
			String username = UserSession.getSession().getUsername();
			// NotificationUtils.saveNotification(fromVal, toVal,
			// GodownInventory.class, username, NotificationType.UPDATE);

			log.debug("persist successful");
		} catch (RuntimeException re) {
			tx.rollback();
			log.error("persist failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public GodownInventory findById(int id) {
		log.debug("getting GodownInventory instance with id: " + id);
		Session session = HibernateUtil.getSession();

		try {
			GodownInventory instance = (GodownInventory) session.get(
					GodownInventory.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public List<GodownInventory> findAll() {
		log.debug("finding all Object instances");
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from "
					+ GodownInventory.class.getCanonicalName();
			Query queryObject = session.createQuery(queryString);
			List<GodownInventory> genericList = queryObject.list();
			return genericList;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public List<GodownInventory> findByProperty(String propertyName,
			Object value) {
		log.debug("finding Object instance with property: " + propertyName
				+ ", value: " + value);
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from "
					+ GodownInventory.class.getCanonicalName()
					+ " as model where model." + propertyName + "= ?";
			Query queryObject = session.createQuery(queryString);
			queryObject.setParameter(0, value);
			List<GodownInventory> genericList = queryObject.list();

			return genericList;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public <T> List<GodownInventory> findByCriteria(
			Map<String, String> properties) {
		Session session = HibernateUtil.getSession();
		try {
			ProjectionList projections = Projections.projectionList();
			for (String property : properties.keySet()) {
				String attribute = properties.get(property);
				projections.add(Projections.property(property), attribute);
			}
			Criteria cr = session
					.createCriteria(GodownInventory.class)
					.setProjection(projections)
					.setResultTransformer(
							Transformers.aliasToBean(GodownInventory.class));

			List<GodownInventory> list = cr.list();

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
	
	public List<GodownInventory> getInventoryForGodown(int id){
		List<GodownInventory> list = new ArrayList<GodownInventory>();
		Session session = HibernateUtil.getSession();
		try {
			
			String queryString = "from "
					+ GodownInventory.class.getCanonicalName()
					+ " as model where model.id.godownId=?";
			Query queryObject = session.createQuery(queryString);
			queryObject.setParameter(0, id);
			list = queryObject.list();
			initializeGodownInventoryList(list);
		} catch (Exception e) {
			
		} finally {
			
			session.close();
		}
		return list;	
		
	}

	public void saveInventory(List<GodownInventory> inventory, String action,
			int godownId) throws Exception {
		Transaction tx = null;
		Session session = HibernateUtil.getSession();
		tx = session.beginTransaction();
		try{
			for (GodownInventory godownInventory : inventory) {
				Product product = godownInventory.getProduct();

				int initialProductCount = 0;
				String queryString = "from "
						+ GodownInventory.class.getCanonicalName()
						+ " as model where model.id.productId= ? and model.id.godownId=? and model.id.customised=?";
				Query queryObject = session.createQuery(queryString);
				queryObject.setParameter(0, product.getId());
				queryObject.setParameter(1, godownId);
				queryObject.setParameter(2, godownInventory.getId()
						.isCustomised());
				List<GodownInventory> genericList = queryObject.list();
				// check if product exists. get the count.
				if (genericList.size() > 0) {
					GodownInventory gi = genericList.get(0);
					initialProductCount = gi.getCount();
					if ("INWARD".equalsIgnoreCase(action)) {
						int finalProductCount = initialProductCount
								+ godownInventory.getCount();
						gi.setCount(finalProductCount);
						session.update(gi);
						
					} else if ("OUTWARD".equalsIgnoreCase(action)) {
						if (initialProductCount == 0
								|| (initialProductCount
										- godownInventory.getCount() < 0)) {
							
							
							 throw new Exception();
						} else {
							int finalProductCount = initialProductCount
									- godownInventory.getCount();
							gi.setCount(finalProductCount);
							session.update(gi);
							
						}

					}

				} else {
					// add the product
					if ("INWARD".equalsIgnoreCase(action)) {
					godownInventory.getId().setGodownId(godownId);
					godownInventory.getId().setProductId(
							godownInventory.getProduct().getId());
					save(godownInventory);
					}else if ("OUTWARD".equalsIgnoreCase(action)) {
						
						 throw new Exception();
					}

				}
				//session.clear();

			}
		} catch (Exception e) {
			tx.rollback();
		} finally {
			tx.commit();
			session.close();
		}

		// if present , then increment the count if action is inward. Decrement
		// if count is outward
		// if not present , then add if inward. throw error if outward
		//

	}
	
	private void initializeGodownInventoryList(List<GodownInventory> list){
		for (GodownInventory inventory : list) {
			initializeGodownInventory(inventory);
		}
	}
	
	public void initializeGodownInventory(GodownInventory godown){
		Hibernate.initialize(godown.getProduct());
		Hibernate.initialize(godown.getProduct().getProductGrp());
	
	}
}
