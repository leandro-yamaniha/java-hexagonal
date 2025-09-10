package com.restaurant.domain.valueobject;

/**
 * Menu category enumeration for restaurant menu items
 */
public enum MenuCategory {
    APPETIZER("Appetizer", "Starters and small plates"),
    MAIN_COURSE("Main Course", "Primary dishes"),
    DESSERT("Dessert", "Sweet treats and desserts"),
    BEVERAGE("Beverage", "Drinks and beverages"),
    SALAD("Salad", "Fresh salads and greens"),
    SOUP("Soup", "Hot and cold soups"),
    PASTA("Pasta", "Pasta dishes"),
    PIZZA("Pizza", "Pizza varieties"),
    SEAFOOD("Seafood", "Fish and seafood dishes"),
    MEAT("Meat", "Meat-based dishes"),
    VEGETARIAN("Vegetarian", "Vegetarian options"),
    VEGAN("Vegan", "Plant-based dishes"),
    GLUTEN_FREE("Gluten Free", "Gluten-free options"),
    KIDS_MENU("Kids Menu", "Children's meals"),
    SPECIAL("Special", "Chef's specials and seasonal items");
    
    private final String displayName;
    private final String description;
    
    MenuCategory(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static MenuCategory fromDisplayName(String displayName) {
        for (MenuCategory category : values()) {
            if (category.displayName.equalsIgnoreCase(displayName)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown menu category: " + displayName);
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
