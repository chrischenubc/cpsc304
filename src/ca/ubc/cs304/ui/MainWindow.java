package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.MainWindowDelegate;

import javax.swing.*;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
                String res = delegate.viewAllTables();
                textArea.append(res);
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
        JLabel label = new JLabel("Enter Text");
        JTextField tf = new JTextField(10); // accepts upto 10 characters
        JButton send = new JButton("Send");
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
        public String viewAllTables() {
            return "Fake delegate testing string";
        }
    }

}
