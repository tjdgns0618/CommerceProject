package Product;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private Category category;
    private final String adminId = "123";
    private int loginTryCount = 0;
    private boolean removeMode = false;

    // 카테고리들
    private List<Category> categories;
    // 카테고리 선택시 안에 들어있는 상품들 저장하기 위한 리스트
    private List<Product> products;

    // 장바구니에 담은 상품들의 참조용 리스트
    private List<Product> selectedProducts = new ArrayList<Product>();
    // 장바구니에 담은 상품들 리스트
    private List<Product> onCartProducts = new  ArrayList<Product>();

    public Database(){
        Product electronicProduct1 = new Product("Galaxy S25", 1200000, "최신 안드로이드 스마트폰", 30);
        Product electronicProduct2 = new Product("iPhone 16", 1350000, "Apple의 최신 스마트폰", 15);
        Product electronicProduct3 = new Product("MacBook Pro", 2400000, "M3 칩셋이 탑재된 노트북", 5);
        Product electronicProduct4 = new Product("AirPods", 350000, "노이즈 캔슬링 무선 이어폰", 0);

        Product clothesProduct1 = new Product("Adidas Pants", 20000, "아디다스의 신상 바지", 300);
        Product clothesProduct2 = new Product("Adidas Shoes", 30000, "아디다스의 신상 신발", 100);

        Product foodProduct1 = new Product("솔의눈", 1500, "왜먹는지 모르겠는 음료", 1);

        // 카테고리별 객체 생성후 상품 객체 등록
        Category electroCategory = new Category("전자제품");
        electroCategory.addProduct(electronicProduct1);
        electroCategory.addProduct(electronicProduct2);
        electroCategory.addProduct(electronicProduct3);
        electroCategory.addProduct(electronicProduct4);

        Category clotheCategory = new Category("의류");
        clotheCategory.addProduct(clothesProduct1);
        clotheCategory.addProduct(clothesProduct2);

        Category foodCategory = new Category("식품");
        foodCategory.addProduct(foodProduct1);

        List<Category> categories = new ArrayList<Category>();
        categories.add(electroCategory);
        categories.add(clotheCategory);
        categories.add(foodCategory);

        saveCategories(categories);
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

    public void setRemoveMode(){
        removeMode = true;
    }

    public void unsetRemoveMode(){
        removeMode = false;
    }

    public boolean getRemoveMode(){
        return removeMode;
    }

    public void addSelectedProduct(Product product){
        selectedProducts.add(product);
    }

    public List<Product> getSelectedProducts(){
        return selectedProducts;
    }

    public void addOnCartProducts(Product product) {
        this.onCartProducts.add(product);
    }

    public List<Product> getOnCartProducts(){
        return onCartProducts;
    }

    public void removeOnCartProduct(Product product){
        onCartProducts.remove(product);
    }

    public void removeSelectedProduct(Product product){
        selectedProducts.remove(product);
    }

}
