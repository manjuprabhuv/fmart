package com.fmart.hibernate.dao;

// Generated Apr 13, 2015 10:25:23 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import java.util.Map;

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
import com.fmart.ui.beans.UserSession;

/**
 * Dao object for domain model class Employee.
 * 
 * @see com.fmart.hibernate.pojos.Employee
 * @author Hibernate Tools
 */

public class EmployeeDao {

	private static final Logger log = LoggerFactory
			.getLogger(EmployeeDao.class);

	public void save(Employee transientInstance) {
		log.debug("persisting Employee instance");
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
	
		try {
			
			tx = session.beginTransaction();
			session.save(transientInstance);
			tx.commit();
			log.debug("persist successful");
		} catch (RuntimeException re) {
			tx.rollback();
			log.error("persist failed", re);
			throw re;
		}finally{
			session.close();
		}
	}
	
	public void update(Employee transientInstance,Integer id) {
		log.debug("persisting Employee instance");
		Transaction tx = null;
	Session session = HibernateUtil.getSession();
		
		try {
			
			tx = session.beginTransaction();
			String fromVal = NotificationUtils.getJsonVal(findById(id));
			session.update(transientInstance);
			tx.commit();
			String toVal = NotificationUtils.getJsonVal(findById(id));
			String username = UserSession.getSession().getUsername();
			NotificationUtils.saveNotification(fromVal, toVal, Employee.class, username, NotificationType.UPDATE);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			tx.rollback();
			log.error("persist failed", re);
			throw re;
		}finally{
			session.close();
		}
	}

	public Employee findById(int id) {
		log.debug("getting Employee instance with id: " + id);
	Session session = HibernateUtil.getSession();
		try {
			
			Employee instance = (Employee) session.get(Employee.class, id);
			initializeEmployee(instance);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}finally{
			session.close();
		}
	}

	public List<Employee> findAll() {
		log.debug("finding all Object instances");
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from " + Employee.class.getCanonicalName();
			Query queryObject = session.createQuery(queryString);
			List<Employee> list = queryObject.list();
			initializeEmployeeList(list);
			return list;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}finally{
			session.close();
		}
	}
	
	public List<Employee> findByProperty(String propertyName,
			Object value) {
		log.debug("finding Object instance with property: " + propertyName
				+ ", value: " + value);
		Session session = HibernateUtil.getSession();
		try {
			
			String queryString = "from " + Employee.class.getCanonicalName() + " as model where model."
					+ propertyName + "= ?";
			Query queryObject = session.createQuery(queryString);
			queryObject.setParameter(0, value);
			List<Employee> list = queryObject.list();
			initializeEmployeeList(list);
			return list;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public <T> List<Employee> findByCriteria(Map<String, String> properties) {
		Session session = HibernateUtil.getSession();
		try {
			ProjectionList projections = Projections.projectionList();
			for (String property : properties.keySet()) {
				String attribute = properties.get(property);
				projections.add(Projections.property(property), attribute);
			}
			Criteria cr = session
					.createCriteria(Employee.class)
					.setProjection(projections)
					.setResultTransformer(
							Transformers.aliasToBean(Employee.class));

			List<Employee> list = cr.list();
			initializeEmployeeList(list);
			return list;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}finally{
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
	public void initializeEmployeeList(List<Employee> list){		
		for (Employee employee : list) {
			initializeEmployee(employee);
		}
	}
	public void initializeEmployee(Employee e){		
		Hibernate.initialize(e.getEmpReports());
		for(EmpReport report :e.getEmpReports()){
			Hibernate.initialize(report.getReport());
			
		}
		Hibernate.initialize(e.getEmpRoles());
		Hibernate.initialize(e.getCompany());
	}
}
