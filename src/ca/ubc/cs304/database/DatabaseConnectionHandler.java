package ca.ubc.cs304.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.ubc.cs304.helper.DateHelper;
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


	public void rentVehicle() throws SQLException{

	};
	public void returnVehicle(String vliense, String returnTime, String fullTank) throws SQLException, ParseException {
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT fromDateTime,odometer,confNo,vtname, FEATURES,wrate,drate,hrate,krate,wirate,dirate,hirate, RID\n" +
					"FROM Vehicles V, Rentals R, VehicleTypes T\n" +
					"WHERE V.status = 'rent' AND V.vlicense = R.vlicense AND V.vlicense = 'R20934493' AND V.vtname = T.vtname;");
			ResultSet rs = stmt.executeQuery();
			Integer odometer = null;
			String feature = new String();
			int hrate = 0;
			int drate = 0;
			int wrate = 0;
			double values = 0;
			int rID = 0;
			String fromTime = "";
			while (rs.next()) {
				odometer = rs.getInt("odometer");
				feature = rs.getString("FEATURES");
				hrate = rs.getInt("hrate");
				drate = rs.getInt("drate");
				wrate = rs.getInt("wrate");
				fromTime = rs.getString("fromDateTime");
				rID = rs.getInt("RID");

			}
			Date fromDate = DateHelper.parse(fromTime);
			Date returnDate = DateHelper.parse(returnTime);
			if (odometer == null) {
				throw new SQLException("odometer reading is wrong");
			}

			if (feature.equals("hourly")) {
				values = (double) (hrate * DateHelper.diffInHours(returnDate, returnDate));
			} else if (feature.equals("weekly")) {
				values = (double) (wrate * DateHelper.diffInWeeks(returnDate, fromDate));
			} else if (feature.equals("daily")) {
				values = (double) (drate * DateHelper.diffInDays(returnDate, fromDate));
			}

			PreparedStatement prepState = connection.prepareStatement(
					"Update Vehicles\n" +
							"SET status ='avaialble', odometer = ?\n" +
							"Where vlicense =?;");

			prepState.setInt(1, odometer);
			prepState.setString(2, vliense);
			prepState.executeUpdate();
			connection.commit();

			prepState = connection.prepareStatement("INSERT INTO Returns\n" +
					"VALUES(?,TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI'),?,?,?);");
			prepState.setInt(1, rID);
			prepState.setString(2, returnTime);
			prepState.setInt(3, odometer);
			prepState.setString(4, fullTank);
			prepState.setDouble(5, values);
		} catch (SQLException e) {
			throw e;
		} catch (ParseException e) {
			throw e;
		}
	}

	public List<String[]> rentVehicle(String vlicense, String dlicense, String fromTime, String endTime,String odometer, String cardName,String cardNo,String ExpDate, boolean hasReservation, String confoNo) throws SQLException{  
		List<String[]> res = new ArrayList<>();
		String[] colName = {"confoNo", "date of reservation", "vtname", "location", "endTime"};
		res.add(colName); 		
		try {
			connection.setAutoCommit(false); // atomic transaction		
			// Cheking if the vehicle to rent is available		
			String check = "SELECT COUNT(*) AS total\n" + 
					"FROM Vehicles\n" + 
					"WHERE vlicense = ? AND status = 'available'";
			int count = 0;
			PreparedStatement prepCheck;
			prepCheck= connection.prepareStatement(check);
			prepCheck.setString(1, vlicense);
			ResultSet cr = prepCheck.executeQuery();
			while(cr.next()) {
			count = cr.getInt("total");		
			}
			if(count==0) {
				throw new SQLException("The car is already rented.");
			}
			
			// get the vehicle's type name
			String vtname = null;
			String location =null;
			String getDetails = "SELECT vtname AS name,location AS loc\n" + 
					"FROM Vehicles\n" + 
					"WHERE vlicense = ?";
			
			PreparedStatement prepGetName;
			prepGetName= connection.prepareStatement(getDetails);
			prepGetName.setString(1, vlicense);
			ResultSet nameResult = prepGetName.executeQuery();
			
			while(nameResult.next()) {
			 vtname = nameResult.getString("name");	
			 location = nameResult.getString("loc");
			}
			
			int actualConfo = -5;
			
			// case where a reservation was made
			if(hasReservation) {
				actualConfo = Integer.parseInt(confoNo);
			}
			// case where a reservation were not made
			else {	
				String getConfo = "select SEQ_CONFNO.nextval from DUAL";
				PreparedStatement ps = connection.prepareStatement(getConfo);
				ResultSet number = ps.executeQuery();
				if(number.next()) {
					actualConfo = number.getInt(1);
					confoNo = Integer.toString(actualConfo);
					PreparedStatement newConfo = connection.prepareStatement(
							"INSERT INTO Reservations\n" +
							   "VALUES(?, ?, ?, TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI'), TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI'))");
					newConfo.setInt(1, actualConfo);
					newConfo.setString(2, vtname);
					newConfo.setString(3, dlicense);
					newConfo.setString(4, fromTime);
					newConfo.setString(5, endTime);
					newConfo.executeUpdate();
					connection.commit();// commit the change first otherwise sql might reject the rental insertion later on 
				}
			}
				
			// Generate next rentalId
				int rentalId = -5;
				String getRentalId = "select SEQ_RENTALID.nextval from DUAL";
				PreparedStatement newRentalId = connection.prepareStatement(getRentalId);
				ResultSet rentalResult = newRentalId.executeQuery();
				if(rentalResult.next()) {
					rentalId = rentalResult.getInt(1);
				}
				
				String updateRental = "INSERT INTO Reservations\n" +
						   "VALUES(?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement rental = connection.prepareStatement(updateRental);
				rental.setInt(1,rentalId);
				rental.setString(2,vlicense);
				rental.setString(3,dlicense);
				rental.setString(4,fromTime);
				rental.setString(5,endTime);
				rental.setInt(6,Integer.parseInt(odometer));
				rental.setString(7,cardName);
				rental.setString(8,cardNo);
				rental.setString(9,ExpDate);
				rental.setInt(10,actualConfo);
				
				String updateVehicle = "Update Vehicles\n" + 
						"Set status='rented'\n" + 
						"WHERE vlicense = ?";
				PreparedStatement prepState;
				prepState = connection.prepareStatement(updateVehicle);
				prepState.setString(1, vlicense);
				prepState.executeUpdate();
				connection.commit();
				
				String[] row = new String[colName.length];
				row[0] = String.valueOf(confoNo);
				row[1] = fromTime;
				row[2] = vtname;
				row[3] = location;
				row[4] = endTime;
				res.add(row);	
				
		} catch (Exception e) {
			connection.rollback();
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return res;
		};
	
	public List<String[]> getReservationInfo(String confoNo) throws SQLException{
		List<String[]> res = new ArrayList<>();
		String[] colName = {"cartype", "dlicense", "fromtime", "totime"};
		res.add(colName);
		try {
			String sql = "SELECT cartype, dlicense, fromDateTime, toDateTime as Info\n" + 
					"FROM Reservations R\n" + 
					"WHERE R.confNo = ?";
			PreparedStatement prepState;
			prepState = connection.prepareStatement(sql);
			prepState.setString(1, confoNo);
			ResultSet rs = prepState.executeQuery();
			while (rs.next()) {
				String[] row = new String[colName.length];
				row[0] = rs.getString("cartype");
				row[1] = rs.getString("dlicense");
				row[2] = rs.getString("fromDateTime");
				row[3] = rs.getString("toDateTime");
				res.add(row);
			}
		} catch (Exception e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return res;
		}
	
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

	public String makeReservation(String vtname, String dlicense, String fromTime, String endTime) throws SQLException{
		try {
			int nextConf = 0;
			String sql = "select SEQ_CONFNO.nextval from DUAL";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				 nextConf = rs.getInt(1);

			PreparedStatement prepState = connection.prepareStatement(
					"INSERT INTO Reservations\n" +
					   "VALUES(?, ?, ?, TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI'), TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI'))");
			prepState.setInt(1, nextConf);
			prepState.setString(2, vtname);
			prepState.setString(3, dlicense);
			prepState.setString(4, fromTime);
			prepState.setString(5, endTime);
			prepState.executeUpdate();
			connection.commit();

			return Integer.toString(nextConf);

		} catch (SQLException e) {
			throw e;
		}
	};

	public boolean checkUserExist(String userName, String dlicense) throws SQLException{
		int count = 0;
		try {
			PreparedStatement prepState = connection.prepareStatement(
					"SELECT COUNT(*) AS total \n" + "FROM Customers\n" + "WHERE DLICENSE = ? AND NAME = ?");
			prepState.setString(1, dlicense);
			prepState.setString(2, userName);
			ResultSet rs = prepState.executeQuery();
			while (rs.next()) {
				count = rs.getInt("total");
			}
		} catch (SQLException e) {
			throw e;
		}
		return count > 0;
	}

	public void addNewUser(String cellphone, String name, String address, String dlicense) throws SQLException {
		try {
			PreparedStatement prepState = connection.prepareStatement(
					"INSERT INTO Customers\n" +
					"VALUES(?, ?, ?, ?)");
			prepState.setString(1, cellphone);
			prepState.setString(2, name);
			prepState.setString(3, address);
			prepState.setString(4, dlicense);

			prepState.executeUpdate();
			connection.commit();

		} catch (SQLException e) {
			throw e;
		}
	}

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
			rollbackConnection();
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
