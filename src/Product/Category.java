package Product;

import IO.InputSystem;
import Exception.GoBackException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;

public class Category {
    private final String categoryName;
    private final List<Product> products = new ArrayList<Product>();

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    // 존재하는 상품들을 모두 출력하는 함수
    private void printProducts() {
        List<Product> selectedProducts = getProducts();

        int i = 1;
        // 선택한 카테고리의 이름을 출력해주며 상품들 출력
        // 여기는 선택한 카테고리의 이름을 표시해줘야 하니 카테고리 객체 사용
        System.out.println("[ " + getCategoryName() + " 카테고리 ]");
        for (Product p : selectedProducts) {
            // 14칸을 소지하고 들어오는 문자를 왼쪽 정렬 시키기
            System.out.printf(i + ". %-14s", p.getProductName());
            // 10칸을 소지하고 1000 단위로 , 를 찍어주고 오른쪽 정렬
            System.out.printf("| %,11d원", p.getProductPrice());
            System.out.println(" | " + p.getProductDescription());

            i++;
        }
        System.out.println("0. 뒤로가기");
    }

    // 존재하는 상품 번호인지 검사하고 그 번호를 반환하는 함수
    private int returnProductID() {
        int inputNum = 0;
        while (true) {
            printProducts();

            try {
                inputNum = InputSystem.inputInt();
            }catch (InputMismatchException e){
                System.out.println("\n숫자만 입력해주세요.\n");
                InputSystem.clearBuffer();
                continue;
            }

            if (inputNum < 0 || inputNum > getProducts().size()) {
                System.out.println("\n카테고리에 해당하는 숫자를 입력해주세요.\n");
                continue;
            }
            break;
        }
        if(inputNum == 0){
            throw new GoBackException();
        }
        return inputNum;
    }

    // 선택한 상품(Product)을 반환해주는 함수
    // category의 역할
    // printProducts 도 따로 만들기
    // commerceSystem.selectProduct() <- 딱보면 이상한 느낌이 든다 == 여기서 할 일이 아니다.
    public Product selectProduct() {
        int id = returnProductID();
        return getProducts().get(id - 1);
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }
}




