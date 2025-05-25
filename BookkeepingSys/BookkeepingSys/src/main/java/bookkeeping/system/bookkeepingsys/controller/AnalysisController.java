package bookkeeping.system.bookkeepingsys.controller;

import bookkeeping.system.bookkeepingsys.BookkeepingApplication;
import bookkeeping.system.bookkeepingsys.model.Transaction;
import bookkeeping.system.bookkeepingsys.model.User;
import bookkeeping.system.bookkeepingsys.util.TransactionManager;
import bookkeeping.system.bookkeepingsys.deepSeekUtils.TalkDeepSeek;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AnalysisController {
    @FXML
    private StackPane loadingPane;
    @FXML
    private Label suggestionLabel;
    @FXML
    private VBox suggestionContainer;

    private User currentUser;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void setCurrentUser(User user) {
        this.currentUser = user;
        // 设置用户后自动开始分析
        handleRefresh();
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(BookkeepingApplication.class.getResource("dashboard-view.fxml"));
            Scene scene = new Scene(loader.load());
            DashboardController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            Stage stage = (Stage) loadingPane.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh() {
        // 显示加载动画
        loadingPane.setVisible(true);
        loadingPane.setManaged(true);
        suggestionLabel.setText("");

        // 在后台线程中处理AI分析
        new Thread(() -> {
            try {
                // 获取最近一个月的交易记录
                LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
                List<Transaction> recentTransactions = TransactionManager.getRecentTransactions(oneMonthAgo);

                // 构建交易记录文本
                StringBuilder transactionText = new StringBuilder();
                for (Transaction t : recentTransactions) {
                    String type = t.getType().toString().equals("INCOME") ? "Income" : "Expenditures";
                    transactionText.append(String.format("%s: %s %.2f$ - %s (%s)\n",
                        t.getDateTime().format(DATE_FORMATTER),
                        type,
                        t.getAmount(),
                        t.getCategoryName(),
                        t.getDescription()
                    ));
                }

                // 调用AI接口
                String systemContent = "你是一个专业的会计师，请根据用户的支出和收入记录用专业且友善的语气分析用户的财务状况，并给出具体的建议。用英文回答我的问题.包含以下几个方面：\n" +
                                     "1. 消费模式分析\n" +
                                     "2. 理财建议\n" +
                                     "3. 风险提示（如果有）" +
                                     "4. 适合我的月度预算(应该存多少钱)";
                
                String aiResponse = TalkDeepSeek.callSiliconFlowAPI(systemContent, transactionText.toString());

                // 在UI线程中更新界面
                Platform.runLater(() -> {
                    loadingPane.setVisible(false);
                    loadingPane.setManaged(false);
                    suggestionLabel.setText(aiResponse);
                });

            } catch (Exception e) {
                e.printStackTrace();
                // 在UI线程中显示错误信息
                Platform.runLater(() -> {
                    loadingPane.setVisible(false);
                    loadingPane.setManaged(false);
                    suggestionLabel.setText("Analysis failed：" + e.getMessage());
                });
            }
        }).start();
    }
} 