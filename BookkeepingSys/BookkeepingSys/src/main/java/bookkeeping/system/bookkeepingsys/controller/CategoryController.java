package bookkeeping.system.bookkeepingsys.controller;

import bookkeeping.system.bookkeepingsys.BookkeepingApplication;
import bookkeeping.system.bookkeepingsys.model.Category;
import bookkeeping.system.bookkeepingsys.model.Category.CategoryType;
import bookkeeping.system.bookkeepingsys.util.CategoryManager;
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

import java.io.IOException;

public class CategoryController {
    @FXML private TableView<Category> categoryTable;
    @FXML private TableColumn<Category, String> nameColumn;
    @FXML private TableColumn<Category, String> descriptionColumn;
    @FXML private TableColumn<Category, CategoryType> typeColumn;
    @FXML private TableColumn<Category, Void> actionColumn;
    
    @FXML private TextField nameField;
    @FXML private TextField descriptionField;
    @FXML private ComboBox<CategoryType> typeComboBox;
    @FXML private Label messageLabel;

    private Category currentCategory;
    private ObservableList<Category> categories = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 设置窗口最大化
        Platform.runLater(() -> {
            Stage stage = (Stage) categoryTable.getScene().getWindow();
            stage.setMaximized(true);
        });

        // 初始化表格列
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        
        // 设置类型列的显示文本
        typeColumn.setCellFactory(column -> new TableCell<Category, CategoryType>() {
            @Override
            protected void updateItem(CategoryType type, boolean empty) {
                super.updateItem(type, empty);
                if (empty || type == null) {
                    setText(null);
                } else {
                    setText(type.getDisplayName());
                }
            }
        });

        // 添加操作按钮列
        actionColumn.setCellFactory(param -> new TableCell<Category, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttons = new HBox(5, editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    Category category = getTableView().getItems().get(getIndex());
                    handleEdit(category);
                });

                deleteButton.setOnAction(event -> {
                    Category category = getTableView().getItems().get(getIndex());
                    handleDelete(category);
                });

                buttons.getStyleClass().add("action-buttons");
                editButton.getStyleClass().add("small-button");
                deleteButton.getStyleClass().addAll("small-button", "delete-button");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });

        // 初始化类型下拉框
        typeComboBox.setItems(FXCollections.observableArrayList(CategoryType.values()));
        typeComboBox.setConverter(new javafx.util.StringConverter<CategoryType>() {
            @Override
            public String toString(CategoryType type) {
                return type == null ? null : type.getDisplayName();
            }

            @Override
            public CategoryType fromString(String string) {
                return null; // 不需要实现，因为ComboBox是只读的
            }
        });

        // 加载分类数据
        loadCategories();
    }

    private void loadCategories() {
        try {
            categories.clear();
            categories.addAll(CategoryManager.getAllCategories());
            categoryTable.setItems(categories);
        } catch (IOException e) {
            showMessage("Failed to load categorical data：" + e.getMessage(), true);
        }
    }

    private void handleEdit(Category category) {
        currentCategory = category;
        nameField.setText(category.getName());
        descriptionField.setText(category.getDescription());
        typeComboBox.setValue(category.getType());
    }

    private void handleDelete(Category category) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm the deletion");
        alert.setHeaderText(null);
//        alert.setContentText("确定要删除分类 \"" + category.getName() + "\" 吗？");
        alert.setContentText("Are you sure you want to delete the category \"" + category.getName() + "\"?");

        if (alert.showAndWait().orElse(null) == ButtonType.OK) {
            try {
                CategoryManager.deleteCategory(category.getId());
                categories.remove(category);
                showMessage("The category was deleted！", false);
            } catch (IOException e) {
                showMessage("Failed to delete the classification：" + e.getMessage(), true);
            }
        }
    }

    @FXML
    private void handleSave() {
        String name = nameField.getText().trim();
        String description = descriptionField.getText().trim();
        CategoryType type = typeComboBox.getValue();

        if (name.isEmpty() || type == null) {
            showMessage("\"Please enter a classification name and select a type\"", true);
            return;
        }

        try {
            Category category = new Category(
                currentCategory != null ? currentCategory.getId() : "",
                name,
                description,
                type
            );

            CategoryManager.saveCategory(category);
            loadCategories(); // 重新加载数据
            handleClear(); // 清空表单
            showMessage("The category was saved successfully！", false);
        } catch (IOException e) {
            showMessage("Failed to save the classification：" + e.getMessage(), true);
        }
    }

    @FXML
    private void handleClear() {
        currentCategory = null;
        nameField.clear();
        descriptionField.clear();
        typeComboBox.setValue(null);
        messageLabel.setText("");
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BookkeepingApplication.class.getResource("dashboard-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) categoryTable.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            showMessage("返回主页失败：" + e.getMessage(), true);
        }
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().removeAll("success-message", "error-message");
        messageLabel.getStyleClass().add(isError ? "error-message" : "success-message");
    }
} 