package com.fmart.db.backup;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class DBBackupScheduler{

	static Scheduler scheduler ;
	public static void scheduleBackupTask() throws SchedulerException {
		JobDetail job = JobBuilder.newJob(BackupJob.class)
				.withIdentity("DBBackupJob", "dbgroup").build();
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("DBBackupJobTriggerName", "dbgroup")
				.withSchedule(
					CronScheduleBuilder.cronSchedule("0 0 12 * * ?"))
				.build();
		
				
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.start();
		scheduler.scheduleJob(job, trigger);
		
	 
	}
	public static void stopSchedule() throws SchedulerException{
		scheduler.shutdown(false);
	}


}
