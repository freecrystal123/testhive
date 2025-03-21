package swingpkg;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDate;
public class JavaFXPanelEtl extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // 创建一个 VBox 布局容器
        VBox vbox = new VBox(10); // 10 是控件之间的间距
        vbox.setStyle("-fx-padding: 20;");  // 设置内边距

        // 创建一个 Label
        Label label = new Label("Data Exp&Imp");
        label.setFont(new Font("Arial", 20));

        // 创建按钮
        Button userinfo = new Button("userinfo");
        Button trafficdata = new Button("trafficdata");
        Button newregister = new Button("newregister");

        // 创建文本框（用于输入日期）
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);

        TextField startFieldRegister = new TextField(yesterday.toString());
        TextField endFieldRegister = new TextField(today.toString());

        TextField startFieldTrafficData = new TextField(yesterday.toString());
        TextField endFieldTrafficData = new TextField(today.toString());

        // 创建 TextArea 和 ScrollPane
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefHeight(600); // 设置为 200 像素高度
        textArea.setPrefWidth(600);  // 设置为 600 像素宽度（可根据需要调整）
        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);

        // 创建按钮点击事件
        userinfo.setOnAction(event -> {
            String starttime = startFieldTrafficData.getText();
            String endtime = endFieldTrafficData.getText();
            userinfo.setDisable(true);
            trafficdata.setDisable(true);
            newregister.setDisable(true);
            primaryStage.getScene().setCursor(javafx.scene.Cursor.WAIT);

            // 显示消息
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Please be patient and do not click the button again!");
            alert.showAndWait();

            try {
                // 执行 SQL 操作（此部分需要你实际的方法实现）
                util.etlsqls.userinfo2SQL(starttime, endtime);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setContentText("Successful!");
                successAlert.showAndWait();
            } catch (Exception e1) {
                Alert failAlert = new Alert(Alert.AlertType.ERROR);
                failAlert.setContentText("Fail!");
                failAlert.showAndWait();
            }

            primaryStage.getScene().setCursor(javafx.scene.Cursor.DEFAULT);
            textArea.setText(util.etlsqls.getLog());
        });

        // trafficdata 按钮事件
        trafficdata.setOnAction(event -> {
            String starttime = startFieldTrafficData.getText();
            String endtime = endFieldTrafficData.getText();

            userinfo.setDisable(true);
            trafficdata.setDisable(true);
            newregister.setDisable(true);
            primaryStage.getScene().setCursor(javafx.scene.Cursor.WAIT);

            // 显示消息
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Please be patient and do not click the button again!");
            alert.showAndWait();

            try {
                util.etlsqls.traffic_data_temp(starttime, endtime);
                util.etlsqls.ftd();
                util.etlsqls.trafficdataandftdDMLSQL(starttime,endtime);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setContentText("Successful!");
                successAlert.showAndWait();
            } catch (Exception e1) {
                Alert failAlert = new Alert(Alert.AlertType.ERROR);
                failAlert.setContentText("Fail! " + e1.getMessage());
                failAlert.showAndWait();
            }

            primaryStage.getScene().setCursor(javafx.scene.Cursor.DEFAULT);
            textArea.setText(util.etlsqls.getLog());
        });

        // newregister 按钮事件
        newregister.setOnAction(event -> {
            String starttime = startFieldRegister.getText();
            String endtime = endFieldRegister.getText();

            userinfo.setDisable(true);
            trafficdata.setDisable(true);
            newregister.setDisable(true);
            primaryStage.getScene().setCursor(javafx.scene.Cursor.WAIT);

            // 显示消息
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Please be patient and do not click the button again!");
            alert.showAndWait();

            try {
                util.etlsqls.failreason(starttime, endtime);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setContentText("Successful!");
                successAlert.showAndWait();
            } catch (Exception e1) {
                Alert failAlert = new Alert(Alert.AlertType.ERROR);
                failAlert.setContentText("Fail!");
                failAlert.showAndWait();
            }

            primaryStage.getScene().setCursor(javafx.scene.Cursor.DEFAULT);
            textArea.setText(util.etlsqls.getLog());
        });

        // 设置控件的位置和大小
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);

        // 创建 HBox 放置按钮和文本框（确保它们在同一行）
        HBox hboxUserInfo = new HBox(10, userinfo, startFieldRegister, endFieldRegister);
        HBox hboxTrafficData = new HBox(10, trafficdata, startFieldTrafficData, endFieldTrafficData);
        HBox hboxNewRegister = new HBox(10, newregister, startFieldRegister, endFieldRegister);

        // 设置按钮的最小宽度或最大宽度，确保它们的大小一致
        userinfo.setPrefWidth(150);
        trafficdata.setPrefWidth(150);
        newregister.setPrefWidth(150);
        // 设置按钮宽度相同
        HBox.setHgrow(userinfo, Priority.ALWAYS);
        HBox.setHgrow(trafficdata, Priority.ALWAYS);
        HBox.setHgrow(newregister, Priority.ALWAYS);


        // 将控件添加到布局
        vbox.getChildren().addAll(label, hboxUserInfo, hboxTrafficData, hboxNewRegister, scrollPane);

        // 创建场景
        Scene scene = new Scene(vbox, 700, 800);

        // 设置窗口的标题和场景
        primaryStage.setTitle("ETL TOOL");
        primaryStage.setScene(scene);

        // 显示窗口
        primaryStage.show();
    }
}