package model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import model.exception.DatabaseException;

public class DatabaseAccess {

	private final static String DB_NAME = "city_lodge";

	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		// Registering the HSQLDB JDBC driver
		Class.forName("org.hsqldb.jdbc.JDBCDriver");

		/*
		 * Database files will be created in the "database" folder in the
		 * project. If no username or password is specified, the default SA user
		 * and an empty password are used
		 */
		Connection con = DriverManager.getConnection("jdbc:hsqldb:file:database/" + DB_NAME, "SA", "");
		return con;
	}

	public static void createTables() throws ClassNotFoundException, SQLException, DatabaseException {
		createRoomTable();
		createHiringRecordTable();
	}

	public static void truncateTables() throws ClassNotFoundException, SQLException, DatabaseException {
		try (Connection con = getConnection(); Statement stmt = con.createStatement();) {
			stmt.executeUpdate(QueryConstants.TRUNCATE_HIRING_RECORD);
			stmt.executeUpdate(QueryConstants.TRUNCATE_ROOM);
		}
	}

	private static void createRoomTable() throws SQLException, DatabaseException, ClassNotFoundException {
		try (Connection con = getConnection(); Statement stmt = con.createStatement();) {
			int result = stmt.executeUpdate(QueryConstants.CREATE_ROOM);

			if (result != 0) {
				throw new DatabaseException("Error occurred while creating Database Table: room");
			}
		}
	}

	private static void createHiringRecordTable() throws ClassNotFoundException, SQLException, DatabaseException {
		try (Connection con = getConnection(); Statement stmt = con.createStatement();) {
			int result = stmt.executeUpdate(QueryConstants.CREATE_HIRING_RECORD);

			if (result != 0) {
				throw new DatabaseException("Error occurred while creating Database Table: room");
			}
		}
	}
}
