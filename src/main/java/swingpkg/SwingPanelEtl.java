package swingpkg;

import swingpkg.components.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class SwingPanelEtl {
    public static void main(String[] args) {

        // 创建一个 JFrame 窗口
        JFrame frame = new JFrame();

        // 创建一个自定义的标题标签
        JLabel titleLabel = new JLabel("Lottery BI Tool", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // 设置字体为 Arial，粗体，字号为 24
        titleLabel.setForeground(Color.black); // 设置字体颜色
        frame.getContentPane().add(titleLabel, BorderLayout.NORTH);

        // 设置关闭操作
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建一个 JPanel，并禁用布局管理器（使用绝对布局）
        JPanel panelETL = new JPanel();
        panelETL.setLayout(null); // 禁用布局管理器，使用绝对布局
        JTabbedPane tabbedPane = new JTabbedPane();

        // 创建 JTabbedPane
        JTabbedPane jtabpanelCURD = new JTabbedPane();

        // 设置 Tab 页标签纵向排列（左侧）
        jtabpanelCURD.setTabPlacement(JTabbedPane.LEFT);

        // show panel
        JPanel panelReplenishShow = new jpanelshowandaddreplenish();

        // 添加 Tab 页，标题为数字
        jtabpanelCURD.addTab("Replenish ", panelReplenishShow);

        // show panel
        JPanel jpanelshowandaddwin = new jpanelshowandaddwin();
        jpanelshowandaddwin.add(new JLabel("Exciting content, stay tuned! "));

        // 添加 CUrD Tab 页
        jtabpanelCURD.addTab("luckdrawwin ", jpanelshowandaddwin);

        // 设置标签选择效果
        jtabpanelCURD.setBackgroundAt(0, Color.gray);
        jtabpanelCURD.setBackgroundAt(1, Color.gray);
        jtabpanelCURD.setForegroundAt(0, Color.black);  // 设置选中Tab的字体颜色
        jtabpanelCURD.setForegroundAt(1, Color.black);  // 设置选中Tab的字体颜色

        // 添加 CUrD Tab 页
        tabbedPane.addTab("ETL", panelETL); // 添加第一个选项卡
        tabbedPane.addTab("CURD", jtabpanelCURD); // 添加第二个选项卡
        tabbedPane.addTab("DIFF&DEDUP", new jpaneldiffdedup()); // 添加第二个选项卡
        tabbedPane.addTab("Schd", new jpanelsched()); //

        // 自定义Tab页样式
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));  // 设置字体
        tabbedPane.setBackgroundAt(0, Color.gray);
        tabbedPane.setBackgroundAt(1, Color.gray);
        tabbedPane.setBackgroundAt(2, Color.gray);
        tabbedPane.setBackgroundAt(3, Color.gray);
        // 设置选中的Tab样式（例如：选中Tab页的背景颜色）
        tabbedPane.setSelectedIndex(0);  // 设置默认选中第二个Tab
        tabbedPane.setForegroundAt(0, Color.black);  // 设置选中Tab的字体颜色
        tabbedPane.setForegroundAt(1, Color.black);  // 设置选中Tab的字体颜色
        tabbedPane.setForegroundAt(2, Color.black);
        tabbedPane.setForegroundAt(3, Color.black);  // 设置选中Tab的字体颜色
        // 将选项卡面板添加到框架
        frame.add(tabbedPane);

        // 创建两个文本框
        JTextArea textArea2 = new JTextArea();

        // 滚动条
        JScrollPane scrollPane2 = new JScrollPane(textArea2);

        scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 总是显示垂直滚动条
        // 设置文本框的位置和大小
        scrollPane2.setBounds(100, 320, 500, 450); // x=50, y=160, 宽=200, 高=100

        // 创建下拉框选项
        String[] options = { "userinfo", "trafficdata","failreason" };

        // 创建自定义的 roundedComboBox 下拉框
        roundedComboBox actionComboBox = new roundedComboBox(options);  // Use the custom roundedComboBox

        // 创建标签
        JLabel label = new JLabel("Data Exp&Imp ");
        label.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel startLabel = new JLabel("start date: ");
        JLabel endLabel = new JLabel("end date: ");
        startLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        endLabel.setFont(new Font("Arial", Font.PLAIN, 15));

        JButton submitButton = new roundedButton("submit");
        JButton clearButton = new roundedButton("clean ");

        // 昨天日期默认
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);

        JTextField queryStart = new JTextField(yesterday.toString());
        JTextField queryEnd = new JTextField(today.toString());

        // 添加下拉框选择事件
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedAction = (String) actionComboBox.getSelectedItem();

                String starttime = queryStart.getText();
                String endtime = queryEnd.getText();

                // 禁用下拉框操作
                actionComboBox.setEnabled(false);
                frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                JOptionPane.showMessageDialog(frame, "Please be patient and do not submit again!");

                try {
                    switch (selectedAction) {
                        case "userinfo":
                            util.etlsqls.userinfo2SQL(starttime, endtime);
                            break;
                        case "trafficdata":
                            util.etlsqls.traffic_data_temp(starttime, endtime);
//                            util.etlsqls.ftd();
//                            util.etlsqls.trafficdataandftdDMLSQL(starttime, endtime);
                            break;
                        case "failreason":
                            util.etlsqls.failreason(starttime, endtime);
                            break;
                    }
                    JOptionPane.showMessageDialog(frame, "Successful! ");
                } catch (Exception ex) {
                    util.etlsqls.InLog(ex.getMessage());
                    JOptionPane.showMessageDialog(frame, "Failed! \n" + ex.getMessage());
                    ex.printStackTrace();
                } finally {
                    frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    textArea2.setText(util.etlsqls.getLog());
                    actionComboBox.setEnabled(true);
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea2.setText("");
            }
        });

        // 设置组件位置
        label.setBounds(100, 20, 300, 40); // 将"Data Exp&Imp"放在 start date 上方
        actionComboBox.setBounds(100, 170, 120, 40); // 下拉框位置
        submitButton.setBounds(230, 170, 120, 40); // 提交按钮位置
        queryStart.setBounds(200, 90, 120, 40); // x=300, y=100, 宽=120, 高=40
        queryEnd.setBounds(460, 90, 120, 40); // x=300, y=100, 宽=120, 高=40
        startLabel.setBounds(120, 90, 100, 40);
        endLabel.setBounds(375, 90, 100, 40);
        clearButton.setBounds(540, 270, 100, 40);

        // 将组件添加到面板
        panelETL.add(scrollPane2);
        panelETL.add(label);
        panelETL.add(actionComboBox);
        panelETL.add(submitButton);
        panelETL.add(queryStart);
        panelETL.add(queryEnd);
        panelETL.add(startLabel);
        panelETL.add(endLabel);
        panelETL.add(clearButton);

        // 创建底部标签
        JLabel footerLabel = new JLabel("@momentum. opstech", JLabel.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 10)); // 设置字体
        footerLabel.setBackground(Color.darkGray);
        footerLabel.setForeground(Color.BLACK);  // 设置字体颜色
        footerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));  // 设置上、左、下、右边距
        frame.getContentPane().add(footerLabel, BorderLayout.SOUTH); // 将底部标签添加到窗口底部

        // 设置窗口的大小
        frame.setSize(700, 900);

        // 设置窗口居中显示
        frame.setLocationRelativeTo(null);

        // 显示窗口
        frame.setVisible(true);
    }
}
