import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class FiftyFifty {
    private Connection connection;
    private int questionId;

    public FiftyFifty(Connection connection, int questionId) {
        this.connection = connection;
        this.questionId = questionId;
    }

    public void use() {
        try {
            // Fetch the specific question using the provided questionId
            String query = "SELECT * FROM QUESTIONS WHERE QUESTIONID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, questionId);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the retrieved row
            if (resultSet.next()) {
                String correctAnswer = resultSet.getString("CORRECTANSWER");
                String[] allAnswers = {
                        correctAnswer,
                        resultSet.getString("ANSWERA"),
                        resultSet.getString("ANSWERB"),
                        resultSet.getString("ANSWERC"),
                        resultSet.getString("ANSWERD")
                };

                // Shuffle the answer choices
                List<String> shuffledAnswers = new ArrayList<>(List.of(allAnswers));
                Collections.shuffle(shuffledAnswers);

                // Find a random answer that is different from the correct answer
                String randomAnswer;
                do {
                    randomAnswer = shuffledAnswers.get(new Random().nextInt(shuffledAnswers.size()));
                } while (randomAnswer.equals(correctAnswer));

                // Call the showFiftyFiftyPopup method with the relevant data
                showFiftyFiftyPopup(correctAnswer, randomAnswer);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showFiftyFiftyPopup(String correctAnswer, String randomAnswer) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Fifty-Fifty");
        alert.setHeaderText(null);

        Random random = new Random();
        boolean displayCorrectFirst = random.nextBoolean();
        String contentText;
        if (displayCorrectFirst) 
            contentText = correctAnswer + " or " + randomAnswer;
        else 
            contentText = randomAnswer + " or " + correctAnswer;

        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
