package bookkeeping.system.bookkeepingsys.controller;

import bookkeeping.system.bookkeepingsys.BookkeepingApplication;
import bookkeeping.system.bookkeepingsys.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {
    @FXML
    private Label welcomeLabel;
    
    @FXML private Button transactionButton;
    @FXML private Button categoryButton;
    @FXML private Button budgetButton;
    @FXML private Button logoutButton;
    
    private User currentUser;

    @FXML
    public void initialize() {
        // 设置窗口最大化
        Platform.runLater(() -> {
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setMaximized(true);
        });

        welcomeLabel.setText("Welcome，" + (currentUser != null ? currentUser.getUsername() : "用户"));
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Welcome，" + user.getUsername());
    }

    @FXML
    private void handleTransactions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookkeeping/system/bookkeepingsys/transaction-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            // 显示错误消息
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("error");
            alert.setHeaderText(null);
            alert.setContentText("The transaction history page can't be opened: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCategories() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BookkeepingApplication.class.getResource("category-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBudget() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookkeeping/system/bookkeepingsys/budget-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAnalysis() {
        try {
            FXMLLoader loader = new FXMLLoader(BookkeepingApplication.class.getResource("analysis-view.fxml"));
            Scene scene = new Scene(loader.load());
            AnalysisController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("error");
            alert.setHeaderText(null);
            alert.setContentText("The analytics page could not be opened: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BookkeepingApplication.class.getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 