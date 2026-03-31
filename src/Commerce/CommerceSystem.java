package Commerce;

import IO.InputSystem;
import Product.Product;
import Product.Category;
import Product.Database;
import Product.Screen;
import Product.CategoryScreen;
import Product.ProductScreen;
import Product.CartScreen;
import Product.AdminScreen;
import Product.AuthenticationScreen;
import Exception.GoBackException;
import Exception.LoopEndException;

import java.util.*;

public class CommerceSystem {
    private final Map<String, Screen> screenMap =  new HashMap<String, Screen>();
    private final Database database =  new Database();

    private final CategoryScreen categoryScreen =  new CategoryScreen(database);
    private final ProductScreen productScreen =  new ProductScreen(database);
    private final CartScreen cartScreen =  new CartScreen(database);
    private final AuthenticationScreen  authenticationScreen =  new AuthenticationScreen(database);
    private final AdminScreen adminScreen =  new AdminScreen(database);

    private final String adminCode = "tjdgns0618";

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
        screenMap.put("관리자인증", authenticationScreen);
        screenMap.put("관리자모드", adminScreen);

        database.setScreenName("카테고리");

        database.saveCategories(categories);
    }

    // 커머스 시스템 실행 함수
    public void start() throws LoopEndException {
        while (!loopEnd) {
            try {
                Screen currentScreen = screenMap.get(database.getScreenName());
                currentScreen.display();

                // 예외처리 부분=====================
            } catch (NullPointerException e) {
                System.out.println("\nNullPointerException 발생.\n");
                break;
            } catch (LoopEndException e) {
                loopEnd = true;
            } catch (GoBackException e) {
                System.out.println(database.getScreenName() + "으로 갑니다.");
            }
        }
    }
}
