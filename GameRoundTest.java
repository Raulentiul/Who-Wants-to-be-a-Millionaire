import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class GameRoundTest {

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
        GameRound gameRound = new GameRound(connection);
        assertNotNull(gameRound);
    }

    @Test
    public void testFiftyFiftyAvailable() {
        GameRound gameRound = new GameRound(connection);
        assertTrue(gameRound.fiftyFiftyAvailable);
    }

    @Test
    public void testPhoneFriendAvailable() {
        GameRound gameRound = new GameRound(connection);
        assertTrue(gameRound.phoneFriendAvailable);
    }

    @Test
    public void testAskAudienceAvailable() {
        GameRound gameRound = new GameRound(connection);
        assertTrue(gameRound.askAudienceAvailable);
    }
}
