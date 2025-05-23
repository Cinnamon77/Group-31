package bookkeeping.system.bookkeepingsys.util;

import bookkeeping.system.bookkeepingsys.model.Category;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CategoryManager {
    private static final String DATABASE_DIR = "databases";
    private static final String CSV_FILE = DATABASE_DIR + "/categories.csv";
    private static final String CSV_HEADER = "id,name,description,type";

    static {
        try {
            // 创建数据库目录
            Files.createDirectories(Paths.get(DATABASE_DIR));
            // 初始化CSV文件
            if (!Files.exists(Paths.get(CSV_FILE))) {
                Files.write(Paths.get(CSV_FILE), CSV_HEADER.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Category> getAllCategories() throws IOException {
        List<Category> categories = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(CSV_FILE));
        
        // 跳过标题行
        for (int i = 1; i < lines.size(); i++) {
            categories.add(Category.fromString(lines.get(i)));
        }
        return categories;
    }

    public static void saveCategory(Category category) throws IOException {
        if (category.getId() == null || category.getId().isEmpty()) {
            category.setId(UUID.randomUUID().toString());
        }

        List<String> lines = new ArrayList<>();
        if (Files.exists(Paths.get(CSV_FILE))) {
            lines = Files.readAllLines(Paths.get(CSV_FILE));
        } else {
            lines.add(CSV_HEADER);
        }

        // 检查是否是更新现有分类
        boolean updated = false;
        for (int i = 1; i < lines.size(); i++) {
            Category existingCategory = Category.fromString(lines.get(i));
            if (existingCategory.getId().equals(category.getId())) {
                lines.set(i, category.toString());
                updated = true;
                break;
            }
        }

        // 如果不是更新，则添加新分类
        if (!updated) {
            lines.add(category.toString());
        }

        Files.write(Paths.get(CSV_FILE), lines);
    }

    public static void deleteCategory(String categoryId) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(CSV_FILE));
        List<String> newLines = new ArrayList<>();
        newLines.add(lines.get(0)); // 添加标题行

        // 过滤掉要删除的分类
        for (int i = 1; i < lines.size(); i++) {
            Category category = Category.fromString(lines.get(i));
            if (!category.getId().equals(categoryId)) {
                newLines.add(lines.get(i));
            }
        }

        Files.write(Paths.get(CSV_FILE), newLines);
    }
} 