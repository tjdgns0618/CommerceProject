package Product;

import IO.InputSystem;

import java.util.InputMismatchException;

public class AdminScreen implements Screen {

    Database database;

    public AdminScreen(Database database) {
        this.database = database;
    }

    @Override
    public String display() {
        while (true) {
            printAdminSelectable();
            try {
                System.out.println("입력 : ");
                int input = InputSystem.inputInt();

                switch (input) {
                    case 1:
                        processAddProduct();
                        break;
                    case 2:
                        InputSystem.clearBuffer();
                        processEditProduct();
                        break;
                    case 3:
                        InputSystem.clearBuffer();
                        processDeleteProduct();
                        break;
                    case 4:
                        InputSystem.clearBuffer();
                        printAllProducts();
                        break;
                    case 0:
                        InputSystem.clearBuffer();
                        return "카테고리";
                    default:
                        System.out.println("\n선택지에 해당하는 숫자를 입력해주세요.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\n숫자만 입력해주세요.");
                InputSystem.clearBuffer();
            }
        }
    }

    private void printAdminSelectable() {
        System.out.println("\n[ 관리자 모드 ]");
        System.out.println("1. 상품 추가");
        System.out.println("2. 상품 수정");
        System.out.println("3. 상품 삭제");
        System.out.println("4. 전체 상품 현황");
        System.out.println("0. 메인으로 돌아가기");
    }

    private void processAddProduct() {
        int categoryNumber = selectCategory();
        if (categoryNumber == 0) return;

        Product newProduct = inputNewProduct(categoryNumber);
        if (newProduct == null) return;

        addNewProductToCategory(categoryNumber, newProduct);
    }

    private int selectCategory() {
        while (true) {
            System.out.println("\n어느 카테고리에 상품을 추가하시겠습니까? (0. 취소)");
            int i = 1;
            for (Category category : database.getCategories()) {
                System.out.println(i + ". " + category.getCategoryName());
                i++;
            }
            try {
                System.out.print("입력 : ");
                int input = InputSystem.inputInt();
                if (input == 0) return 0;

                if (input < 0 || input > database.getCategories().size()) {
                    System.out.println("\n항목에 존재하는 숫자만 입력해주세요.");
                    continue;
                }
                return input;
            } catch (InputMismatchException e) {
                InputSystem.clearBuffer();
                System.out.println("\n항목에 존재하는 숫자만 입력해주세요.");
            }
        }
    }

    private Product inputNewProduct(int categoryNumber) {
        String newProductName;
        int newProductPrice;
        String newProductDescription;
        int newProductStock;

        InputSystem.clearBuffer();

        while (true) {
            try {
                System.out.println("\n[ " + database.getCategories().get(categoryNumber - 1).getCategoryName() + " 카테고리에 상품 추가 ]");
                System.out.print("상품명을 입력해주세요 : ");
                newProductName = InputSystem.inputString();

                if (newProductName.equals("0")) return null;

                boolean isDuplicate = false;
                for (Product product : database.getCategories().get(categoryNumber - 1).getProducts()) {
                    if (newProductName.equals(product.getProductName())) {
                        System.out.println("동일한 이름의 제품이 존재합니다.");
                        isDuplicate = true;
                        break;
                    }
                }
                if (isDuplicate) continue;

                System.out.print("가격을 입력해주세요 : ");
                newProductPrice = InputSystem.inputInt();
                InputSystem.clearBuffer();

                System.out.print("상품 설명을 입력해주세요 : ");
                newProductDescription = InputSystem.inputString();

                System.out.print("재고수량을 입력해주세요 : ");
                newProductStock = InputSystem.inputInt();

                return new Product(newProductName, newProductPrice, newProductDescription, newProductStock);

            } catch (InputMismatchException e) {
                System.out.println("필요입력사항만 입력해주세요.");
                InputSystem.clearBuffer();
            }
        }
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

            try {
                int input = InputSystem.inputInt();
                if (input == 1) {
                    database.addNewProductToCategory(categoryNumber, newProduct);
                    System.out.println("\n상품이 성공적으로 추가되었습니다!\n");
                    return;
                } else if (input == 2) {
                    System.out.println("\n 상품 추가가 취소되었습니다.");
                    return;
                } else {
                    System.out.println("\n1 또는 2만 입력해주세요.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\n숫자만 입력해주세요.");
                InputSystem.clearBuffer();
            }
        }
    }

    private void processEditProduct() {
        System.out.println("\n수정할 상품명을 입력하세요. (0. 취소)");
        String editProductName = InputSystem.inputString();
        if (editProductName.equals("0")) return;

        Product editProduct = findProduct(editProductName);
        if (editProduct == null) {
            System.out.println("\n해당 상품은 존재하지 않습니다.");
            return;
        }

        editProduct.printProductInfo();
        editProductDetails(editProduct);
    }

    private void editProductDetails(Product editProduct) {
        Product onCartEditProduct = database.getOnCartProducts().stream().
                filter(product -> product.getProductName().equals(editProduct.getProductName())).
                findFirst().orElse(null);
        while (true) {
            System.out.println("\n수정할 항목을 선택해주세요. (0. 뒤로가기)");
            System.out.println("1. 가격");
            System.out.println("2. 설명");
            System.out.println("3. 재고수량");
            System.out.print("입력 : ");

            try {
                int input = InputSystem.inputInt();
                if (input == 0) return;

                switch (input) {
                    case 1:
                        InputSystem.clearBuffer();
                        int beforePrice = editProduct.getProductPrice();
                        System.out.println("\n현재 가격 : " + beforePrice);
                        System.out.print("새로운 가격을 입력해주세요 : ");
                        int inputPrice = InputSystem.inputInt();
                        editProduct.setProductPrice(inputPrice);
                        if (onCartEditProduct != null) onCartEditProduct.setProductPrice(inputPrice);
                        System.out.printf("%s의 가격이 %,d원 -> %,d원으로 수정되었습니다.\n", editProduct.getProductName(), beforePrice, inputPrice);
                        break;
                    case 2:
                        InputSystem.clearBuffer();
                        String beforeDescription = editProduct.getProductDescription();
                        System.out.println("\n현재 설명 : " + beforeDescription);
                        System.out.print("새로운 설명을 입력해주세요 : ");
                        String inputDescription = InputSystem.inputString();
                        editProduct.setProductDescription(inputDescription);
                        if (onCartEditProduct != null) onCartEditProduct.setProductDescription(inputDescription);
                        System.out.printf("%s의 설명이 \"%s\" -> \"%s\"으로 수정되었습니다.\n", editProduct.getProductName(), beforeDescription, inputDescription);
                        break;
                    case 3:
                        InputSystem.clearBuffer();
                        int beforeStock = editProduct.getProductStock();
                        System.out.println("\n현재 수량 : " + beforeStock);
                        System.out.print("새로운 수량을 입력해주세요 : ");
                        int inputStock = InputSystem.inputInt();
                        editProduct.setProductStock(inputStock);
                        System.out.printf("%s의 수량이 %d개 -> %d개로 수정되었습니다.\n", editProduct.getProductName(), beforeStock, inputStock);
                        break;
                    default:
                        System.out.println("\n항목에 존재하는 숫자를 입력해주세요.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\n숫자만 입력해주세요.");
                InputSystem.clearBuffer();
            }
        }
    }

    private void processDeleteProduct() {
        System.out.println("\n삭제할 상품명을 입력하세요. (0. 취소)");
        System.out.print("입력 : ");
        String deleteProductName = InputSystem.inputString();
        if (deleteProductName.equals("0")) return;

        Category targetCategory = null;
        Product targetProduct = null;

        for (Category category : database.getCategories()) {
            for (Product product : category.getProducts()) {
                if (product.getProductName().equals(deleteProductName)) {
                    targetCategory = category;
                    targetProduct = product;
                    break;
                }
            }
            if (targetProduct != null) break;
        }

        if (targetCategory == null) {
            System.out.println("\n해당 상품은 존재하지 않습니다.");
            return;
        }

        System.out.println("\n" + targetProduct.getProductName() + "를 삭제하겠습니다...");
        targetCategory.removeProduct(targetProduct);

        Product cartTarget = database.getOnCartProducts().stream().
                filter(p -> p.getProductName().equals(deleteProductName)).
                findFirst().orElse(null);
        Product selectedTarget = database.getSelectedProducts().stream().
                filter(product -> product.getProductName().equals(deleteProductName)).
                findFirst().orElse(null);

        if(cartTarget != null) database.removeOnCartProduct(cartTarget);
        if(selectedTarget != null) database.removeSelectedProduct(selectedTarget);

        System.out.println("삭제가 완료되었습니다.");
    }

    private void printAllProducts(){
        System.out.println("\n[ 전체 상품 재고 현황 ]");
        for(Category category : database.getCategories()){
            System.out.println("\n카테고리 : " + category.getCategoryName());
            for(Product p : category.getProducts()){
                System.out.printf("- %-14s | %,11d원 | 재고 : %3d개 | %s\n", p.getProductName(), p.getProductPrice(), p.getProductStock(), p.getProductDescription());
            }
        }
        System.out.println("\n(엔터를 눌러서 돌아가기)");
        InputSystem.clearBuffer();
    }

    private Product findProduct(String name){
        for(Category category : database.getCategories()){
            for(Product  product : category.getProducts()){
                if(product.getProductName().equals(name)){
                    return product;
                }
            }
        }
        return null;
    }

}
