package bookkeeping.system.bookkeepingsys.model;

public class Category {
    private String id;
    private String name;
    private String description;
    private CategoryType type;

    public enum CategoryType {
        EXPENSE("支出"),
        INCOME("收入");

        private final String displayName;

        CategoryType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public Category(String id, String name, String description, CategoryType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.join(",", id, name, description, type.name());
    }

    public static Category fromString(String line) {
        String[] parts = line.split(",");
        return new Category(
            parts[0],
            parts[1],
            parts[2],
            CategoryType.valueOf(parts[3])
        );
    }
} 