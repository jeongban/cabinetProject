package cabinet;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

//import db.Member.Menu;
//import db.Flight.SeatDAO;
//import db.Flight.SeatDTO;
//import db.Flight.Frame_C;

public class Frame_CS extends JFrame {


    JTabbedPane jtp = new JTabbedPane();

    JPanel contentPane2 = new JPanel();


    private final JLabel lblNewLabel = new JLabel("위치");
    private final JTextField tfRno = new JTextField();
    private final JLabel label = new JLabel("락커번호");
    private final JTextField tfSno = new JTextField();
    private final JLabel label_1 = new JLabel("사용유무");
    private final JTextField tfState = new JTextField();
    private final JLabel label_2 = new JLabel("아이디");
    private final JTextField tfId = new JTextField();
    private final JLabel label_3 = new JLabel("남은 기간");
    private final JTextField tfPeriod = new JTextField();

    private final JScrollPane scrollPane = new JScrollPane();
    private final JTable table = new JTable();

    private final JButton btAdd = new JButton("추가");
    private final JButton btFind = new JButton("조회");
    private final JButton btAll = new JButton("전체보기");
    private final JButton btDel = new JButton("삭제");
    private final JButton btCancel = new JButton("취소");

    private final JLabel lblRoom = new JLabel("1F");
    //private final JLabel lblRoom_1 = new JLabel("ROOM 420");
    private final JPanel panel_1 = new JPanel();
    private final JPanel panel_2 = new JPanel();
    private final JPanel panel_3 = new JPanel();
    private final JPanel panel_4 = new JPanel();
    private final JPanel panel_5 = new JPanel();
    private final JPanel panel_6 = new JPanel();
    private final JPanel panel_7 = new JPanel();
    private final JPanel panel_8 = new JPanel();

    private final JButton[] buttons_410_1 = new JButton[10];
    private final JButton[] buttons_410_2 = new JButton[10];
    private final JButton[] buttons_410_3 = new JButton[10];
    private final JButton[] buttons_410_4 = new JButton[10];
    //room420
    private final JButton[] buttons_420 = new JButton[10];


    SeatDTO dto=new SeatDTO();
    SeatDAO dao=new SeatDAO();
    DefaultTableModel model
            =new DefaultTableModel();

    public static final int NONE=0;
    public static final int ADD=1;
    public static final int DEL=2;
    public static final int FIND=3;
    public static final int ALL=4;

    int cmd=NONE;
    private final JLabel lblBG = new JLabel("");



