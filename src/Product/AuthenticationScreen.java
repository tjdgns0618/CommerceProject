package Product;

import Exception.LoopEndException;
import Exception.GoBackException;
import IO.InputSystem;

public class AuthenticationScreen implements Screen {

    private final Database database;

    public AuthenticationScreen(Database database) {
        this.database = database;
    }

    @Override
    public String display() throws LoopEndException {
        String inputID;
        // 1. 관리자 아이디 입력 받기
        inputID = inputAdminID();
        // 2. 관리자 인증 성공 실패 여부 확인하는 함수, 관리자 인증 성공시 관리자 스크린으로 변경
        return checkID(inputID);
    }

    private String inputAdminID() {
        if(database.getLoginTryCount() > 2) {
            System.out.println("\n로그인 잠금 : 관리자에게 문의해보시기 바랍니다.\n");
            throw new GoBackException("카테고리");
        }
        String adminId;

        while (true) {
            System.out.println("관리자 비밀번호를 입력해주세요.");
            System.out.print("입력 : ");
            adminId = InputSystem.inputString();
            if (adminId.isEmpty()) {
                System.out.println("비밀번호를 입력해주세요.");
            } else {
                return adminId;
            }
        }
    }

    private String checkID(String id) {
        if (id.equals(database.getAdminId())) {
            System.out.println("성공하였습니다.");
            database.resetLoginTryCount();
            return "관리자모드";
        } else {
            database.addLoginTryCount();
            System.out.println("\n비밀번호가 틀렸습니다. 남은 횟수 : " + database.getLoginTryCount() + " / 3");
            return "관리자인증";
        }
    }
}
