package com.fmart.db.backup;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fmart.hibernate.dao.DbBackupJobDao;
import com.fmart.hibernate.pojos.DbBackupJob;
import com.fmart.ui.utils.FmartProperties;

public class BackupJob implements Job {
	private static final Logger log = LoggerFactory.getLogger(BackupJob.class);

	enum STATUS {
		INPROGRESS, COMPLETED
	}

	DbBackupJobDao dao = new DbBackupJobDao();
	DbBackupJob job;
	static String output = "Started";
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			CreateScripts.create();
			log.debug("starting cron execute");
			log.debug("Adding Job entry");
			addJobEntry();
			log.debug("droping product backup table");
			dropProductBckTable();
			log.debug("recreating product bck table");
			createProductBckTable();
			log.debug("truncate Imagesrc");
			truncateImgeSrc();
			log.debug("Backing up all table except product_bck");
			backupDB();
			log.debug("Backing up product_bck table");
			backupProductBckDB();
			log.debug("Restoring product image src");
			restoreProductImagesrc();
			log.debug("droping product backup table");
			dropProductBckTable();
			log.debug("Clearing old logs");
			clearOldJobs();
			log.debug("Updating job table");
			updateJob();
		} catch (Exception e) {
			log.error("Error occoured while running cron jobs");
			e.printStackTrace();
		}

	}

	private void updateJob() {
		this.job.setEndts(new Date());
		this.job.setStatus(STATUS.COMPLETED.toString());
		dao.save(this.job);
	}

	private void addJobEntry() {
		this.job = new DbBackupJob();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date curDate = new Date();
		job.setCronName("Db backup for " + format.format(curDate));
		job.setStatus(STATUS.INPROGRESS.toString());
		job.setStartts(curDate);
		job.setDirectory("dbbackup-" + format.format(curDate));
		dao.save(job);

	}

	private void dropProductBckTable() {
		dao.dropProductBckTable();
	}

	private void createProductBckTable() {
		dao.createProductBckTable();
	}

	private void truncateImgeSrc() {
		dao.truncateImgeSrc();
	}

	private static final String PRODUCT_BACKUP = "PRODUCT_TYPE";
	private static final String FULL_BACKUP = "FULL_TYPE";

	private void callBackupScript(String type) throws IOException,
			InterruptedException {
		log.debug("Backup of "+type + " started");
		String script = "";
		String filename = "fmart";
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date curDate = new Date();
		if (type.equalsIgnoreCase(FULL_BACKUP)) {
			script = FmartProperties.getValue("db.backup.script");
			filename = filename + "-full" + format.format(curDate);
			job.setFullBckFilename(filename);
		} else if (type.equalsIgnoreCase(PRODUCT_BACKUP)) {
			script = FmartProperties.getValue("db.backup.product.script");
			filename = filename + "-product" + format.format(curDate);
			job.setProdBckFilename(filename);
		}
		String username = FmartProperties.getValue("db.backup.username");
		String server = FmartProperties.getValue("db.backup.server");
		String datadir = FmartProperties.getValue("db.backup.datadir");
		String bckupdir = job.getDirectory();
		String[] command = { script, username, server, filename, bckupdir };
		ProcessBuilder probuilder = new ProcessBuilder(command);
		probuilder.directory(new File(datadir));
		Process process = probuilder.start();
		process.waitFor();
		log.debug("Backup of "+type + " done");
	}

	private void backupDB() throws IOException, InterruptedException {
		callBackupScript(FULL_BACKUP);
	}

	private void backupProductBckDB() throws IOException, InterruptedException {
		callBackupScript(PRODUCT_BACKUP);
	}

	private void restoreProductImagesrc() {
		dao.restoreImagesrc();
	}

	private void clearOldJobs() throws IOException, InterruptedException {
		dao.clearOldJobs(FmartProperties.getValue("db.backup.datadir"));

	}

}
