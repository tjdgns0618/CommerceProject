package Product;

import Customer.VipLevel;
import Exception.GoBackException;
import IO.InputSystem;

import java.util.InputMismatchException;

public class CartScreen implements Screen {

    private final Database database;
    private Product onCartProduct;
    private Product selectedCartProduct;

    public CartScreen(Database database) {
        this.database = database;
    }

    @Override
    public String display() {
        // 1. 선택한 상품들 출력
        // 2. 구매 할지 안할지 입력
        if(!database.getRemoveMode())
            orderCart();
        // 3. 재고 정리
        else {
            selectDeleteProduct();
            deleteCartProduct();
        }

        return "카테고리";
    }

    private void printCartProducts() {
        int i = 1;
        for (Product p : database.getOnCartProducts()) {
            System.out.printf("%d. %s | %,d원 | %s | 수량 : %d개\n",
                    i++, p.getProductName(), p.getProductPrice(), p.getProductDescription(), p.getProductStock());
        }
    }

    private int getAllPrices() {
        int price = 0;
        for (Product p : database.getOnCartProducts()) {
            price += (p.getProductPrice() * p.getProductStock());
        }
        return price;
    }

    public void printCart() {
        // 장바구니에 상품이 존재하지 않는데 들어올 경우
        if (database.getOnCartProducts().isEmpty()) {
            throw new GoBackException("카테고리");
        }
        System.out.println("\n[ 장바구니 내역 ]\n");
        printCartProducts();
        System.out.println();
        System.out.println("[ 총 주문 금액 ]");
        System.out.printf("%,d원\n", getAllPrices());
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
                        int percentage = selectVipLevel();
                        completeOrder(percentage);
                        break;
                    case 2:
                        InputSystem.clearBuffer();
                        throw new GoBackException("카테고리");
                }
            } catch (InputMismatchException e) {
                System.out.println("선택지에 해당하는 숫자를 입력해주세요.");
                InputSystem.clearBuffer();
                continue;
            }
            break;
        }
    }

    public int selectVipLevel(){
        System.out.println("고객 등급을 입력해주세요.");
        int i = 1;
        for(VipLevel vipLevel : VipLevel.values() ){
            System.out.printf("%d. %-7s: %2d",i, vipLevel, VipLevel.getPercentage(i++));
            System.out.println("% 할인");
        }
        int level = InputSystem.inputInt();
        InputSystem.clearBuffer();
        return VipLevel.getPercentage(level);
    }

    public void completeOrder(int salePercentage){
        System.out.printf("\n주문이 완료되었습니다! 할인 전 금액 : %,d원\n", getAllPrices());
        System.out.print(VipLevel.getVipLevel(salePercentage) + "등급 할인 (" + salePercentage + "%) : ");
        System.out.printf("%,d원\n",(-getAllPrices() * salePercentage / 100));
        System.out.printf("최종 결제 금액 : %,d원\n", (getAllPrices() - (getAllPrices() * salePercentage / 100)));
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
    }

    private void selectDeleteProduct(){
        int inputDeleteNumber;
        while(true) {
            try {
                printCartProducts();
                System.out.println("장바구니에서 제거하실 상품을 입력해주세요.");
                System.out.print("입력 : ");

                inputDeleteNumber = InputSystem.inputInt();
                if(inputDeleteNumber > 0 && inputDeleteNumber <= database.getOnCartProducts().size()){
                    onCartProduct = database.getOnCartProducts().get(inputDeleteNumber - 1);
                    selectedCartProduct = database.getSelectedProducts().get(inputDeleteNumber - 1);
                    break;
                }else
                    throw new InputMismatchException();

            } catch (InputMismatchException e) {
                System.out.println("항목 내의 숫자만 입력해주세요.");
                InputSystem.clearBuffer();
            }
        }
    }

    private void deleteCartProduct(){
        while(true) {
            try {
                System.out.printf("%s를 장바구니에서 제거하시겠습니까?\n", onCartProduct.getProductName());
                System.out.println("1. 제거      2. 취소");
                System.out.print("입력 : ");
                int input = InputSystem.inputInt();
                if (input == 1) {
                    database.removeOnCartProduct(onCartProduct);
                    database.removeSelectedProduct(selectedCartProduct);
                    System.out.println(onCartProduct.getProductName() + "이 장바구니에서 제거되었습니다.");
                } else if (input == 2) {
                    System.out.println("상품 제거를 취소하였습니다.");
                    throw new GoBackException("장바구니");
                } else
                    throw new InputMismatchException();
                break;
            } catch (InputMismatchException e) {
                System.out.println("항목 내의 숫자만 입력해주세요.");
                InputSystem.clearBuffer();
            }
        }
    }
}
