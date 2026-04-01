package Product;

import IO.InputSystem;
import Exception.LoopEndException;
import Exception.GoBackException;

import java.util.InputMismatchException;
import java.util.List;

public class ProductScreen implements Screen {

    private final Database database;
    private List<Product> filteredProducts;
    private Product selectedProduct;
    public ProductScreen(Database database) {
        this.database = database;
    }

    @Override
    public void display() throws LoopEndException {
        // 4. 상품 목록 출력

        // 5. 입력 받기
        int input = returnProductID();
        // 6. 상품 선택
        selectProduct(input);
        // 7. 상품 카트에 추가하기
        addToCart();
    }

    private List<Product> filterProducts(){

        List<Product> selectedProducts = database.getSelectedCategory().getProducts();

        while(true) {
            try {
                System.out.println("\n[ " + database.getSelectedCategory().getCategoryName() + " 카테고리 }");
                System.out.println("1. 전체 상품 보기");
                System.out.println("2. 가격대별 필터링 (100만원 이하)");
                System.out.println("3. 가격대별 필터링 (100만원 초과)");
                System.out.println("0. 뒤로가기");

                int inputFilter = InputSystem.inputInt();

                switch (inputFilter) {
                    case 1:
                        filteredProducts = selectedProducts;
                        break;
                    case 2:
                        filteredProducts = selectedProducts.stream().
                                filter(product -> product.getProductPrice() <= 1000000).
                                toList();
                        break;
                    case 3:
                        filteredProducts = selectedProducts.stream().
                                filter(product -> product.getProductPrice() > 1000000).
                                toList();
                        break;
                    case 0:
                        database.setScreenName("카테고리");
                        InputSystem.clearBuffer();
                        throw new GoBackException();
                    default:
                        throw new InputMismatchException();
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("항목 내에 있는 숫자만 입력해주세요.");
                InputSystem.clearBuffer();
            }
        }
        return filteredProducts;
    }

    private void printUnderProducts(){

    }

    // 존재하는 상품들을 모두 출력하는 함수
    private void printProducts(List<Product> filteredProducts) {

        int i = 1;
        // 선택한 카테고리의 이름을 출력해주며 상품들 출력
        // 여기는 선택한 카테고리의 이름을 표시해줘야 하니 카테고리 객체 사용
        System.out.println("\n[ " + database.getSelectedCategory().getCategoryName() + " 카테고리 ]");
        for (Product p : filteredProducts) {
            // 14칸을 소지하고 들어오는 문자를 왼쪽 정렬 시키기
            System.out.printf(i + ". %-14s", p.getProductName());
            // 10칸을 소지하고 1000 단위로 , 를 찍어주고 오른쪽 정렬
            System.out.printf("| %,11d원", p.getProductPrice());
            System.out.print(" | " + p.getProductDescription());
            System.out.printf(" | 재고 : %2d개\n", p.getProductStock());

            i++;
        }
        System.out.println("0. 뒤로가기");
    }

    // 존재하는 상품 번호인지 검사하고 그 번호를 반환하는 함수
    private int returnProductID() {
        int inputNum;
        InputSystem.clearBuffer();
        while (true) {
            printProducts(filterProducts());

            try {
                inputNum = InputSystem.inputInt();
            } catch (InputMismatchException e) {
                System.out.println("\n숫자만 입력해주세요.\n");
                InputSystem.clearBuffer();
                continue;
            }

            if (inputNum < 0 || inputNum > filteredProducts.size()) {
                System.out.println("\n카테고리에 해당하는 숫자를 입력해주세요.\n");
                continue;
            }
            break;
        }
        if (inputNum == 0) {
            database.setScreenName("카테고리");
            InputSystem.clearBuffer();
            throw new GoBackException();
        }
        return inputNum;
    }

    // 선택한 상품(Product)을 반환해주는 함수
    // category의 역할
    // printProducts 도 따로 만들기
    // commerceSystem.selectProduct() <- 딱보면 이상한 느낌이 든다 == 여기서 할 일이 아니다.
    public void selectProduct(int productID) {
        selectedProduct = filteredProducts.get(productID - 1);
    }

    public void addToCart() {
        int input;

        while(true) {
            try {
                selectedProduct.printProductInfo();
                System.out.println("위 상품을 장바구니에 추가하시겠습니까?");
                System.out.println("1. 확인        2. 취소");
                input = InputSystem.inputInt();
                switch (input) {
                    case 1:
                        boolean exist = false;
                        // 상품 수량이 없으면 주문 불가
                        if (selectedProduct.getProductStock() == 0) {
                            System.out.println("주문 가능 수량이 없습니다.");
                            break;
                        }

                        for (Product p : database.getOnCartProducts()) {
                            // 이미 담은 상품인지 검사
                            if (selectedProduct.getProductName().equals(p.getProductName())) {
                                exist = true;
                                if (p.getProductStock() < selectedProduct.getProductStock()) {
                                    p.increaseStock();
                                    System.out.println(p.getProductName() + "가 장바구니에 1개 더 추가되었습니다.");
                                } else {
                                    System.out.println("주문 가능 수량을 초과하였습니다.");
                                }
                                break;
                            }
                        }
                        if (!exist) {
                            // 아직 담지 않은 상품이라면 새로 담기
                            makeCartProduct(selectedProduct);
                            System.out.println(selectedProduct.getProductName() + "가 장바구니에 추가되었습니다.");
                        }
                        throw new GoBackException();
                    case 2:
                        throw new GoBackException();
                }
            }catch (InputMismatchException e) {
                System.out.println("항목에 존재하는 숫자만 입력해주세요.");
                InputSystem.clearBuffer();
            }
        }
    }

    // 아직 카트에 추가하지 않은 상품을 새로운 객체로 만들어서 저장
    private void makeCartProduct(Product product) {
        // 수량을 제외한 정보는 객체를 참조하여 통일
        Product cartProduct = new Product(product.getProductName(), product.getProductPrice(), product.getProductDescription(), 1);
        database.addOnCartProducts(cartProduct);
        database.addSelectedProduct(product);
    }
}