package Product;

import Exception.LoopEndException;
import Exception.GoBackException;
import IO.InputSystem;

import java.util.InputMismatchException;

public class AdminScreen implements Screen {

    Database database;

    public AdminScreen(Database database) {
        this.database = database;
    }

    @Override
    public void display() throws LoopEndException {
        // 1. 관리자모드 선택지 출력
        printAdminSelectable();
        // 2. 선택된 번호 실행
        inputAdminSelect();
        // 3. 상품 추가 가능 카테고리 출력


    }

    private void printAdminSelectable() {
        System.out.println("\n[ 관리자 모드 ]");
        System.out.println("1. 상품 추가");
        System.out.println("2. 상품 수정");
        System.out.println("3. 상품 삭제");
        System.out.println("4. 전체 상품 현황");
        System.out.println("0. 메인으로 돌아가기");
    }

    private void inputAdminSelect(){
        int input = InputSystem.inputInt();

        switch (input) {
            case 1:
                printCategories();
                int categoryNumber = selecteCategory();
                Product newProduct = inputNewProduct(categoryNumber);
                addNewProductToCategory(categoryNumber, newProduct);
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }

    private void printCategories() {
        System.out.println("\n어느 카테고리에 상품을 추가하시겠습니까?");
        int i = 1;
        for (Category category : database.getCategories()) {
            System.out.println(i + ". " + category.getCategoryName());
            i++;
        }
    }

    private int selecteCategory(){
        System.out.print("입력 : ");
        return InputSystem.inputInt();
    }

    private Product inputNewProduct(int categoryNumber) {

        InputSystem.clearBuffer();
        System.out.println("\n[ " + database.getCategories().get(categoryNumber - 1).getCategoryName() + " 카테고리에 상품 추가 ]");
        System.out.print("상품명을 입력해주세요 : ");
        String newProductName = InputSystem.inputString();
        for(Product product : database.getCategories().get(categoryNumber - 1).getProducts()) {
            if(newProductName.equals(product.getProductName())) {
                System.out.println("동일한 이름의 제품이 존재합니다.");
                throw new GoBackException();
            }
        }
        System.out.print("가격을 입력해주세요 : ");
        int newProductPrice = InputSystem.inputInt();
        InputSystem.clearBuffer();
        System.out.print("상품 설명을 입력해주세요 : ");
        String newProductDescription = InputSystem.inputString();
        System.out.print("재고수량을 입력해주세요 : ");
        int newProductStock = InputSystem.inputInt();
        InputSystem.clearBuffer();

        return new Product(newProductName, newProductPrice, newProductDescription, newProductStock);
    }

    private void addNewProductToCategory(int categoryNumber, Product newProduct) {
        while (true) {
            System.out.printf("%-14s", newProduct.getProductName());
            // 10칸을 소지하고 1000 단위로 , 를 찍어주고 오른쪽 정렬
            System.out.printf("| %,11d원", newProduct.getProductPrice());
            System.out.print(" | " + newProduct.getProductDescription());
            System.out.printf(" | 재고 : %2d개\n", newProduct.getProductStock());
            System.out.println("위 정보로 상품을 추가하시겠습니까?");
            System.out.println("1. 확인    2. 취소");

            int input;
            try {
                input = InputSystem.inputInt();
                database.setScreenName("카테고리");
                if(input == 1){
                    database.addNewProductToCategory(categoryNumber, newProduct);
                    System.out.println("\n상품이 성공적으로 추가되었습니다!\n");
                }
                InputSystem.clearBuffer();
                throw new GoBackException();
            } catch (InputMismatchException e) {
                System.out.println("1, 2 만 입력해주세요.");
                InputSystem.clearBuffer();
            }
        }
    }


}
