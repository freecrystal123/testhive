package swingpkg.components;

import jdbc.dmlacid;
import jdbc.sqlserverjdbcconn;
import pojp.dbconntype;
import pojp.factjobscheduler;
import util.etlsqls;
import util.timeutils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 显示Job相关数据的面板，包含刷新功能和自动更新进度。
 */
public class jpanelsched extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private ScheduledExecutorService scheduler;
    private JTextArea textArea;
    private JButton cleanButton; // 新增按钮变量

    public jpanelsched() {
        setLayout(new BorderLayout());

        // 定义列名
        String[] columnNames = {"Select", "Job Id", "Job Name", "Job CallTime", "Job EndTime", "Job frequency", "Num of calls"};
        Object[][] data = {
                {false, "1", "Monitor Lottery", timeutils.getCurrentTime(), timeutils.getCurrentTime(), "15", 0},
                {false, "2", "Monitor TW", timeutils.getCurrentTime(), timeutils.getCurrentTime(), "15", 0}
        };

        // 初始化表格模型
        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : (columnIndex == 5 ? Integer.class : String.class);
            }
        };

        table = new JTable(tableModel);
        table.setBackground(Color.lightGray);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setForeground(Color.black);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.NORTH);

        // 创建 TextArea 并添加滚动支持
        textArea = new JTextArea(5, 50);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane textScrollPane = new JScrollPane(textArea);
        textScrollPane.setBorder(BorderFactory.createTitledBorder("Logs / Messages"));

        // 创建新的面板用于放置按钮
        JPanel textPanel = new JPanel(new BorderLayout());
        // 在文本区上方添加按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // 右对齐按钮
        cleanButton = new roundedButton("Clean Log");
        buttonPanel.add(cleanButton);
        textPanel.add(buttonPanel, BorderLayout.NORTH);
        textPanel.add(textScrollPane, BorderLayout.CENTER);
        add(textPanel, BorderLayout.CENTER);

        // 按钮事件：向 TextArea 追加日志信息
        cleanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
            }
        });

        // 按钮面板
        JButton actionButton = new JButton("Submit");
        JPanel buttonPanel2 = new JPanel();
        buttonPanel2.add(actionButton);
        add(buttonPanel2, BorderLayout.SOUTH);

        // 按钮事件：向 TextArea 追加日志信息
        actionButton.addActionListener(e -> {
            textArea.append("=== Selected Jobs at " + timeutils.getCurrentTime() + " ===\n");
            boolean invokeflag = false;
            boolean hasSelection = false;
            List<factjobscheduler> listjobs = etlsqls.listScheduerInfos();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                boolean needUpdate = false;

                Boolean isSelected = (Boolean) tableModel.getValueAt(i, 0);
                if (isSelected != null && isSelected) {
                    hasSelection = true;
                    factjobscheduler factjobschedulerItemUpdate = new factjobscheduler();
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        // 处理每一行数据
                    }
                }
            }

            if (!hasSelection) {
                textArea.append("No jobs selected.\n");
            }
            textArea.append("====================================\n");
            if (invokeflag) {
                startAutoRefresh();
            }

        });
    }

    private void startAutoRefresh() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::CallAndCheckMonitorJob, 0, 3, TimeUnit.MINUTES);
    }

    private void CallAndCheckMonitorJob() {
        Integer numofcalls = 0;
        try {
            List<factjobscheduler> listjobs = etlsqls.listScheduerInfos();
            for (factjobscheduler factjobschedulerItem : listjobs) {
                if ("running".equals(factjobschedulerItem.getStatus())) {
                    // 更新任务进度
                }
            }
            SwingUtilities.invokeLater(() -> {
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    int JobId = i + 1;
                    for (factjobscheduler factjobscheduler : listjobs) {
                        if (JobId == factjobscheduler.getJob_id()) {
                            tableModel.setValueAt(factjobscheduler.getCall_start_time(), i, 3);
                            tableModel.setValueAt(timeutils.getCurrentTime(), i, 4);
                            tableModel.setValueAt(factjobscheduler.getNum_of_calls() + 1, i, 6);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Job Schedule Panel with Progress & Logs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 400); // 调整窗口大小以适应 TextArea
        frame.add(new jpanelsched());
        frame.setVisible(true);
    }
}
