package cabinet;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class FlightReservationSystem extends JFrame {
    private Map<String, String> users; // 사용자 정보를 저장할 맵 (아이디, 비밀번호)

    public FlightReservationSystem() {
        users = new HashMap<>();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Flight Reservation System");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JButton loginButton = new JButton("로그인");
        loginButton.setBounds(150, 50, 100, 30);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        panel.add(loginButton);

        JButton registerButton = new JButton("회원가입");
        registerButton.setBounds(150, 100, 100, 30);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });
        panel.add(registerButton);

        JButton findIDButton = new JButton("ID 찾기");
        findIDButton.setBounds(150, 150, 100, 30);
        findIDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findID();
            }
        });
        panel.add(findIDButton);

        JButton findPasswordButton = new JButton("Password 찾기");
        findPasswordButton.setBounds(120, 200, 160, 30);
        findPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findPassword();
            }
        });
        panel.add(findPasswordButton);

        add(panel);
        setVisible(true);
    }

    private void login() {
        String userId = JOptionPane.showInputDialog("아이디를 입력하세요:");
        String password = JOptionPane.showInputDialog("비밀번호를 입력하세요:");

        if (users.containsKey(userId) && users.get(userId).equals(password)) {
            JOptionPane.showMessageDialog(this, "Login 성공!");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password!");
        }
    }

    private void register() {
        String username = JOptionPane.showInputDialog("Enter new username:");
        String password = JOptionPane.showInputDialog("Enter new password:");

        if (users.containsKey(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists!");
        } else {
            users.put(username, password);
            JOptionPane.showMessageDialog(this, "아이디 및 비밀번호가 올바르지 않습니다!");
        }
    }

    private void findID() {
        String email = JOptionPane.showInputDialog("Enter email address:");
        // 이메일을 기준으로 사용자의 아이디를 찾아서 보여줌
        // 여기에 해당 로직을 추가하세요
    }

    private void findPassword() {
        String username = JOptionPane.showInputDialog("Enter username:");
        String email = JOptionPane.showInputDialog("Enter email address:");
        // 아이디와 이메일을 기준으로 비밀번호 재설정 링크를 이메일로 보냄
        // 여기에 해당 로직을 추가하세요
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FlightReservationSystem();
            }
        });
    }
}