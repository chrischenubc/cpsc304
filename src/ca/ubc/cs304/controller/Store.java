package ca.ubc.cs304.controller;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.delegates.*;
import ca.ubc.cs304.ui.LoginWindow;
import ca.ubc.cs304.ui.MainWindow;

import java.sql.SQLException;
import java.text.ParseException;
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
       loginWindow = new LoginWindow();
       loginWindow.showFrame(this);
//          this.testLogin("ora_keith520", "a11691152");
 //     this.testLogin("ora_ziyux", "a50664135");
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
            mainWindow = new MainWindow();
            mainWindow.showFrame(this);
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
    public String makeReservation(String vtname, String dlicense, String fromTime, String endTime) throws SQLException {
        return dbHandler.makeReservation(vtname, dlicense, fromTime, endTime);
    }


    @Override
    public List<String[]> rentVehicle(String vlicense, String dlicense, String fromTime, String endTime, String odometer, String cardName, String cardNo, String expDate, boolean hasReservation, String confNo) throws SQLException {
        return dbHandler.rentVehicle(vlicense, dlicense, fromTime, endTime, odometer, cardName, cardNo, expDate, hasReservation, confNo);
    }

    @Override
    public String[] returnVehicle(String vlicense, String returnTime, String fullTank) throws SQLException, ParseException {
       dbHandler.returnVehicle(vlicense, returnTime, fullTank);
       return null;
    }

    @Override
    public void generateReport() {

    }

    @Override
    public List<String[]> getRentReportForAllsBranches(String date) throws SQLException {
//        return dbHandler.getRentReportForAllsBranches(date);
        return null;
    }

    @Override
    public List<String[]> getRentReportForABranch(String date, String location) throws SQLException {
        return null;
    }

    @Override
    public List<String[]> getReturnReportForAllsBranches(String date) throws SQLException {
        return null;
    }

    @Override
    public List<String[]> getReturnReportForABranch(String date, String location) throws SQLException {
        return null;
    }

    @Override
    public boolean checkUserExists(String username, String cellphone) throws SQLException{
        return dbHandler.checkUserExist(username, cellphone);
    }

    @Override
    public void addNewUser(String cellphone, String name, String address, String dlicense) throws SQLException {
        dbHandler.addNewUser(cellphone, name, address, dlicense);
    }

    @Override
    public String[] getReservationInfo(String confNo) {
//        return dbHandler.getReservationInfo(confNo);
        return null;
    }
}
