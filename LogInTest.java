import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LogInTest {

    public static Connection connection;

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
        LogIn loginSystem = new LogIn(connection);
        assertNotNull(loginSystem);
    }

    @Test
    public void testCreateAccount() {
        LogIn loginSystem = new LogIn(connection);
        boolean result = loginSystem.createAccount("testUser", "testPassword");
        assertTrue(result);
    }

    @Test
    public void testLogInWithValidCredentials() {
        LogIn loginSystem = new LogIn(connection);
        boolean result = loginSystem.logIn("admin", "admin");
        assertTrue(result);
    }
}
