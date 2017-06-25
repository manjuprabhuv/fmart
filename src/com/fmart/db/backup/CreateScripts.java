package com.fmart.db.backup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.fmart.ui.utils.FmartProperties;

public class CreateScripts {
	public static void create() throws IOException, InterruptedException {
		createFullBackupScript();
		createProdBackupScript();

	}

	private static void createFullBackupScript() throws IOException,
			InterruptedException {
		String str = "#!/bin/bash \n"
				+ "usrname=$1 \n"
				+ "host=$2 \n"
				+ "filename=$3 \n"
				+ "directory=$4 \n"
				+ "mkdir $directory \n"
				+ "pg_dump -U $usrname -h $host -w $OPENSHIFT_APP_NAME --schema=fmart --exclude-table-data=product_bck | gzip > $OPENSHIFT_DATA_DIR/$directory/$filename";

		File file = new File(FmartProperties.getValue("db.backup.script"));
		if (!file.exists()) {
			BufferedWriter bw = null;
			try {
				file.createNewFile();
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(str);
			} finally {
				if(bw!=null)
					bw.close();
			}
		}
		String[] command = { "chmod", "777",
				file.getAbsoluteFile().getAbsolutePath() };
		ProcessBuilder probuilder = new ProcessBuilder(command);
		probuilder.directory(new File(FmartProperties
				.getValue("db.backup.datadir")));
		Process process = probuilder.start();
		process.waitFor();

	}

	private static void createProdBackupScript() throws IOException,
			InterruptedException {
		String str = "#!/bin/bash \n"
				+ "usrname=$1 \n"
				+ "host=$2 \n"
				+ "filename=$3 \n"
				+ "directory=$4 \n"
				+ "pg_dump --schema=fmart -U $usrname -h $host -w $OPENSHIFT_APP_NAME --table=fmart.product_bck | gzip > $OPENSHIFT_DATA_DIR/$directory/$filename";

		File file = new File(
				FmartProperties.getValue("db.backup.product.script"));
		if (!file.exists()) {
			BufferedWriter bw = null;
			try {
				file.createNewFile();
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(str);
			} finally {
				bw.close();
			}
		}
		String[] command = { "chmod", "777",
				file.getAbsoluteFile().getAbsolutePath() };
		ProcessBuilder probuilder = new ProcessBuilder(command);
		probuilder.directory(new File(FmartProperties
				.getValue("db.backup.datadir")));
		Process process = probuilder.start();
		process.waitFor();

	}

}
