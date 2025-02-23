package swingpkg.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class jpanelsched extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private Timer timer;

    public jpanelsched() {
        setLayout(new BorderLayout());

        String[] columnNames = {"Select", "Name", "Description", "Progress"};
        Object[][] data = {
                {false, "Item 1", "Description 1", 50},
                {false, "Item 2", "Description 2", 75},
                {false, "Item 3", "Description 3", 30}
        };

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : (columnIndex == 3 ? Integer.class : String.class);
            }
        };

        table = new JTable(tableModel);
        table.setBackground(Color.lightGray);
        table.getTableHeader().setFont(new Font("Arial",Font.BOLD,14));
        table.getTableHeader().setForeground(Color.black);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        table.getColumn("Progress").setCellRenderer(new ProgressRenderer());

        JButton actionButton = new JButton("Submit");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(actionButton);
        add(buttonPanel, BorderLayout.SOUTH);

        startAutoRefresh();
    }

    private void startAutoRefresh() {
        timer = new Timer(60000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProgress();
            }
        });
        timer.start();
    }

    private void updateProgress() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int progress = (int) tableModel.getValueAt(i, 3);
            progress = Math.min(100, progress + (int) (Math.random() * 10)); // Increment progress randomly
            tableModel.setValueAt(progress, i, 3);
        }
    }

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
        JFrame frame = new JFrame("Panel Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.add(new jpanelsched());
        frame.setVisible(true);
    }
}
