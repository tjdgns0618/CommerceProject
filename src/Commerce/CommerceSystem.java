package Commerce;

import Product.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommerceSystem {
    Scanner sc = new Scanner(System.in);
    List<Product> products =new ArrayList<Product>() ;

    public CommerceSystem(List<Product> product) {
        this.products = product;
    }

    public void Start(){
        int input;
        while(true){
            int i = 1;

            for(Product p : products){
                // 14칸을 소지하고 들어오는 문자를 왼쪽 정렬 시키기
                System.out.printf(i + ". %-14s", p.getProductName());
                // 10칸을 소지하고 1000 단위로 , 를 찍어주고 오른쪽 정렬
                System.out.printf("| %,11d원", p.getProductPrice());
                System.out.println(" | " + p.getProductDescription());

                i++;
            }

            System.out.println("0. 종료          | 프로그램 종료");

            input = sc.nextInt();

            if(input==0){
                sc.close();
                break;
            }
        }
    }


}
