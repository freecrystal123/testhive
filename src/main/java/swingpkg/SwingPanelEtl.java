package swingpkg;

import util.deduplication;
import util.etlsqls;
import util.executesql;
import util.minus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.concurrent.*;

public class SwingPanelEtl {
    public static void main(String[] args) {



        // 创建一个 JFrame 窗口
        JFrame frame = new JFrame("ETL TOOL");

        // 设置关闭操作
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 创建一个 JPanel，并禁用布局管理器（使用绝对布局）
        JPanel panel = new JPanel();
        panel.setLayout(null); // 禁用布局管理器，使用绝对布局

        // 创建两个文本框
        JTextArea textArea1 = new JTextArea();
        JTextArea textArea2 = new JTextArea();

        // 滚动条

        JScrollPane scrollPane2 = new JScrollPane(textArea2);


        scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 总是显示垂直滚动条
        // 设置文本框的位置和大小
//        textArea1.setBounds(50, 30, 500, 300); // x=50, y=50, 宽=200, 高=100
        scrollPane2.setBounds(100, 320, 500, 400); // x=50, y=160, 宽=200, 高=100



        // 创建三个按钮

        JLabel label = new JLabel("Data Exp&Imp ");
        label.setFont(new Font("Arial",Font.BOLD,20));

        JButton userinfo = new JButton("userinfo ");
        JButton trafficdata = new JButton("trafficdata ");
        JButton newregister = new JButton("newregister");


        // 文本输入
        // 昨天日期默认
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);

        JTextField startFieldRegister =  new JTextField(yesterday.toString());
        JTextField endFieldRegister =  new JTextField(today.toString());

        JTextField startFieldTrafficData =  new JTextField(yesterday.toString());
        JTextField endFieldTrafficData =  new JTextField(today.toString());

        // 创建三个按钮
        JButton button2 = new JButton("DATA INGESTION：");
        JButton button2_1 = new JButton("userinfo ");
        JButton button2_2 = new JButton("factallorders");
        JButton button2_3 = new JButton("newregisteredusers");


        // 添加按钮点击事件
        userinfo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // 按钮点击后弹出一个对话框
                // 沙漏

                userinfo.setEnabled(false);
                trafficdata.setEnabled(false);
                newregister.setEnabled(false);
                frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                JOptionPane.showMessageDialog(frame, "Please be patient and do not click the button again!");
                try {
                    util.etlsqls.userinfo2SQL();
                    JOptionPane.showMessageDialog(frame, "Successful! ");
                }catch(Exception e1){
                    JOptionPane.showMessageDialog(frame, "fail ! ");
                }
                frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                textArea2.setText( util.etlsqls.getLog());
            }

        });



        // 添加按钮点击事件
        newregister.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // 按钮点击后弹出一个对话框
                // 沙漏

                String starttime =  startFieldRegister.getText();
                String endtime = endFieldRegister.getText();
                userinfo.setEnabled(false);
                trafficdata.setEnabled(false);
                newregister.setEnabled(false);
                frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                JOptionPane.showMessageDialog(frame, "Please be patient and do not click the button again!");

                try {
                    util.etlsqls.newregisteredusers(starttime, endtime);
                    JOptionPane.showMessageDialog(frame, "Successful! ");
                }catch(Exception e1){
                    util.etlsqls.InLog(e1.getMessage());
                    JOptionPane.showMessageDialog(frame, "fail ! ");
                }

                JOptionPane.showMessageDialog(frame, "Successful! ");
                frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                textArea2.setText( util.etlsqls.getLog());
            }

        });


        trafficdata.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                String starttime =  startFieldTrafficData.getText();
                String endtime = endFieldTrafficData.getText();

                userinfo.setEnabled(false);
                trafficdata.setEnabled(false);
                newregister.setEnabled(false);
                // 沙漏
                frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                JOptionPane.showMessageDialog(frame, "Please be patient and do not click the button again!");
                try {
                    util.etlsqls.traffic_data_temp(starttime,endtime);
                    util.etlsqls.ftd();
                    util.etlsqls.trafficdataandftdDMLSQL();
                    JOptionPane.showMessageDialog(frame, "Successful! ");
                }catch (Exception e1){
                    util.etlsqls.InLog(e1.getMessage());
                    JOptionPane.showMessageDialog(frame, "failed! \n "+e1.getMessage());
                    e1.printStackTrace();
                }
                finally {
                    frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    textArea2.setText(util.etlsqls.getLog());
                }
            }
        });


        // 设置按钮的位置和大小
        label.setBounds(100, 50, 300, 40);  // x=300, y=50, 宽=120, 高=40
        userinfo.setBounds(100, 100, 120, 40); // x=300, y=100, 宽=120, 高=40
        trafficdata.setBounds(100, 170, 120, 40); // x=300, y=100, 宽=120, 高=40
        newregister.setBounds(100, 240, 120, 40); // x=300, y=100, 宽=120, 高=40


        startFieldRegister.setBounds(240, 240, 120, 40); // x=300, y=100, 宽=120, 高=40
        endFieldRegister.setBounds(410, 240, 120, 40); // x=300, y=100, 宽=120, 高=40


        startFieldTrafficData.setBounds(240, 170, 120, 40); // x=300, y=100, 宽=120, 高=40
        endFieldTrafficData.setBounds(410, 170, 120, 40); // x=300, y=100, 宽=120, 高=40


        // 将文本框和按钮添加到 JPanel
        panel.add(scrollPane2);
        panel.add(label);
        panel.add(userinfo);
        panel.add(trafficdata);
        panel.add(startFieldTrafficData);
        panel.add(endFieldTrafficData);
        panel.add(startFieldRegister);
        panel.add(endFieldRegister);
        panel.add(newregister);
        // 将 JPanel 添加到 JFrame
        frame.add(panel);

        // 设置窗口的大小
        frame.setSize(700, 800);

        // 设置窗口居中显示
        frame.setLocationRelativeTo(null);

        // 显示窗口
        frame.setVisible(true);

    }
}



//    // 创建一个 JFrame（窗口）
//    JFrame frame = new JFrame("Swing Example with Action");
//
//// 设置窗口的关闭操作
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//                // 创建一个按钮
//                JButton button = new JButton("Click Me!");
//
                // 添加按钮点击事件
//                button.addActionListener(new ActionListener() {
//public void actionPerformed(ActionEvent e) {
//        // 按钮点击后弹出一个对话框
//        JOptionPane.showMessageDialog(frame, "Button clicked!");
//        }
//        });
//
//        // 将按钮添加到窗口中
//        frame.getContentPane().add(button);
//        frame.setLocationRelativeTo(null);
//        // 设置窗口大小
//        frame.setSize(800, 500);
//
//        // 设置窗口可见
//        frame.setVisible(true);