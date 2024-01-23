import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AskAudienceTest {

    private static Connection connection;

    @Before
    public void setUp() {
        String dbURL = "jdbc:oracle:thin:@localhost:1521:XE";
        String username = "SYS as SYSDBA";
        String password = "BlaBlaBla";
        
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(dbURL, username, password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConstructor() {
        AskAudience askAudience = new AskAudience(connection, 1);
        assertNotNull(askAudience);
    }

    @Test
    public void testUseWithValidQuestionId() {
        AskAudience askAudience = new AskAudience(connection, 1);
        String result = askAudience.use();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains("Audience 1 chose:"));
        assertTrue(result.contains("Audience 2 chose:"));
        assertTrue(result.contains("Both audiences are favorable.") || 
                    result.contains("Audience 1 is more favorable.") ||
                    result.contains("Audience 2 is more favorable."));
    }

    @Test
    public void testUseWithInvalidQuestionId() {
        AskAudience askAudience = new AskAudience(connection, -1);
        String result = askAudience.use();
        assertNotNull(result);
        assertTrue(result.contains("No data found for the given questionId."));
    }
}
