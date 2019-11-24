package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.MainWindowDelegate;

import javax.swing.*;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DateFormat;
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
                JTextField cellphoneText = new JTextField(5);

                JPanel myPanel = new JPanel();
                myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
                myPanel.add(new JLabel("User name:"));
                myPanel.add(userNameText);
                myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                myPanel.add(new JLabel("Driver's license:"));
                myPanel.add(cellphoneText);
                int result = JOptionPane.showConfirmDialog(null, myPanel,
                        "Please Enter your name and cellphone", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                try {
                    if (result == JOptionPane.OK_OPTION) {
                        username = userNameText.getText();
                        dlicense = cellphoneText.getText();
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
        JButton returnVehicleBtn = new JButton("Return Vehicle");
        JButton generateReportBtn = new JButton("Generate Report");


        JMenuBar menuBar = new JMenuBar();

        menuBar.add(viewAllTablesBtn);
        menuBar.add(viewAvailableVehiclesBtn);
        menuBar.add(makeReservationBtn);
        menuBar.add(rentVehicleBtn);
        menuBar.add(returnVehicleBtn);
        menuBar.add(generateReportBtn);



        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JLabel label = new JLabel("Enter Your SQL");
        JTextField tf = new JTextField(50); // accepts upto 10 characters
        JButton send = new JButton("Send");
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String text = tf.getText();
            }
        });
        JButton reset = new JButton("Reset");
        panel.add(label); // Components Added using Flow Layout
        panel.add(label); // Components Added using Flow Layout
        panel.add(tf);
        panel.add(send);
        panel.add(reset);



        //Adding Components to the frame.
        this.getContentPane().add(BorderLayout.SOUTH, panel);
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

//        DateFormat inputFormat = new SimpleDateFormat("YYYY-MM-DD HH24:MI");


        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "What vehicles are you looking for?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            res[0] = carTypeText.getText();
            res[1] = locationText.getText();
            res[2] = startTime.getText();
            res[3] = endTime.getText();
            System.out.println("Car Type: " + carTypeText.getText());
            System.out.println("Location: " + locationText.getText());
            System.out.println("Start time: " + startTime.getText());
            System.out.println("End time: " + endTime.getText());
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
        }
        return res;
    }
}
