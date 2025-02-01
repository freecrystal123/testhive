package swingpkg;

import util.deduplication;
import util.executesql;
import util.minus;

import javax.swing.*;
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
        scrollPane2.setBounds(50, 300, 500, 200); // x=50, y=160, 宽=200, 高=100


        // 创建三个按钮
        JButton button1 = new JButton("DATA Exp&Imp：");
        JButton button1_1 = new JButton("userinfo ");
        JButton button1_2 = new JButton("fact_allOrders ");
        JButton button1_3 = new JButton("-----");

        // 文本输入
        // 昨天日期默认
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        JTextField startField =  new JTextField(yesterday.toString());
        JTextField endField =  new JTextField(today.toString());


        // 创建三个按钮
        JButton button2 = new JButton("DATA INGESTION：");
        JButton button2_1 = new JButton("userinfo ");
        JButton button2_2 = new JButton("fact_allOrders");
        JButton button2_3 = new JButton("-----");


        // 添加按钮点击事件
        button1_1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // 按钮点击后弹出一个对话框


                util.etlsqls.userinfo2SQL();
                textArea2.setText( util.etlsqls.getLog());

                // 数据库操作部分


            }

        });


        button1_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // 输入值是否合理

                String starttime =  startField.getText();
                String endtime = endField.getText();
                button1_2.setEnabled(false);
                JOptionPane.showMessageDialog(frame, "The button cannot be clicked twice!");
                util.etlsqls.output2SQL(starttime,endtime);
                textArea2.setText(util.etlsqls.getLog());
            }
        });



        // 设置按钮的位置和大小
        button1.setBounds(50, 50, 120, 40);  // x=300, y=50, 宽=120, 高=40
        button1_1.setBounds(50, 100, 120, 40); // x=300, y=100, 宽=120, 高=40
        button1_2.setBounds(50, 170, 120, 40); // x=300, y=100, 宽=120, 高=40
//        button1_3.setBounds(350, 100, 120, 40); // x=300, y=100, 宽=120, 高=40

        startField.setBounds(190, 170, 120, 40); // x=300, y=100, 宽=120, 高=40
        endField.setBounds(360, 170, 120, 40); // x=300, y=100, 宽=120, 高=40


        // 将文本框和按钮添加到 JPanel
        panel.add(scrollPane2);
        panel.add(button1);
        panel.add(button1_1);
        panel.add(button1_2);
        panel.add(startField);
        panel.add(endField);
//        panel.add(button1_3);
        // 将 JPanel 添加到 JFrame
        frame.add(panel);

        // 设置窗口的大小
        frame.setSize(1000, 800);

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