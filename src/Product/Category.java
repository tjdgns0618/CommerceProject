package Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Category {
    private final String categoryName;
    private final List<Product> products = new ArrayList<Product>();

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

}




