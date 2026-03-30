package Commerce;

import IO.InputSystem;
import Product.Product;
import Product.Category;
import Product.Database;
import Product.Screen;
import Product.CategoryScreen;
import Product.ProductScreen;
import Product.CartScreen;
import Exception.GoBackException;
import Exception.LoopEndException;

import java.util.*;

public class CommerceSystem {
    private final Map<String, Screen> screenMap =  new HashMap<String, Screen>();
    private final Database database =  new Database();

    private final CategoryScreen categoryScreen =  new CategoryScreen(database);
    private final ProductScreen productScreen =  new ProductScreen(database);
    private final CartScreen cartScreen =  new CartScreen(database);

    private final String adminCode = "tjdgns0618";
    private int adminTryCount = 0;

    protected boolean loopEnd = false;

    // CommerceSystem을 main이외에도 여기저기서 사용할 거면 main에 적어두는게 좋다.
    public CommerceSystem() {
        // 상품들 객체 생성
        Product electronicProduct1 = new Product("Galaxy S25", 1200000, "최신 안드로이드 스마트폰", 30);
        Product electronicProduct2 = new Product("iPhone 16", 1350000, "Apple의 최신 스마트폰", 15);
        Product electronicProduct3 = new Product("MacBook Pro", 2400000, "M3 칩셋이 탑재된 노트북", 5);
        Product electronicProduct4 = new Product("AirPods", 350000, "노이즈 캔슬링 무선 이어폰", 0);

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

        screenMap.put("카테고리", categoryScreen);
        screenMap.put("상품선택", productScreen);
        screenMap.put("장바구니", cartScreen);

        database.setScreenName("카테고리");

        database.saveCategories(categories);
    }

    // 존재하는 카테고리 번호인지 검사하고 그 번호를 반환하는 함수
//    private int returnCategoryID() throws LoopEndException {
//        int input;
//        while (true) {
//            // 카테고리 내용물 출력
//            // printCategories();
//        }
//
//        if (input == 0) {
//            System.out.println("==========================\n프로그램을 종료합니다.");
//            throw new LoopEndException();
//        }
//        return input;
//    }
//    // input 이 0이면 loopEnd = true 하고
//
//    // 선택한 카테고리를 반환해주는 함수
//    // commerceSystem.selectCategory() <- 얘는 여기서 써도 되는것
//    protected Category selectCategory(int id) throws LoopEndException {
//        // List에서 특정 인덱스의 값에 접근하는 방법 get (0부터이니 -1 해줘야함)
//        return categories.get(id - 1);
//    }

    // 커머스 시스템 실행 함수
    public void start() throws LoopEndException {
        while (!loopEnd) {
            try {
                Screen currentScreen = screenMap.get(database.getScreenName());
                currentScreen.display();

//                // 입력한 번호가 카테고리 내의 숫자라면
//                if (menuId >= 1 && menuId <= categories.size()) {
//                    // 3개의 카테고리중 한개의 카테고리를 반환해서 초기화
//                    Category selectedCategory = selectCategory(menuId);
//                    // 카테고리 내에 있는 상품을 반환해서 초기화
//                    Product selectedProduct = selectedCategory.selectProduct();
//                    // 선택된 상품의 정보를 출력
//                    selectedProduct.printProductInfo();
//                    cart.addToCart(selectedProduct);
//                } else if (menuId == categories.size() + 1) {
//                    cart.orderCart();
//                } else if (menuId == categories.size() + 2) {
//                    System.out.println("주문 취소 ");
//                } else if (menuId == 6 && adminTryCount < 3) {
//                    System.out.println("관리자 비밀번호를 입력해주세요:");
//                    while (adminTryCount < 3) {
//                        String adminCode = InputSystem.inputString();
//                        if (adminCode.equals(this.adminCode)) {
//                            System.out.println("관리자 인증 성공");
//                            break;
//                        } else {
//                            System.out.println("인증 실패 다시입력해주세요. (남은 횟수 : " + (2 - adminTryCount) + ")");
//                            adminTryCount++;
//                            if (adminTryCount == 3)
//                                System.out.println("인증 실패");
//                        }
//                    }
//                } else {
//                    System.out.println("관리자 모드에 접근할 수 없습니다.");
//                }

                // 예외처리 부분=====================
            } catch (NullPointerException e) {
                System.out.println("\nNullPointerException 발생.\n");
                System.out.println(database.getScreenName());
                System.out.println(database.getSelectedCategory());
                System.out.println(database.getSelectedProduct());
                break;
            } catch (LoopEndException e) {
                loopEnd = true;
            } catch (GoBackException e) {
                InputSystem.clearBuffer();
            }
        }
    }
}
