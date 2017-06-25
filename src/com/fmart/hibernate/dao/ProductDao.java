package com.fmart.hibernate.dao;

// Generated Apr 13, 2015 10:25:23 PM by Hibernate Tools 3.4.0.CR1

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import org.hibernate.internal.SessionImpl;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fmart.framework.hibernate.HibernateUtil;
import com.fmart.hibernate.dao.NotificationUtils.NotificationType;
import com.fmart.hibernate.pojos.Employee;
import com.fmart.hibernate.pojos.Product;
import com.fmart.hibernate.pojos.Product;
import com.fmart.ui.beans.UserSession;

/**
 * Dao object for domain model class Product.
 * 
 * @see com.fmart.hibernate.pojos.Product
 * @author Hibernate Tools
 */

public class ProductDao {

	private static final Logger log = LoggerFactory.getLogger(ProductDao.class);

	public void save(Product transientInstance) {
		log.debug("persisting Product instance");
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

	public void update(Product transientInstance, Integer id) {
		log.debug("persisting Product instance");
		Transaction tx = null;
		Session session = HibernateUtil.getSession();
		try {
			tx = session.beginTransaction();
			Product p = findById(id);
			if(transientInstance.getImagesrc()==null){
				if(p.getImagesrc()!=null){
					transientInstance.setImagesrc(p.getImagesrc());
					transientInstance.setImage(p.getImage());
				}
			}
			String fromVal = NotificationUtils.getJsonVal(p);
			session.update(transientInstance);

			tx.commit();			
			String toVal = NotificationUtils.getJsonVal(findById(id));
			String username = UserSession.getSession().getUsername();
			NotificationUtils.saveNotification(fromVal, toVal, Product.class,
					username, NotificationType.UPDATE);
			
			log.debug("persist successful");
		} catch (RuntimeException re) {
			tx.rollback();
			log.error("persist failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public Product findById(int id) {
		log.debug("getting Product instance with id: " + id);
		Session session = HibernateUtil.getSession();

		try {
			Product instance = (Product) session.get(Product.class, id);
			log.debug("get successful");
			initilizeProduct(instance);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public List<Product> findAll() {
		log.debug("finding all Object instances");
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from " + Product.class.getCanonicalName();
			Query queryObject = session.createQuery(queryString);
			List<Product> list = queryObject.list();
			initializeEmployeeList(list);
			return list;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public List<Product> findByProperty(String propertyName, Object value) {
		log.debug("finding Object instance with property: " + propertyName
				+ ", value: " + value);
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from " + Product.class.getCanonicalName()
					+ " as model where model." + propertyName + "= ?";
			Query queryObject = session.createQuery(queryString);
			queryObject.setParameter(0, value);
			List<Product> list = queryObject.list();
			initializeEmployeeList(list);
			return list;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public <T> List<Product> findByCriteria(Map<String, String> properties,int first, int pageSize) {
		Session session = HibernateUtil.getSession();
		try {
			ProjectionList projections = Projections.projectionList();
			for (String property : properties.keySet()) {
				String attribute = properties.get(property);
				projections.add(Projections.property(property), attribute);
			}
			Criteria cr = session
					.createCriteria(Product.class)
					.setProjection(projections)
					.setResultTransformer(
							Transformers.aliasToBean(Product.class));
			cr.setFirstResult(first);
			cr.setMaxResults(pageSize);

			List<Product> list = cr.list();
			initializeEmployeeList(list);
			return list;
			
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		} finally {
			session.close();
		}
	}
	
	public int countByProperty() {
		log.debug("counting number of records for Product");
		Connection conn = ((SessionImpl) HibernateUtil.getSession()).connection();
		Statement stmt = null;
		String sql = null;
		ResultSet rs = null;
		int records=0;
		try {
			stmt=conn.createStatement();
			sql = "SELECT count(*) count FROM fmart.product";
			rs = stmt.executeQuery(sql);
			if(rs.next())
				records= rs.getInt("count");
		} catch (SQLException re) {
			log.error("record count failed for Product", re);
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
	public void initializeEmployeeList(List<Product> list){		
		for (Product product : list) {
			initilizeProduct(product);
		}
	}
	
	private void initilizeProduct(Product p){
		Hibernate.initialize(p.getProductGrp());
	}
	
	private void updateProductImg(Product p,Session session){
		PreparedStatement pstmt = null;
		Connection conn = ((SessionImpl) session).connection();
		String sql = "update fmart.product set imagesrc=? where id=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setBytes(1, p.getImagesrc());
			pstmt.setInt(2, p.getId());
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
