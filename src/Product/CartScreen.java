package Product;

import Exception.LoopEndException;
import Exception.GoBackException;
import IO.InputSystem;

import java.util.InputMismatchException;

public class CartScreen implements Screen {

    private final Database database;

    public CartScreen(Database database) {
        this.database = database;
    }

    @Override
    public void display() throws LoopEndException {
        // 1. 선택한 상품들 출력

        // 2. 구매 할지 안할지 입력
        orderCart();
        // 3. 재고 정리
    }

    private void printCartProducts() {
        for (Product p : database.getOnCartProducts()) {
            System.out.printf("\n%s | %,d원 | %s | 수량 : %d개\n",
                    p.getProductName(), p.getProductPrice(), p.getProductDescription(), p.getProductStock());
        }
    }

    private int printAllPrices() {
        int price = 0;
        for (Product p : database.getOnCartProducts()) {
            price += (p.getProductPrice() * p.getProductStock());
        }
        return price;
    }

    public void printCart() {
        // 장바구니에 상품이 존재하지 않는데 들어올 경우
        if (database.getOnCartProducts().isEmpty()) {
            database.setScreenName("카테고리");
            throw new GoBackException();
        }
        System.out.println("\n[ 장바구니 내역 ]");
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
                        for (Product originProduct : database.getSelectedProducts()) {
                            System.out.print(originProduct.getProductName() + " 재고가 " + originProduct.getProductStock() + "개 -> ");
                            Product orderProduct = database.getOnCartProducts().stream().
                                    filter(product -> product.getProductName().equals(originProduct.getProductName())).
                                    findFirst().
                                    orElse(null);
                            if (orderProduct != null) {
                                originProduct.subtractStock(orderProduct.getProductStock());
                                System.out.print(originProduct.getProductStock());
                                System.out.println("개로 업데이트되었습니다.");
                            } else {
                                throw new NullPointerException("NOP 발생(주문 할 상품에 동일한 상품이 없습니다.)");
                            }
                        }
                        database.getOnCartProducts().clear();
                        database.getSelectedProducts().clear();
                        break;
                    case 2:
                        database.setScreenName("카테고리");
                        InputSystem.clearBuffer();
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
        return !database.getOnCartProducts().isEmpty();
    }
}
