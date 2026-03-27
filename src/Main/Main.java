package Main;

import Product.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Product product1 = new Product("Galaxy S25", 1200000, "최신 안드로이드 스마트폰", 30);
        Product product2 = new Product("iPhone 16", 1350000, "Apple의 최신 스마트폰", 15);
        Product product3 = new Product("MacBook Pro", 2400000, "M3 칩셋이 탑재된 노트북", 5);
        Product product4 = new Product("AirPods", 350000, "노이즈 캔슬링 무선 이어폰", 1);

        List<Product> products = new ArrayList<Product>();
        int input = 0;


        products.add(product1);
        products.add(product2);
        products.add(product3);
        products.add(product4);

        int i = 1;

        for(Product p : products){
            System.out.printf(i + ". %-14s", p.getProductName());
            System.out.printf("| %10s", String.format("%,d",p.getProductPrice()) + "원");
            System.out.println(" | " + p.getProductDescription());

            i++;
        }

        System.out.println("0. 종료          | 프로그램 종료");

        input = sc.nextInt();




    }
}
