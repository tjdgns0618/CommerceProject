package Product;

import IO.InputSystem;
import Exception.GoBackException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Stream;

public class Cart {
    private final List<Product> productList = new ArrayList<Product>();
    private final List<Product> cartList = new ArrayList<Product>();

    // 선택한 상품 정보 출력, 상품 추가 사항 선택지, 추가시 Cart 안에 상품 추가, 취소시 상품으로 돌아가기
    public void addToCart(Product product) {
        int input;
        System.out.printf("\"%s | %,d원 | %s\"\n", product.getProductName(), product.getProductPrice(), product.getProductDescription());
        System.out.println("위 상품을 장바구니에 추가하시겠습니까?");
        System.out.println("1. 확인        2. 취소");
        input = InputSystem.inputInt();
        switch (input) {
            case 1:
                boolean exist = false;
                // 상품 수량이 없으면 주문 불가
                if (product.getProductStock() == 0) {
                    System.out.println("주문 가능 수량이 없습니다.");
                    break;
                }
                for (Product p : productList) {
                    // 이미 담은 상품인지 검사
                    if (product.getProductName().equals(p.getProductName())) {
                        exist = true;
                        if (p.getProductStock() <= product.getProductStock()) {
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
                    makeCartProduct(product);
                    System.out.println(product.getProductName() + "가 장바구니에 추가되었습니다.");
                }
                break;
            case 2:
                throw new GoBackException();
        }
    }

    // 아직 카트에 추가하지 않은 상품을 새로운 객체로 만들어서 저장
    private void makeCartProduct(Product product) {
        // 수량을 제외한 정보는 객체를 참조하여 통일
        Product cartProduct = new Product(product.getProductName(), product.getProductPrice(), product.getProductDescription(), 1);
        cartList.add(product);
        productList.add(cartProduct);
    }

    public void printCartMenu() {
        if (productList.isEmpty())
            return;
        System.out.println();
        System.out.println("[ 주문 관리 ]");
        System.out.println("4. 장바구니 확인    | 장바구니를 확인 후 주문합니다.");
        System.out.println("5. 주문 취소       | 진행중인 주문을 취소합니다.");
    }

    private void printCartProducts() {
        for (Product p : productList) {
            System.out.printf("%s | %,d원 | %s | 수량 : %d개\n", p.getProductName(), p.getProductPrice(), p.getProductDescription(), p.getProductStock());
        }
    }

    private int printAllPrices() {
        int price = 0;
        for (Product p : productList) {
            price += (p.getProductPrice() * p.getProductStock());
        }
        return price;
    }

    public void printCart() {
        System.out.println("[ 장바구니 내역 ]");
        printCartProducts();
        System.out.println();
        System.out.println("[ 총 주문 금액 ]");
        System.out.printf("%,d원\n", printAllPrices());
        System.out.println();
        System.out.println("1. 주문 확정      2. 메인으로 돌아가기");
    }

    public void orderCart() {
        int input;

        while (true) {
            printCart();
            try {
                input = InputSystem.inputInt();
                if (input <= 0 || input >= 3) {
                    System.out.println("선택지에 해당하는 숫자를 입력해주세요.");
                    continue;
                }
                switch (input) {
                    case 1:
                        System.out.printf("주문이 완료되었습니다! 총 금액 : %,d원\n", printAllPrices());
                        // 원본 상품 객체에 접근해서 재고를 가져와야함
                        for (Product c : cartList) {
                            System.out.print(c.getProductName() + " 재고가 " + c.getProductStock() + "개 -> ");
                            Product p = productList.stream().filter(product -> product.getProductName().equals(c.getProductName())).findFirst().orElse(null);
                            c.subtractStock(p.getProductStock());
                            System.out.print(c.getProductStock());
                            System.out.println("개로 업데이트되었습니다.");
                        }
                        productList.clear();
                        cartList.clear();
                        break;
                    case 2:
                        throw new GoBackException();
                }
            } catch (InputMismatchException e) {
                System.out.println("선택지에 해당하는 숫자를 입력해주세요.");
                InputSystem.clearBuffer();
                continue;
            }
            break;
        }
    }

    public boolean isEmpty() {
        return !productList.isEmpty();
    }
}
