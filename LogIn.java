import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;

public class LogIn extends Application{
    public Set<String> usernames;
    public Set<String> passwords;
    public static String uname;
    private Connection connection;
    private Scene originalScene;

    public LogIn(Connection connection) {
        this.usernames = new HashSet<>();
        this.passwords = new HashSet<>();
        this.connection = connection;
    }

    @Override
    public void start(Stage primaryStage) {
        
        primaryStage.setTitle("Account");

        // Load an image
        Image backgroundImage = new Image("background.jpg");
        ImageView backgroundImageView = new ImageView(backgroundImage);

        // Welcome text and buttons
        Button logInButton = new Button("LogIn");
        Button createAccountButton = new Button("Create Account");

        // Vertical box to hold buttons
        VBox vbox = new VBox(20); 
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(logInButton, createAccountButton);

        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, vbox);

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);

        // Set background image
        root.setBackground(new Background(new BackgroundImage(backgroundImage, null, null, null, null)));

        // Button actions
        logInButton.setOnAction(event -> handleLogInButton(primaryStage));
        createAccountButton.setOnAction(event -> handleCreateAccountButton(primaryStage));
        originalScene = scene;
        primaryStage.show();
    }

    private void handleLogInButton(Stage primaryStage) {
        LogIn loginSystem = new LogIn(connection);
        primaryStage.setTitle("Log In");

        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        Button submitButton = new Button("Log In");

        VBox loginVBox = new VBox(10);
        loginVBox.setAlignment(Pos.CENTER);
        loginVBox.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, submitButton);

        StackPane loginRoot = new StackPane();
        loginRoot.getChildren().add(loginVBox);

        Scene loginScene = new Scene(loginRoot, 1000, 600);
        primaryStage.setScene(loginScene);
        
        BackgroundImage backgroundImage = new BackgroundImage(
                new javafx.scene.image.Image("purple_background.jpg", 1000, 600, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT
        );
        loginRoot.setBackground(new Background(backgroundImage));
        
        submitButton.setOnAction(event -> {
            String loginUsername = usernameField.getText();
            String loginPassword = passwordField.getText();

            boolean loginSuccessful = loginSystem.logIn(loginUsername, loginPassword);
            if (!loginSuccessful) 
                primaryStage.setScene(originalScene);
            else{
                if (isAdmin(connection, loginUsername)) {
                    System.out.println("is Admin");
                    Admin adminInstance = new Admin();
                    adminInstance.start(primaryStage);
                } else {
                    GameRound gameRoundInstance = new GameRound(connection);
                    Questions questionsInstance = new Questions();
                    questionsInstance.resetAvailabilityForAllQuestions();
                    gameRoundInstance.start(primaryStage);
                }
            }
        });
    }

    private static boolean isAdmin(Connection connection, String username) {
        String sql = "SELECT BOSS FROM ACCOUNTS WHERE username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int bossValue = resultSet.getInt("BOSS");
                    return bossValue == 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void handleCreateAccountButton(Stage primaryStage) {
        LogIn loginSystem = new LogIn(connection);

        primaryStage.setTitle("Create Account");

        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();

        Button submitButton = new Button("Create Account");

        VBox createAccountVBox = new VBox(10);
        createAccountVBox.setAlignment(Pos.CENTER);
        createAccountVBox.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, submitButton);

        StackPane createAccountRoot = new StackPane();
        createAccountRoot.getChildren().add(createAccountVBox);

        Scene createAccountScene = new Scene(createAccountRoot, 1000, 600);
        primaryStage.setScene(createAccountScene);

          BackgroundImage backgroundImage = new BackgroundImage(
            new javafx.scene.image.Image("purple_background.jpg", 1000, 600, false, true),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT
        );

        createAccountRoot.setBackground(new Background(backgroundImage));
        submitButton.setOnAction(event -> {
            String createUsername = usernameField.getText();
            String createPassword = passwordField.getText();
            
            loginSystem.createAccount(createUsername, createPassword);
            primaryStage.setScene(originalScene);
        });
    }


    public boolean createAccount(String username, String password) {
        try {
            // Check if the username already exists
            if (usernameExists(username)) {
                System.out.println("Username already taken. Please use another one.");
                return false;
            }

            if (username.isEmpty() || password.isEmpty()) {
                System.out.println("Error: Username/Password cannot be empty.");
                return false;
            }
            

            // SQL part
            String sql = "INSERT INTO accounts (username, password) VALUES ('" + username +  "', '" + password + "')";
            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate(sql);

            if (rows > 0) 
                System.out.println("Row added successfully");
            
            statement.close();
            System.out.println("Account created successfully!");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean usernameExists(String username) throws SQLException {
    String checkUsernameSQL = "SELECT COUNT(*) FROM accounts WHERE username = ?";

    try (PreparedStatement statement = connection.prepareStatement(checkUsernameSQL)) {
        statement.setString(1, username);
        try (ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            int count = resultSet.getInt(1);
            boolean exists = count > 0;

            if (exists)
                showUsernameExistsPopup();

            return exists;
        }
    }
}

    private void showUsernameExistsPopup() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Username Exists");
        alert.setHeaderText(null);
        alert.setContentText("Username already exists in the database.");

        alert.showAndWait();
    }

    public boolean logIn(String username, String password) {
        try {
            String sql = "SELECT * FROM accounts WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Login successful!");
                uname = username;
                return true;
            } else {
                System.out.println("Invalid username or password. Please try again.");
                showInvalidLoginPopup();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showInvalidLoginPopup() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Login");
        alert.setHeaderText(null);
        alert.setContentText("Invalid username or password. Please try again.");

        alert.showAndWait();
    }

    public static String getname(){
        return uname;
    }
}
