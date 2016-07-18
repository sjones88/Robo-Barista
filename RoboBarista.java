import java.util.*;
import java.lang.*;
import java.io.*;

public class RoboBarista{
    
    ArrayList<String> menu = menuItems();
    HashMap<String, Double> inventoryUnits = inventoryUnitsMapGen();
    HashMap<String, Double> inventoryCosts = inventoryCostsMapGen();
    HashMap<String, HashMap<String, Double>> menuInventory = menuInventoryGen();

    // returns array list of ingredients
    private static ArrayList<String> inventoryItems() {
        ArrayList<String> inventory = new ArrayList<String>();
        inventory.add("coffee");
        inventory.add("decafCoffee");
        inventory.add("sugar");
        inventory.add("cream");
        inventory.add("steamedMilk");
        inventory.add("foamedMilk");
        inventory.add("espresso");
        inventory.add("cocoa");
        inventory.add("whippedCream");
        Collections.sort(inventory);
        return inventory;
    }
    
    // 
    private static ArrayList<String> menuItems() {
        ArrayList<String> menu = new ArrayList<String>();
        menu.add("coffee");
        menu.add("caffeAmericano");
        menu.add("decafCoffee");
        menu.add("caffeLatte");
        menu.add("cappuccino");
        menu.add("caffeMocha");
        Collections.sort(menu);
        return menu;
    }
    
    private static HashMap<String, Double> inventoryUnitsMapGen() {
        HashMap<String, Double> inventoryUnitsMap = new HashMap<String, Double>();
        ArrayList<String> inventoryArray = inventoryItems();
        for ( String item : inventoryArray ) {
            inventoryUnitsMap.put(item, 10.0);
        }
        return inventoryUnitsMap;
    }
    
    private static HashMap<String, Double> inventoryCostsMapGen() {
        HashMap<String, Double> inventoryCostsMap = new HashMap<String, Double>();
        inventoryCostsMap.put("coffee", 0.75);
        inventoryCostsMap.put("decafCoffee", 0.75);
        inventoryCostsMap.put("sugar", 0.25);
        inventoryCostsMap.put("cream", 0.25);
        inventoryCostsMap.put("steamedMilk", 0.35);
        inventoryCostsMap.put("foamedMilk", 0.35);
        inventoryCostsMap.put("espresso", 0.75);
        inventoryCostsMap.put("cocoa", 0.90);
        inventoryCostsMap.put("whippedCream", 1.00);
        return inventoryCostsMap;
    }
    
    private static HashMap<String, HashMap<String, Double>> menuInventoryGen() {
        HashMap<String, HashMap<String, Double>> menuInventory = new HashMap<String, HashMap<String, Double>>();
        ArrayList<String> menu = menuItems();
        for (String item : menu) {
            HashMap<String, Double> itemIngredients = new HashMap<String, Double>();
            menuInventory.put(item, itemIngredients);
        }
        // { coffee: { coffee: 3, sugar: 1, cream: 1 } }
        menuInventory.get("coffee").put("coffee", 3.0);
        menuInventory.get("coffee").put("sugar", 1.0);
        menuInventory.get("coffee").put("cream", 1.0);
        
        // { decafCoffee: { decafCoffee: 3, sugar: 1, cream: 1 } }
        menuInventory.get("decafCoffee").put("decafCoffee", 3.0);
        menuInventory.get("decafCoffee").put("sugar", 1.0);
        menuInventory.get("decafCoffee").put("cream", 1.0);
        
        // { caffeLatte: { espresso: 2, steamedMilk: 1 } }
        menuInventory.get("caffeLatte").put("espresso", 2.0);
        menuInventory.get("caffeLatte").put("steamedMilk", 1.0);
        
        // { caffeAmericano: { espresso: 3 } }
        menuInventory.get("caffeAmericano").put("espresso", 3.0);
        
        // { caffeMocha: { espresso: 1, cocoa: 1, steamedMilk: 1, whippedCream: 1 } }
        menuInventory.get("caffeMocha").put("espresso", 1.0);
        menuInventory.get("caffeMocha").put("cocoa", 1.0);
        menuInventory.get("caffeMocha").put("steamedMilk", 1.0);
        menuInventory.get("caffeMocha").put("whippedCream", 1.0);
        
        // { cappuccino: { espresso: 2, steamedMilk: 1, foamedMilk: 1 } }
        menuInventory.get("cappuccino").put("espresso", 2.0);
        menuInventory.get("cappuccino").put("steamedMilk", 1.0);
        menuInventory.get("cappuccino").put("foamedMilk", 1.0);
        
        return menuInventory;
    }
    
