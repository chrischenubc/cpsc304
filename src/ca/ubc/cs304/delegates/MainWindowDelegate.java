package ca.ubc.cs304.delegates;

import java.sql.SQLException;
import java.util.List;

public interface MainWindowDelegate {
    public List<String[]> viewAllTables() throws SQLException;
    public void makeReservation();
    public List<String[]> viewAvaiableVehicles(String type, String location, String timeStart, String timeEnd);
    public int findNumOfAvailableVehicles(String type, String location, String timeStart, String timeEnd);
    public String executeSelect(String sql);
    public void rentVehicle();
    public void returnVehicle();
    public void generateReport();
}
