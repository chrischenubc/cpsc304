package ca.ubc.cs304.delegates;

import java.sql.SQLException;
import java.util.List;

public interface MainWindowDelegate {
    public List<String[]> viewAllTables() throws SQLException;
    public String makeReservation(String vtname, String dlicense, String fromTime, String endTime) throws SQLException;
    public List<String[]> viewAvaiableVehicles(String type, String location, String timeStart, String timeEnd);
    public int findNumOfAvailableVehicles(String type, String location, String timeStart, String timeEnd);
    public String executeSelect(String sql);
    public void rentVehicle();
    public void returnVehicle();
    public void generateReport();
    public boolean checkUserExists(String username, String cellphone) throws SQLException;
    public void addNewUser(String cellphone, String name, String address, String dlicense) throws SQLException;
}
