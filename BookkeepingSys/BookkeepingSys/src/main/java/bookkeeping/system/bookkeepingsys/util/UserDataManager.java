package bookkeeping.system.bookkeepingsys.util;

import bookkeeping.system.bookkeepingsys.model.User;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class UserDataManager {
    private static final String DATABASE_DIR = "databases";
    private static final String CSV_FILE = DATABASE_DIR + "/users.csv";
    private static final String CSV_HEADER = "username,password,email";

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

    public static void initializeCSV() {
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

    public static void saveUser(User user) throws IOException {
        List<String> lines = new ArrayList<>();
        if (Files.exists(Paths.get(CSV_FILE))) {
            lines = Files.readAllLines(Paths.get(CSV_FILE));
        } else {
            lines.add(CSV_HEADER);
        }
        lines.add(user.toString());
        Files.write(Paths.get(CSV_FILE), lines);
    }

    public static User findUser(String username, String password) throws IOException {
        if (!Files.exists(Paths.get(CSV_FILE))) {
            return null;
        }

        List<String> lines = Files.readAllLines(Paths.get(CSV_FILE));
        for (int i = 1; i < lines.size(); i++) { // Skip header
            String[] data = lines.get(i).split(",");
            if (data.length == 3 && data[0].equals(username) && data[1].equals(password)) {
                return new User(data[0], data[1], data[2]);
            }
        }
        return null;
    }

    public static boolean usernameExists(String username) throws IOException {
        if (!Files.exists(Paths.get(CSV_FILE))) {
            return false;
        }

        List<String> lines = Files.readAllLines(Paths.get(CSV_FILE));
        for (int i = 1; i < lines.size(); i++) { // Skip header
            String[] data = lines.get(i).split(",");
            if (data.length > 0 && data[0].equals(username)) {
                return true;
            }
        }
        return false;
    }
} 