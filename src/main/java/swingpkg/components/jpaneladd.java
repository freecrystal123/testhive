package swingpkg.components;

import javax.swing.*;
import java.awt.*;

public class jpaneladd extends JPanel {


    public jpaneladd() {
        // 设置布局管理器为 GridLayout，6行2列（5个标签和输入框 + 1行用于提交按钮）
        setLayout(null);

        // 创建五个标签和五个输入框
        JLabel label1 = new JLabel("Label 1:");
        JTextField textField1 = new JTextField(20);
        JLabel label2 = new JLabel("Label 2:");
        JTextField textField2 = new JTextField(20);
        JLabel label3 = new JLabel("Label 3:");
        JTextField textField3 = new JTextField(20);
        JLabel label4 = new JLabel("Label 4:");
        JTextField textField4 = new JTextField(20);
        JLabel label5 = new JLabel("Label 5:");
        JTextField textField5 = new JTextField(20);


        // 创建提交按钮
        JButton submitButton = new JButton("submit");
        // 设置绝对布局
        label1.setBounds(220, 50, 120, 40);
        label2.setBounds(220, 150, 120, 40);
        label3.setBounds(220, 250, 120, 40);
        label4.setBounds(220, 350, 120, 40);
        label5.setBounds(220, 450, 120, 40);

        textField1.setBounds(320, 50, 150, 40);
        textField2.setBounds(320, 150, 150, 40);
        textField3.setBounds(320, 250, 150, 40);
        textField4.setBounds(320, 350, 150, 40);
        textField5.setBounds(320, 450, 150, 40);

        submitButton.setBounds(240, 600, 100, 40);
        // 将标签和输入框添加到面板
        add(label1);
        add(textField1);
        add(label2);
        add(textField2);
        add(label3);
        add(textField3);
        add(label4);
        add(textField4);
        add(label5);
        add(textField5);

        // 创建提交按钮
        submitButton.addActionListener(e -> {
            // 获取输入框的内容并打印
            String text1 = textField1.getText();
            String text2 = textField2.getText();
            String text3 = textField3.getText();
            String text4 = textField4.getText();
            String text5 = textField5.getText();




        });

        // 在面板中添加一个空的标签作为占位符，使提交按钮显示在最后
        add(new JLabel());  // 占位符
        add(submitButton);   // 添加提交按钮
    }

    public static void main(String[] args) {
        // 创建 JFrame
        JFrame frame = new JFrame("Form with Labels and Text Fields");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // 创建 jpaneladd 实例并将其添加到 JFrame 中
        frame.add(new jpaneladd(), BorderLayout.CENTER);

        // 显示窗口
        frame.setVisible(true);
    }


}
