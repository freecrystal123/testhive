package swingpkg;

import swingpkg.components.jpaneldiffdedup;
import swingpkg.components.jpanelshowandadd;
import swingpkg.components.roundedButton;

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
        JPanel panelReplenishShow = new jpanelshowandadd();

        // 添加 Tab 页，标题为数字
        jtabpanelCURD.addTab("Replenish " , panelReplenishShow);

        JPanel panelDimseries = new JPanel();
        panelDimseries.add(new JLabel("Exciting content, stay tuned!  "));

        // 添加 CUrD Tab 页
        jtabpanelCURD.addTab("panelDimseries " , panelDimseries);

         // 设置标签选择效果
        jtabpanelCURD.setBackgroundAt(0, Color.gray);
        jtabpanelCURD.setBackgroundAt(1, Color.gray);
        jtabpanelCURD.setForegroundAt(0, Color.black);  // 设置选中Tab的字体颜色
        jtabpanelCURD.setForegroundAt(1, Color.black);  // 设置选中Tab的字体颜色




        // 添加 CUrD Tab 页


        // 设置标签选择效果



        tabbedPane.addTab("ETL", panelETL); // 添加第一个选项卡

        tabbedPane.addTab("CURD", jtabpanelCURD); // 添加第二个选项卡

        tabbedPane.addTab("DIFF&DEDUP", new jpaneldiffdedup()); // 添加第二个选项卡

        // 自定义Tab页样式
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));  // 设置字体
        tabbedPane.setBackgroundAt(0, Color.gray);
        tabbedPane.setBackgroundAt(1, Color.gray);
        tabbedPane.setBackgroundAt(2, Color.gray);

        // 设置选中的Tab样式（例如：选中Tab页的背景颜色）
        tabbedPane.setSelectedIndex(0);  // 设置默认选中第二个Tab
        tabbedPane.setForegroundAt(0, Color.black);  // 设置选中Tab的字体颜色
        tabbedPane.setForegroundAt(1, Color.black);  // 设置选中Tab的字体颜色
        tabbedPane.setForegroundAt(2, Color.black);  // 设置选中Tab的字体颜色
        // 将选项卡面板添加到框架
        frame.add(tabbedPane);



        // 创建两个文本框
        JTextArea textArea2 = new JTextArea();

        // 滚动条
        JScrollPane scrollPane2 = new JScrollPane(textArea2);


        scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 总是显示垂直滚动条
        // 设置文本框的位置和大小
//        textArea1.setBounds(50, 30, 500, 300); // x=50, y=50, 宽=200, 高=100
        scrollPane2.setBounds(100, 320, 500, 450); // x=50, y=160, 宽=200, 高=100



        // 创建三个按钮

        JLabel label = new JLabel("Data Exp&Imp ");
        label.setFont(new Font("Arial",Font.BOLD,20));


        JLabel startLabel = new JLabel("start date: ");
        JLabel endLabel = new JLabel("end date: ");
        startLabel.setFont(new Font("Arial",Font.PLAIN,15));
        endLabel.setFont(new Font("Arial",Font.PLAIN,15));



        JButton userinfo = new JButton("userinfo ");
        JButton trafficdata = new JButton("trafficdata ");
        JButton newregister = new JButton("newregister");

        JButton clearButton = new roundedButton("clean ");

        // 文本输入
        // 昨天日期默认
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);

        JTextField queryStart =  new JTextField(yesterday.toString());
        JTextField queryEnd =  new JTextField(today.toString());


        // 添加按钮点击事件
        userinfo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // 按钮点击后弹出一个对话框
                // 沙漏

                userinfo.setEnabled(false);
                trafficdata.setEnabled(false);
                newregister.setEnabled(false);
                frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                JOptionPane.showMessageDialog(frame, "Please be patient and do not click the button again!");
                try {
                    util.etlsqls.userinfo2SQL();
                    JOptionPane.showMessageDialog(frame, "Successful! ");
                }catch(Exception e1){
                    JOptionPane.showMessageDialog(frame, "fail ! ");
                }
                frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                textArea2.setText( util.etlsqls.getLog());
                userinfo.setEnabled(true);
                trafficdata.setEnabled(true);
                newregister.setEnabled(true);
            }

        });



        // 添加按钮点击事件
        newregister.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // 按钮点击后弹出一个对话框
                // 沙漏

                String starttime =  queryStart.getText();
                String endtime = queryEnd.getText();
                userinfo.setEnabled(false);
                trafficdata.setEnabled(false);
                newregister.setEnabled(false);
                frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                JOptionPane.showMessageDialog(frame, "Please be patient and do not click the button again!");

                try {
                    util.etlsqls.newregisteredusers(starttime, endtime);
                    JOptionPane.showMessageDialog(frame, "Successful! ");
                }catch(Exception e1){
                    util.etlsqls.InLog(e1.getMessage());
                    JOptionPane.showMessageDialog(frame, "fail ! ");
                }
                finally {
                    JOptionPane.showMessageDialog(frame, "Successful! ");
                    frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    textArea2.setText(util.etlsqls.getLog());
                    userinfo.setEnabled(true);
                    trafficdata.setEnabled(true);
                    newregister.setEnabled(true);
                }
            }

        });


        trafficdata.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                String starttime =  queryStart.getText();
                String endtime = queryEnd.getText();

                userinfo.setEnabled(false);
                trafficdata.setEnabled(false);
                newregister.setEnabled(false);
                // 沙漏
                frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                JOptionPane.showMessageDialog(frame, "Please be patient and do not click the button again!");
                try {
                    util.etlsqls.traffic_data_temp(starttime,endtime);
                    util.etlsqls.ftd();
                    util.etlsqls.trafficdataandftdDMLSQL();
                    JOptionPane.showMessageDialog(frame, "Successful! ");
                }catch (Exception e1){
                    util.etlsqls.InLog(e1.getMessage());
                    JOptionPane.showMessageDialog(frame, "failed! \n "+e1.getMessage());
                    e1.printStackTrace();
                }
                finally {
                    frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    textArea2.setText(util.etlsqls.getLog());
                    userinfo.setEnabled(true);
                    trafficdata.setEnabled(true);
                    newregister.setEnabled(true);
                }
            }
        });


        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea2.setText("");
            }
            });






        // 设置按钮的位置和大小
        label.setBounds(100, 100, 300, 40);  // x=300, y=50, 宽=120, 高=40
        userinfo.setBounds(100, 170, 120, 40); // x=300, y=100, 宽=120, 高=40
        trafficdata.setBounds(280, 170, 120, 40); // x=300, y=100, 宽=120, 高=40
        newregister.setBounds(460, 170, 120, 40); // x=300, y=100, 宽=120, 高=40

        clearButton.setBounds(540,270,100,40);

        queryStart.setBounds(200, 50, 120, 40); // x=300, y=100, 宽=120, 高=40
        queryEnd.setBounds(460, 50, 120, 40); // x=300, y=100, 宽=120, 高=40
        startLabel.setBounds(120, 50, 100, 40);
        endLabel.setBounds(375, 50, 100, 40);

        // 将文本框和按钮添加到 JPanel
        panelETL.add(scrollPane2);
        panelETL.add(label);
        panelETL.add(userinfo);
        panelETL.add(trafficdata);
        panelETL.add(queryStart);
        panelETL.add(queryEnd);
        panelETL.add(newregister);
        panelETL.add(startLabel);
        panelETL.add(endLabel);
        panelETL.add(clearButton);
        // 将 JPanel 添加到 JFrame


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


