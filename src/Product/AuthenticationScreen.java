package Product;

import IO.InputSystem;

public class AuthenticationScreen implements Screen {

    private final Database database;

    public AuthenticationScreen(Database database) {
        this.database = database;
    }

    @Override
    public String display() {
        if (database.getLoginTryCount() >= 3) {
            System.out.println("\n로그인 잠금 : 관리자에게 문의해보시기 바랍니다.\n");
            return "카테고리";
        }

        while (true) {
            String inputID = inputAdminID();

            String nextStep = checkID(inputID);

            if (nextStep.equals("관리자인증")) {
                if (database.getLoginTryCount() >= 3) {
                    System.out.println("\n[ 로그인 잠금 ] : 시도 횟수를 모두 소모했습니다.\n");
                    return "카테고리";
                }
                continue;
            }
            return nextStep;
        }
    }

    private String inputAdminID() {
        String adminId;
        InputSystem.clearBuffer();
        while(true){
            System.out.println("\n[ 관리자 인증 ]");
            System.out.println("관리자 비밀번호를 입력해주세요. (0. 뒤로가기)");
            System.out.print("입력 : ");
            adminId = InputSystem.inputString();

            if(adminId.equals("0")){
                return "뒤로가기";
            }

            if(adminId.isEmpty()){
                System.out.println("비밀번호가 입력되지 않았습니다.");
            } else{
                return adminId;
            }
        }
    }

    private String checkID(String id) {
        if(id.equals("뒤로가기")){
            return "카테고리";
        }

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

