package cabinet;

public class Main {
    LoginView loginView; //로그인을 하는 인스턴트 변수 선언
    Frame_CS cs;//인스턴트 변수 선언

    public static void main(String[] args) {

        // 메인클래스 실행
        Main main = new Main();//메인 클래스의 인스턴스 생성
        main.loginView = new LoginView(); // 설정한 로그인창을 main클래스의 loginview에 대입한다
        main.loginView.setMain(main); // 로그인창에게 메인 클래스보내기
    }

    // 메인프레임창
    public void showFrame_CS(){
        loginView.dispose(); // 로그인창닫기
        this.cs = new Frame_CS(); // 메인프레임 오픈
    }

}
