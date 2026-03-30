package Product;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private String screenName;
    private Category category;
    private Product product;

    // 카테고리들
    private List<Category> categories;
    // 카테고리 선택시 안에 들어있는 상품들 저장하기 위한 리스트
    private List<Product> products;

    // 장바구니에 담은 상품들의 참조용 리스트
    private List<Product> selectedProducts = new ArrayList<Product>();
    // 장바구니에 담은 상품들 리스트
    private List<Product> onCartProducts = new  ArrayList<Product>();

    public void setCategory(int categoryNumber){
        category = categories.get(categoryNumber - 1);
    }

    public void setProduct(int productID){
        product = products.get(productID - 1);
    }

    public void setScreenName(String screenName){
        this.screenName = screenName;
    }

    public void addSelectedProduct(Product product){
        selectedProducts.add(product);
    }

    public void setSelectedProducts(List<Product> selectedProducts){
        this.selectedProducts = selectedProducts;
    }

    public List<Product> getSelectedProducts(){
        return selectedProducts;
    }

    public void setSelectedCategory(Category category) {
        this.category = category;
    }

    public void addOnCartProducts(Product product) {
        this.onCartProducts.add(product);
    }

    public List<Product> getOnCartProducts(){
        return onCartProducts;
    }

    public void saveCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts(){
        return this.products;
    }

    public Category getSelectedCategory() {
        return this.category;
    }

    public Product getSelectedProduct() {
        return this.product;
    }

    public List<Category> getCategories() {
        return this.categories;
    }

    public String getScreenName() {
        return screenName;
    }

    public int getCategoriesSize() {
        return this.categories.size();
    }

}
