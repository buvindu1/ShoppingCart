import java.io.*;
import java.sql.SQLOutput;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class WestminsterShoppingManager implements ShoppingManager {
    private static List<Product> productList;

    public WestminsterShoppingManager() {
        this.productList = new ArrayList<>();
    }

    public static void main(String[] args) {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        boolean exitProgram = false;



        while (!exitProgram) {
            System.out.println("1).Add a new product");
            System.out.println("2).Delete a product");
            System.out.println("3).Print the list of the products");
            System.out.println("4).Save in a file");
            System.out.println("5).Load from the file");
            System.out.println("6).Exit");

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter an option: ");
            try {
                int option = scanner.nextInt();
                switch (option) {
                    case 1:
                        // Add a new product
                        System.out.println("Enter product type (Electronics/Clothing): ");
                        String productType = scanner.next();

                        if (productType.equalsIgnoreCase("Electronics")) {
                            shoppingManager.addElectronicsProduct();
                        } else if (productType.equalsIgnoreCase("Clothing")) {
                            shoppingManager.addClothingProduct();
                        } else {
                            System.out.println("Invalid product type. Please enter 'Electronics' or 'Clothing'.");
                        }
                        break;
                    case 2:
                        // Delete a product
                        System.out.println("Enter the product ID to delete:");
                        String productIdToDelete = scanner.next();
                        shoppingManager.deleteProductFromSystem(productIdToDelete);
                        break;
                    case 3:
                        // Print list
                        shoppingManager.printProductsInSystem();
                        break;
                    case 4:
                        // Save to file
                        shoppingManager.saveProductsToFile("products.txt");
                        break;
                    case 5:
                        // Load from file
                        try {
                            List<Product> loadedProducts = shoppingManager.loadProducts("products.txt");
                            for (Product loadedProduct : loadedProducts) {
                                System.out.println(loadedProduct);
                            }
                        }catch(Exception e){
                            System.out.println(e);
                        }
                        break;
                    case 6:
                        // Exit
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Enter an option within 1-6:");
                }

            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Integer Required! Enter a number in the range 1-6.");
            }
        }
    }

    @Override
    public void addProductToSystem(Product product) {
        boolean itemAdded = false;
        if (productList.size() < 50) {
            productList.add(product);
            System.out.println("Product added to the system.");
            itemAdded = true;
        } else {
            System.out.println("System can have a maximum of 50 products.");
        }
        // Exit the method if an item has been added
        if (itemAdded) {
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter product type (Electronics/Clothing): ");
        String productType = scanner.next();

        if (productType.equalsIgnoreCase("Electronics")) {
            addElectronicsProduct();
        } else if (productType.equalsIgnoreCase("Clothing")) {
            addClothingProduct();
        } else {
            System.out.println("Invalid product type. Please enter 'Electronics' or 'Clothing'.");
        }

    }

    private void addClothingProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter product ID: ");
        String productID = scanner.next();
        System.out.println("Enter product name: ");
        String productName = scanner.next();
        System.out.println("Enter number of available items: ");
        int availableItems = scanner.nextInt();
        System.out.println("Enter price: ");
        double price = scanner.nextDouble();
        System.out.println("Enter size: ");
        String size = scanner.next();
        System.out.println("Enter color: ");
        String color = scanner.next();

        Clothing clothing = new Clothing(productID, productName, availableItems, price, size, color);
        addProductToSystem(clothing);
    }

    private void addElectronicsProduct() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter product ID: ");
        String productID = scanner.next();
        System.out.println("Enter product name: ");
        String productName = scanner.next();
        System.out.println("Enter number of available items: ");
        int availableItems = scanner.nextInt();
        System.out.println("Enter price: ");
        double price = scanner.nextDouble();
        System.out.println("Enter brand: ");
        String brand = scanner.next();
        System.out.println("Enter warranty period (in months): ");
        int warrantyPeriod = scanner.nextInt();

        Electronics electronics = new Electronics(productID, productName, availableItems, price, brand, warrantyPeriod);
        addProductToSystem(electronics);
    }

    @Override
    public void deleteProductFromSystem(String productID) {
        Iterator<Product> iterator = productList.iterator();
        boolean productFound = false;

        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getProductID().equals(productID)) {
                // Product found, remove it from the list
                iterator.remove();
                productFound = true;

                // Display information about the deleted product
                System.out.println("Product deleted:");
                System.out.println("Product ID: " + product.getProductID());
                System.out.println("Product Type: " + (product instanceof Electronics ? "Electronics" : "Clothing"));

                break; // Exit the loop after deleting the first matching product
            }
        }

        if (!productFound) {
            System.out.println("Product with ID " + productID + " not found in the system.");
        }

        // Display the total number of products left in the system
        System.out.println("Total number of products left in the system: " + productList.size());
    }

    @Override
    public void printProductsInSystem() {
        // Sort the productList alphabetically by product ID
        Collections.sort(productList, (product1, product2) ->
                product1.getProductID().compareToIgnoreCase(product2.getProductID()));

        System.out.println("List of the products");
        // Print information for each product in the sorted list
        for (Product product : productList) {
            System.out.println("Product ID: " + product.getProductID());
            System.out.println("Product Type: " + (product instanceof Electronics ? "Electronics" : "Clothing"));

            // Print other attributes specific to the product type
            if (product instanceof Electronics) {
                Electronics electronics = (Electronics) product;
                System.out.println("Brand: " + electronics.getBrand());
                System.out.println("Warranty Period: " + electronics.getWarrantyPeriod() + " months");
            } else if (product instanceof Clothing) {
                Clothing clothing = (Clothing) product;
                System.out.println("Size: " + clothing.getSize());
                System.out.println("Color: " + clothing.getColor());
            }

            System.out.println();  // Add a newline for better readability
        }
    }

    @Override
    public void saveProductsToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Product product : productList) {
                String productType = (product instanceof Electronics) ? "Electronics" : "Clothing";
                String productID = product.getProductID();
                String productName = product.getProductName();
                int availableItems = product.getAvailableItems();
                double price = product.getPrice();

                writer.write(String.format("Product Type:%s\nproductID:%s\nproductName:%s\navailableItems:%d\nprice:%.2f\n", productType, productID, productName, availableItems, price));

                if (product instanceof Electronics) {
                    Electronics electronics = (Electronics) product;
                    writer.write(String.format("Brand:%s\n,Warranty period:%d\n\n", electronics.getBrand(), electronics.getWarrantyPeriod()));
                } else if (product instanceof Clothing) {
                    Clothing clothing = (Clothing) product;
                    writer.write(String.format("Size:%s\ncolour:%s\n\n", clothing.getSize(), clothing.getColor()));
                }

                writer.newLine();
            }

            System.out.println("Product list saved to file: " + filename);
        } catch (IOException e) {
            System.err.println("Error saving product list to file: " + e.getMessage());
        }
    }

    @Override
    public List<Product> loadProducts(String filename) {
        List<Product> products = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by newline characters to get individual properties
                String[] propertyLines = line.split("\n");

                String productType = "";
                String productID = "";
                String productName = "";
                int availableItems = 0;
                double price = 0.0;
                String additionalProperty1 = "";
                String additionalProperty2 = "";
                String additionalProperty3 = "";
                String additionalProperty4 = "";

                for (String propertyLine : propertyLines) {
                    // Split each property line by colon to get the property name and value
                    String[] property = propertyLine.split(":");
                    if (property.length == 2) {
                        String propertyName = property[0].trim();
                        String propertyValue = property[1].trim();

                        // Assign values based on the property name
                        switch (propertyName) {
                            case "Product Type":
                                productType = propertyValue;
                                break;
                            case "productID":
                                productID = propertyValue;
                                break;
                            case "productName":
                                productName = propertyValue;
                                break;
                            case "availableItems":
                                availableItems = Integer.parseInt(propertyValue);
                                break;
                            case "price":
                                price = Double.parseDouble(propertyValue);
                                break;
                            case "Brand":
                                additionalProperty1 = propertyValue;
                                break;
                            case "Warranty period":
                                additionalProperty2 = propertyValue;
                                break;
                            case "size":
                                additionalProperty3 = propertyValue;
                                break;
                            case "colour":
                                additionalProperty4 = propertyValue;
                                break;
                            // Add cases for other properties like Size, Colour, etc.
                        }
                    }
                }

                Product product;
                if (productType.equals("Electronics")) {
                    // Create an Electronics product with the extracted properties
                    product = new Electronics(productID, productName, availableItems, price, additionalProperty1, Integer.parseInt(additionalProperty2));
                } else if (productType.equals("Clothing")) {
                    // Create a Clothing product with the extracted properties
                    product = new Clothing(productID, productName, availableItems, price, additionalProperty1, additionalProperty2);
                } else {
                    // Handle unknown product type
                    continue;
                }

                products.add(product);
            }

            System.out.println("Products loaded from file: " + filename);
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading products from file: " + e.getMessage());
        }

        return products;
    }

}
