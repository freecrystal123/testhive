package swingpkg.components;

import com.toedter.calendar.JDateChooser;
import jdbc.dmlacid;
import jdbc.sqlserverjdbcconn;
import pojp.dbconntype;
import pojp.replenish;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class jpanelshowandaddwin extends JPanel {

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

    public jpanelshowandaddwin() {
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
            List<replenish> replenishLists = null;
            try {
                replenishLists = dmlacid.listTableRecord(sqlserverjdbcconn.getInstance(dbconntype.sqlserverconn.vivian).getConnection(),"fact_instant_replenish", replenish.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        // 创建 JDateChooser 作为日期输入框
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy/MM/dd"); // 设置日期格式
        dateChooser.setPreferredSize(new Dimension(150, 25));

        // 创建 GameID 下拉框
        JComboBox<String> gameIDComboBox = new JComboBox<>();
        gameIDComboBox.setPreferredSize(new Dimension(150, 25));
        gameIDComboBox.addItem("Loading...");
        // 异步加载 GameID 数据
        CompletableFuture.runAsync(() -> {
            Map<String,Object> gameIDs = dmlacid.listMapColumn("SELECT DISTINCT seriesno,gamename FROM dim_series where gametype = 'Instant' ", financeJDBC);
            SwingUtilities.invokeLater(() -> {
                gameIDComboBox.removeAllItems();
                for (Map.Entry<String, Object> entry : gameIDs.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    gameIDComboBox.addItem(key+"-"+value.toString());

                }
            });
        });




        JTextField replenishField = new JTextField(10);
        JTextField openingField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("DateID:"));
        panel.add(dateChooser);
        panel.add(new JLabel("GameID:"));
        panel.add(gameIDComboBox);
        panel.add(new JLabel("Replenish:"));
        panel.add(replenishField);
        panel.add(new JLabel("Opening:"));
        panel.add(openingField);

        while (true) {
            int option = JOptionPane.showConfirmDialog(this, panel, "Enter New Data",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (option != JOptionPane.OK_OPTION) {
                return;
            }

            // 获取日期
            Date selectedDate = dateChooser.getDate();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this, "Please select a valid date!", "Input Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            // 格式化日期
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            String dateID = sdf.format(selectedDate);

            String gameIDText = (String) gameIDComboBox.getSelectedItem();
            String replenishText = replenishField.getText().trim();
            String openingText = openingField.getText().trim();

            if (gameIDText == null || replenishText.isEmpty() || openingText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required. Please re-enter!", "Input Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            try {
                Integer gameID = Integer.parseInt(gameIDText.split("-")[0]);
                int replenish = Integer.parseInt(replenishText);
                int opening = Integer.parseInt(openingText);

                replenish newReplenish = new replenish();
                newReplenish.DateID = dateID;
                newReplenish.GameID = gameID;
                newReplenish.Replenish = replenish;
                newReplenish.Opening = opening;

                addRowToTable(newReplenish);
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Replenish and Opening must be numbers!", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
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

        try {
            dmlacid.insertTableSingleRecord(sqlserverjdbcconn.getInstance(dbconntype.sqlserverconn.vivian).getConnection(),"fact_instant_replenish", newReplenish);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Form with Labels and Text Fields");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        frame.add(new jpanelshowandaddwin(), BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
