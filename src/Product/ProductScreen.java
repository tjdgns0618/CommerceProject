package Product;

import IO.InputSystem;
import Exception.LoopEndException;

import java.util.InputMismatchException;
import java.util.List;

public class ProductScreen implements Screen {

    private final Database database;
    private Product selectedProduct;

    public ProductScreen(Database database) {
        this.database = database;
    }

    @Override
    public String display() throws LoopEndException {
        // 💡 화면 전체를 통제하는 최상위 루프!
        while (true) {
            // 1. 필터 선택 메뉴 띄우기
            List<Product> filtered = filterProducts();

            // 필터 메뉴에서 0(뒤로가기)을 눌렀을 때 -> "카테고리"로 복귀
            if (filtered == null) {
                return "카테고리";
            }

            // 2. 필터링된 상품 목록 띄우고 번호 입력받기 (리스트를 매개변수로 넘김)
            int input = returnProductID(filtered);

            // 상품 목록에서 0(뒤로가기)을 눌렀을 때 -> continue로 루프 처음(필터 메뉴)으로 복귀
            if (input == 0) {
                continue;
            }

            // 3. 정상 번호 입력 시 상품 선택 및 장바구니 추가
            selectProduct(input, filtered);
            addToCart();

            // 4. 장바구니 로직이 (추가든 취소든) 끝나면 최종적으로 메인으로 복귀
            return "카테고리";
        }
    }

    // 예외를 던지지 않고, 0번이면 null을 반환합니다.
    private List<Product> filterProducts() {
        List<Product> selectedProducts = database.getSelectedCategory().getProducts();

        while (true) {
            try {
                System.out.println("\n[ " + database.getSelectedCategory().getCategoryName() + " 카테고리 ]");
                System.out.println("1. 전체 상품 보기");
                System.out.println("2. 가격대별 필터링 (100만원 이하)");
                System.out.println("3. 가격대별 필터링 (100만원 초과)");
                System.out.println("0. 뒤로가기");

                int inputFilter = InputSystem.inputInt();

                switch (inputFilter) {
                    case 1:
                        return selectedProducts;
                    case 2:
                        return selectedProducts.stream().
                                filter(product -> product.getProductPrice() <= 1000000).
                                toList();
                    case 3:
                        return selectedProducts.stream().
                                filter(product -> product.getProductPrice() > 1000000).
                                toList();
                    case 0:
                        InputSystem.clearBuffer();
                        return null;
                    default:
                        System.out.println("항목 내에 있는 숫자만 입력해주세요.");
                }
            } catch (InputMismatchException e) {
                System.out.println("항목 내에 있는 숫자만 입력해주세요.");
                InputSystem.clearBuffer();
            }
        }
    }

    // 넘겨받은 리스트만 출력합니다.
    private void printProducts(List<Product> filteredProducts) {
        int i = 1;
        System.out.println("\n[ " + database.getSelectedCategory().getCategoryName() + " 카테고리 ]");
        for (Product p : filteredProducts) {
            System.out.printf(i + ". %-14s", p.getProductName());
            System.out.printf("| %,11d원", p.getProductPrice());
            System.out.print(" | " + p.getProductDescription());
            System.out.printf(" | 재고 : %2d개\n", p.getProductStock());
            i++;
        }
        System.out.println("0. 뒤로가기");
    }

    private int returnProductID(List<Product> filteredProducts) {
        int inputNum;
        InputSystem.clearBuffer();

        while (true) {
            printProducts(filteredProducts);

            try {
                inputNum = InputSystem.inputInt();
            } catch (InputMismatchException e) {
                System.out.println("\n숫자만 입력해주세요.\n");
                InputSystem.clearBuffer();
                continue;
            }

            if (inputNum < 0 || inputNum > filteredProducts.size()) {
                System.out.println("\n항목에 해당하는 숫자를 입력해주세요.\n");
                continue;
            }
            break;
        }

        if (inputNum == 0) {
            InputSystem.clearBuffer();
            return 0;
        }
        return inputNum;
    }

    public void selectProduct(int productID, List<Product> filteredProducts) {
        selectedProduct = filteredProducts.get(productID - 1);
    }

    public void addToCart() {
        int input;

        while (true) {
            try {
                selectedProduct.printProductInfo();
                System.out.println("위 상품을 장바구니에 추가하시겠습니까?");
                System.out.println("1. 확인        2. 취소");
                input = InputSystem.inputInt();

                switch (input) {
                    case 1:
                        boolean exist = false;
                        if (selectedProduct.getProductStock() == 0) {
                            System.out.println("주문 가능 수량이 없습니다.");
                            break;
                        }

                        for (Product p : database.getOnCartProducts()) {
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
                            makeCartProduct(selectedProduct);
                            System.out.println(selectedProduct.getProductName() + "가 장바구니에 추가되었습니다.");
                        }
                        break;

                    case 2:
                        break;

                    default:
                        throw new InputMismatchException();
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("항목에 존재하는 숫자만 입력해주세요.");
                InputSystem.clearBuffer();
            }
        }
    }

    private void makeCartProduct(Product product) {
        Product cartProduct = new Product(product.getProductName(), product.getProductPrice(), product.getProductDescription(), 1);
        database.addOnCartProducts(cartProduct);
        database.addSelectedProduct(product);
    }
}