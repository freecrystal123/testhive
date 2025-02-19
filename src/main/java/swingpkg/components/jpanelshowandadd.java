package swingpkg.components;

import jdbc.mysqljdbc;
import pojp.replenish;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

public class jpanelshowandadd extends JPanel {


    // 定义JDBC 数据库连接
    static Properties financeJDBC = null;
    static Properties alibabaJDBC = null;
    static {
        financeJDBC = new Properties();
        financeJDBC.put("jdbcurl", "jdbc:mysql://20.174.38.36:3306/lottery_reporting?allowLoadLocalInfile=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useCompression=true");
        financeJDBC.put("username","Viviene");
        financeJDBC.put("password","VALe@1234");


        alibabaJDBC = new Properties();
        alibabaJDBC.put("jdbcurl", "jdbc:mysql://47.99.103.128:3306/Lottery?allowLoadLocalInfile=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useCompression=true");
        alibabaJDBC.put("username","root");
        alibabaJDBC.put("password","1234");

    }
    private DefaultTableModel tableModel1 = new DefaultTableModel() ;

    public jpanelshowandadd() {
        // 设置布局管理器为 GridLayout，6行2列（5个标签和输入框 + 1行用于提交按钮）
        setLayout(null);
        // 定义表格的列名

        Field[] fields = replenish.class.getDeclaredFields();
        String[] columnNames = new String[fields.length];

        for (int i = 0; i < fields.length; i++) {
            columnNames[i] = fields[i].getName(); // 使用类的字段名作为列名
        }
        replenish replenish = new replenish();
        // 从数据库获取数据

        List<replenish> replenishLists =  mysqljdbc.listTableRecord("fact_instant_replenish" ,financeJDBC ,replenish.class);

        Object[][] data = new Object[replenishLists.size()][fields.length];


        // 定义表格的数据（每一行代表一条记录）
        // 遍历 replenishLists，将每一条数据填充到二维数组中
        for (int i = 0; i < replenishLists.size(); i++) {
            replenish r = replenishLists.get(i);
            data[i][0] = r.DateID;      // DateID
            data[i][1] = r.GameID;      // GameID
            data[i][2] = r.Replenish;   // Replenish
            data[i][3] = r.Opening;     // Opening
        }



        // 创建 "Add" 按钮
        JButton addButton = new JButton("add");
        // 创建 "Add" 按钮
        JButton submitButton = new JButton("submit");
        addButton.setBounds(400, 10, 100, 30); // 设置按钮的位置和大小
        submitButton.setBounds(500, 10, 100, 30); // 设置按钮的位置和大小
        // 添加按钮点击事件监听
        addButton.addActionListener(e -> {
            // 这里可以写点击按钮后的逻辑
            showInputDialog();
            // 例如弹出一个对话框来添加新数据
            // showDialog(); // 你可以自己创建一个方法来显示对话框
        });

        // 添加按钮点击事件监听
        submitButton.addActionListener(e -> {
            // 这里可以写点击按钮后的逻辑
            System.out.println("updateButton button clicked");
            // 例如弹出一个对话框来添加新数据
            // showDialog(); // 你可以自己创建一个方法来显示对话框
        });





        // 创建表格模型，并将数据和列名传入
        tableModel1  = new DefaultTableModel(data,columnNames);

        // 创建 JTable 并设置模型
        JTable table = new JTable(tableModel1);
        table.setBackground(Color.lightGray);
        table.getTableHeader().setFont(new Font("Arial",Font.BOLD,14));
        table.getTableHeader().setForeground(Color.black);
        // 为表格设置可滚动面板
        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBounds(30,50,570,700);
        // 将标签和输入框添加到面板
        add(scrollPane);
        add(addButton);
//        add(updateButton);

        // 在面板中添加一个空的标签作为占位符，使提交按钮显示在最后

    }



    // 显示输入对话框，让用户输入数据
    private void showInputDialog() {
        JTextField dateIDField = new JTextField(10);
        JTextField gameIDField = new JTextField(10);
        JTextField replenishField = new JTextField(10);
        JTextField openingField = new JTextField(10);

        // 创建一个面板来放置文本框
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("DateID:"));
        panel.add(dateIDField);
        panel.add(new JLabel("GameID:"));
        panel.add(gameIDField);
        panel.add(new JLabel("Replenish:"));
        panel.add(replenishField);
        panel.add(new JLabel("Opening:"));
        panel.add(openingField);

        // 弹出输入框，获取用户输入的数据
        int option = JOptionPane.showConfirmDialog(this, panel, "Enter New Data", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            // 获取用户输入的数据
            String dateID = dateIDField.getText();
            int gameID = Integer.parseInt(gameIDField.getText());
            int replenish = Integer.parseInt(replenishField.getText());
            int opening = Integer.parseInt(openingField.getText());

            // 创建新的 replenish 对象
            replenish newReplenish = new replenish();
            newReplenish.DateID = dateID;
            newReplenish.GameID = gameID;
            newReplenish.Replenish = replenish;
            newReplenish.Opening = opening;

            // 将新的数据添加到表格中
            addRowToTable(newReplenish);
        }
    }

    // 将新行数据添加到表格中
    private void addRowToTable(replenish newReplenish) {
        Object[] newRow = new Object[]{
                newReplenish.DateID,
                newReplenish.GameID,
                newReplenish.Replenish,
                newReplenish.Opening
        };
        tableModel1.addRow(newRow);
    }


    public static void main(String[] args) {
        // 创建 JFrame
        JFrame frame = new JFrame("Form with Labels and Text Fields");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // 创建 jpaneladd 实例并将其添加到 JFrame 中

        frame.add(new jpanelshowandadd(), BorderLayout.CENTER);

        // 显示窗口
        frame.setVisible(true);
    }


}
