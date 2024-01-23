import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PhoneFriend {
    public int questionId;
    private int correctAnswer;
    private String questionText;

    public PhoneFriend(int questionId, int correctAnswer, String questionText) {
        this.questionId = questionId;
        this.correctAnswer = correctAnswer;
        this.questionText = questionText;
    }

    public void use() {
        Stage stage = new Stage();
        stage.setTitle("Phone a Friend");

        StackPane root = new StackPane();
        Scene scene = new Scene(root, 300, 200);

        Label timerLabel = new Label(questionText + " Hmmm... Lemme think.\nTime remaining: 30 seconds");
        root.getChildren().add(timerLabel);

        // Create a service for the timer
        TimerService timerService = new TimerService(30);
        timerLabel.textProperty().bind(timerService.messageProperty());

         // Set action to be performed on successful completion (when timer expires)
         timerService.setOnSucceeded(event -> {
            stage.close();
            displayAnswer();
        });
        timerService.start();

        stage.setScene(scene);
        stage.showAndWait();
    }

    private void displayAnswer() {
        // This is where you can print the correct answer on the popup
        Stage answerStage = new Stage();
        answerStage.setTitle("Correct Answer");
        StackPane answerRoot = new StackPane();
        Scene answerScene = new Scene(answerRoot, 300, 200);
        Label answerLabel = new Label("I think the correct answer is Option " + correctAnswer);
        answerRoot.getChildren().add(answerLabel);
        answerStage.setScene(answerScene);
        answerStage.show();
    }

    private class TimerService extends Service<Void> {
        private int secondsRemaining;

        public TimerService(int seconds) {
            this.secondsRemaining = seconds;
        }

        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    try {
                        while (secondsRemaining > 0) {
                            Thread.sleep(1000);
                            secondsRemaining--;
                            updateMessage(questionText + "\nHmmm... Lemme think.\nTime remaining: " + secondsRemaining + " seconds");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
        }
    }
}
