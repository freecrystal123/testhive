package swingpkg;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
public class lookbeauty  extends Application {

        @Override
        public void start(Stage primaryStage) {
            Button btn = new Button("点击我");
            btn.setOnAction(e -> System.out.println("按钮被点击了"));

            StackPane root = new StackPane();
            root.getChildren().add(btn);

            Scene scene = new Scene(root, 300, 250);
            primaryStage.setTitle("JavaFX 示例");
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }
}
