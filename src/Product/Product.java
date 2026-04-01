package Product;

public class Product {
    private String productName;
    private int productPrice;
    private String productDescription;
    private int productStock;

    public Product(String productName, int productPrice, String productDescription, int productStock) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.productStock = productStock;
    }

    // 선택함 상품의 정보를 출력해주는 함수
    // product의 역할
    // printProductInfo로 이름을 바꾸기
    public void printProductInfo() {
        System.out.printf("\n선택한 상품 : %-14s", getProductName());
        // 10칸을 소지하고 1000 단위로 , 를 찍어주고 오른쪽 정렬
        System.out.printf("|%,11d원", getProductPrice());
        System.out.print(" | " + getProductDescription());
        System.out.printf(" | 재고 :%,3d개\n\n", getProductStock());
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public int getProductStock() {
        return productStock;
    }

    public void decreaseStock() {
        if (productStock > 0)
            productStock--;
        else {
            throw new IllegalArgumentException();
        }
    }

    public void increaseStock() {
        productStock++;
    }

    public void subtractStock(int amount) {
        productStock -= amount;
    }

    public void addStock(int amount) {
        productStock += amount;
    }
}
