package swingpkg.components;

import util.deduplication;
import util.minus;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class jpaneldiffdedup  extends JPanel {
    public jpaneldiffdedup (){

        setLayout(null); // 使用绝对布局

        // 创建文本框和标签
        JTextArea textArea1 = new JTextArea();
        textArea1.setLineWrap(true);
        textArea1.setWrapStyleWord(true);

        JTextArea textArea2 = new JTextArea();
        textArea2.setLineWrap(true);
        textArea2.setWrapStyleWord(true);

        JTextArea textArea3 = new JTextArea();
        textArea3.setLineWrap(true);
        textArea3.setWrapStyleWord(true);

        JLabel lineCountLabel1 = new JLabel("A --->  line Count: 0");
        JLabel lineCountLabel2 = new JLabel("A-B --->  line Count: 0");
        JLabel lineCountLabel3 = new JLabel("B --->  line Count: 0");

        // 滚动面板设置
        JPanel LabelscrollPane1 = new JPanel();
        LabelscrollPane1.setLayout(new BorderLayout());
        JScrollPane scrollPane1 = new JScrollPane(textArea1);
        LabelscrollPane1.add(scrollPane1, BorderLayout.CENTER);
        LabelscrollPane1.add(lineCountLabel1, BorderLayout.SOUTH);

        JPanel LabelscrollPane2 = new JPanel();
        LabelscrollPane2.setLayout(new BorderLayout());
        JScrollPane scrollPane2 = new JScrollPane(textArea2);
        LabelscrollPane2.add(scrollPane2, BorderLayout.CENTER);
        LabelscrollPane2.add(lineCountLabel2, BorderLayout.SOUTH);

        JPanel LabelscrollPane3 = new JPanel();
        LabelscrollPane3.setLayout(new BorderLayout());
        JScrollPane scrollPane3 = new JScrollPane(textArea3);
        LabelscrollPane3.add(scrollPane3, BorderLayout.CENTER);
        LabelscrollPane3.add(lineCountLabel3, BorderLayout.SOUTH);

        // 设置滚动条
        scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // 设置面板位置和大小
        LabelscrollPane1.setBounds(50, 30, 240, 300);
        LabelscrollPane2.setBounds(50, 500, 500, 200);
        LabelscrollPane3.setBounds(300, 30, 240, 300);

        // 创建按钮
        JButton button1 = new JButton("Deduplication");
        JButton button2 = new JButton("Diff");


        // 按钮点击事件
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String inputText = textArea1.getText();
                textArea1.setText(deduplication.deduplication(inputText));
                JOptionPane.showMessageDialog(null, "dudup is successful!");
                textArea2.setText(deduplication.LogSender());
            }
        });

        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String inputText3 = textArea3.getText();
                String inputText1 = textArea1.getText();
                textArea2.setText(minus.minusdata(inputText1, inputText3));
            }
        });


        // 监听文本框变化，更新行数
        textArea1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLineCount();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateLineCount();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateLineCount();
            }

            private void updateLineCount() {
                int lineCount = textArea1.getLineCount();
                lineCountLabel1.setText("A --->  line Count: " + lineCount);
            }
        });

        textArea2.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLineCount();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateLineCount();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateLineCount();
            }

            private void updateLineCount() {
                int lineCount = textArea2.getLineCount();
                lineCountLabel2.setText("A-B --->  line Count: " + lineCount);
            }
        });

        textArea3.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLineCount();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateLineCount();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateLineCount();
            }

            private void updateLineCount() {
                int lineCount = textArea3.getLineCount();
                lineCountLabel3.setText("B --->  line Count: " + lineCount);
            }
        });

        // 设置按钮位置和大小
        button1.setBounds(550, 50, 120, 40);
        button2.setBounds(550, 100, 120, 40);

        // 将组件添加到面板
        add(LabelscrollPane1);
        add(LabelscrollPane2);
        add(LabelscrollPane3);
        add(button1);
        add(button2);
    }

    public static void main(String[] args) {
        // 创建 JFrame 并设置内容面板
        JFrame frame = new JFrame("数据清洗小工具");
        jpaneldiffdedup panel = new jpaneldiffdedup();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setSize(1000, 800);
        frame.setLocationRelativeTo(null); // 居中显示
        frame.setVisible(true);
    }
}