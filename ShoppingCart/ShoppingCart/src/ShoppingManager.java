import java.util.List;

public interface ShoppingManager {
    void addProductToSystem(Product product);

    void deleteProductFromSystem(String productID);

    void printProductsInSystem();

    List<Product> loadProducts(String filename);

    void saveProductsToFile(String filename);
}