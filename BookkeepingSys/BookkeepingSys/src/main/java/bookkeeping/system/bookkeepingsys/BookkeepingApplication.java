package bookkeeping.system.bookkeepingsys;

import bookkeeping.system.bookkeepingsys.util.UserDataManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BookkeepingApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // 初始化用户数据文件
        UserDataManager.initializeCSV();
        
        FXMLLoader fxmlLoader = new FXMLLoader(BookkeepingApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 500);
        stage.setTitle("Bookkeeping");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}