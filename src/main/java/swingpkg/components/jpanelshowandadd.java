package swingpkg.components;

import jdbc.mysqljdbc;
import pojp.replenish;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class jpanelshowandadd extends JPanel {

    // 定义JDBC 数据库连接
    static Properties financeJDBC = null;
    static Properties alibabaJDBC = null;
    static {
        financeJDBC = new Properties();
        financeJDBC.put("jdbcurl", "jdbc:mysql://20.174.38.36:3306/lottery_reporting?allowLoadLocalInfile=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useCompression=true");
        financeJDBC.put("username", "Viviene");
        financeJDBC.put("password", "VALe@1234");

        alibabaJDBC = new Properties();
        alibabaJDBC.put("jdbcurl", "jdbc:mysql://47.99.103.128:3306/Lottery?allowLoadLocalInfile=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useCompression=true");
        alibabaJDBC.put("username", "root");
        alibabaJDBC.put("password", "1234");
    }

    private DefaultTableModel tableModel1 = new DefaultTableModel();

    public jpanelshowandadd() {
        setLayout(null);

        // 获取字段并定义表格列名
        Field[] fields = replenish.class.getDeclaredFields();
        String[] columnNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            columnNames[i] = fields[i].getName();
        }

        // 创建表格和按钮
        JButton addButton = new JButton("add");
        JButton reflashButton = new JButton("reflash");
        addButton.setBounds(400, 10, 100, 30);
        reflashButton.setBounds(500, 10, 100, 30);

        addButton.addActionListener(e -> showInputDialog());
        reflashButton.addActionListener(e -> loadDataAsync(columnNames));

        // 初始化 JTable
        JTable table = new JTable(tableModel1);
        table.setBackground(Color.lightGray);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setForeground(Color.black);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 50, 570, 700);
        add(scrollPane);
        add(addButton);
        add(reflashButton);

        // 异步加载数据
        loadDataAsync(columnNames);
    }

    private void loadDataAsync(String[] columnNames) {
        // 使用CompletableFuture异步加载数据
        CompletableFuture.supplyAsync(() -> {
            List<replenish> replenishLists = mysqljdbc.listTableRecord("fact_instant_replenish", financeJDBC, replenish.class);
            Object[][] data = new Object[replenishLists.size()][columnNames.length];

            for (int i = 0; i < replenishLists.size(); i++) {
                replenish r = replenishLists.get(i);
                data[i][0] = r.DateID;
                data[i][1] = r.GameID;
                data[i][2] = r.Replenish;
                data[i][3] = r.Opening;
            }
            return data;
        }).thenAccept(data -> {
            // 更新UI线程中的表格数据
            tableModel1.setDataVector(data, columnNames);
        }).exceptionally(ex -> {
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        });
    }

    private void showInputDialog() {
        JTextField dateIDField = new JTextField(10);
        JTextField gameIDField = new JTextField(10);
        JTextField replenishField = new JTextField(10);
        JTextField openingField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("DateID:"));
        panel.add(dateIDField);
        panel.add(new JLabel("GameID:"));
        panel.add(gameIDField);
        panel.add(new JLabel("Replenish:"));
        panel.add(replenishField);
        panel.add(new JLabel("Opening:"));
        panel.add(openingField);

        int option = JOptionPane.showConfirmDialog(this, panel, "Enter New Data", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String dateID = dateIDField.getText();
            int gameID = Integer.parseInt(gameIDField.getText());
            int replenish = Integer.parseInt(replenishField.getText());
            int opening = Integer.parseInt(openingField.getText());

            replenish newReplenish = new replenish();
            newReplenish.DateID = dateID;
            newReplenish.GameID = gameID;
            newReplenish.Replenish = replenish;
            newReplenish.Opening = opening;

            addRowToTable(newReplenish);
        }
    }

    private void addRowToTable(replenish newReplenish) {
        Object[] newRow = new Object[]{
                newReplenish.DateID,
                newReplenish.GameID,
                newReplenish.Replenish,
                newReplenish.Opening
        };
        tableModel1.addRow(newRow);
        mysqljdbc.insertTableSingleRecord("fact_instant_replenish", newReplenish, financeJDBC);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Form with Labels and Text Fields");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        frame.add(new jpanelshowandadd(), BorderLayout.CENTER);
        frame.setVisible(true);
    }
}