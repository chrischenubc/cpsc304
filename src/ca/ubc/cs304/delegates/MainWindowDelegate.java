package ca.ubc.cs304.delegates;

import java.sql.SQLException;
import java.util.List;

public interface MainWindowDelegate {
    public List<String[]> viewAllTables() throws SQLException;
    public String executeSelect(String sql);
}
