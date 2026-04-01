package Main;

import Commerce.CommerceSystem;
import Product.*;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Database database = new Database();
        Map<String, Screen> screenMap =  new HashMap<String, Screen>();

        screenMap.put("카테고리", new CategoryScreen(database));
        screenMap.put("상품선택", new ProductScreen(database));
        screenMap.put("장바구니", new CartScreen(database));
        screenMap.put("관리자인증", new AuthenticationScreen(database));
        screenMap.put("관리자모드", new AdminScreen(database));

        CommerceSystem commerceSystem = new CommerceSystem(screenMap);

        commerceSystem.start();
    }
}