    private Double itemCost(String item) {
        HashMap<String, Double> ingredients = menuInventory.get(item);
        double cost = 0.0;
        for ( String ingredient : ingredients.keySet() ) {
            cost += ingredients.get(ingredient) * inventoryCosts.get(ingredient);
        }
        return cost;
    }
    
    public static String prettyString(String s) {
        String capitalized = s.substring(0, 1).toUpperCase() + s.substring(1);
        return capitalized.replaceAll(
            String.format("%s|%s|%s",
                "(?<=[A-Z])(?=[A-Z][a-z])",
                "(?<=[^A-Z])(?=[A-Z])",
                "(?<=[A-Za-z])(?=[^A-Za-z])"
            ),
            " "
        );
    }
    
    public void printInventory() {
        Map<String, Double> inventory = new TreeMap<String, Double>(inventoryUnits);
        System.out.println("Inventory:");
        for (String item : inventory.keySet()) {
            System.out.println(prettyString(item) + ", " + inventory.get(item).intValue());
        }
    }
    
    public void printMenu() {
        int i = 1;
        System.out.println("Menu:");
        for ( String item : menu ) {
            System.out.println(i + "," + prettyString(item) + ",$" + String.format("%.2f", itemCost(item)) + "," + isAvailable(item));
            i++;
        }
    }
    
    private void dispense(String item) {
        HashMap<String, Double> itemIngredients = menuInventory.get(item);
        
        for ( String ingredient : itemIngredients.keySet() ) {
            consumeIngredient(ingredient, itemIngredients.get(ingredient));
        }
        
        System.out.println("Dispensing: " + prettyString(item));
    }
    
    public boolean isAvailable(String item) {
        HashMap<String, Double> ingredients = menuInventory.get(item);
        for ( String ingredient : ingredients.keySet() ) {
            if ( inventoryUnits.get(ingredient) < ingredients.get(ingredient) ) {
                return false;
            }
        }
        return true;
    }
    
    private void consumeIngredient(String ingredient, double unitsConsumed) {
        double c = inventoryUnits.get(ingredient);
        inventoryUnits.put(ingredient, c - unitsConsumed);
    }
    
    private void runner() {
        ArrayList<String> orderNums = new ArrayList<String>();
        for (int i = 1; i <= menu.size(); i++) {
            String j = String.valueOf(i);
            orderNums.add(j);
        }
        ArrayList<String> validInputs = new ArrayList<String>();
        validInputs.add("q");
        validInputs.add("r");
        
        Scanner scanner = new Scanner(System.in);
        Character request = scanner.next().toLowerCase().charAt(0);
        if ( orderNums.contains(request) ) {
            String order = menu.get(Integer.parseInt(String.valueOf(request)) - 1);
            if ( isAvailable(order) ) {
                dispense(order);
                printInventory();
                printMenu();
                runner();
            } else {
                System.out.println("Out of stock: " + prettyString(order));
                printInventory();
                printMenu();
                runner();
            }
        } else if ( String.valueOf(request) == "q" ) {
            return;
        } else if ( String.valueOf(request) == validInputs.get(1) ) {
            for ( String item : inventoryUnits.keySet() ) {
                inventoryUnits.put(item, 10.0);
            }
            printInventory();
            printMenu();
            runner();
        } else {
            System.out.println("Invalid selection: " + request);
            printInventory();
            printMenu();
            runner();
        }
    }
    
     public static void main(String []args)
     throws java.io.IOException
     {
        RoboBarista robo = new RoboBarista();
        robo.printInventory();
        robo.printMenu();
        robo.runner();
     }
}