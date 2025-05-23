package bookkeeping.system.bookkeepingsys.controller;

import bookkeeping.system.bookkeepingsys.model.Category;
import bookkeeping.system.bookkeepingsys.model.Transaction;
import bookkeeping.system.bookkeepingsys.util.CategoryManager;
import bookkeeping.system.bookkeepingsys.util.TransactionManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class TransactionController {
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, LocalDateTime> dateColumn;
    @FXML private TableColumn<Transaction, Category.CategoryType> typeColumn;
    @FXML private TableColumn<Transaction, String> categoryColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;
    @FXML private TableColumn<Transaction, String> descriptionColumn;
    @FXML private TableColumn<Transaction, Void> actionsColumn;

    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Integer> hourComboBox;
    @FXML private ComboBox<Integer> minuteComboBox;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private TextField amountField;
    @FXML private TextField descriptionField;
    @FXML private Label messageLabel;

    @FXML private Label totalIncomeLabel;
    @FXML private Label totalExpenseLabel;
    @FXML private Label balanceLabel;

    private Transaction currentTransaction;
    private ObservableList<Transaction> transactions = FXCollections.observableArrayList();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    public void initialize() {
        // 设置窗口最大化
        Platform.runLater(() -> {
            Stage stage = (Stage) transactionTable.getScene().getWindow();
            stage.setMaximized(true);
        });

        setupTable();
        setupTimeComboBoxes();
        setupCategoryComboBox();
        loadTransactions();
        datePicker.setValue(LocalDate.now());
    }

    private void setupTimeComboBoxes() {
        // 设置小时选择（0-23）
        ObservableList<Integer> hours = FXCollections.observableArrayList();
        IntStream.rangeClosed(0, 23).forEach(hours::add);
        hourComboBox.setItems(hours);
        hourComboBox.setValue(LocalTime.now().getHour());

        // 设置分钟选择（0-59）
        ObservableList<Integer> minutes = FXCollections.observableArrayList();
        IntStream.rangeClosed(0, 59).forEach(minutes::add);
        minuteComboBox.setItems(minutes);
        minuteComboBox.setValue(LocalTime.now().getMinute());

        // 格式化显示
        StringConverter<Integer> timeFormatter = new StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                if (value == null) return "";
                return String.format("%02d", value);
            }

            @Override
            public Integer fromString(String string) {
                if (string == null || string.trim().isEmpty()) return null;
                return Integer.parseInt(string);
            }
        };

        hourComboBox.setConverter(timeFormatter);
        minuteComboBox.setConverter(timeFormatter);
    }

    private void setupTable() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // 设置日期时间列的格式
        dateColumn.setCellFactory(column -> new TableCell<Transaction, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime dateTime, boolean empty) {
                super.updateItem(dateTime, empty);
                if (empty || dateTime == null) {
                    setText(null);
                } else {
                    setText(dateTime.format(DATE_TIME_FORMATTER));
                }
            }
        });

        // 设置类型列的格式
        typeColumn.setCellFactory(column -> new TableCell<Transaction, Category.CategoryType>() {
            private final Label typeLabel = new Label();
            {
                typeLabel.getStyleClass().add("type-label");
            }

            @Override
            protected void updateItem(Category.CategoryType type, boolean empty) {
                super.updateItem(type, empty);
                if (empty || type == null) {
                    setGraphic(null);
                } else {
                    typeLabel.setText(type == Category.CategoryType.INCOME ? "Income" : "Expenditures");
                    typeLabel.getStyleClass().removeAll("income-label", "expense-label");
                    typeLabel.getStyleClass().add(type == Category.CategoryType.INCOME ? "income-label" : "expense-label");
                    setGraphic(typeLabel);
                }
            }
        });

        // 设置表格行样式
        transactionTable.setRowFactory(tv -> new TableRow<Transaction>() {
            @Override
            protected void updateItem(Transaction transaction, boolean empty) {
                super.updateItem(transaction, empty);
                getStyleClass().removeAll("income-row", "expense-row");
                if (transaction != null && !empty) {
                    getStyleClass().add(transaction.getType() == Category.CategoryType.INCOME ? 
                        "income-row" : "expense-row");
                }
            }
        });

        // 设置金额列的格式
        amountColumn.setCellFactory(column -> new TableCell<Transaction, Double>() {
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

        // 设置操作列
        actionsColumn.setCellFactory(column -> new TableCell<Transaction, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttons = new HBox(4, editButton, deleteButton);

            {
                buttons.getStyleClass().add("action-buttons-container");
                editButton.getStyleClass().add("edit-button");
                deleteButton.getStyleClass().add("delete-button");
                editButton.setOnAction(event -> {
                    Transaction transaction = (Transaction) getTableRow().getItem();
                    if (transaction != null) {
                        handleEdit(transaction);
                    }
                });
                deleteButton.setOnAction(event -> {
                    Transaction transaction = (Transaction) getTableRow().getItem();
                    if (transaction != null) {
                        handleDelete(transaction);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });

        transactionTable.setItems(transactions);
    }

    private void setupCategoryComboBox() {
        try {
            List<Category> categories = CategoryManager.getAllCategories();
            categoryComboBox.setItems(FXCollections.observableArrayList(categories));
            categoryComboBox.setConverter(new StringConverter<Category>() {
                @Override
                public String toString(Category category) {
                    return category == null ? "" : category.getName();
                }

                @Override
                public Category fromString(String string) {
                    return null;
                }
            });
        } catch (IOException e) {
            showMessage("Failed to load classification: " + e.getMessage(), true);
        }
    }

    private void loadTransactions() {
        try {
            transactions.clear();
            transactions.addAll(TransactionManager.getAllTransactions());
            updateSummary();
        } catch (IOException e) {
            showMessage("Failed to load transactions: " + e.getMessage(), true);
        }
    }

    private void updateSummary() {
        double totalIncome = 0;
        double totalExpense = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getType() == Category.CategoryType.INCOME) {
                totalIncome += transaction.getAmount();
            } else {
                totalExpense += transaction.getAmount();
            }
        }

        double balance = totalIncome - totalExpense;

        totalIncomeLabel.setText(String.format("$ %.2f", totalIncome));
        totalExpenseLabel.setText(String.format("$ %.2f", totalExpense));
        balanceLabel.setText(String.format("$ %.2f", balance));

        // 根据结余情况设置颜色
        if (balance > 0) {
            balanceLabel.setStyle("-fx-text-fill: #27ae60;"); // 绿色
        } else if (balance < 0) {
            balanceLabel.setStyle("-fx-text-fill: #e74c3c;"); // 红色
        } else {
            balanceLabel.setStyle("-fx-text-fill: #2980b9;"); // 蓝色
        }
    }

    @FXML
    private void handleSave() {
        if (!validateInput()) {
            return;
        }

        try {
            Category selectedCategory = categoryComboBox.getValue();
            LocalDateTime dateTime = LocalDateTime.of(
                datePicker.getValue(),
                LocalTime.of(
                    hourComboBox.getValue(),
                    minuteComboBox.getValue()
                )
            );

            Transaction transaction = new Transaction(
                currentTransaction != null ? currentTransaction.getId() : "",
                dateTime,
                Double.parseDouble(amountField.getText()),
                selectedCategory.getId(),
                selectedCategory.getName(),
                descriptionField.getText(),
                selectedCategory.getType()
            );

            TransactionManager.saveTransaction(transaction);
            loadTransactions();
            clearForm();
            showMessage("The save was successful!", false);
        } catch (IOException e) {
            showMessage("Save failed: " + e.getMessage(), true);
        }
    }

    private boolean validateInput() {
        if (datePicker.getValue() == null) {
            showMessage("Please select a date", true);
            return false;
        }

        if (categoryComboBox.getValue() == null) {
            showMessage("Please select a category", true);
            return false;
        }

        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                showMessage("The amount must be greater than 0", true);
                return false;
            }
        } catch (NumberFormatException e) {
            showMessage("Please enter a valid amount", true);
            return false;
        }

        if (descriptionField.getText().trim().isEmpty()) {
            showMessage("Please enter a description", true);
            return false;
        }

        return true;
    }

    private void handleEdit(Transaction transaction) {
        currentTransaction = transaction;
        datePicker.setValue(transaction.getDateTime().toLocalDate());
        hourComboBox.setValue(transaction.getDateTime().getHour());
        minuteComboBox.setValue(transaction.getDateTime().getMinute());
        amountField.setText(String.format("%.2f", transaction.getAmount()));
        descriptionField.setText(transaction.getDescription());

        // 设置对应的分类
        categoryComboBox.getItems().stream()
            .filter(category -> category.getId().equals(transaction.getCategoryId()))
            .findFirst()
            .ifPresent(category -> categoryComboBox.setValue(category));
    }

    private void handleDelete(Transaction transaction) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm the deletion");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this transaction？");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                TransactionManager.deleteTransaction(transaction.getId());
                loadTransactions();
                showMessage("The deletion is successful!", false);
            } catch (IOException e) {
                showMessage("Deletion failed: " + e.getMessage(), true);
            }
        }
    }

    @FXML
    private void handleClear() {
        clearForm();
    }

    private void clearForm() {
        currentTransaction = null;
        datePicker.setValue(LocalDate.now());
        hourComboBox.setValue(LocalTime.now().getHour());
        minuteComboBox.setValue(LocalTime.now().getMinute());
        categoryComboBox.setValue(null);
        amountField.clear();
        descriptionField.clear();
        messageLabel.setText("");
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookkeeping/system/bookkeepingsys/dashboard-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) transactionTable.getScene().getWindow();
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
} 