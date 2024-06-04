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

    private Main main;
    private JButton btnLogin;
    private JButton btnInit;
    private JPasswordField passText;
    private JTextField userText;
    private boolean bLoginCheck;
    private JLabel label;

    public static void main(String[] args) {
        //new LoginView();
    }

    public LoginView() {
        // setting
        setTitle("Admin Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setVisible(true);
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\project\\src\\images\\3.jpg"));

        // panel
        JPanel panel = new JPanel();
        placeLoginPanel(panel);


        //initGUI
        this.setSize(1000,700);
        this.setLocationRelativeTo(null);

        //배경이미지
        label = new JLabel();
        label.setIcon(new ImageIcon("C:\\project\\src\\images\\3.jpg"));
        label.setBounds(-16, -19, 1000, 700);
        panel.add(label);

        // add
        getContentPane().add(panel);

        // visiible
        setVisible(true);
    }

    public void placeLoginPanel(JPanel panel){
        panel.setLayout(null);
        JLabel userLabel = new JLabel("아이디");
        userLabel.setForeground(Color.black);
        userLabel.setBounds(367, 259, 80, 25);
        panel.add(userLabel);

        JLabel passLabel = new JLabel("비밀번호");
        passLabel.setForeground(Color.black);
        passLabel.setBounds(367, 289, 80, 25);
        panel.add(passLabel);

        userText = new JTextField(20);
        userText.setBounds(447, 259, 160, 25);
        panel.add(userText);

        passText = new JPasswordField(20);
        passText.setBounds(447, 289, 160, 25);
        panel.add(passText);
        passText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isLoginCheck();
            }
        });

        btnInit = new JButton("Reset");
        btnInit.setBackground(Color.GRAY);
        btnInit.setForeground(Color.black);
        btnInit.setBounds(357, 329, 121, 25);
        panel.add(btnInit);
        btnInit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userText.setText("");
                passText.setText("");
            }
        });

        btnLogin = new JButton("Login");
        btnLogin.setBackground(Color.GRAY);
        btnLogin.setForeground(Color.black);
        btnLogin.setBounds(490, 329, 117, 25);
        panel.add(btnLogin);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isLoginCheck();
            }
        });
    }
    public void isLoginCheck(){
        if(userText.getText().equals("") && new String(passText.getPassword()).equals("")){
            JOptionPane.showMessageDialog(null, "로그인 성공");
            bLoginCheck = true;

            // 로그인 성공이라면 매니져창 뛰우기
            if(isLogin()){
                main.showFrame_CS(); // 메인창 메소드를 이용해 창뛰우기
            }
        }else{
            JOptionPane.showMessageDialog(null, "로그인 실패");
        }
    }
    // mainProcess와 연동
    public void setMain(Main main) {
        this.main = main;
    }

    public boolean isLogin() {
        return bLoginCheck;
    }
}
