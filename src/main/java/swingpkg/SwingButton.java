package swingpkg;

import javax.swing.*;
import java.awt.*;

public class SwingButton
{

    public static void main(String[] args) {
        // 创建 JFrame 窗口
        JFrame frame = new JFrame("SetBounds Button Example");

        // 设置关闭操作
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 禁用布局管理器，使得可以手动控制组件位置和大小
        frame.setLayout(null);

        // 创建一个 JButton（按钮）
        JButton button = new JButton("Click Me");

        // 使用 setBounds() 设置按钮的位置和大小
        // x=50, y=100 (按钮的左上角位置)，宽度 150，高度 50
        button.setBounds(50, 100, 150, 50);

        // 将按钮添加到 JFrame 中
        frame.add(button);

        // 设置窗口的大小
        frame.setSize(400, 300);

        // 设置窗口居中显示
        frame.setLocationRelativeTo(null);

        // 显示窗口
        frame.setVisible(true);
    }


}
