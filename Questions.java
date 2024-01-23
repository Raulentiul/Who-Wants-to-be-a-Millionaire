import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class Questions {
    private Connection connection;
    public int getCorrectAnswer;
    public static int questionId;
    public static String questionText;
    public static String getQuestionText;

    public void displayRandomQuestionAndAnswers(Button button1, Button button2, Button button3, Button button4, Label label) {
        try {
            String url = "jdbc:oracle:thin:@localhost:1521:XE";
            String username = "SYS as SYSDBA";
            String password = "BlaBlaBla";
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        try {
            // Query to retrieve a random question from the QUESTIONS table
            String questionQuery = "SELECT * FROM (SELECT * FROM QUESTIONS WHERE AVAILABLE = 1 ORDER BY DBMS_RANDOM.VALUE) WHERE ROWNUM <= 1";
            String updateAvailableQuery = "UPDATE QUESTIONS SET AVAILABLE = 0 WHERE QUESTIONID = ?";

            // Query to check if there are available questions
            String checkAvailabilityQuery = "SELECT COUNT(*) FROM QUESTIONS WHERE AVAILABLE = 1";
            try (PreparedStatement checkAvailabilityStatement = connection.prepareStatement(checkAvailabilityQuery)) {
                ResultSet availabilityResultSet = checkAvailabilityStatement.executeQuery();
                availabilityResultSet.next();
                int availableQuestionCount = availabilityResultSet.getInt(1);
                if (availableQuestionCount == 0) {
                    showDonePopup();
                    System.exit(0);
                    return;
                }
            }

            try (PreparedStatement questionStatement = connection.prepareStatement(questionQuery);
                 PreparedStatement updateStatement = connection.prepareStatement(updateAvailableQuery)) {
                ResultSet questionResultSet = questionStatement.executeQuery();

                if (questionResultSet.next()) {
                    // Retrieve the randomly selected question details
                    questionId = questionResultSet.getInt("QUESTIONID");
                    String questionText = questionResultSet.getString("QUESTIONTEXT");
                    String correctAnswer = questionResultSet.getString("CORRECTANSWER");
                    String answerA = questionResultSet.getString("ANSWERA");
                    String answerB = questionResultSet.getString("ANSWERB");
                    String answerC = questionResultSet.getString("ANSWERC");
                    String answerD = questionResultSet.getString("ANSWERD");

                    // Store the answers in a list
                    List<String> answers = new ArrayList<>();
                    answers.add(answerA);
                    answers.add(answerB);
                    answers.add(answerC);
                    answers.add(answerD);

                    // Randomize the order of answers
                    Collections.shuffle(answers);

                    // Display the randomized question and answers (for testing purpose)
                    System.out.println("Question ID: " + questionId);
                    System.out.println("Question: " + questionText);

                    for (int i = 0; i < answers.size(); i++) {
                        String optionLabel = "Option " + (i + 1) + ": ";
                        if (answers.get(i).equals(correctAnswer)) 
                            getCorrectAnswer = i + 1;
                        System.out.println(optionLabel + answers.get(i));
                    }

                    button1.setText("Option 1: " + answers.get(0));
                    button2.setText("Option 2: " + answers.get(1));
                    button3.setText("Option 3: " + answers.get(2));
                    button4.setText("Option 4: " + answers.get(3));
                    label.setText(questionText);
                    getQuestionText = questionText;
                    // Update the "AVAILABLE" column to 0 for the selected question
                    updateStatement.setInt(1, questionId);
                    updateStatement.executeUpdate();
                    questionResultSet.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showDonePopup() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "No available questions. Done!", ButtonType.OK);
        alert.showAndWait();
    }

    public int getCorrectQuestionNumber() {
        return getCorrectAnswer;
    }

    public String getQuestionText(){
        System.out.println(getQuestionText);
        return getQuestionText;
    }

    public void resetAvailabilityForAllQuestions() {
        try {
            String url = "jdbc:oracle:thin:@localhost:1521:XE";
            String username = "SYS as SYSDBA";
            String password = "BlaBla";
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(url, username, password);

            // Query to update all questions to set "AVAILABLE" to 1
            String resetAvailabilityQuery = "UPDATE QUESTIONS SET AVAILABLE = 1";

            try (PreparedStatement resetStatement = connection.prepareStatement(resetAvailabilityQuery)) {
                resetStatement.executeUpdate();
                System.out.println("Availability reset for all questions.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public int getQuestionId(){
        return questionId;
    }
}
