import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Admin extends Application {
    private TextField questionTextField = new TextField();
    private TextField correctAnswerField = new TextField();
    private TextField answerAField = new TextField();
    private TextField answerBField = new TextField();
    private TextField answerCField = new TextField();
    private TextField answerDField = new TextField();
    public static boolean errorVariable = false;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Add Question to Database");

        // Set up the root layout
        GridPane grid = new GridPane();
        grid.setAlignment(javafx.geometry.Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
    
        // Set up the background for the GridPane
        BackgroundImage backgroundImage = new BackgroundImage(
                new javafx.scene.image.Image("purple_background.jpg", 1000, 600, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT
        );
        grid.setBackground(new Background(backgroundImage));
    
        grid.add(new Label("Question Text:"), 0, 1);
        grid.add(questionTextField, 1, 1);

        grid.add(new Label("Correct Answer:"), 0, 2);
        grid.add(correctAnswerField, 1, 2);

        grid.add(new Label("Answer A:"), 0, 3);
        grid.add(answerAField, 1, 3);

        grid.add(new Label("Answer B:"), 0, 4);
        grid.add(answerBField, 1, 4);

        grid.add(new Label("Answer C:"), 0, 5);
        grid.add(answerCField, 1, 5);

        grid.add(new Label("Answer D:"), 0, 6);
        grid.add(answerDField, 1, 6);

        Button addButton = new Button("Add Question");
        addButton.setOnAction(e -> {
            errorVariable = false;
            addQuestionToDatabase();
            if(errorVariable == false)
                showConfirmationPopup("Question added to database");
        });
    
        grid.add(addButton, 1, 7);
    
        Scene scene = new Scene(grid, 1000, 600);
        primaryStage.setScene(scene);
    
        primaryStage.show();
    }

    private void showConfirmationPopup(String message) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Confirmation");
    alert.setHeaderText(null);
    alert.setContentText(message);

    alert.showAndWait();
    }

    public void addQuestionToDatabase() {
        Connection connection = getDatabaseConnection();

        String countQuery = "SELECT COUNT(*) FROM QUESTIONS";
        String insertQuery = "INSERT INTO QUESTIONS (QUESTIONID, QUESTIONTEXT, CORRECTANSWER, ANSWERA, ANSWERB, ANSWERC, ANSWERD) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            // Validate that none of the input fields are empty
            if (questionTextField.getText().isEmpty() || correctAnswerField.getText().isEmpty() ||
                answerAField.getText().isEmpty() || answerBField.getText().isEmpty() ||
                answerCField.getText().isEmpty() || answerDField.getText().isEmpty()) {
                    showErrorPopup("All fields must be filled in.");
                    errorVariable = true;
                    return;
            }

            // Get the current row count
            Statement countStatement = connection.createStatement();
            ResultSet resultSet = countStatement.executeQuery(countQuery);
            resultSet.next();
            int rowCount = resultSet.getInt(1);
            int questionId = rowCount + 1;  // Increment the current row count to get the new QUESTIONID

            // Prepare the insert statement
            try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
                statement.setInt(1, questionId);
                statement.setString(2, questionTextField.getText());
                statement.setString(3, correctAnswerField.getText());
                statement.setString(4, answerAField.getText());
                statement.setString(5, answerBField.getText());
                statement.setString(6, answerCField.getText());
                statement.setString(7, answerDField.getText());

                // Execute the insert query
                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0)
                    throw new SQLException("Creating question failed, no rows affected.");
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorPopup("An error occurred while adding the question to the database.");
        }
    }

    private void showErrorPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Error");
        alert.showAndWait();
    }

    private Connection getDatabaseConnection() {
        String url = "jdbc:oracle:thin:@localhost:1521:XE";
        String username = "SYS as SYSDBA";
        String password = "BlaBlaBla";
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database");
        }
    }
}