    public Frame_CS() {
        Frame_C c= new Frame_C();

        jtp.addTab("회원정보", Frame_C.contentPane1);
        jtp.addTab("락커정보", contentPane2);
        getContentPane().add(jtp);



        start();

        try {
            (dao).dbConnect();
        } catch (Exception e) {
            System.out.println("DB연결 실패"+e.getMessage());
        }//db와 커넥션
        //////////////////
        model.addColumn("위치");
        model.addColumn("락커번호");
        model.addColumn("사용유무");
        model.addColumn("아이디");
        model.addColumn("남은기간");

        //model을 view와 연결---------
        table.setModel(model);
        table.getTableHeader().setBackground(
                Color.PINK);

        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(20);
        initialTf();

//		setVisible(true);

        //윈도우 창 가운데 생성
        int xpos, ypos;
        Dimension dimen1, dimen2;
        this.setResizable(true);
        dimen1 = Toolkit.getDefaultToolkit().getScreenSize(); //화면해상도 불러오기
        dimen2 = this.getSize();  //프레임의크기
        xpos = (int)(dimen1.getWidth()/2-dimen2.getWidth()/2);
        ypos = (int)(dimen1.getHeight()/2-dimen2.getHeight()/2);
        this.setLocation(xpos,ypos);
        this.setTitle("Admin login");
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\project\\src\\images\\3.jpg"));
        this.setVisible(true);

        initGUI();


    }
    private void start() {
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                dao.close();
                //db와 연결된 자원 반납
                System.exit(0);
            }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                System.out.println("mousePressed()");
                int row=table.getSelectedRow();
                setTitle(row+"행");
                for(int i=0;i<4;i++){
                    Object obj
                            =table.getValueAt(row, i);
                    String objStr=obj.toString();
                    switch(i){
                        case 0:
                            tfRno.setText(objStr);
                            break;
                        case 1:
                            tfSno.setText(objStr);
                            break;
                        case 2:
                            tfState.setText(objStr);
                            break;
                        case 3:
                            tfId.setText(objStr);
                            break;
                        case 4:
                            tfPeriod.setText(objStr);
                            break;

                    }//switch---------

                }//for----------
            }
        });

        btAdd.setBackground(Color.pink);
        btAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Add actionPerformed()");
                if(cmd!=ADD){  //모든 메뉴중에 ADD를 마우스로 클릭했을때
                    setEnabled(ADD);
                    tfSno.requestFocus();//커서

                }else{
                    add();
                    setEnabled(NONE);
                    cmd=NONE;
                    initialTf();
                    clearTf(); //입력하는 칸을 빈칸으로 만들어줌
                }
            }
        });
        btFind.setBackground(Color.pink);
        btFind.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Find actionPerformed()");
                if(cmd!=FIND){
                    setEnabled(FIND);
                    tfId.requestFocus();
                }else{
                    showData(FIND);
                    cmd=NONE;
                    setEnabled(cmd);
                    initialTf();
                    clearTf();

                }
            }
        });
        btAll.setBackground(Color.pink);
        btAll.setText("전체보기");
        btAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("All actionPerformed()");
                cmd=ALL;
                setEnabled(cmd);
                initialTf();
                showData(ALL);
            }
        });
        btDel.setBackground(Color.pink);
        btDel.setText("삭제");
        btDel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Delete actionPerformed()");
                if(cmd!=DEL){
                    setEnabled(DEL);
                    tfSno.requestFocus(); //커서
                }else{
                    delete();//id로 삭제
                    setEnabled(NONE);
                    cmd=NONE;
                    initialTf();
                    clearTf();
                }
            }
        });
        btCancel.setBackground(Color.pink);
        btCancel.setText("취소");
        btCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("actionPerformed()");
                cmd=NONE;
                setEnabled(cmd);
                initialTf();
            }
        });

    }
    private void initGUI() {                           //jpanel

        contentPane2.setLayout(null);

        this.setResizable(true); //frame 크기 임의 설정(false)시 불가
        this.setSize(1000,700); //frame 사이즈 설정 method
        this.setLocationRelativeTo(null);// 창 가운데 생성코드


        this.lblNewLabel.setBounds(140, 5, 100, 28);
        lblNewLabel.setForeground(Color.PINK);
        contentPane2.add(this.lblNewLabel);
        this.tfRno.setBounds(140, 33, 128, 28);
        this.tfRno.setColumns(10);
        contentPane2.add(this.tfRno);               //Rno

        this.label.setBounds(290, 5, 100, 28);
        label.setForeground(Color.PINK);
        contentPane2.add(this.label);
        this.tfSno.setColumns(10);
        this.tfSno.setBounds(290, 33, 128, 28);
        contentPane2.add(this.tfSno);                //Sno

        this.label_1.setBounds(440, 5, 73, 28);
        label_1.setForeground(Color.PINK);
        contentPane2.add(this.label_1);
        this.tfState.setColumns(10);
        this.tfState.setBounds(440, 33, 128, 28);
        contentPane2.add(this.tfState);              //setState

        this.label_2.setBounds(590, 5, 73, 28);
        label_2.setForeground(Color.PINK);
        contentPane2.add(this.label_2);
        this.tfId.setColumns(10);
        this.tfId.setBounds(590, 33, 128, 28);
        contentPane2.add(this.tfId);              //Id

        this.label_3.setBounds(740, 5, 73, 28);
        label_3.setForeground(Color.PINK);
        contentPane2.add(this.label_3);
        this.tfPeriod.setColumns(10);
        this.tfPeriod.setBounds(740, 33, 128, 28);
        contentPane2.add(this.tfPeriod);                //Period



        //안쪽 자료 나오는 결과 테이블
        this.scrollPane.setBounds(239, 70, 500, 226);
        contentPane2.add(this.scrollPane);

        this.scrollPane.setViewportView(this.table);

        this.btAdd.setBounds(738, 70, 85, 30);
        contentPane2.add(this.btAdd);

        this.btFind.setBounds(738, 103, 85, 30);
        contentPane2.add(this.btFind);

        this.btAll.setBounds(738, 136, 85, 30);
        contentPane2.add(this.btAll);

        this.btDel.setBounds(738, 169, 85, 30);
        contentPane2.add(this.btDel);

        this.btCancel.setBounds(738, 202, 85, 30
        );
        contentPane2.add(this.btCancel);

        //410글자필드라벨
        lblRoom.setBounds(239, 312, 73, 28);
        contentPane2.add(lblRoom);
        //420글자필드라벨
        //lblRoom_1.setBounds(774, 312, 73, 28);
        //contentPane2.add(lblRoom_1);


        //room 410

        //1~10번째 좌석묶음

        panel_2.setBounds(239, 341, 95, 242);
        contentPane2.add(panel_2);
        panel_2.setLayout(new GridLayout(5, 2, 2, 2));
        for (int i = 0; i < buttons_410_1.length; i++) {
            buttons_410_1[i] = new JButton();
            buttons_410_1[i].setText(""+(i+1));
            panel_2.add(buttons_410_1[i]);
            buttons_410_1[i].setBackground(Color.PINK);
            buttons_410_1[i].setFont(new Font("굴림", Font.BOLD, 9));
        }

        panel_3.setBounds(334, 341, 40, 242);
        contentPane2.add(panel_3);
        //11~20번째 좌석묶음

        panel_4.setBounds(374, 341, 95, 242);
        contentPane2.add(panel_4);
        panel_4.setLayout(new GridLayout(5, 2, 2, 2));
        for (int i = 0; i < buttons_410_2.length; i++) {
            buttons_410_2[i] = new JButton();
            buttons_410_2[i].setText(""+(i+11));
            panel_4.add(buttons_410_2[i]);
            buttons_410_2[i].setBackground(Color.PINK);
            buttons_410_2[i].setFont(new Font("굴림", Font.BOLD, 9));
        }

        panel_5.setBounds(469, 341, 40, 242);
        contentPane2.add(panel_5);
        //21~30번째 좌석묶음

        panel_6.setBounds(509, 341, 95, 242);
        contentPane2.add(panel_6);
        panel_6.setLayout(new GridLayout(5, 2, 2, 2));
        for (int i = 0; i < buttons_410_3.length; i++) {
            buttons_410_3[i] = new JButton();
            buttons_410_3[i].setText(""+(i+21));
            panel_6.add(buttons_410_3[i]);
            buttons_410_3[i].setBackground(Color.PINK);
            buttons_410_3[i].setFont(new Font("굴림", Font.BOLD, 9));
        }

        panel_7.setBounds(604, 341, 40, 242);
        contentPane2.add(panel_7);
        //31~40번째 좌석묶음

        panel_8.setBounds(644, 341, 95, 242);
        contentPane2.add(panel_8);
        panel_8.setLayout(new GridLayout(5, 2, 2, 2));
        for (int i = 0; i < buttons_410_4.length; i++) {
            buttons_410_4[i] = new JButton();
            buttons_410_4[i].setText(""+(i+31));
            panel_8.add(buttons_410_4[i]);
            buttons_410_4[i].setBackground(Color.PINK);
            buttons_410_4[i].setFont(new Font("굴림", Font.BOLD, 9));
        }
        //room420
        /*
        contentPane2.add(panel_1);
        panel_1.setBackground(SystemColor.controlHighlight);
        panel_1.setBounds(774, 341, 253, 242);
        panel_1.setLayout(new GridLayout(2, 5, 2, 90));
        for (int i = 0; i < buttons_420.length; i++) {
            buttons_420[i] = new JButton();
            buttons_420[i].setText(""+(i+41));
            panel_1.add(buttons_420[i]);
            buttons_420[i].setBackground(Color.PINK);
        }
        */

        lblBG.setIcon(new ImageIcon("C:\\project\\src\\images\\3.jpg"));
        lblBG.setBounds(0, -46, 1100, 700);
        contentPane2.add(lblBG);
    }

    public void changeSeatColor(){

        SeatDTO[] Sdao = null;
        Sdao = dao.selectAll();
        String[][] data = new String[Sdao.length][5];
        for(int i=0; i< data.length; i++) {
            data[i][0] = Sdao[i].getRno();  //+"" 로 문자열로 만들어줌
            data[i][1] = Sdao[i].getSno();
            data[i][2] = Sdao[i].getState();
            data[i][3] = Sdao[i].getId();
            data[i][4] = Sdao[i].getPeriod()+"";
        }

        for(int i=0; i< data.length; i++) {
            int Sno = Integer.parseInt(data[i][1]);
            int period = Integer.parseInt(data[i][4]);

            if(1 <=Sno && Sno <= 10) {
                buttons_410_1[Sno-1].setBackground(Color.pink);
                if(period>=6) { buttons_410_1[Sno-1].setBackground(Color.cyan);}
                else if (period >0 && period<6) {
                    buttons_410_1[Sno-1].setBackground(Color.red);
                }
            }else if(11 <=Sno && Sno <= 20){
                buttons_410_2[Sno-11].setBackground(Color.pink);
                if(period>=6) { buttons_410_2[Sno-11].setBackground(Color.cyan);}
                else if (period >0 && period<6) {
                    buttons_410_2[Sno-11].setBackground(Color.red);
                }
            }else if(21 <=Sno && Sno <= 30) {
                buttons_410_3[Sno-21].setBackground(Color.pink);
                if(period>=6) { buttons_410_3[Sno-21].setBackground(Color.cyan);}
                else if (period >0 && period<6) {
                    buttons_410_3[Sno-21].setBackground(Color.red);
                }
            }else if(31 <=Sno && Sno <= 40) {
                buttons_410_4[Sno-31].setBackground(Color.pink);
                if(period>=6) { buttons_410_4[Sno-31].setBackground(Color.cyan);}
                else if (period >0 && period<6) {
                    buttons_410_4[Sno-41].setBackground(Color.red);
                }
            }
                /*else if(41 <=Sno &&Sno <= 50) {
                    buttons_420[Sno-41].setBackground(Color.CYAN);
                    if(period<6) { buttons_420[Sno-41].setBackground(Color.RED);}
                }
                */
            else{

                buttons_410_1[Sno-1].setBackground(Color.PINK);
                buttons_410_2[Sno-11].setBackground(Color.PINK);
                buttons_410_3[Sno-21].setBackground(Color.PINK);
                buttons_410_4[Sno-31].setBackground(Color.PINK);
                //buttons_420[Sno-41].setBackground(Color.PINK);
            }

        }//for
    }

    public void initialTf(){
        //tf들을 비활성화
        boolean b=false;
        tfRno.setEditable(b);
        tfSno.setEditable(b);
        tfState.setEditable(b);
        tfId.setEditable(b);
        tfPeriod.setEditable(b);

    }//initialTf()--------
    /*tf의 편집 가능 여부를 결정하는 메소드*/
    public void setEditable(int n){
        boolean b=false;
        switch(n){
            case ADD:
                tfRno.setEditable(b);
                tfSno.setEditable(!b);
                tfId.setEditable(!b);
                tfPeriod.setEditable(!b);
                break;
            case FIND://좌석번호 검색
                tfId.setEditable(!b);
                break;
            case DEL:// 좌석번호로 삭제
                tfSno.setEditable(!b);

                break;
            case NONE:
            case ALL:
                initialTf();
                break;
        }

    }//setEditable()---------
    /*버튼의 활성화 여부를 결정하는 메소드*/
    public void setEnabled(int n){
        boolean b=false;
        this.intialBt(b);
        switch(n){
            case ADD:
                btAdd.setEnabled(!b);
                btCancel.setEnabled(!b);
                cmd=ADD;
                break;
            case DEL:
                btDel.setEnabled(!b);
                btCancel.setEnabled(!b);
                cmd=DEL;
                break;
            case FIND:
                btFind.setEnabled(!b);
                btCancel.setEnabled(!b);
                cmd=FIND;
                break;
            case ALL:
                btAll.setEnabled(!b);
                btCancel.setEnabled(!b);
                cmd=ALL;
                break;

            case NONE:
                this.intialBt(!b);//모든 버튼 활성화
                break;
        }
        this.setEditable(cmd);
        //tf의 활성화 여부 결정..

    }
    /*버튼 비활성화 메소드*/
    public void intialBt(boolean b){
        btAdd.setEnabled(b);
        btDel.setEnabled(b);
        btAll.setEnabled(b);
        btFind.setEnabled(b);
        btCancel.setEnabled(b);
    }
    /*tf를 비워주는 메소드*/
    public void clearTf(){
        tfRno.setText("");
        tfSno.setText("");
        tfState.setText("");
        tfId.setText("");
        tfPeriod.setText("");
    }

    public void add() {

        String msg = "";

        dto.setSno(tfSno.getText());
        dto.setId(tfId.getText());
        dto.setPeriod(tfPeriod.getText());

        //유효성체크
        if(dto.getId()==null||dto.getSno() ==null
                ||dto.getId().trim().equals("")
                ||dto.getSno().trim().equals("")){
            msg = "ID와 좌석번호를 입력하세요";
            JOptionPane.showMessageDialog(this, msg);
            return;
        }
        //중복성체크
        if(dao.duplicateCheck(dto)){
            msg = "중복된 좌석번호입니다.";
            JOptionPane.showMessageDialog(this, msg);
            return;
        }
        if(dao.duplicateSeatCheck(dto)) {
            msg = "사용좌석이 추가됩니다.";
            JOptionPane.showMessageDialog(this, msg);

        }



        int n = dao.updateSeatInfo(dto);
        if(n>0) { msg = "좌석지정 성공";
        }else { msg = "좌석지정 실패";
        }
        JOptionPane.showMessageDialog(this, msg); //성공 메세지 팝업창 생성
        showData(ALL); //창에 전체데이터 보여줌
    }

    public void showData(int n) {
        SeatDTO[] arr = null;  //자료가 몇개가 될 지 모르므로 null 설정
        if(n==ALL) { //모두보기
            arr=dao.selectAll();
        }else if(n==FIND) { //ID 검색
            String id = tfId.getText();
            arr = dao.selectById(id);
        }
        if(arr== null ) {  /*****|| arr.length == 0 넣으면 안됌!!!!!!(rs.next()때문)*******/
            JOptionPane.showMessageDialog(this, "현재 등록된 회원 없음.");
            return;
        }
        String[] colNames = {"방 번호", "락커번호", "사용유무", "아이디",
                "남은기간"};
        String[][] data = new String[arr.length][5];
        //insert, delet 등등으로 길이가 항상 변하므로 길이가 [arr.length]가 됨
        for(int i=0; i< data.length; i++) {
            data[i][0] = arr[i].getRno();  //+"" 로 문자열로 만들어줌
            data[i][1] = arr[i].getSno();
            data[i][2] = arr[i].getState();
            data[i][3] = arr[i].getId();
            data[i][4] = arr[i].getPeriod()+"";

        }
        model.setDataVector(data, colNames);
        table.setModel(model);

        changeSeatColor();
    }

    public void delete() {
        //삭제할 ID입력하지 않으면 경고창 생성    -> JOptionPane.showMessageDialog() 사용
        //정말 ??님의 정보를 삭제하시겠습니까? -> JOptionPane.showConfirmDialog() 사용

        String Sno = tfSno.getText(); //화면에 입력한 아이디값 가져오기
        String msg = "";

        //유효성체크
        if(Sno ==null||Sno.trim().equals("")){  //trim = 공백제거
            msg = "삭제할 좌석번호를 입력하세요";
            tfId.requestFocus();
            JOptionPane.showMessageDialog(this, msg);
            return; //여기서 끊어주기위해 반드시 입력해야함
        }
        msg = "정말 " + Sno + "좌석의 정보를 삭제하시겠습니까?";
        int yn = JOptionPane.showConfirmDialog(this, msg);
        // MessageDialog와 차이점 : 팝업 창에 예,아니오,취소 세가지 버튼이 추가됨
        if(yn == JOptionPane.YES_OPTION) {
            //삭제성공 혹은 삭제실패 확인
            int isDel = dao.deleteSeatInfo(Sno.trim()); //호출 후 삭제 성공 혹은 삭제 실패 메세지 생성
            if(isDel>0) {
                msg = "좌석정보 삭제성공";
            } else {
                msg = "좌석정보 삭제실패";}
            JOptionPane.showMessageDialog(this, msg); //회원삭제성공 메세지 팝업창 생성
            showData(ALL); //창에 회원삭제 후 전체데이터 보여줌
        }
    }
//public static void main(String[] args) {
//	Frame_CS cs = new Frame_CS();
//}

}
