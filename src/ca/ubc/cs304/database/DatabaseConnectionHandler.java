package ca.ubc.cs304.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ca.ubc.cs304.model.BranchModel;
import ca.ubc.cs304.model.VehicleType;

/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
	private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
//	private static final String ORACLE_URL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";
	private static final String EXCEPTION_TAG = "[EXCEPTION]";
	private static final String WARNING_TAG = "[WARNING]";
	private final static String NEWLINE = "\n";
	
	private Connection connection = null;
	
	public DatabaseConnectionHandler() {
		try {
			// Load the Oracle JDBC driver
			// Note that the path could change for new drivers
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}
	
	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	// the following methods are SQL operations used for our projects
	public String executeSelect(String sql) {
		String res = new String();
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				res.concat(rs.getString("TABLE_NAME"));
			}
		} catch (SQLException e) {
			res = e.getMessage();
			System.out.println(EXCEPTION_TAG + " " + res);
			return res;
		}
		return res;
	}


	public void rentVehicle() {

	};
	public void returnVehicle() {

	};
	public void generateReport() {

	};

	public int findNumOfAvailableVehicles(String type, String location, String timeStart, String timeEnd) {
//		type = "SUV";
//		location = "1250 Granville St";
//		timeStart = "2019-12-14 13:00";
//		timeEnd = "2019-12-18 17:45";
		String sql = "SELECT * FROM Vehicles WHERE status = 'available'";
		int count = 0;
		try {
			PreparedStatement prepState;
			if (!timeStart.equals("") && !timeEnd.equals("")) {
				sql =   "SELECT COUNT(*) AS total \n" +
						"FROM Vehicles\n" +
						"WHERE Vehicles.vlicense NOT IN(\n" +
						"    SELECT Rentals.vlicense\n" +
						"    FROM Vehicles, Rentals\n" +
						"    WHERE (Vehicles.vlicense = Rentals.vlicense) AND\n" +
						"        (Rentals.fromDateTime < TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI')) OR\n" +
						"        ((Rentals.fromDateTime < TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI')) AND\n" +
						"            TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI') < Rentals.toDateTime) OR\n" +
						"        (TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI') < Rentals.toDateTime)\n" +
						")";
				if (!type.equals("")) {
					sql += " AND VTNAME = '" + type + "\'";
				}
				if (!location.equals("")) {
					sql += " AND LOCATION = '" + location + "\'";
				}
				prepState = connection.prepareStatement(sql);
				prepState.setString(1, timeEnd);
				prepState.setString(2, timeStart);
				prepState.setString(3, timeEnd);
				prepState.setString(4, timeStart);
			} else {
				if (!type.equals("")) {
					sql += " AND VTNAME = '" + type + "\'";
				}
				if (!location.equals("")) {
					sql += " AND LOCATION = '" + location + "\'";
				}
				sql = "SELECT COUNT(*) AS total FROM (" + sql + ")";
				prepState = connection.prepareStatement(sql);
			}

			ResultSet rs = prepState.executeQuery();
			while (rs.next()) {
				count = rs.getInt("total");
			}


		} catch (Exception e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return count;
	}

	public List<String[]> viewAvailableVehicles(String type, String location, String timeStart, String timeEnd) {
		List<String[]> res = new ArrayList<>();
		String[] colName = {"vlicense", "make", "model", "year", "color", "status", "vtname", "location", "city"};
		res.add(colName);
		try {
			String sql = "SELECT * FROM Vehicles WHERE status = 'available'";
			PreparedStatement prepState;
			if (!timeStart.equals("") && !timeEnd.equals("")) {
				prepState = connection.prepareStatement("SELECT * \n" +
						"FROM Vehicles\n" +
						"WHERE Vehicles.vlicense NOT IN(\n" +
						"    SELECT Rentals.vlicense\n" +
						"    FROM Vehicles, Rentals\n" +
						"    WHERE (Vehicles.vlicense = Rentals.vlicense) AND\n" +
						"        (Rentals.fromDateTime < TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI')) OR\n" +
						"        ((Rentals.fromDateTime < TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI')) AND\n" +
						"            TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI') < Rentals.toDateTime) OR\n" +
						"        (TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI') < Rentals.toDateTime)\n" +
						")");
				if (!type.equals("")) {
					sql += " AND VTNAME = '" + type + "\'";
				}
				if (!location.equals("")) {
					sql += " AND LOCATION = '" + location + "\'";
				}
				prepState.setString(1, timeEnd);
				prepState.setString(2, timeStart);
				prepState.setString(3, timeEnd);
				prepState.setString(4, timeStart);
			} else {
				if (!type.equals("")) {
					sql += " AND VTNAME = '" + type + "\'";
				}
				if (!location.equals("")) {
					sql += " AND LOCATION = '" + location + "\'";
				}
				prepState = connection.prepareStatement(sql);
			}
			ResultSet rs = prepState.executeQuery();
			while (rs.next()) {
				String[] row = new String[colName.length];
				row[0] = rs.getString("vlicense");
				row[1] = rs.getString("make");
				row[2] = rs.getString("model");
				row[3] = rs.getString("year");
				row[4] = rs.getString("color");
				row[5] = rs.getString("status");
				row[6] = rs.getString("vtname");
				row[7] = rs.getString("location");
				row[8] = rs.getString("city");
				res.add(row);
			}
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return res;
    };

	public void makeReservation() {

	};

	//Viewing all tables in the database
	public List<String[]> viewAllTables() throws SQLException{
		String[] colName = {"TABLE_NAME"};
		List<String[]> res = new ArrayList<>();
		res.add(colName);

		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT table_name FROM user_tables");
			while (rs.next()) {
				String[] row = new String[colName.length];
				row[0] = rs.getString("TABLE_NAME");
				res.add(row);
			}
		} catch (SQLException e) {
			throw e;
		}
		return res;
	}

//	public void insertIntoTable(String table, ) {
//
//	}

	public void deleteBranch(int branchId) {
		try {
			PreparedStatement ps = connection.prepareStatement("DELETE FROM branch WHERE branch_id = ?");
			ps.setInt(1, branchId);
			
			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				System.out.println(WARNING_TAG + " Branch " + branchId + " does not exist!");
			}
			
			connection.commit();
	
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}
	
	public void insertBranch(BranchModel model) {
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO branch VALUES (?,?,?,?,?)");
			ps.setInt(1, model.getId());
			ps.setString(2, model.getName());
			ps.setString(3, model.getLocation());
			ps.setString(4, model.getCity());
			if (model.getPhoneNumber() == 0) {
				ps.setNull(5, java.sql.Types.INTEGER);
			} else {
				ps.setInt(5, model.getPhoneNumber());
			}

			ps.executeUpdate();
			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}
	
	public BranchModel[] getBranchInfo() {
		ArrayList<BranchModel> result = new ArrayList<BranchModel>();
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM branch");
		
//    		// get info on ResultSet
//    		ResultSetMetaData rsmd = rs.getMetaData();
//
//    		System.out.println(" ");
//
//    		// display column names;
//    		for (int i = 0; i < rsmd.getColumnCount(); i++) {
//    			// get column name and print it
//    			System.out.printf("%-15s", rsmd.getColumnName(i + 1));
//    		}
			
			while(rs.next()) {
				BranchModel model = new BranchModel(rs.getString("branch_addr"),
													rs.getString("branch_city"),
													rs.getInt("branch_id"),
													rs.getString("branch_name"),
													rs.getInt("branch_phone"));
				result.add(model);
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}	
		
		return result.toArray(new BranchModel[result.size()]);
	}
	
	public void updateBranch(int id, String name) {
		try {
		  PreparedStatement ps = connection.prepareStatement("UPDATE branch SET branch_name = ? WHERE branch_id = ?");
		  ps.setString(1, name);
		  ps.setInt(2, id);
		
		  int rowCount = ps.executeUpdate();
		  if (rowCount == 0) {
		      System.out.println(WARNING_TAG + " Branch " + id + " does not exist!");
		  }
	
		  connection.commit();
		  
		  ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}	
	}



	
	public boolean login(String username, String password) {
		try {
			if (connection != null) {
				connection.close();
			}
	
			connection = DriverManager.getConnection(ORACLE_URL, username, password);
			connection.setAutoCommit(false);
	
			System.out.println("\nConnected to Oracle!");
			return true;
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return false;
		}
	}

	private void rollbackConnection() {
		try  {
			connection.rollback();	
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}
}
