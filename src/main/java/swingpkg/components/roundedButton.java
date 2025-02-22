package swingpkg.components;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class roundedButton extends JButton {

    public roundedButton(String text) {
        super(text);
        setContentAreaFilled(false); // 使背景透明
        setFocusPainted(false);      // 取消焦点框
        setBorderPainted(false);     // 取消边框
        setFont(new Font("Arial", Font.BOLD, 14)); // 可选：自定义字体
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
            g.setColor(Color.darkGray); // 按下时的颜色
        } else if (getModel().isRollover()) {
            g.setColor(Color.lightGray); // 悬浮时的颜色
        } else {
            g.setColor(Color.white); // 默认颜色
        }

        g.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight()/2, getHeight()/2); // 画圆形按钮
        super.paintComponent(g);
    }

}
