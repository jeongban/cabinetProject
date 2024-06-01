package cabinet;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginView extends JFrame{

    private Main main;//main클래스와 연동하기 위한 변수
    private JButton btnLogin;//로그인 버튼
    private JButton btnInit;//초기화 버튼
    private JPasswordField passText;//비밀번호 입력 필드
    private JTextField userText;//사용자 이름 입력 필드
    private boolean bLoginCheck;//로그인 성공 여부를 저장하는 변수
    private JLabel label;//라벨

    public static void main(String[] args) {//?
        //new LoginView();
    }

    public LoginView() {
        // setting
        setTitle("StudyRoom");//창 제목은 studyroom
        setDefaultCloseOperation(EXIT_ON_CLOSE);//닫기
        this.setResizable(true);//창 크기 조절 가능
        this.setVisible(true);//화면 보이게 설정
        setIconImage(Toolkit.getDefaultToolkit().getImage("D:\\images\\Book.jpg"));//창 아이콘 설정

        // panel
        JPanel panel = new JPanel();// jpanel객체 생성
        placeLoginPanel(panel);//로그인 화면과 기능을 가진 panel을 설정한다


        //initGUI
        this.setSize(1100,700);
        this.setLocationRelativeTo(null);

        //배경이미지
        label = new JLabel();
        label.setIcon(new ImageIcon("D:\\images\\study6.jpg"));
        label.setBounds(-16, -19, 1100, 700);
        panel.add(label);

        // add
        getContentPane().add(panel);

        // visiible
        setVisible(true);
    }

    public void placeLoginPanel(JPanel panel){//panel을 받는다
        panel.setLayout(null);//레이아웃 없음
        JLabel userLabel = new JLabel("User");//user라는 텍스트가 담긴 라벨 생성
        userLabel.setBounds(427, 229, 80, 25);//위치 크기 지정
        panel.add(userLabel);//panel에 userlabel 추가

        JLabel passLabel = new JLabel("PassWord");//password라는 텍스트가 담긴 라벨 생성
        passLabel.setBounds(427, 259, 80, 25);//위치와 크기 지정
        panel.add(passLabel);//panel에 passlabel 추가

        userText = new JTextField(20);//20글자를 담을 user 텍스트 필드 생성
        userText.setBounds(517, 229, 160, 25);//위치 크기 설정
        panel.add(userText);//panel에 usertext 추가

        passText = new JPasswordField(20);//20글자를 담고 password기능을 가진 password필드 추가
        passText.setBounds(517, 259, 160, 25);//크기 위치 지정
        panel.add(passText);//panel에 passtext 추가
        passText.addActionListener(new ActionListener() {//passtext에서 동작에 반응하는 addActionListener추가
            @Override
            public void actionPerformed(ActionEvent e) {//동작을 하는 actionPerformed메소드 설정
                isLoginCheck();
            }
        });

        btnInit = new JButton("Reset");//reset이 적힌 버튼을 생성
        btnInit.setBackground(Color.PINK);//버튼 배경색은 분홍색
        btnInit.setBounds(427, 299, 121, 25);//위치와 크기 지정
        panel.add(btnInit);//panel에 버튼 추가
        btnInit.addActionListener(new ActionListener() {//btnInit에서 동작에 반응하는 addActionListener 추가
            @Override
            public void actionPerformed(ActionEvent e) {//동작을 감지해서 actionPerformed 메소드 실행
                userText.setText("");//usertext를 빈 문자열로
                passText.setText("");//passtext를 빈 문자열로
            }
        });

        btnLogin = new JButton("Login");//로그인이 적힌 버튼 생성
        btnLogin.setBackground(Color.PINK);//버튼 배경색은 분홍색
        btnLogin.setBounds(560, 299, 117, 25);//위치와 크기 설정
        panel.add(btnLogin);//panel에 btnlogin추가
        btnLogin.addActionListener(new ActionListener() {//btnlogin에서 동작에 반응하는 addActionListener 추가
            @Override
            public void actionPerformed(ActionEvent e) {//동작을 감지해서 actionPerformed 메소드 실행
                isLoginCheck();//로그인 성공 유무 결정
            }
        });
    }
    public void isLoginCheck(){
        if(userText.getText().equals("") && new String(passText.getPassword()).equals("")){//usertext가 studyroom과 같고, passtext에서 받은 비밀번호를 문자열로 변환후 1234와 같으면
            JOptionPane.showMessageDialog(null, "로그인 성공");//로그인 성공창이 화면가운데에 생성
            bLoginCheck = true;// 로그인 체크를 true로 변경

            // 로그인 성공이라면 매니져창 뛰우기
            if(isLogin()){//로그인 여부 확인을 한다
                main.showFrame_CS(); // 설정된 로그인 창을 띄운다
            }
        }else{
            JOptionPane.showMessageDialog(null, "로그인 실패");//실패하면 로그인 실패를 생성
        }
    }
    // mainProcess와 연동
    public void setMain(Main main) {//설정된 로그인 창을 받아서 Loginview의 메인에 대입
        this.main = main;
    }

    public boolean isLogin() {//로그인을 확인하고 불리언 리턴을 줄 islogin 메소드
        return bLoginCheck;//로그인에 성공하면 true 리턴
    }
}
