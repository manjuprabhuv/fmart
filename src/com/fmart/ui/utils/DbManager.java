package com.fmart.ui.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbManager {

	private static Connection conn = null;

	public static Connection getConnection() {
			try {

				Class.forName("org.postgresql.Driver");
				conn = DriverManager.getConnection(
						"jdbc:postgresql://552577f95973ca7bba000224-tesla111.rhcloud.com:54196/fmart", "adminbr8zvnm",
						"fFE4X99mXBM7");

			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return conn;
	}
}