package com.fmart.hibernate.dao;

// Generated Apr 18, 2015 9:52:07 PM by Hibernate Tools 3.4.0.CR1

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fmart.framework.hibernate.HibernateUtil;
import com.fmart.hibernate.pojos.DbBackupJob;

/**
 * Home object for domain model class DbBackupJob.
 * 
 * @see com.fmart.hibernate.pojos.DbBackupJob
 * @author Hibernate Tools
 */

public class DbBackupJobDao {
	private static final String DROP_TABLE = "DROP TABLE IF EXISTS fmart.product_bck";
	private static final String CREATE_PRODUCT_BCK_TABLE = "create table fmart.product_bck as select id,imagesrc from fmart.product";
	private static final String TRUNCATE_IMAGESRC_COLUMN = "update fmart.product set imagesrc =null";
	private static final String RESTORE_IMAGESRC_COLUMN = "UPDATE fmart.product SET imagesrc = subquery.imagesrc FROM (select id,imagesrc from fmart.product_bck) AS subquery WHERE fmart.product.id=subquery.id";
	private static final String GET_ALL_COMPLETED_JOBS = "from com.fmart.hibernate.pojos.DbBackupJob job where job.status='COMPLETED' order by job.endts desc";
	private static final Logger log = LoggerFactory
			.getLogger(DbBackupJobDao.class);

	public Session getSession() {
		return HibernateUtil.getSession();
	}

	public void dropProductBckTable() {
		Session session = HibernateUtil.getSession();
		Transaction tx;
		tx = session.beginTransaction();
		SQLQuery query = session.createSQLQuery(DROP_TABLE);
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public void createProductBckTable() {
		Session session = HibernateUtil.getSession();
		Transaction tx;
		tx = session.beginTransaction();
		SQLQuery query = session.createSQLQuery(CREATE_PRODUCT_BCK_TABLE);
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public void truncateImgeSrc() {
		Session session = HibernateUtil.getSession();
		Transaction tx;
		tx = session.beginTransaction();
		SQLQuery query = session.createSQLQuery(TRUNCATE_IMAGESRC_COLUMN);
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public void restoreImagesrc() {
		Session session = HibernateUtil.getSession();
		Transaction tx;
		tx = session.beginTransaction();
		SQLQuery query = session.createSQLQuery(RESTORE_IMAGESRC_COLUMN);
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public void clearOldJobs(String datadir) throws IOException, InterruptedException {
		// get all jobs
		Session session = HibernateUtil.getSession();
		Transaction tx;
		tx = session.beginTransaction();
		Query query = session.createQuery(GET_ALL_COMPLETED_JOBS);
		List<DbBackupJob> list = query.list();

		if (list.size() > 3) {
			int count = 0;
			for (DbBackupJob job : list) {
				if (count > 2) {
					String[] command = { "rm", "-rf" ,job.getDirectory() };
					ProcessBuilder probuilder = new ProcessBuilder(command);	
					probuilder.directory(new File(datadir));
					Process process = probuilder.start();
					process.waitFor();
					session.delete(job);
				}
				count++;
			}
		}	
		tx.commit();
		session.close();
	}

	public void save(DbBackupJob transientInstance) {
		log.debug("persisting DbBackupJob instance");
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
		} finally {
			session.close();
		}
	}

	public void delete(DbBackupJob persistentInstance) {
		log.debug("removing DbBackupJob instance");
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.delete(persistentInstance);
			tx.commit();
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public void update(DbBackupJob transientInstance, Integer id) {
		log.debug("persisting DbBackupJob instance");
		Transaction tx = null;
		Session session = HibernateUtil.getSession();

		try {

			tx = session.beginTransaction();
			session.update(transientInstance);
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

	public DbBackupJob findById(int id) {
		log.debug("getting DbBackupJob instance with id: " + id);
		Session session = HibernateUtil.getSession();

		try {
			DbBackupJob instance = (DbBackupJob) session.get(DbBackupJob.class,
					id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		} finally {
			session.close();
		}
	}

	public List<DbBackupJob> findAll() {
		log.debug("finding all Object instances");
		Session session = HibernateUtil.getSession();
		try {

			String queryString = "from " + DbBackupJob.class.getCanonicalName();
			Query queryObject = session.createQuery(queryString);
			List<DbBackupJob> list = queryObject.list();
			return list;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		} finally {
			session.close();
		}
	}
}
