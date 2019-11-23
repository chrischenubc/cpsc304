package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.*;
import ca.ubc.cs304.ui.LoginWindow;
import ca.ubc.cs304.ui.MainWindow;

import java.sql.SQLException;
import java.util.List;

public   class Store implements LoginWindowDelegate, MainWindowDelegate {
    private DatabaseConnectionHandler dbHandler = null;
    private LoginWindow loginWindow = null;
    private MainWindow mainWindow = null;
    private final static String NEWLINE = "\n";

    public Store() {
        dbHandler = new DatabaseConnectionHandler();
    }

    private void start() {
//        loginWindow = new LoginWindow();
//        loginWindow.showFrame(this);
          this.testLogin("ora_keith520", "a11691152");
    }

    /**
     * LoginWindowDelegate Implementation
     *
     * connects to Oracle database with supplied username and password
     */
    public void login(String username, String password) {
        boolean didConnect = dbHandler.login(username, password);

        if (didConnect) {
            // Once connected, remove login window and start text transaction flow
            loginWindow.dispose();

            //Temporary remove to the console prompt
//            TerminalTransactions transaction = new TerminalTransactions();
//            transaction.showMainMenu(this);
        } else {
            loginWindow.handleLoginFailed();

            if (loginWindow.hasReachedMaxLoginAttempts()) {
                loginWindow.dispose();
                System.out.println("You have exceeded your number of allowed attempts");
                System.exit(-1);
            }
        }
    }

    public void testLogin(String username, String password) {
        boolean didConnect = dbHandler.login(username, password);

        if (didConnect) {
            mainWindow = new MainWindow();
            mainWindow.showFrame(this);
        } else {
            System.out.println("Fail to connect to DB");
        }
    }

    public List<String[]> viewAllTables() throws SQLException {
        return dbHandler.viewAllTables();
    }

    @Override
    public String executeSelect(String sql) {
        return dbHandler.executeSelect(sql);
    }

    /**
     * TerminalTransactionsDelegate Implementation
     *
     * The TerminalTransaction instance tells us that it is done with what it's
     * doing so we are cleaning up the connection since it's no longer needed.
     */
    public void terminalTransactionsFinished() {
        dbHandler.close();
        dbHandler = null;

        System.exit(0);
    }

    /**
     * Main method called at launch time
     */
    public static void main(String args[]) {
        Store store = new Store();
        store.start();
    }

    @Override
    public int findNumOfAvailableVehicles(String type, String location, String timeStart, String timeEnd) {
        return dbHandler.findNumOfAvailableVehicles(type, location, timeStart, timeEnd);
    }
    @Override
    public List<String[]> viewAvaiableVehicles(String type, String location, String timeStart, String timeEnd) {
        return dbHandler.viewAvailableVehicles(type,location,timeStart,timeEnd);
    }

    @Override
    public void makeReservation() {

    }

    @Override
    public void rentVehicle() {

    }

    @Override
    public void returnVehicle() {

    }

    @Override
    public void generateReport() {

    }
}
