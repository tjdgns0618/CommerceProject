package Product;

import Exception.LoopEndException;
import Exception.GoBackException;
import IO.InputSystem;

import java.util.InputMismatchException;

public class CategoryScreen implements Screen {
    private final Database database;

    public CategoryScreen(Database database) {
        this.database = database;
    }

    @Override
    public String display() throws LoopEndException {
        int categoryID = 0;
        // 1. 카테고리 목록 출력
        printCategories();
        printCartMenu();
        // 2. 입력 받기
        categoryID = returnCategoryNumber();
        // 3. 카테고리 선택
        selectCategory(categoryID);

        return "상품선택";
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

    private int returnCategoryNumber() throws LoopEndException {
        int input;
        while (true) {
            try {
                input = InputSystem.inputInt();

                // 카테고리를 선택해서 번호를 반환하는 함수 예외시 반복

                // 장바구니에 상품이 없다면 0~카테고리 사이즈, 6 입력 가능
                // 관리자 입력 먼저 검사

                if (input == 0) {
                    System.out.println("==========================\n프로그램을 종료합니다.");
                    throw new LoopEndException();
                }

                if (input == database.getCategoriesSize() + 3) {
                    InputSystem.clearBuffer();
                    throw new GoBackException("관리자인증");
                } else if (database.getOnCartProducts().isEmpty()) {
                    if (input < 0 || input >= database.getCategoriesSize() + 1) {
                        System.out.println("\n선택지에 해당하는 숫자를 입력해주세요.\n");
                        InputSystem.clearBuffer();
                        throw new GoBackException("카테고리");
                    } else
                        break;
                } else {
                    if (input < 0 || input >= database.getCategoriesSize() + 3) {
                        System.out.println("\n선택지에 해당하는 숫자를 입력해주세요.\n");
                        InputSystem.clearBuffer();
                        throw new GoBackException("카테고리");
                    } else if (input == database.getCategoriesSize() + 1) {
                        database.unsetRemoveMode();
                        InputSystem.clearBuffer();
                        throw new GoBackException("장바구니");
                    } else if (input == database.getCategoriesSize() + 2) {
                        database.setRemoveMode();
                        InputSystem.clearBuffer();
                        throw new GoBackException("장바구니");
                    } else
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("\n선택지에 해당하는 숫자를 입력해주세요.\n");
                InputSystem.clearBuffer();
                throw new GoBackException("카테고리");
            }
        }
        return input;
    }

    // 선택한 카테고리 데이터베이스에 저장시키기
    private void selectCategory(int categoryNumber) {
        database.setCategory(categoryNumber);
        database.setProducts(database.getSelectedCategory().getProducts());
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
}

