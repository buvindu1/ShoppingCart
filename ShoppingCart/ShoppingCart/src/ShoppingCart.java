import java.util.ArrayList;
import java.util.List;

class ShoppingCart {
    private List<Product> productList;

    //Constructor
    public ShoppingCart() {
        this.productList = new ArrayList<>();
    }

    //Add Product
    public void addProduct(Product product) {
        productList.add(product);
    }

    //Remove Product
    public void removeProduct(Product product) {
        productList.remove(product);
    }

    //Total Cost Calculation
    public double calculateTotalCost() {
        double totalCost = 0;
        for (Product product : productList) {
           totalCost += product.getPrice();
    }
        return totalCost;
    }
}