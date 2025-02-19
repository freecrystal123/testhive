package swingpkg;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class lookbeauty {


    public static void main(String[] args) {
        JFrame frame = new JFrame("Table Display Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400); // 设置窗口大小
        frame.setLayout(new BorderLayout()); // 使用 BorderLayout 布局

        // 定义表格的列名
        String[] columnNames = {"ID", "Name", "Age", "City"};

        // 定义表格的数据（每一行代表一条记录）
        Object[][] data = {
                {1, "John", 28, "New York"},
                {2, "Emma", 35, "Los Angeles"},
                {3, "Sophia", 22, "Chicago"},
                {4, "Michael", 40, "San Francisco"},
                {5, "Daniel", 30, "Miami"}
        };

        // 创建表格模型，并将数据和列名传入
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);

        // 创建 JTable 并设置模型
        JTable table = new JTable(tableModel);
        table.setBackground(Color.lightGray);
        table.getTableHeader().setFont(new Font("Arial",Font.BOLD,14));
        table.getTableHeader().setForeground(Color.black);
        // 为表格设置可滚动面板
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER); // 把表格放入滚动面板并添加到窗口

        // 显示窗口
        frame.setVisible(true);

    }



}