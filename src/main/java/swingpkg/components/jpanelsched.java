package swingpkg.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public jpanelsched() {
        setLayout(new BorderLayout());

        // 定义列名，新增“Job 名称”列
        String[] columnNames = {"Select", "Job 名称", "Job 调用时间", "Job 停止时间", "Job 启动频率", "Progress"};
        Object[][] data = {
                {false, "Job 1", getCurrentTime(), getFutureTime(), "每分钟", 50},
                {false, "Job 2", getCurrentTime(), getFutureTime(), "每小时", 30},
                {false, "Job 3", getCurrentTime(), getFutureTime(), "每天", 75}
        };

        // 初始化表格模型
        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // 第一列为布尔值，最后一列为进度条，其他列为字符串
                return columnIndex == 0 ? Boolean.class : (columnIndex == 5 ? Integer.class : String.class);
            }
        };

        table = new JTable(tableModel);
        table.setBackground(Color.lightGray);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setForeground(Color.black);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // 为“Progress”列设置进度条渲染器
        table.getColumn("Progress").setCellRenderer(new ProgressRenderer());

        // 操作按钮
        JButton actionButton = new JButton("Submit");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(actionButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 启动自动刷新
        startAutoRefresh();
    }

    /**
     * 获取当前时间，格式化为字符串
     */
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * 获取未来时间，格式化为字符串（例如，当前时间+1小时）
     */
    private String getFutureTime() {
        long futureTime = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(futureTime));
    }

    /**
     * 使用ScheduledExecutorService启动定时刷新
     */
    private void startAutoRefresh() {
        // 创建ScheduledExecutorService来调度任务
        scheduler = Executors.newSingleThreadScheduledExecutor();

        // 定时任务，每分钟执行一次
        scheduler.scheduleAtFixedRate(this::updateJobProgress, 0, 1, TimeUnit.MINUTES);
    }

    /**
     * 更新Job的进度
     */
    private void updateJobProgress() {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                // 更新“Job 调用时间”和“Job 停止时间”
                tableModel.setValueAt(getCurrentTime(), i, 2);
                tableModel.setValueAt(getFutureTime(), i, 3);

                // 更新进度
                int progress = (int) tableModel.getValueAt(i, 5);
                progress = Math.min(100, progress + (int) (Math.random() * 10)); // 随机增加进度
                tableModel.setValueAt(progress, i, 5);
            }
        });
    }

    /**
     * 进度条渲染器，显示进度条
     */
    private static class ProgressRenderer extends JProgressBar implements TableCellRenderer {
        public ProgressRenderer() {
            setMinimum(0);
            setMaximum(100);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Integer) {
                setValue((Integer) value);
            }
            return this;
        }
    }

    public static void main(String[] args) {
        // 创建JFrame并添加面板
        JFrame frame = new JFrame("Job Schedule Panel with Progress");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 300); // 调整窗口大小以适应新列
        frame.add(new jpanelsched());
        frame.setVisible(true);
    }
}
