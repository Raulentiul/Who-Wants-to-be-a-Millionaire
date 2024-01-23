import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {
    public static String dbURL = "jdbc:oracle:thin:@localhost:1521:XE";
    public static String username = "SYS as SYSDBA";
    public static String password = "BlaBlaBla";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Who wants to be a millionaire?");

        // Load an image
        Image backgroundImage = new Image("background.jpg");
        ImageView backgroundImageView = new ImageView(backgroundImage);

        // Welcome text and buttons
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");
        
        // Vertical box to hold buttons
        VBox vbox = new VBox(20); 
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(yesButton, noButton);

        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, vbox);

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);

        // Set background image
        root.setBackground(new Background(new BackgroundImage(backgroundImage, null, null, null, null)));

        // Button actions
        yesButton.setOnAction(event -> handleYesButton(primaryStage));
        noButton.setOnAction(event -> handleNoButton());

        primaryStage.show();
    }

    private void handleYesButton(Stage primaryStage) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection connection = DriverManager.getConnection(dbURL, username, password);
            LogIn logInInstance = new LogIn(connection);
            logInInstance.start(primaryStage);
        } catch (IllegalArgumentException | SQLException | ClassNotFoundException e) {
            System.out.println("doesn't work");
        }
    }

    private void handleNoButton() {
        Platform.exit();
    }
}
