package Commerce;

import IO.InputSystem;
import Product.Product;
import Product.Category;
import Exception.GoBackException;
import Exception.LoopEndException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class CommerceSystem {

    private final List<Category> categories;

    protected boolean isEnd = false;

    // CommerceSystem을 main이외에도 여기저기서 사용할 거면 main에 적어두는게 좋다.
    public CommerceSystem() {
        // 상품들 객체 생성
        Product electronicProduct1 = new Product("Galaxy S25", 1200000, "최신 안드로이드 스마트폰", 30);
        Product electronicProduct2 = new Product("iPhone 16", 1350000, "Apple의 최신 스마트폰", 15);
        Product electronicProduct3 = new Product("MacBook Pro", 2400000, "M3 칩셋이 탑재된 노트북", 5);
        Product electronicProduct4 = new Product("AirPods", 350000, "노이즈 캔슬링 무선 이어폰", 1);

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

        this.categories = categories;
    }

    // 존재하는 카테고리 전체 출력해주는 함수
    private void printCategories() {
        int i = 1;

        System.out.println("[ 실시간 커머스 플랫폼 메인 ]");
        for (Category category : categories) {
            // 카테고리명 출력
            System.out.println(i + ". " + category.getCategoryName());
            i++;
        }
        System.out.println("0. 종료      | 프로그램 종료");
    }

    // 존재하는 카테고리 번호인지 검사하고 그 번호를 반환하는 함수
    private int returnCategoryID() throws LoopEndException {
        int input;
        while (true) {
            // 카테고리 내용물 출력
            printCategories();

            try {
                input = InputSystem.inputInt();
            } catch (InputMismatchException e) {
                System.out.println("\n숫자만 입력해주세요.\n");
                InputSystem.clearBuffer();
                continue;
            }

            if (input < 0 || input > categories.size()) {
                // 카테고리 리스트의 인덱스 예외처리
                System.out.println("\n카테고리에 해당하는 숫자를 입력해주세요.\n");
                continue;
            }
            break;
        }
        if (input == 0) {
            System.out.println("==========================\n프로그램을 종료합니다.");
            throw new LoopEndException();
        }
        return input;
    }
    // input 이 0이면 isEnd = true 하고

    // 선택한 카테고리를 반환해주는 함수
    // commerceSystem.selectCategory() <- 얘는 여기서 써도 되는것
    protected Category selectCategory() throws LoopEndException {
        int id = returnCategoryID();

        // List에서 특정 인덱스의 값에 접근하는 방법 get (0부터이니 -1 해줘야함)
        return categories.get(id - 1);
    }

    // 커머스 시스템 실행 함수
    public void start() {
        while (!isEnd) {
            try {
                // 3개의 카테고리중 한개의 카테고리를 반환해서 초기화
                Category selectedCategory = selectCategory();
                // 카테고리 내에 있는 상품을 반환해서 초기화
                Product selectedProduct = selectedCategory.selectProduct();     // <=== 0을 입력하면 NullPointerException 발생
                // 선택된 상품의 정보를 출력
                selectedProduct.printProductInfo();
            } catch (GoBackException e) {
                System.out.println("\n이전 메뉴로 돌아갑니다.\n");
                InputSystem.clearBuffer();
            } catch (NullPointerException e) {
                System.out.println("\nNullPointerException 발생.\n");
                break;
            } catch (LoopEndException e) {
                isEnd = true;
            }
        }
    }
}
