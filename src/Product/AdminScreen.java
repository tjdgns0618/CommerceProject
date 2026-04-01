package Product;

import Exception.GoBackException;
import IO.InputSystem;

import java.util.InputMismatchException;

public class AdminScreen implements Screen {

    Database database;
    private Product editProduct;
    private Category editCategory;
    private Product onCartEditProduct;

    public AdminScreen(Database database) {
        this.database = database;
    }

    @Override
    public void display() {
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

    private void inputAdminSelect() {
        int input = InputSystem.inputInt();

        switch (input) {
            case 1:
                InputSystem.clearBuffer();
                int categoryNumber = selectCategory();
                Product newProduct = inputNewProduct(categoryNumber);
                addNewProductToCategory(categoryNumber, newProduct);
                break;
            case 2:
                InputSystem.clearBuffer();
                findProductFromAllCategories();
                editFindProduct();
                break;
            case 3:
                InputSystem.clearBuffer();
                findProductFromAllCategories();
                deleteProduct();
                break;
            case 4:
                break;
            case 0:
                database.setScreenName("카테고리");
                InputSystem.clearBuffer();
                throw new GoBackException();
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

    private int selectCategory() {
        int input;
        while (true) {
            printCategories();
            try {
                System.out.print("입력 : ");
                input = InputSystem.inputInt();
                if(input < 0 || input > database.getCategories().size()) {
                    throw new InputMismatchException();
                }
                break;
            } catch (InputMismatchException e) {
                InputSystem.clearBuffer();
                System.out.println("\n항목에 존재하는 숫자만 입력해주세요.");
            }
        }
        InputSystem.clearBuffer();
        return input;
    }

    // 이것도 Product에 추가해야 하는 것인가?
    private Product inputNewProduct(int categoryNumber) {
        String newProductName;
        int newProductPrice;
        String newProductDescription;
        int newProductStock;

        while(true) {
            try {
                System.out.println("\n[ " + database.getCategories().get(categoryNumber - 1).getCategoryName() + " 카테고리에 상품 추가 ]");
                System.out.print("상품명을 입력해주세요 : ");
                newProductName = InputSystem.inputString();
                for (Product product : database.getCategories().get(categoryNumber - 1).getProducts()) {
                    if (newProductName.equals(product.getProductName())) {
                        System.out.println("동일한 이름의 제품이 존재합니다.");
                        throw new GoBackException();
                    }
                }
                System.out.print("가격을 입력해주세요 : ");
                newProductPrice = InputSystem.inputInt();
                InputSystem.clearBuffer();
                System.out.print("상품 설명을 입력해주세요 : ");
                newProductDescription = InputSystem.inputString();
                System.out.print("재고수량을 입력해주세요 : ");
                newProductStock = InputSystem.inputInt();
                InputSystem.clearBuffer();
                break;
            } catch (InputMismatchException e){
                InputSystem.clearBuffer();
                System.out.println("필요입력사항만 입력해주세요.");
            }
        }

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
                if (input == 1) {
                    database.addNewProductToCategory(categoryNumber, newProduct);
                    System.out.println("\n상품이 성공적으로 추가되었습니다!\n");
                }else if(input == 2) {
                    InputSystem.clearBuffer();
                    throw new GoBackException();
                }else{
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.println("1, 2 만 입력해주세요.");
                InputSystem.clearBuffer();
            }
        }
    }

    private void findProductFromAllCategories() {
        System.out.print("수정할 상품명을 입력하세요 : ");
        String editProductName = InputSystem.inputString();

        for (Category category : database.getCategories()) {
            editProduct = category.getProducts().
                    stream().
                    filter(product -> product.getProductName().equals(editProductName)).
                    findFirst().
                    orElse(null);
            if (editProduct != null) {
                editProduct.printProductInfo();
                editCategory = category;
                onCartEditProduct = database.getOnCartProducts().stream().
                        filter(product -> product.getProductName().
                        equals(editProductName)).
                        findFirst().
                        orElse(null);
                break;
            }
        }
        if (editProduct == null) {
            System.out.println("해당 상품은 존재하지 않습니다.");
            throw new GoBackException();
        }
    }

    private void editFindProduct() {
        int input;
        while (true) {
            System.out.println("수정할 항목을 선택해주세요.");
            System.out.println("1. 가격");
            System.out.println("2. 설명");
            System.out.println("3. 재고수량");
            try {
                input = InputSystem.inputInt();
                if (input > 0 && input < 4) {
                    break;
                }
                System.out.println("항목에 존재하는 숫자를 입력해주세요.");
                InputSystem.clearBuffer();
            } catch (InputMismatchException e) {
                System.out.println("항목에 존재하는 숫자를 입력해주세요.");
                InputSystem.clearBuffer();
            }
        }

        while(true) {
            try {
                switch (input) {
                    case 1:
                        InputSystem.clearBuffer();
                        int beforePrice = editProduct.getProductPrice();
                        System.out.println("현재 가격 : " + editProduct.getProductPrice());
                        System.out.print("새로운 가격을 입력해주세요 : ");
                        int inputPrice = InputSystem.inputInt();
                        editProduct.setProductPrice(inputPrice);
                        if(onCartEditProduct != null)
                            onCartEditProduct.setProductPrice(inputPrice);
                        InputSystem.clearBuffer();
                        System.out.printf("%s의 가격이 %,d원 -> %,d원으로 수정되었습니다.", editProduct.getProductName(), beforePrice, editProduct.getProductPrice());
                        break;
                    case 2:
                        InputSystem.clearBuffer();
                        String beforeDescription = editProduct.getProductDescription();
                        System.out.println("현재 설명 : " + editProduct.getProductDescription());
                        System.out.print("새로운 설명을 입력해주세요 : ");
                        String inputDescription = InputSystem.inputString();
                        editProduct.setProductDescription(inputDescription);
                        if(onCartEditProduct != null)
                            onCartEditProduct.setProductDescription(inputDescription);
                        System.out.printf("%s의 설명이 \"%s\" -> \"%s\"으로 수정되었습니다.", editProduct.getProductName(), beforeDescription, editProduct.getProductDescription());
                        break;
                    case 3:
                        InputSystem.clearBuffer();
                        int beforeStock = editProduct.getProductStock();
                        System.out.println("현재 수량 : " + editProduct.getProductStock());
                        System.out.print("새로운 수량을 입력해주세요 : ");
                        int inputStock = InputSystem.inputInt();
                        editProduct.setProductStock(inputStock);
                        InputSystem.clearBuffer();
                        System.out.printf("%s의 수량이 %d개 -> %d개로 수정되었습니다.", editProduct.getProductName(), beforeStock, editProduct.getProductStock());
                        break;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("필요입력사항만 입력해주세요.");
            }
        }
    }

    private void deleteProduct(){
        System.out.println(editProduct.getProductName() + "를 삭제하겠습니다...");
        editCategory.removeProduct(editProduct);

        System.out.println("삭제가 완료되었습니다.");
        throw new GoBackException();
    }
}
