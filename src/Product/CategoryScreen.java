package Product;

import Exception.LoopEndException;
import IO.InputSystem;

import java.util.InputMismatchException;

public class CategoryScreen implements Screen {
    private final Database database;

    public CategoryScreen(Database database) {
        this.database = database;
    }

    @Override
    public String display() throws LoopEndException {
        int input;

        while(true) {
            // 1. 카테고리 목록 출력
            printCategories();
            // 1-1. 장바구니에 상품이 담기면 출력
            printCartMenu();
            try {
                // 2. 입력 받기
                input = InputSystem.inputInt();
                // 3. 카테고리 선택

                if(database.getOnCartProducts().isEmpty()){
                    if(input < 0 || input > database.getCategoriesSize() && input != database.getCategoriesSize() + 3){
                        System.out.println("\n항목에 존재하는 숫자만 입력해주세요.\n");
                        continue;
                    }
                } else {
                    if(input < 0 || input > database.getCategoriesSize() + 3){
                        System.out.println("\n항목에 존재하는 숫자만 입력해주세요.\n");
                        continue;
                    }
                }

                if (input == 0) {
                    System.out.println("==========================\n프로그램을 종료합니다.");
                    throw new LoopEndException();
                } else if (input == database.getCategoriesSize() + 3) {
                    return "관리자인증";
                } else if (input == database.getCategoriesSize() + 1) {
                    database.unsetRemoveMode();
                    return "장바구니";
                } else if (input == database.getCategoriesSize() + 2) {
                    database.setRemoveMode();
                    return "장바구니";
                } else {
                    selectCategory(input);
                    return "상품선택";
                }
            } catch (InputMismatchException e) {
                System.out.println("\n항목에 존재하는 숫자만 입력해주세요.\n");
                InputSystem.clearBuffer();
            }
        }
    }

    // 존재하는 카테고리 전체 출력해주는 함수
    private void printCategories() {
        int i = 1;

        System.out.println("[ 실시간 커머스 플랫폼 메인 ]");
        for (Category category : database.getCategories()) {
            // 카테고리명 출력
            System.out.println(i + ". " + category.getCategoryName());
            i++;
        }
        System.out.println("0. 종료      | 프로그램 종료");

        // 이 위에 장바구니 확인과 주문 취소가 있어서 + 3
        System.out.println((database.getCategories().size() + 3) + ". 관리자 모드");
    }

    public void printCartMenu() {
        if (database.getOnCartProducts().isEmpty())
            return;
        int size = database.getCategories().size();

        System.out.println();
        System.out.println("[ 주문 관리 ]");
        System.out.println((size + 1) + ". 장바구니 확인    | 장바구니를 확인 후 주문합니다.");
        System.out.println((size + 2) + ". 주문 취소       | 진행중인 주문을 취소합니다.");
    }

    // 선택한 카테고리 데이터베이스에 저장시키기
    private void selectCategory(int categoryNumber) {
        database.setCategory(categoryNumber);
        database.setProducts(database.getSelectedCategory().getProducts());
    }


}

