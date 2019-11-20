package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.MainWindowDelegate;

import javax.swing.*;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                } catch (SQLException e) {
                    System.out.println("SQL Exception: " + e.getMessage());
                    displayErrorMsg(e.getMessage());
                }
//                textArea.append(res);
            }
        });
        JButton makeReservationBtn = new JButton("Make Reservation");
        JButton rentVehicleBtn = new JButton("Rent Vehicle");
        JButton returnVehicleBtn = new JButton("Return Vehicle");
        JButton generateReportBtn = new JButton("Generate Report");


        JMenuBar menuBar = new JMenuBar();

        menuBar.add(viewAllTablesBtn);
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
        m.showFrame(new FakeDelegate());

    }

    static class FakeDelegate implements MainWindowDelegate {
        @Override
        public List<String[]> viewAllTables() {
            String[] colName = {"name", "age"};
            String[] row1 = {"Anna", "27"};
            String[] row2 = {"Anna", "27"};
            String[] row3 = {"Anna", "27"};
            String[] row4 = {"Anna", "27"};
            List<String[]> res = new ArrayList<>();
            res.add(colName);
            res.add(row1);
            res.add(row2);
            res.add(row3);
            res.add(row4);
            return res;
        }

        @Override
        public String executeSelect(String sql) {
            return null;
        }
    }

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

    }

}
