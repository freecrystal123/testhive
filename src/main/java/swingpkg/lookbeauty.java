package swingpkg;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

public class lookbeauty  {



        public static void main(String[] args) {
            // 创建 JFrame
            JFrame frame = new JFrame("Vertical Tabs Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 300);

            // 创建 JTabbedPane
            JTabbedPane tabbedPane = new JTabbedPane();

            // 设置 Tab 页标签纵向排列（左侧）
            tabbedPane.setTabPlacement(JTabbedPane.LEFT);

            // 添加多个 Tab 页
            for (int i = 1; i <= 5; i++) {
                JPanel panel = new JPanel();
                panel.add(new JLabel("内容 " + i));

                // 添加 Tab 页，标题为数字
                tabbedPane.addTab("Tab " + i, panel);
            }

            // 将 JTabbedPane 添加到 JFrame
            frame.add(tabbedPane, BorderLayout.CENTER);

            // 显示 JFrame
            frame.setVisible(true);
        }
        }



