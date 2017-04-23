import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Graph");
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);

        Pane root = new Pane();

        GraphPainter graphPainter = new GraphPainter();

        Button btn = new Button();
        btn.setText("Add node");
        btn.setTranslateX(0);
        btn.setTranslateY(0);
        btn.setOnAction(event -> {
            System.out.println("Button clicked");
            graphPainter.addNode();
        });

        Button btn2 = new Button();
        btn2.setText("Add edge");
        btn2.setTranslateX(75);
        btn2.setTranslateY(0);
        btn2.setOnAction(event -> {
            System.out.println("Button clicked");
            graphPainter.addRandomEdge();
        });

        root.getChildren().addAll(btn, btn2, graphPainter);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}