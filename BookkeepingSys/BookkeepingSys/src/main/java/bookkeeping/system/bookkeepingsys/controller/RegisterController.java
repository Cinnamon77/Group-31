package bookkeeping.system.bookkeepingsys.controller;

import bookkeeping.system.bookkeepingsys.BookkeepingApplication;
import bookkeeping.system.bookkeepingsys.model.User;
import bookkeeping.system.bookkeepingsys.util.UserDataManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField emailField;
    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        // 设置窗口最大化
        Platform.runLater(() -> {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setMaximized(true);
        });
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String email = emailField.getText().trim();

        // 验证输入
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
            messageLabel.setText("Please fill in all the fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("The password entered twice is inconsistent");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            messageLabel.setText("Please enter a valid email address");
            return;
        }

        try {
            if (UserDataManager.usernameExists(username)) {
                messageLabel.setText("The username already exists");
                return;
            }

            User newUser = new User(username, password, email);
            UserDataManager.saveUser(newUser);
            messageLabel.setText("Registration is successful！");
            
            // 自动切换到登录页面
            switchToLogin();
        } catch (IOException e) {
            messageLabel.setText("Registration failed：" + e.getMessage());
        }
    }

    @FXML
    private void switchToLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BookkeepingApplication.class.getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            messageLabel.setText("Failed to switch to the login page：" + e.getMessage());
        }
    }
} 