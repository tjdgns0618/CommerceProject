package Product;

import Customer.VipLevel;
import IO.InputSystem;

import java.util.InputMismatchException;

public class CartScreen implements Screen {

    private final Database database;

    public CartScreen(Database database) {
        this.database = database;
    }

    @Override
    public String display() {
        // 1. 장바구니가 비었는지 체크, 비어있다면 카테고리로
        if(database.getOnCartProducts().isEmpty()) {
            System.out.println("\n장바구니가 비어있습니다.\n");
            return "카테고리";
        }

        if(!database.getRemoveMode())
            return orderCart();
        // 3. 재고 정리
        else {
            return deleteCartProcess();
        }
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
        System.out.println("\n[ 장바구니 내역 ]\n");
        printCartProducts();
        System.out.println();
        System.out.println("[ 총 주문 금액 ]");
        System.out.printf("%,d원\n", getAllPrices());
        System.out.println();
        System.out.println("1. 주문 확정      2. 메인으로 돌아가기");
    }

    public String orderCart() {
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
                        return "카테고리";
                    case 2:
                        InputSystem.clearBuffer();
                        return "카테고리";
                }
            } catch (InputMismatchException e) {
                System.out.println("선택지에 해당하는 숫자를 입력해주세요.");
                InputSystem.clearBuffer();
            }
        }
    }

    public int selectVipLevel(){
        while(true) {
            System.out.println("고객 등급을 입력해주세요.");
            int i = 1;
            for (VipLevel vipLevel : VipLevel.values()) {
                System.out.printf("%d. %-7s: %2d", i, vipLevel, VipLevel.getPercentage(i++));
                System.out.println("% 할인");
            }
            System.out.print("입력 : ");
            try {
                int level = InputSystem.inputInt();
                if(level > 0 && level <= VipLevel.values().length) {
                    InputSystem.clearBuffer();
                    return VipLevel.getPercentage(level);
                }
                System.out.println("\n해당하는 등급 번호를 입력해주세요.");
            } catch (InputMismatchException e){
                System.out.println("\n목록에 해당하는 숫자만 입력해주세요.\n");
            }
        }
    }

    public void completeOrder(int salePercentage){
        int totalPrice = getAllPrices();
        System.out.printf("\n주문이 완료되었습니다! 할인 전 금액 : %,d원\n", totalPrice);
        System.out.print(VipLevel.getVipLevel(salePercentage) + "등급 할인 (" + salePercentage + "%) : ");
        System.out.printf("%,d원\n",(-totalPrice * salePercentage / 100));
        System.out.printf("최종 결제 금액 : %,d원\n", (totalPrice - (totalPrice * salePercentage / 100)));
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

    private String deleteCartProcess() {
        while(true) {
            printCartProducts();
            // ✨ UX 개선: 잘못 들어왔을 때 나갈 수 있도록 0번(뒤로가기) 추가
            System.out.println("\n장바구니에서 제거하실 상품 번호를 입력해주세요. (0. 뒤로가기)");
            System.out.print("입력 : ");

            int inputDeleteNumber;
            try {
                inputDeleteNumber = InputSystem.inputInt();

                // 💡 0번 누르면 안전하게 메인 화면으로 복귀!
                if (inputDeleteNumber == 0) {
                    InputSystem.clearBuffer();
                    return "카테고리";
                }

                if(0 < inputDeleteNumber && inputDeleteNumber <= database.getOnCartProducts().size()){

                    // ✨ 핵심: 클래스 변수(onCartProduct)에 저장하지 않고, 여기서 '지역 변수'로 꺼냅니다!
                    Product targetProduct = database.getOnCartProducts().get(inputDeleteNumber - 1);
                    Product targetOriginProduct = database.getSelectedProducts().get(inputDeleteNumber - 1);

                    // 💡 꺼낸 지역 변수를 삭제 확인 메서드(confirmDelete)로 던져줍니다.
                    // 삭제가 성공(true)하면 메인으로 가고, 취소(false)하면 continue로 다시 장바구니 목록을 보여줍니다!
                    if (confirmDelete(targetProduct, targetOriginProduct)) {
                        return "카테고리";
                    } else {
                        continue;
                    }

                } else {
                    System.out.println("\n항목 내의 숫자만 입력해주세요.");
                }

            } catch (InputMismatchException e) {
                System.out.println("\n숫자만 입력해주세요.");
                InputSystem.clearBuffer();
            }
        }
    }

    private boolean confirmDelete(Product onCartProduct, Product selectedCartProduct){
        while(true){
            try{
                System.out.printf("\n%s를 장바구니에서 제거하시겠습니까?\n", onCartProduct.getProductName());
                System.out.println("1. 제거      2. 취소");
                System.out.print("입력 : ");
                int input = InputSystem.inputInt();

                if (input == 1) {
                    database.removeOnCartProduct(onCartProduct);
                    database.removeSelectedProduct(selectedCartProduct);
                    System.out.println("\n" + onCartProduct.getProductName() + "이(가) 장바구니에서 제거되었습니다.\n");
                    return true; // 삭제 성공
                } else if (input == 2) {
                    System.out.println("\n상품 제거를 취소하였습니다.\n");
                    return false; // 삭제 취소
                } else {
                    System.out.println("\n1 또는 2만 입력해주세요.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\n숫자만 입력해주세요.");
                InputSystem.clearBuffer();
            }
        }
    }
}
