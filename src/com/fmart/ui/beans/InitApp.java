package com.fmart.ui.beans;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import com.fmart.db.backup.BackupJob;
import com.fmart.db.backup.DBBackupScheduler;
import com.fmart.framework.hibernate.HibernateUtil;
import com.fmart.hibernate.dao.CompanyDao;
import com.fmart.hibernate.pojos.Company;
import com.fmart.hibernate.pojos.Employee;

/**
 * Servlet implementation class InitApp
 */

public class InitApp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InitApp() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		CompanyDao dao = new CompanyDao();
		dao.findById(0);
		

		/*try {
			BackupJob job = new BackupJob();
			job.execute(null);
			DBBackupScheduler.scheduleBackupTask();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}

	/*@Override
	public void destroy() {
		try {
			DBBackupScheduler.stopSchedule();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		super.destroy();
	}*/
}
