import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class AskAudience {
    private Connection connection;
    private int questionId;

    AskAudience(Connection connection, int questionId){
        this.connection = connection;
        this.questionId = questionId;
    }

    public String use() {
        String query = "SELECT ANSWERA, ANSWERB, ANSWERC, ANSWERD FROM QUESTIONS WHERE QUESTIONID = ?";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, questionId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String answerA = resultSet.getString("ANSWERA");
                    String answerB = resultSet.getString("ANSWERB");
                    String answerC = resultSet.getString("ANSWERC");
                    String answerD = resultSet.getString("ANSWERD");
    
                    return getFavorableAudience(answerA, answerB, answerC, answerD);
                } else 
                    return "No data found for the given questionId.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error occurred while processing the question.";
        }
    }

    private static String getFavorableAudience(String answerA, String answerB, String answerC, String answerD) {
        Random random = new Random();
        int audience1Choice = random.nextInt(4) + 1;
        int audience2Choice = random.nextInt(4) + 1;
    
        String audience1Answer = getAnswerByIndex(answerA, answerB, answerC, answerD, audience1Choice);
        String audience2Answer = getAnswerByIndex(answerA, answerB, answerC, answerD, audience2Choice);
    
        String result = "Audience 1 chose: " + audience1Answer + "\n" +
                        "Audience 2 chose: " + audience2Answer + "\n\n";
    
        if (audience1Choice == audience2Choice) 
            result += "Both audiences are favorable.";
        else if (audience1Choice > audience2Choice) 
            result += "Audience 1 is more favorable.";
        else 
            result += "Audience 2 is more favorable.";
        return result;
    }
    

    private static String getAnswerByIndex(String answerA, String answerB, String answerC, String answerD, int index) {
        switch (index) {
            case 1:
                return answerA;
            case 2:
                return answerB;
            case 3:
                return answerC;
            case 4:
                return answerD;
            default:
                return "";
        }
    }
}
