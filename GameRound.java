import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;

public class GameRound extends Application{
    public boolean fiftyFiftyAvailable=true;
    public boolean phoneFriendAvailable=true;
    public boolean askAudienceAvailable=true;
    public static Connection connection;
    public static int questionId;
    public static String questionText;
    private int score = 0;
    private int correctAnswer;

    GameRound(Connection connection){
        this.connection = connection;
    }

    @Override
    public void start(Stage primaryStage) {
        Questions questionsInstance = new Questions();
    
        // Set up the root layout
        BorderPane root = new BorderPane();
    
        // Set up the background
        BackgroundImage backgroundImage = new BackgroundImage(
                new javafx.scene.image.Image("purple_background.jpg", 1000, 600, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT
        );
        root.setBackground(new Background(backgroundImage));
    
        // Set up the text label
        Label label = new Label("Game Round");
        label.setStyle("-fx-font-size: 24; -fx-text-fill: black;");

        Label scoreLabel = new Label("Score: " + score);
        scoreLabel.setStyle("-fx-font-size: 18; -fx-text-fill: black;");

        // Set up the layout for the labels
        VBox labelScoreBox = new VBox(5);
        labelScoreBox.getChildren().addAll(scoreLabel);
        labelScoreBox.setAlignment(Pos.TOP_RIGHT);
    
        // Set up the buttons
        Button button1 = new Button();
        Button button2 = new Button();
        Button button3 = new Button();
        Button button4 = new Button();
    
        // Assign random answers to each button
        questionsInstance.displayRandomQuestionAndAnswers(button1, button2, button3, button4, label);
        
        // Set up the layout for buttons on the left (button1 and button2)
        VBox leftButtons = new VBox(10);
        leftButtons.getChildren().addAll(button1, button2);
        leftButtons.setAlignment(Pos.CENTER);
    
        // Set up the layout for buttons on the right (button3 and button4)
        VBox rightButtons = new VBox(10);
        rightButtons.getChildren().addAll(button3, button4);
        rightButtons.setAlignment(Pos.CENTER);
    
        // Add elements to the top-left corner of the root layout
        HBox topLeftButtons = new HBox(10);
        Button fiftyFiftyButton = new Button("FiftyFifty");
        Button phoneFriendButton = new Button("PhoneFriend");
        Button askAudienceButton = new Button("AskAudience");
        topLeftButtons.getChildren().addAll(fiftyFiftyButton, phoneFriendButton, askAudienceButton);
        root.setTop(topLeftButtons);
    
        // Create a VBox for the label and set its alignment
        VBox labelBox = new VBox(label);
        labelBox.setAlignment(Pos.CENTER);
    
        // Create an HBox for the label and answer buttons with spacing
        HBox centerBox = new HBox(20);
        centerBox.getChildren().addAll(leftButtons, labelBox, labelScoreBox, rightButtons);
        centerBox.setAlignment(Pos.CENTER);
    
        // Add elements to the root layout
        root.setCenter(centerBox);
    
        // Create the scene
        Scene scene = new Scene(root, 1000, 600);
    
        // Set up the stage
        primaryStage.setTitle("Game Round");
        primaryStage.setScene(scene);
        primaryStage.show();
    
        // Set up the correct answer
        correctAnswer = questionsInstance.getCorrectQuestionNumber();
        questionText = questionsInstance.getQuestionText();
        System.out.println(correctAnswer);
    
        // Set up button click actions
        button1.setOnAction(event -> checkAnswer(button1.getText(), button1, button2, button3, button4, label, scoreLabel));
        button2.setOnAction(event -> checkAnswer(button2.getText(), button1, button2, button3, button4, label, scoreLabel));
        button3.setOnAction(event -> checkAnswer(button3.getText(), button1, button2, button3, button4, label, scoreLabel));
        button4.setOnAction(event -> checkAnswer(button4.getText(), button1, button2, button3, button4, label, scoreLabel));
        fiftyFiftyButton.setOnAction(event -> showFiftyFiftyPopup(questionsInstance.getQuestionId()));
        fiftyFiftyButton.setOnAction(event -> {
            if (fiftyFiftyAvailable) {
                showFiftyFiftyPopup(questionsInstance.getQuestionId());
                fiftyFiftyButton.setDisable(true);
                fiftyFiftyButton.setVisible(false);
            }
        });

        phoneFriendButton.setOnAction(event -> showPhoneFriendPopup(questionsInstance.getQuestionId(),questionsInstance.getQuestionText()));
        phoneFriendButton.setOnAction(event -> {
            if (phoneFriendAvailable) {
                showPhoneFriendPopup(questionsInstance.getQuestionId(),questionsInstance.getQuestionText());
                phoneFriendButton.setDisable(true);
                phoneFriendButton.setVisible(false);
            }
        });
        
        askAudienceButton.setOnAction(event -> showAskAudiencePopup(questionsInstance.getQuestionId()));
        askAudienceButton.setOnAction(event -> {
            if (askAudienceAvailable) {
                showAskAudiencePopup(questionsInstance.getQuestionId());
                askAudienceButton.setDisable(true);
                askAudienceButton.setVisible(false);
            }
        });
    }    
    
    private void showPhoneFriendPopup(int questionId,String questionText) {
        System.out.println(questionText);
        PhoneFriend phoneFriendInstance = new PhoneFriend(questionId,correctAnswer,questionText);
        phoneFriendAvailable = false;
        phoneFriendInstance.use();
    }

    private void showAskAudiencePopup(int questionId) {
        AskAudience askAudienceInstance = new AskAudience(connection, questionId);
        String audienceResult = askAudienceInstance.use();
        askAudienceAvailable = false;

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Ask the Audience Result");
        alert.setHeaderText(null);
        alert.setContentText(audienceResult);
        alert.showAndWait();
    }

    private void showFiftyFiftyPopup(int questionId) {
        fiftyFiftyAvailable = false;
        FiftyFifty fiftyFiftyInstance = new FiftyFifty(connection,questionId);
        fiftyFiftyInstance.use();
    }

    private void checkAnswer(String selectedAnswer, Button button1, Button button2, Button button3, Button button4,Label label,Label scoreLabel) {
        int selectedAnswerNumber = Integer.parseInt(selectedAnswer.replaceAll("[^0-9]", ""));
        if (selectedAnswerNumber == correctAnswer) {
            showPopup("Correct!");
            new Thread(() -> {
                score++;
                Platform.runLater(() -> scoreLabel.setText("Score: " + score));
            }).start();
        } else {
            showPopup("Incorrect. You Lost!");
            Platform.exit();
        }

        Questions questionsInstance = new Questions();
        questionsInstance.displayRandomQuestionAndAnswers(button1, button2, button3, button4,label);
        correctAnswer = questionsInstance.getCorrectQuestionNumber();
        System.out.println(correctAnswer);
    }
    
    private void showPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Result");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}