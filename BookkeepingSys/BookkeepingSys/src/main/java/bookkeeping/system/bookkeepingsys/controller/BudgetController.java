package bookkeeping.system.bookkeepingsys.controller;

import bookkeeping.system.bookkeepingsys.deepSeekUtils.TalkDeepSeek;
import bookkeeping.system.bookkeepingsys.model.Budget;
import bookkeeping.system.bookkeepingsys.model.Note;
import bookkeeping.system.bookkeepingsys.util.BudgetManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BudgetController {
    @FXML private ComboBox<YearMonth> monthPicker;
    @FXML private TextField targetAmountField;
    @FXML private Label currentAmountLabel;
    @FXML private ProgressBar progressBar;
    @FXML private Label progressLabel;
    @FXML private TableView<Budget> budgetTable;
    @FXML private TableColumn<Budget, YearMonth> monthColumn;
    @FXML private TableColumn<Budget, Double> targetColumn;
    @FXML private TableColumn<Budget, Double> currentColumn;
    @FXML private TableColumn<Budget, Double> progressColumn;
    @FXML private TableColumn<Budget, Void> actionsColumn;
    @FXML private Label messageLabel;
    @FXML private TextField noteInput;
    @FXML private ListView<Note> notesListView;
    @FXML private TextArea festivalNotes;
    @FXML private Button getAISuggestionButton;
    @FXML private ProgressIndicator loadingIndicator;
    @FXML private TextArea climateNotes;
    @FXML private Button getClimateAISuggestionButton;
    @FXML private ProgressIndicator climateLoadingIndicator;

    private Budget currentBudget;
    private ObservableList<Budget> budgets = FXCollections.observableArrayList();
    private ObservableList<Note> notes = FXCollections.observableArrayList();
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy年MM月");

    @FXML
    public void initialize() {
        setupMonthPicker();
        setupTable();
        setupActionsColumn();
        setupNotesList();
        setupAISuggestion();
        setupClimateAISuggestion();
        try {
            // 每次初始化时，更新当前月份的储蓄金额
            YearMonth currentMonth = YearMonth.now();
            BudgetManager.updateCurrentAmount(currentMonth);
            loadBudgets();
        } catch (IOException e) {
            showMessage("Initialization failed: " + e.getMessage(), true);
        }
    }

    private void setupMonthPicker() {
        // 设置月份选择器的选项（当前月份及未来11个月）
        List<YearMonth> months = new ArrayList<>();
        YearMonth current = YearMonth.now();
        for (int i = 0; i < 12; i++) {
            months.add(current.plusMonths(i));
        }
        monthPicker.setItems(FXCollections.observableArrayList(months));
        monthPicker.setValue(current);

        // 设置月份显示格式
        monthPicker.setConverter(new StringConverter<YearMonth>() {
            @Override
            public String toString(YearMonth yearMonth) {
                if (yearMonth == null) return "";
                return yearMonth.format(MONTH_FORMATTER);
            }

            @Override
            public YearMonth fromString(String string) {
                return null;
            }
        });

        // 月份选择变化时更新显示
        monthPicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadBudgetForMonth(newVal);
            }
        });
    }

    private void setupTable() {
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("yearMonth"));
        targetColumn.setCellValueFactory(new PropertyValueFactory<>("targetAmount"));
        currentColumn.setCellValueFactory(new PropertyValueFactory<>("currentAmount"));
        progressColumn.setCellValueFactory(new PropertyValueFactory<>("progress"));

        // 设置月份列的格式
        monthColumn.setCellFactory(column -> new TableCell<Budget, YearMonth>() {
            @Override
            protected void updateItem(YearMonth yearMonth, boolean empty) {
                super.updateItem(yearMonth, empty);
                if (empty || yearMonth == null) {
                    setText(null);
                } else {
                    setText(yearMonth.format(MONTH_FORMATTER));
                }
            }
        });

        // 设置金额列的格式
        targetColumn.setCellFactory(column -> new TableCell<Budget, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(String.format("$ %.2f", amount));
                }
            }
        });

        currentColumn.setCellFactory(column -> new TableCell<Budget, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(String.format("$ %.2f", amount));
                }
            }
        });

        // 设置进度列的格式
        progressColumn.setCellFactory(column -> new TableCell<Budget, Double>() {
            @Override
            protected void updateItem(Double progress, boolean empty) {
                super.updateItem(progress, empty);
                if (empty || progress == null) {
                    setText(null);
                } else {
                    setText(String.format("%.1f%%", progress));
                }
            }
        });

        // 设置操作列
        setupActionsColumn();

        budgetTable.setItems(budgets);
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(column -> new TableCell<Budget, Void>() {
            private final HBox container = new HBox(5);
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.getStyleClass().add("edit-button");
                deleteButton.getStyleClass().add("delete-button");
                container.getStyleClass().add("action-buttons-container");
                container.getChildren().addAll(editButton, deleteButton);

                editButton.setOnAction(event -> {
                    Budget budget = getTableView().getItems().get(getIndex());
                    handleEdit(budget);
                });

                deleteButton.setOnAction(event -> {
                    Budget budget = getTableView().getItems().get(getIndex());
                    try {
                        BudgetManager.deleteBudget(budget.getId());
                        loadBudgets();
                        showMessage("The deletion is successful!", false);
                    } catch (IOException e) {
                        showMessage("Deletion failed: " + e.getMessage(), true);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });
    }

    private void setupNotesList() {
        notesListView.setItems(notes);
        notesListView.setCellFactory(listView -> new ListCell<Note>() {
            private HBox container = new HBox();
            private VBox contentBox = new VBox();
            private Label contentLabel = new Label();
            private Label timestampLabel = new Label();
            private Button deleteButton = new Button("×");

            {
                container.getStyleClass().add("note-item");
                contentLabel.getStyleClass().add("note-content");
                timestampLabel.getStyleClass().add("note-timestamp");
                deleteButton.getStyleClass().add("note-delete-button");
                
                contentBox.getChildren().addAll(contentLabel, timestampLabel);
                container.getChildren().addAll(contentBox, deleteButton);
                HBox.setHgrow(contentBox, Priority.ALWAYS);

                deleteButton.setOnAction(event -> {
                    Note note = getItem();
                    if (note != null) {
                        try {
                            BudgetManager.deleteNote(note.getId());
                            loadNotes();
                        } catch (IOException e) {
                            showMessage("Failed to delete note: " + e.getMessage(), true);
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Note note, boolean empty) {
                super.updateItem(note, empty);
                if (empty || note == null) {
                    setGraphic(null);
                } else {
                    contentLabel.setText(note.getContent());
                    timestampLabel.setText(note.getFormattedTimestamp());
                    setGraphic(container);
                }
            }
        });
    }

    @FXML
    private void handleAddNote() {
        if (currentBudget == null) {
            showMessage("Please select a budget first", true);
            return;
        }

        String content = noteInput.getText().trim();
        if (content.isEmpty()) {
            showMessage("Please enter a note", true);
            return;
        }

        try {
            Note note = new Note(
                UUID.randomUUID().toString(),
                currentBudget.getId(),
                content,
                LocalDateTime.now()
            );
            BudgetManager.saveNote(note);
            loadNotes();
            noteInput.clear();
            showMessage("Note added successfully", false);
        } catch (IOException e) {
            showMessage("Failed to add note: " + e.getMessage(), true);
        }
    }

    private void loadNotes() {
        try {
            if (currentBudget != null) {
                notes.clear();
                notes.addAll(BudgetManager.getNotesForBudget(currentBudget.getId()));
                notes.sort(null); // 使用Note类中定义的compareTo方法排序
            } else {
                notes.clear();
            }
        } catch (IOException e) {
            showMessage("Failed to load notes: " + e.getMessage(), true);
        }
    }

    private void loadBudgets() {
        try {
            budgets.clear();
            List<Budget> allBudgets = BudgetManager.getAllBudgets();
            
            // 重新计算每个月份的储蓄金额
            for (Budget budget : allBudgets) {
                BudgetManager.updateCurrentAmount(budget.getYearMonth());
            }
            
            // 重新获取更新后的预算数据
            budgets.addAll(BudgetManager.getAllBudgets());
            loadBudgetForMonth(monthPicker.getValue());
        } catch (IOException e) {
            showMessage("Failed to load budget records: " + e.getMessage(), true);
        }
    }

    private void loadBudgetForMonth(YearMonth yearMonth) {
        try {
            // 先更新当前月份的储蓄金额
            BudgetManager.updateCurrentAmount(yearMonth);
            
            Optional<Budget> budgetOpt = BudgetManager.getBudgetForMonth(yearMonth);
            if (budgetOpt.isPresent()) {
                Budget budget = budgetOpt.get();
                currentBudget = budget;
                targetAmountField.setText(String.format("%.2f", budget.getTargetAmount()));
                updateBudgetDisplay(budget);
                loadNotes(); // 加载备忘录
            } else {
                currentBudget = null;
                targetAmountField.clear();
                updateBudgetDisplay(null);
                notes.clear(); // 清空备忘录
            }
        } catch (IOException e) {
            showMessage("Failed to load monthly budget: " + e.getMessage(), true);
        }
    }

    private void updateBudgetDisplay(Budget budget) {
        if (budget == null) {
            currentAmountLabel.setText("0.00 $");
            progressBar.setProgress(0);
            progressLabel.setText("0%");
            return;
        }

        currentAmountLabel.setText(String.format("%.2f $", budget.getCurrentAmount()));
        double progress = budget.getProgress() / 100.0;
        progressBar.setProgress(Math.min(1.0, progress));
        progressLabel.setText(String.format("%.1f%%", budget.getProgress()));
    }

    @FXML
    private void handleSave() {
        if (!validateInput()) {
            return;
        }

        try {
            double targetAmount = Double.parseDouble(targetAmountField.getText());
            YearMonth selectedMonth = monthPicker.getValue();

            Budget budget = new Budget(
                currentBudget != null ? currentBudget.getId() : "",
                selectedMonth,
                targetAmount,
                currentBudget != null ? currentBudget.getCurrentAmount() : 0
            );

            BudgetManager.saveBudget(budget);
            BudgetManager.updateCurrentAmount(selectedMonth);
            loadBudgets();
            showMessage("The save was successful!", false);
        } catch (IOException e) {
            showMessage("Save failed: " + e.getMessage(), true);
        }
    }

    private boolean validateInput() {
        if (monthPicker.getValue() == null) {
            showMessage("Please select a month", true);
            return false;
        }

        try {
            double amount = Double.parseDouble(targetAmountField.getText());
            if (amount <= 0) {
                showMessage("The target amount must be greater than 0", true);
                return false;
            }
        } catch (NumberFormatException e) {
            showMessage("Please enter a valid target amount", true);
            return false;
        }

        return true;
    }

    private void handleEdit(Budget budget) {
        monthPicker.setValue(budget.getYearMonth());
        targetAmountField.setText(String.format("%.2f", budget.getTargetAmount()));
        currentBudget = budget;
    }

    @FXML
    private void handleReset() {
        currentBudget = null;
        targetAmountField.clear();
        updateBudgetDisplay(null);
        messageLabel.setText("");
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookkeeping/system/bookkeepingsys/dashboard-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) budgetTable.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            showMessage("Failed to return: " + e.getMessage(), true);
        }
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().clear();
        messageLabel.getStyleClass().add(isError ? "error-message" : "success-message");
    }

    private void setupAISuggestion() {
        getAISuggestionButton.setOnAction(event -> {
            if (currentBudget == null) {
                showMessage("Please select a budget first", true);
                return;
            }

            // 禁用按钮和显示加载指示器
            getAISuggestionButton.setDisable(true);
            loadingIndicator.setVisible(true);
            festivalNotes.setDisable(true);

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            String prompt = String.format(
                    "请根据%s的预算情况，现在的时间是%s，分析临近节日可能产生的支出，并提供合理的支出建议和预测。当前预算目标为%.2f元，已储蓄%.2f元。用英语给我建议。",
                    currentBudget.getYearMonth().format(MONTH_FORMATTER),
                    LocalDateTime.now().format(timeFormatter), // 获取当前时间并格式化
                    currentBudget.getTargetAmount(),
                    currentBudget.getCurrentAmount()
            );

            // 在新线程中调用AI接口
            new Thread(() -> {
                try {
                    String response = TalkDeepSeek.callSiliconFlowAPI(null,prompt);
                    // 在JavaFX线程中更新UI
                    Platform.runLater(() -> {
                        festivalNotes.setText(response);
                        showMessage("AI recommendations have been updated", false);
                        // 恢复按钮和隐藏加载指示器
                        getAISuggestionButton.setDisable(false);
                        loadingIndicator.setVisible(false);
                        festivalNotes.setDisable(false);
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        showMessage("Failed to get AI suggestions: " + e.getMessage(), true);
                        // 恢复按钮和隐藏加载指示器
                        getAISuggestionButton.setDisable(false);
                        loadingIndicator.setVisible(false);
                        festivalNotes.setDisable(false);
                    });
                }
            }).start();
        });
    }

    private void setupClimateAISuggestion() {
        getClimateAISuggestionButton.setOnAction(event -> {
            if (currentBudget == null) {
                showMessage("Please select a budget first", true);
                return;
            }

            // 禁用按钮和显示加载指示器
            getClimateAISuggestionButton.setDisable(true);
            climateLoadingIndicator.setVisible(true);
            climateNotes.setDisable(true);

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            String prompt = String.format(
                    "请根据%s的预算情况，现在的时间是%s，分析现在的季节123可能对预算产生的影响，并提供合理的建议和预测。当前预算目标为%.2f元，已储蓄%.2f元。用英语给我建议。",
                    currentBudget.getYearMonth().format(MONTH_FORMATTER),
                    LocalDateTime.now().format(timeFormatter),
                    currentBudget.getTargetAmount(),
                    currentBudget.getCurrentAmount()
            );

//            String prompt = "给我列出未来7天的天气情况";

            // 在新线程中调用AI接口
            new Thread(() -> {
                try {
                    String response = TalkDeepSeek.callSiliconFlowAPI(null,prompt);
                    // 在JavaFX线程中更新UI
                    Platform.runLater(() -> {
                        climateNotes.setText(response);
                        showMessage("Climate AI recommendations have been updated", false);
                        // 恢复按钮和隐藏加载指示器
                        getClimateAISuggestionButton.setDisable(false);
                        climateLoadingIndicator.setVisible(false);
                        climateNotes.setDisable(false);
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        showMessage("Failed to get Climate AI recommendations" + e.getMessage(), true);
                        // 恢复按钮和隐藏加载指示器
                        getClimateAISuggestionButton.setDisable(false);
                        climateLoadingIndicator.setVisible(false);
                        climateNotes.setDisable(false);
                    });
                }
            }).start();
        });
    }
} 