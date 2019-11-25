package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.MainWindowDelegate;
import ca.ubc.cs304.helper.DateHelper;

import javax.swing.*;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class MainWindow extends JFrame{
    private JButton btn;
    private JTextArea txtarea;
    private final static String NEWLINE = "\n";
    // delegate
    private MainWindowDelegate delegate;
    public MainWindow() {
        super("Main Window");
    }

    public void showFrame(MainWindowDelegate delegate) {
        this.setSize(1000,600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // Text Area at the Center
        JTextArea textArea = new JTextArea();

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textArea.setEditable(false);

        //Creating the MenuBar and adding components
        JButton viewAllTablesBtn = new JButton("View All Tables");
        viewAllTablesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    List<String[]> res = delegate.viewAllTables();
                    displayResult(res, scrollPane);
                } catch (Exception e) {
                    displayErrorMsg(e.getMessage());
//                    System.out.println("SQL Exception: " + e.getMessage());
                }
//                textArea.append(res);
            }
        });
        JButton viewAvailableVehiclesBtn = new JButton("View Available Vehicles");
        viewAvailableVehiclesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
              String[] res = promptInputSetofAvaiableCars();
                try {
                    int count = delegate.findNumOfAvailableVehicles(res[0], res[1], res[2], res[3]);
                    JPanel myPanel = new JPanel();
                    myPanel.add(new JLabel("Num of available vehicles: " + count));
                    JButton showDetails = new JButton("Details");
                    showDetails.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            List<String[]> cars = delegate.viewAvaiableVehicles(res[0], res[1], res[2], res[3]);
                            displayResult(cars, scrollPane);
                        }
                    });
                    myPanel.add(showDetails);
                    scrollPane.setViewportView(myPanel);

