package swingpkg.components;

import jdbc.dmlacid;
import jdbc.sqlserverjdbcconn;
import pojp.dbconntype;
import pojp.factjobscheduler;
import util.etlsqls;
import util.timeutils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
    private JTextArea textArea; // 新增 TextArea

    public jpanelsched() {
        setLayout(new BorderLayout());

        // 定义列名
        String[] columnNames = {"Select", "Job Id","Job Name", "Job CallTime", "Job EndTime", "Job frequency", "Num of calls"};
        Object[][] data = {
                {false,"1","Monitor Lottery", getCurrentTime(), getCurrentTime(), "15", 0},
                {false,"2","Monitor TW", getCurrentTime(), getCurrentTime(), "15", 0}
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
        textArea = new JTextArea(5, 50); // 设置初始行数和列数
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane textScrollPane = new JScrollPane(textArea);
        textScrollPane.setBorder(BorderFactory.createTitledBorder("Logs / Messages")); // 增加边框标题
        add(textScrollPane, BorderLayout.CENTER);

        // 按钮面板
        JButton actionButton = new JButton("Submit");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(actionButton);
        add(buttonPanel, BorderLayout.SOUTH); // 让按钮处于 TextArea 下方

        // 按钮事件：向 TextArea 追加日志信息
        actionButton.addActionListener(e -> {
            textArea.append("=== Selected Jobs at " + getCurrentTime() + " ===\n");
            boolean invokeflag = false;
            boolean hasSelection = false; // 标记是否有选中的行
            List<factjobscheduler> listjobs = etlsqls.listScheduerInfos();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                boolean needUpdate = false;

                Boolean isSelected = (Boolean) tableModel.getValueAt(i, 0);
                // 如果JobId被选中了，而且是Down 状态可以启动
                if (isSelected != null && isSelected) {
                    hasSelection = true;
                    factjobscheduler  factjobschedulerItemUpdate = new factjobscheduler();
                    StringBuilder rowContent = new StringBuilder();
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        rowContent.append(tableModel.getColumnName(j)).append(": ")
                                .append(tableModel.getValueAt(i, j)).append(" | ");

                        for(factjobscheduler factjobschedulerItem:listjobs){

                            if(j==1){
                                Integer Job_Id = Integer.parseInt(tableModel.getValueAt(i, j).toString());
                                if(Job_Id.equals(factjobschedulerItem.getJob_id()) ){
                                    if("Down".equals(factjobschedulerItem.getStatus())){
                                        factjobschedulerItemUpdate.setJob_id(Job_Id);
                                        needUpdate = true;
                                        invokeflag = true;
                                    }
                                }

                            }
                            if(needUpdate){
                                if("Down".equals(factjobschedulerItem.getStatus())){
                                    factjobschedulerItemUpdate.setCall_start_time(timeutils.getConvertDate(getCurrentTime()));
                                    factjobschedulerItemUpdate.setLast_call_time(timeutils.getConvertDate(getCurrentTime()));
                                    factjobschedulerItemUpdate.setJob_name(tableModel.getValueAt(i, 2).toString());
                                    factjobschedulerItemUpdate.setJob_frequency(Integer.parseInt("15"));
                                }

                            }
                        }
                        factjobschedulerItemUpdate.setStatus("running");
                        factjobschedulerItemUpdate.setNum_of_calls(0);

                    }
                    try {
                        if(needUpdate){
                            dmlacid.updateTableRecord(sqlserverjdbcconn.getInstance(dbconntype.sqlserverconn.general).getConnection(),"fact_job_scheduler",factjobschedulerItemUpdate,"job_id" );
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            if (!hasSelection) {
                textArea.append("No jobs selected.\n");
            }
            textArea.append("====================================\n");
            // 启动自动刷新 只是启动一次
            if(invokeflag){
                startAutoRefresh();
            }

        });


    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }


    private void startAutoRefresh() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::CallAndCheckMonitorJob, 0, 3, TimeUnit.MINUTES);
    }
    // Call and check all tasks
    private void CallAndCheckMonitorJob(){
        Integer numofcalls = 0;
        try {
            List<factjobscheduler> listjobs = etlsqls.listScheduerInfos();
            for(factjobscheduler factjobschedulerItem:listjobs){
               if("running".equals( factjobschedulerItem.getStatus())){
                   etlsqls.fail_reason_monitoring(factjobschedulerItem.getJob_name());
                   factjobscheduler factjobschedulerItemUpdate = new factjobscheduler();
                   factjobschedulerItemUpdate.setJob_id(factjobschedulerItem.getJob_id());
                   factjobschedulerItemUpdate.setStatus("running");
                   factjobschedulerItemUpdate.setJob_name(factjobschedulerItem.getJob_name());
                   factjobschedulerItemUpdate.setCall_start_time(factjobschedulerItem.getCall_start_time());
                   factjobschedulerItemUpdate.setJob_frequency(factjobschedulerItem.getJob_frequency());
                    numofcalls = factjobschedulerItem.getNum_of_calls()+1;
                   factjobschedulerItemUpdate.setNum_of_calls(numofcalls);
                   factjobschedulerItemUpdate.setLast_call_time(timeutils.getConvertDate(getCurrentTime()));
                   dmlacid.updateTableRecord(sqlserverjdbcconn.getInstance(dbconntype.sqlserverconn.general).getConnection(),"fact_job_scheduler",factjobschedulerItemUpdate,"job_id" );
                   textArea.append("Progress updated at " + util.etlsqls.getLog() + "\n");
               }
            }
            SwingUtilities.invokeLater(() -> {
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    int JobId = i+1;
                    for(factjobscheduler factjobscheduler:listjobs){
                        if(JobId==factjobscheduler.getJob_id()){
                            tableModel.setValueAt(factjobscheduler.getCall_start_time(), i, 3);
                            tableModel.setValueAt(getCurrentTime(), i, 4);
                            tableModel.setValueAt(factjobscheduler.getNum_of_calls()+1, i, 6);
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