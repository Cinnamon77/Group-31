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

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
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
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please fill in your username and password");
            return;
        }

        try {
            User user = UserDataManager.findUser(username, password);
            if (user != null) {
                // 加载仪表板
                FXMLLoader fxmlLoader = new FXMLLoader(BookkeepingApplication.class.getResource("dashboard-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                
                // 设置当前用户
                DashboardController dashboardController = fxmlLoader.getController();
                dashboardController.setCurrentUser(user);
                
                // 切换场景
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(scene);
            } else {
                messageLabel.setText("Wrong username or password");
            }
        } catch (IOException e) {
            messageLabel.setText("Login failed：" + e.getMessage());
        }
    }

    @FXML
    private void switchToRegister() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BookkeepingApplication.class.getResource("register-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            messageLabel.setText("Failed to switch to the registration page：" + e.getMessage());
        }
    }
} 