//                    displayResult(res, scrollPane);
                } catch (Exception e) {
                    displayErrorMsg(e.getMessage());
//                    System.out.println("SQL Exception: " + e.getMessage());
                }
            }
        });
        JButton makeReservationBtn = new JButton("Make Reservation");
        makeReservationBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String username;
                String dlicense;
                JTextField userNameText = new JTextField(5);
                JTextField dlicenseText = new JTextField(5);

                JPanel myPanel = new JPanel();
                myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
                myPanel.add(new JLabel("User name:"));
                myPanel.add(userNameText);
                myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                myPanel.add(new JLabel("Driver's license:"));
                myPanel.add(dlicenseText);
                int result = JOptionPane.showConfirmDialog(null, myPanel,
                        "Please Enter your name and cellphone", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                try {
                    if (result == JOptionPane.OK_OPTION) {
                        username = userNameText.getText();
                        dlicense = dlicenseText.getText();
                        boolean userExists = delegate.checkUserExists(username, dlicense);
                        if (!userExists) {
                            String[] userInfo = promptClientInfo();
                            delegate.addNewUser(userInfo[0], userInfo[1], userInfo[2], userInfo[3]);
                            dlicense = userInfo[3];
                        }
                        String[] reservationInfo = promptReservationInfo();
                        String confNum = delegate.makeReservation(reservationInfo[0], dlicense, reservationInfo[1], reservationInfo[2]);
                        JOptionPane.showConfirmDialog(null,"Success!",
                                "You reservation is completed\nYour confirmation number is "+confNum, JOptionPane.PLAIN_MESSAGE);
                    }
                } catch (SQLException e) {
                    displayErrorMsg(e.getMessage());
                }
            }
        });
        JButton rentVehicleBtn = new JButton("Rent Vehicle");
        rentVehicleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JPanel myPanel = new JPanel();
                myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
                myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                String confNo;
                String location;
                String username;
                String vlicense;
                String dlicense;
                String fromTime;
                String endTime;
                String odometerReading;
                String cardName;
                String cardNo;
                String expDate;
                JTextField confNoText = new JTextField(5);
                JTextField locationText = new JTextField(5);
                myPanel.add(new JLabel("If Yes, Please Enter your confirmation number and location"));
                myPanel.add(Box.createVerticalStrut(10));
                myPanel.add(new JLabel("Confirmation Number:"));
                myPanel.add(confNoText);
                myPanel.add(new JLabel("Your location:"));
                myPanel.add(locationText);
                int n = JOptionPane.showConfirmDialog(
                        null,
                        myPanel,
                        "Do you have any reservation?",
                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (n == JOptionPane.YES_OPTION) {
                    confNo = confNoText.getText();
                    location = locationText.getText();
                    String[] reservationInfo = delegate.getReservationInfo(confNo);
                    List<String[]> cars = delegate.viewAvaiableVehicles(reservationInfo[0], location, reservationInfo[2], reservationInfo[3]);
                    if (cars.size() < 2) {
                        displayErrorMsg("No available cars for the given time period ");
                    } else {
                        vlicense = cars.get(1)[0];
                        dlicense = reservationInfo[1];
                        fromTime = reservationInfo[2];
                        endTime = reservationInfo[3];
                        try {
                            DateHelper.isThisDateValid(fromTime);
                            DateHelper.isThisDateValid(endTime);
                        } catch (ParseException e) {
                            displayErrorMsg(e.getMessage());
                        }

                        myPanel = new JPanel();
                        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
                        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                        JTextField odometerText = new JTextField(5);
                        JTextField cardNameText = new JTextField(5);
                        JTextField cardNoText = new JTextField(5);
                        JTextField expDateText = new JTextField(5);
                        myPanel.add(new JLabel("Odometer readings:"));
                        myPanel.add(odometerText);
                        myPanel.add(new JLabel("Credit card name:"));
                        myPanel.add(cardNameText);
                        myPanel.add(new JLabel("Credit card number:"));
                        myPanel.add(cardNoText);
                        myPanel.add(new JLabel("Expiration Date"));
                        myPanel.add(expDateText);
                        int result = JOptionPane.showConfirmDialog(null, myPanel,
                                "Please enter te rental information", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                        if (result == JOptionPane.OK_OPTION) {
                            odometerReading = odometerText.getText();
                            cardName = cardNameText.getText();
                            cardNo = cardNoText.getText();
                            expDate = expDateText.getText();

                            try {
                                List<String[]> res = delegate.rentVehicle(vlicense, dlicense, fromTime, endTime, odometerReading, cardName, cardNo, expDate, true, confNo);
                                displayResult(res, scrollPane);
                            } catch (SQLException e) {
                                displayErrorMsg(e.getMessage());
                            }
                        }
                    }

                } else {
                    myPanel = new JPanel();
                    myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
                    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                    JTextField vlicenseText = new JTextField(5);
                    JTextField dlicenseText = new JTextField(5);
                    JTextField startTimeText = new JTextField(5);
                    JTextField endTimeText = new JTextField(5);
                    JTextField odometerText = new JTextField(5);
                    JTextField cardNameText = new JTextField(5);
                    JTextField cardNoText = new JTextField(5);
                    JTextField expDateText = new JTextField(5);
                    JTextField userNameText = new JTextField(5);

                    myPanel.add(new JLabel("User name:"));
                    myPanel.add(userNameText);
                    myPanel.add(new JLabel("Please enter the driver's license"));
                    myPanel.add(dlicenseText);
                    myPanel.add(new JLabel("Please enter the vehicle's license"));
                    myPanel.add(vlicenseText);
                    myPanel.add(new JLabel("Start Time in YYYY-MM-DD HH24:MI format"));
                    myPanel.add(startTimeText);
                    myPanel.add(new JLabel("End Time in YYYY-MM-DD HH24:MI format"));
                    myPanel.add(endTimeText);
                    myPanel.add(new JLabel("Odometer readings:"));
                    myPanel.add(odometerText);
                    myPanel.add(new JLabel("Credit card name:"));
                    myPanel.add(cardNameText);
                    myPanel.add(new JLabel("Credit card number:"));
                    myPanel.add(cardNoText);
                    myPanel.add(new JLabel("Expiration Date"));
                    myPanel.add(expDateText);

                    int result = JOptionPane.showConfirmDialog(null, myPanel,
                            "Please enter te rental information", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    try {
                        if (result == JOptionPane.OK_OPTION) {
                            vlicense = vlicenseText.getText();
                            dlicense = dlicenseText.getText();
                            fromTime = startTimeText.getText();
                            endTime = endTimeText.getText();
                            odometerReading = odometerText.getText();
                            cardName = cardNameText.getText();
                            cardNo = cardNoText.getText();
                            expDate = expDateText.getText();
                            username = userNameText.getText();
                            try {
                                DateHelper.isThisDateValid(fromTime);
                                DateHelper.isThisDateValid(endTime);
                            } catch (ParseException e) {
                                displayErrorMsg(e.getMessage());
                            }

                            boolean userExists = delegate.checkUserExists(username, dlicense);
                            if (!userExists) {
                                String[] userInfo = promptClientInfo();
                                delegate.addNewUser(userInfo[0], userInfo[1], userInfo[2], userInfo[3]);
                                dlicense = userInfo[3];
                            }
                            List<String[]> res = delegate.rentVehicle(vlicense, dlicense, fromTime, endTime, odometerReading, cardName, cardNo, expDate, false, "");
                            displayResult(res, scrollPane);

                        }
                    } catch (SQLException e) {
                        displayErrorMsg(e.getMessage());
                    }
                }



            }
        });
        JButton returnVehicleBtn = new JButton("Return Vehicle");
        returnVehicleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JTextField vlicenseText = new JTextField(5);
                JTextField returnTimeText = new JTextField(5);
                JTextField fullTankText = new JTextField(5);
                String vlicense;
                String returnTime;
                String fulltank;
                JPanel myPanel = new JPanel();
                myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
                myPanel.add(new JLabel("Please enter the return vehicle's license?"));
                myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                myPanel.add(Box.createVerticalStrut(10));
                myPanel.add(new JLabel("Vehicle's license:"));
                myPanel.add(vlicenseText);
                myPanel.add(new JLabel("Return time and date:"));
                myPanel.add(returnTimeText);
                myPanel.add(new JLabel("Enter 1 for fulltank"));
                myPanel.add(fullTankText);

                int result = JOptionPane.showConfirmDialog(null, myPanel,
                        "Please enter the return vehicle's license?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    vlicense = vlicenseText.getText();
                    returnTime = returnTimeText.getText();
                    fulltank = fullTankText.getText();

                    try {
                        String[] receipt = delegate.returnVehicle(vlicense, returnTime, fulltank);
                    } catch (SQLException | ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        JButton generateReportBtn = new JButton("Generate Report");
        generateReportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JPanel selectionDialog = new JPanel();
                selectionDialog.setLayout(new BoxLayout(selectionDialog, BoxLayout.Y_AXIS));
                JButton daily_rentals_for_all_branches = new JButton("Daily rentals for all branches");
                JButton daily_rentals_for_a_branches = new JButton("Daily rentals for a branches");
                JButton daily_returns_for_all_branches = new JButton("Daily returns for all branches");
                JButton daily_returns_for_a_branches = new JButton("Daily returns for a branches");
                daily_rentals_for_all_branches.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        JPanel myPanel = new JPanel();
                        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
                        String date;
                        JTextField dataText = new JTextField(5);
                        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
                        myPanel.add(new JLabel("Please enter the date of the report?"));
                        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                        myPanel.add(Box.createVerticalStrut(10));
                        myPanel.add(new JLabel("Date:"));
                        myPanel.add(dataText);
                        int result = JOptionPane.showConfirmDialog(null, myPanel,
                                "Please enter the Date?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (result == JOptionPane.OK_OPTION) {
                            date = dataText.getText();
                            try {
                                List<String[]> report = delegate.getRentReportForAllsBranches(date);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                daily_rentals_for_a_branches.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        JPanel myPanel = new JPanel();
                        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
                        String date;
                        String location;
                        JTextField dataText = new JTextField(5);
                        JTextField locationText = new JTextField(5);
                        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
                        myPanel.add(new JLabel("Please enter the date of the report?"));
                        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                        myPanel.add(Box.createVerticalStrut(10));
                        myPanel.add(new JLabel("Date:"));
                        myPanel.add(dataText);
                        myPanel.add(new JLabel("Location:"));
                        myPanel.add(locationText);
                        int result = JOptionPane.showConfirmDialog(null, myPanel,
                                "Please enter date and location of a branch?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (result == JOptionPane.OK_OPTION) {
                            date = dataText.getText();
                            location = locationText.getText();
                            try {
                                List<String[]> report = delegate.getRentReportForABranch(date, location);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                daily_returns_for_all_branches.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        JPanel myPanel = new JPanel();
                        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
                        String date;
                        JTextField dataText = new JTextField(5);
                        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
                        myPanel.add(new JLabel("Please enter the date of the report?"));
                        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                        myPanel.add(Box.createVerticalStrut(10));
                        myPanel.add(new JLabel("Date:"));
                        myPanel.add(dataText);
                        int result = JOptionPane.showConfirmDialog(null, myPanel,
                                "Please enter the date of the report?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (result == JOptionPane.OK_OPTION) {
                            date = dataText.getText();
                            try {
                                List<String[]> report = delegate.getReturnReportForAllsBranches(date);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                daily_returns_for_a_branches.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        JPanel myPanel = new JPanel();
                        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
                        String date;
                        String location;
                        JTextField dataText = new JTextField(5);
                        JTextField locationText = new JTextField(5);
                        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
                        myPanel.add(new JLabel("Please enter the date of the report?"));
                        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                        myPanel.add(Box.createVerticalStrut(10));
                        myPanel.add(new JLabel("Date:"));
                        myPanel.add(dataText);
                        myPanel.add(new JLabel("Location:"));
                        myPanel.add(locationText);
                        int result = JOptionPane.showConfirmDialog(null, myPanel,
                                "Please enter date and location of a branch?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (result == JOptionPane.OK_OPTION) {
                            date = dataText.getText();
                            location = locationText.getText();
                            try {
                                List<String[]> report = delegate.getReturnReportForABranch(date, location);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                selectionDialog.add(daily_rentals_for_all_branches);
                selectionDialog.add(daily_rentals_for_a_branches);
                selectionDialog.add(daily_returns_for_all_branches);
                selectionDialog.add(daily_returns_for_a_branches);
                JOptionPane.showConfirmDialog(null, selectionDialog,
                        "Please Select Report Type?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            }
        });


        JMenuBar menuBar = new JMenuBar();

        menuBar.add(viewAllTablesBtn);
        menuBar.add(viewAvailableVehiclesBtn);
        menuBar.add(makeReservationBtn);
        menuBar.add(rentVehicleBtn);
        menuBar.add(returnVehicleBtn);
        menuBar.add(generateReportBtn);


        //Adding Components to the frame.
//        this.getContentPane().add(BorderLayout.SOUTH, panel);
        this.getContentPane().add(BorderLayout.NORTH, menuBar);
        this.getContentPane().add(BorderLayout.CENTER, scrollPane);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        MainWindow m=new MainWindow();
//        m.showFrame(new FakeDelegate());

    }

//    static class FakeDelegate implements MainWindowDelegate {
//        @Override
//        public List<String[]> viewAllTables() throws SQLException{
//            String[] colName = {"name", "age"};
//            String[] row1 = {"Anna", "27"};
//            String[] row2 = {"Anna", "27"};
//            String[] row3 = {"Anna", "27"};
//            String[] row4 = {"Anna", "27"};
//            List<String[]> res = new ArrayList<>();
//            res.add(colName);
//            res.add(row1);
//            res.add(row2);
//            res.add(row3);
//            res.add(row4);
//            //return res;
//            throw new SQLException("My error");
//
//        }
//
//        @Override
//        public int findNumOfAvailableVehicles(String type, String location, String timeStart, String timeEnd) {
//            return 0;
//        }
//
//        @Override
//        public List<String[]> viewAvaiableVehicles() {
//            return null;
//        }
//
//        @Override
//        public String executeSelect(String sql) {
//            return null;
//        }
//    }

    private void displayResult(List<String[]> res, JScrollPane scrollPane) {
        if (res.size() == 0) {
            return;
        }
        String[] columnNames = new String[res.get(0).length];
        String[][] data = new String[res.size() - 1][res.get(0).length];
        for (int i = 0; i < res.size(); i++) {
            for (int j = 0; j < res.get(0).length; j++) {
                if (i == 0) {
                    columnNames[j] = res.get(0)[j];
                } else {
                    data[i - 1][j] = res.get(i)[j];
                }
            }
        }
        // Initializing the JTable
        JTable jTable;
        jTable = new JTable(data, columnNames);
        jTable.setBounds(30, 40, 200, 300);
        scrollPane.setViewportView(jTable);
    }


    private void displayErrorMsg(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private String[] promptClientInfo() {
        String[] res = new String[4];
        JTextField cellphone = new JTextField(5);
        JTextField name = new JTextField(5);
        JTextField address = new JTextField(5);
        JTextField dlicense = new JTextField(5);

        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
        myPanel.add(new JLabel("Cell phone:"));
        myPanel.add(cellphone);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("Name:"));
        myPanel.add(name);
        myPanel.add(new JLabel("Address"));
        myPanel.add(address);
        myPanel.add(new JLabel("Driver's license"));
        myPanel.add(dlicense);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "New User - Please enter your information", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            res[0] = cellphone.getText();
            res[1] = name.getText();
            res[2] = address.getText();
            res[3] = dlicense.getText();
        }
        return res;
    }

    private String[] promptInputSetofAvaiableCars() {
        String[] res = new String[4];
        JTextField carTypeText = new JTextField(5);
        JTextField locationText = new JTextField(5);
        JTextField startTime = new JTextField(5);
        JTextField endTime = new JTextField(5);


        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
        myPanel.add(new JLabel("Car Type:"));
        myPanel.add(carTypeText);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("Location:"));
        myPanel.add(locationText);
        myPanel.add(new JLabel("Start Time in YYYY-MM-DD HH24:MI format"));
        myPanel.add(startTime);
        myPanel.add(new JLabel("End Time in YYYY-MM-DD HH24:MI format"));
        myPanel.add(endTime);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "What vehicles are you looking for?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            res[0] = carTypeText.getText();
            res[1] = locationText.getText();
            res[2] = startTime.getText();
            res[3] = endTime.getText();
            try {
                DateHelper.isThisDateValid(res[2]);
                DateHelper.isThisDateValid(res[3]);
            } catch (ParseException e) {
                displayErrorMsg(e.getMessage());
            }
        }
        return res;
    }

    private String[] promptReservationInfo() {
        String[] res = new String[3];
        JTextField carTypeText = new JTextField(5);
        JTextField startTime = new JTextField(5);
        JTextField endTime = new JTextField(5);

        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
        myPanel.add(new JLabel("Car Type:"));
        myPanel.add(carTypeText);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("Start Time in YYYY-MM-DD HH24:MI format"));
        myPanel.add(startTime);
        myPanel.add(new JLabel("End Time in YYYY-MM-DD HH24:MI format"));
        myPanel.add(endTime);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "What vehicles do you want to reserve?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            res[0] = carTypeText.getText();
            res[1] = startTime.getText();
            res[2] = endTime.getText();
            try {
                DateHelper.isThisDateValid(res[1]);
                DateHelper.isThisDateValid(res[2]);
            } catch (ParseException e) {
                displayErrorMsg(e.getMessage());
            }
        }
        return res;
    }
}
