package cabinet;

public class Main {
    LoginView loginView;
    Frame_CS cs;

    public static void main(String[] args) {

        // 메인클래스 실행
        Main main = new Main();
        main.loginView = new LoginView(); // 로그인창 보이기
        main.loginView.setMain(main); // 로그인창에게 메인 클래스보내기
    }

    // 메인프레임창
    public void showFrame_CS(){
        loginView.dispose(); // 로그인창닫기
        this.cs = new Frame_CS(); // 메인프레임 오픈
    }

}
