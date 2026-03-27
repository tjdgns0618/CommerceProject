package IO;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InputSystem {
    private static final Scanner sc = new Scanner(System.in);

    // InputSystem을 외부에서 new를 이용하여 객체화 못하게 하기 위한 방법
    private InputSystem() {}

    // 숫자를 입력받아 반환해주는 함수
    public static int inputInt(){
        return  sc.nextInt();
    }

    public static void clearBuffer(){
        sc.nextLine();
    }

    public static void closeScanner(){
        sc.close();
    }
}
