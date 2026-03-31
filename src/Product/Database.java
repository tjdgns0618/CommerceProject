package Product;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private String screenName;
    private Category category;
    private Product product;
    private final String adminId = "tjdgns0618";
    private int loginTryCount = 0;

    // 카테고리들
    private List<Category> categories;
    // 카테고리 선택시 안에 들어있는 상품들 저장하기 위한 리스트
    private List<Product> products;

    // 장바구니에 담은 상품들의 참조용 리스트
    private List<Product> selectedProducts = new ArrayList<Product>();
    // 장바구니에 담은 상품들 리스트
    private List<Product> onCartProducts = new  ArrayList<Product>();

    /**
     *
     * @param screenName
     * 스크린 이름을 초기화하는 함수
     */
    public void setScreenName(String screenName){
        this.screenName = screenName;
    }

    /**
     *
     * @return 현재 스크린 이름
     */
    public String getScreenName() {
        return screenName;
    }

    /**
     * @param categoryNumber
     * 카테고리들 중에서 선택된 카테고리를 지정하는 함수
     */
    public void setCategory(int categoryNumber){
        category = categories.get(categoryNumber - 1);
    }

    /**
     * @return 선택된 카테고리
     */
    public Category getSelectedCategory() {
        return this.category;
    }

    /**
     *
     * @param productID
     * 상품들 중에서 선택된 상품을 초기화하는 함수
     */
    public void setProduct(int productID){
        product = products.get(productID - 1);
    }

    /**
     *
     * @return 선택된 상품
     */
    public Product getSelectedProduct() {
        return this.product;
    }

    /**
     *
     * @param categories
     * 데이터베이스에 카테고리들을 저장하는 함수
     */
    public void saveCategories(List<Category> categories) {
        this.categories = categories;
    }

    /**
     *
     * @return 모든 카테고리들
     */
    public List<Category> getCategories() {
        return this.categories;
    }

    /**
     *
     * @return 카테고리들의 갯수
     */
    public int getCategoriesSize() {
        return this.categories.size();
    }

    /**
     *
     * @param products
     * 상품들을 데이터베이스에 초기화하는 함수
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     *
     * @return 어드민 아이디
     */
    public String getAdminId() {
        return adminId;
    }

    /**
     * 로그인 시도 횟수 1 증가
     */
    public void addLoginTryCount(){
        loginTryCount++;
    }

    /**
     *
     * @return 로그인 시도 횟수
     */
    public int  getLoginTryCount() {
        return loginTryCount;
    }

    /**
     * 로그인 시도 횟수 초기화 함수
     */
    public void resetLoginTryCount() {
        loginTryCount = 0;
    }

    /**
     *
     * @param categoryNumber
     * @param product
     * 번호에 해당하는 카테고리에 새로운 상품을 추가하는 함수
     */
    public void addNewProductToCategory(int categoryNumber, Product product){
        categories.get(categoryNumber - 1).addProduct(product);
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

    public List<Product> getProducts(){
        return this.products;
    }

}
