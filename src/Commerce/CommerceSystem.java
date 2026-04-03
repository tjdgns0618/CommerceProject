package Commerce;

import IO.InputSystem;
import Product.Screen;
import Exception.GoBackException;
import Exception.LoopEndException;

import java.util.*;

public class CommerceSystem {
    private final Map<String, Screen> screenMap;
    private String currentScreenName = "카테고리";

    protected boolean loopEnd = false;

    public CommerceSystem(Map<String, Screen> screenMap) {
        this.screenMap = screenMap;
    }

    // 스크린들의 흐름 제어
    // 커머스 시스템 실행 함수
    public void start(){
        while (!loopEnd) {
            try {
                Screen currentScreen = screenMap.get(currentScreenName);
                currentScreenName = currentScreen.display();

                // 예외처리 부분=====================
            }  catch (LoopEndException e) {
                InputSystem.closeScanner();
                loopEnd = true;
            } catch (GoBackException e) {
                currentScreenName = e.getNextScreen();
            }
        }
    }
}
