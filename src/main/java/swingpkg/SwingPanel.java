package swingpkg;

import util.deduplication;
import util.minus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingPanel {
    public static void main(String[] args) {



        // 创建一个 JFrame 窗口
        JFrame frame = new JFrame("数据清洗小工具");

        // 设置关闭操作
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建一个 JPanel，并禁用布局管理器（使用绝对布局）
        JPanel panel = new JPanel();
        panel.setLayout(null); // 禁用布局管理器，使用绝对布局

        // 创建两个文本框
        JTextArea textArea1 = new JTextArea();
        JTextArea textArea2 = new JTextArea();
        JTextArea textArea3 = new JTextArea();
        // 滚动条
        JScrollPane scrollPane1 = new JScrollPane(textArea1);

        JScrollPane scrollPane2 = new JScrollPane(textArea2);
        JScrollPane scrollPane3 = new JScrollPane(textArea3);

        scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 总是显示垂直滚动条
        // 设置文本框的位置和大小
//        textArea1.setBounds(50, 30, 500, 300); // x=50, y=50, 宽=200, 高=100
        scrollPane2.setBounds(50, 500, 500, 200); // x=50, y=160, 宽=200, 高=100

        scrollPane1.setBounds(50, 30, 240, 300);
        scrollPane3.setBounds(300, 30, 240, 300);
        // 创建三个按钮
        JButton button1 = new JButton("去重");
        JButton button2 = new JButton("去重&取差集");
        JButton button3 = new JButton("Button 3");

        // 添加按钮点击事件
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 按钮点击后弹出一个对话框
                String inputText = textArea1.getText();
                textArea1.setText(deduplication.deduplication(inputText));
                JOptionPane.showMessageDialog(frame, "数据去重复生效!");
                textArea2.setText(deduplication.LogSender());
            }
        });


        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 按钮点击后弹出一个对话框
                String inputText3 = textArea3.getText();
                String inputText1 = textArea1.getText();
                textArea2.setText(minus.minusdata(inputText1,inputText3));
            }
        });




        // 设置按钮的位置和大小
        button1.setBounds(750, 50, 120, 40);  // x=300, y=50, 宽=120, 高=40
        button2.setBounds(750, 100, 120, 40); // x=300, y=100, 宽=120, 高=40
        button3.setBounds(750, 150, 120, 40); // x=300, y=150, 宽=120, 高=40

        // 将文本框和按钮添加到 JPanel
        panel.add(scrollPane1);
        panel.add(scrollPane2);
        panel.add(scrollPane3);
        panel.add(button1);
        panel.add(button2);
        panel.add(button3);

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