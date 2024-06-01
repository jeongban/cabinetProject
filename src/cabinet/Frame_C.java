package cabinet;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.swing.*;
import javax.swing.table.*;

//import db.Member.Menu;
//import db.Flight.CustomerDAO;
//import db.Flight.CustomerDTO;
//import db.Flight.SeatDTO;

public class Frame_C extends JFrame {

    JTabbedPane jtp = new JTabbedPane();//탭 추가

    static JPanel contentPane1 = new JPanel();//customer의 정보를 관리하는 panel추가
    //글씨가 적힌 label과 텍스트를 적을 textfield추가
    private final JLabel lblNewLabel = new JLabel("NO");//
    private final JTextField tfNo = new JTextField();
    private final JLabel label = new JLabel("Sno");
    private final JTextField tfSno = new JTextField();
    private final JLabel label_1 = new JLabel("ID");
    private final JTextField tfId = new JTextField();
    private final JLabel label_2 = new JLabel("NAME");
    private final JTextField tfName = new JTextField();
    private final JLabel label_3 = new JLabel("phone");
    private final JTextField tfphone = new JTextField();
    private final JLabel label_4 = new JLabel("regD");
    private final JTextField tfregD = new JTextField();
    private final JLabel label_5 = new JLabel("exD");
    private final JTextField tfexD = new JTextField();
    private final JLabel label_6 = new JLabel("period");
    private final JTextField tfperiod = new JTextField();
    private final JLabel label_7 = new JLabel("warning");
    private final JTextField tfwarning = new JTextField();

    //////////////////////////////////////////////////////////
    //스크롤 기능 추가
    private final JScrollPane scrollPane = new JScrollPane();
    //테이블 표 추가
    private final JTable table = new JTable();
    //////////////////////////////////////////////////////////
    //버튼 추가
    private final JButton btAdd = new JButton("ADD");
    private final JButton btUpdate = new JButton("UPDATE");
    private final JButton btFind = new JButton("FIND");
    private final JButton btAll = new JButton("All");
    private final JButton btDel = new JButton("DELETE");
    private final JButton btCancel = new JButton("CANCEL");

    CustomerDTO dto = new CustomerDTO();//customer의 정보를 설정하는 객체 추가
    CustomerDAO dao = new CustomerDAO();//cutomer의 정보를 db랑 주고받는 객체 추가
    DefaultTableModel model
            = new DefaultTableModel();//테이블 데이터 관리를 하는 객체 추가
    //버튼을 컨트롤하는 변수선언
    public static final int NONE = 0;
    public static final int ADD = 1;
    public static final int DEL = 2;
    public static final int FIND = 3;
    public static final int ALL = 4;
    public static final int UPDATE = 5;

    int cmd = NONE;
    private final JLabel lblBG = new JLabel();//배경을 넣을 label추가

    public Frame_C() {

        jtp.addTab("Customer", contentPane1);//탭에 customer를 관리하는 panel을 추가

        start();

        try {
            dao.dbConnect();//db랑 연결을 주고받을 메소드 실행
        } catch (Exception e) {
            System.out.println("DB연결 실패" + e.getMessage());
        }//db와 커넥션
        //테이블 표 컬럼에 컬럼명 입력
        model.addColumn("회원번호");
        model.addColumn("락커번호");
        model.addColumn("아이디");
        model.addColumn("이  름");
        model.addColumn("전화번호");
        model.addColumn("등록일");
        model.addColumn("만료일");
        model.addColumn("남은기간");
        model.addColumn("만료임박");

        //model을 view와 연결---------
        table.setModel(model);//테이블에 model에서 입력받은 컬럼명을 표시한다
        table.getTableHeader().setBackground(
                Color.PINK);//테이블의 헤더 배경색을 분홍
        table.getTableHeader().setForeground(
                Color.DARK_GRAY);//테이블 헤더 글씨색을 다크그레이
        table.getTableHeader().setReorderingAllowed(false);//열의 재정렬을 불가능하게 설정
        table.setRowHeight(20);//행의 높이를 20px로 설정

        initialTf();//모든 텍스트 필드를 비활성화
        initGUI();//gui화면 표출

    }

    private void start() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dao.close();//메모리 누수를 방지하기 위해 닫기
                //db와 연결된 자원 반납
                System.exit(0);//로그인 창 지우기
            }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {//테이블 표에서 마우스를 감지하면
                System.out.println("mousePressed()");//출력
                int row = table.getSelectedRow();//테이블의 선택한 행을 row에 입력
                System.out.println("row : " + row + "행(");
                setTitle(row + "행");//?



                for (int i = 0; i < 8; i++) {//열의 개수만큼 반복
                    Object obj
                            = table.getValueAt(row, i);//테이블의 선택한 행의 i열을 obj에 저장(모든 자료형을 저장하기 위해 object 자료형으로 선언)
                    String objStr = obj.toString();//object 자료형을로 선언된 obj를 문자열 objStr로 형변환
                    switch (i) {//선택한 행의 i 열의 데이터를 반복문을 사용해서 순서대로 textfield에 저장한다
                        case 0:
                            tfNo.setText(objStr);
                            break;
                        case 1:
                            tfSno.setText(objStr);
                            break;
                        case 2:
                            tfId.setText(objStr);
                            break;
                        case 3:
                            tfName.setText(objStr);
                            break;
                        case 4:
                            tfphone.setText(objStr);
                            break;
                        case 5:
                            tfregD.setText(objStr);
                            break;
                        case 6:
                            tfexD.setText(objStr);
                            break;
                        case 7:
                            tfperiod.setText(objStr);
                            break;
                        case 8:
                            tfwarning.setText(objStr);
                            break;

                    }//switch---------

                }//for----------
            }
        });
        btAdd.setBackground(Color.PINK);//add버튼의 배경색을 분홍색으로 지정
        btAdd.addActionListener(new ActionListener() {//add버튼에 동작을 감지한다
            public void actionPerformed(ActionEvent e) {//동작이 감지되면
                System.out.println("Add actionPerformed()");//출력
                if (cmd != ADD) {  //모든 메뉴중에 ADD를 마우스로 클릭했을때 cmd = 0 add = 1
                    setEnabled(ADD);//버튼을 활성화 시키고 cmd -> 1로 치환
                    tfSno.requestFocus();//커서를 sno 텍스트 필드로 이동
                } else {//cmd = 1 add = 1
                    add();//회원정보를 추가
                    setEnabled(NONE);
                    cmd = NONE;
                    initialTf();
                    clearTf(); //입력하는 칸을 빈칸으로 만들어줌
                }
            }
        });
        btUpdate.setBackground(Color.PINK);
        btUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Update actionPerformed()");
                if (cmd != UPDATE) {  //모든 메뉴중에 UPDATE를 마우스로 클릭했을때
                    setEnabled(UPDATE);
                    tfId.requestFocus();//커서

                } else {
                    update();
                    setEnabled(NONE);
                    cmd = NONE;
                    initialTf();
                    clearTf(); //입력하는 칸을 빈칸으로 만들어줌
                }
            }
        });
        btFind.setBackground(Color.PINK);
        btFind.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Find actionPerformed()");
                if (cmd != FIND) {
                    setEnabled(FIND);
                    tfSno.requestFocus();
                } else {
                    showData(FIND);
                    cmd = NONE;
                    setEnabled(cmd);
                    initialTf();
                    clearTf();
                }
            }
        });
        btAll.setBackground(Color.PINK);
        btAll.setText("ALL");
        btAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("All actionPerformed()");
                cmd = ALL;
                setEnabled(cmd);
                initialTf();
                showData(ALL);
            }
        });
        btDel.setBackground(Color.PINK);
        btDel.setText("DELETE");
        btDel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Delete actionPerformed()");
                if (cmd != DEL) {
                    setEnabled(DEL);
                    tfId.requestFocus(); //커서
                } else {
                    delete();//id로 삭제
                    setEnabled(NONE);
                    cmd = NONE;
                    initialTf();
                    clearTf();
                }
            }
        });
        btCancel.setBackground(Color.PINK);
        btCancel.setText("CANCEL");
        btCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("actionPerformed()");
                cmd = NONE;
                setEnabled(cmd);
                initialTf();
            }
        });

    }

    private void initGUI() {                           //jpanel

        contentPane1.setLayout(null);

        this.setResizable(true); //frame 크기 임의 설정(false)시 불가
        this.setSize(1100, 700); //frame 사이즈 설정 method
        this.setLocationRelativeTo(null);// 창 가운데 생성코드


        //왼쪽 텍스트필드
        this.lblNewLabel.setBounds(12, 10, 73, 28);
        contentPane1.add(this.lblNewLabel);
        this.tfNo.setBounds(12, 38, 128, 28);
        this.tfNo.setColumns(10);
        contentPane1.add(this.tfNo);               //no

        this.label.setBounds(12, 66, 73, 28);
        contentPane1.add(this.label);
        this.tfSno.setColumns(10);
        this.tfSno.setBounds(12, 94, 128, 28);
        contentPane1.add(this.tfSno);                //Sno

        this.label_1.setBounds(12, 122, 73, 28);
        contentPane1.add(this.label_1);
        this.tfId.setColumns(10);
        this.tfId.setBounds(12, 150, 128, 28);
        contentPane1.add(this.tfId);              //Id

        this.label_2.setBounds(12, 178, 73, 28);
        contentPane1.add(this.label_2);
        this.tfName.setColumns(10);
        this.tfName.setBounds(12, 206, 128, 28);
        contentPane1.add(this.tfName);              //Name

        this.label_3.setBounds(12, 234, 73, 28);
        contentPane1.add(this.label_3);
        this.tfphone.setColumns(10);
        this.tfphone.setBounds(12, 262, 128, 28);
        contentPane1.add(this.tfphone);                //phone

        this.label_4.setBounds(12, 290, 73, 28);
        contentPane1.add(this.label_4);
        this.tfregD.setColumns(10);
        this.tfregD.setBounds(12, 318, 128, 28);
        contentPane1.add(this.tfregD);                 //regD

        this.label_5.setBounds(12, 346, 73, 28);
        contentPane1.add(this.label_5);
        this.tfexD.setColumns(10);
        this.tfexD.setBounds(12, 374, 128, 28);
        contentPane1.add(this.tfexD);              //exD

        this.label_6.setBounds(12, 402, 73, 28);
        contentPane1.add(this.label_6);
        this.tfperiod.setColumns(10);
        this.tfperiod.setBounds(12, 430, 128, 28);
        contentPane1.add(this.tfperiod);              //period

        this.label_7.setBounds(12, 458, 73, 28);
        contentPane1.add(this.label_7);
        this.tfwarning.setColumns(10);
        this.tfwarning.setBounds(12, 486, 128, 28);
        contentPane1.add(this.tfwarning);              //warning

        //안쪽 자료 나오는 결과 테이블
        this.scrollPane.setBounds(239, 66, 700, 226);
        contentPane1.add(this.scrollPane);

        this.scrollPane.setViewportView(this.table);

        this.btAdd.setBounds(239, 34, 85, 33);
        contentPane1.add(this.btAdd);

        this.btUpdate.setBounds(322, 34, 85, 33);
        contentPane1.add(this.btUpdate);

        this.btFind.setBounds(405, 34, 85, 33);
        contentPane1.add(this.btFind);

        this.btAll.setBounds(488, 34, 85, 33);
        contentPane1.add(this.btAll);

        this.btDel.setBounds(571, 34, 85, 33);
        contentPane1.add(this.btDel);

        this.btCancel.setBounds(654, 34, 85, 33);
        contentPane1.add(this.btCancel);

        lblBG.setBackground(Color.PINK);
        lblBG.setIcon(new ImageIcon("D:\\images\\study6.jpg"));
        lblBG.setBounds(0, -46, 1100, 700);
        contentPane1.add(lblBG);


    }

    /*tf들을 비활성화*/
    public void initialTf() {

        boolean b = false;
        tfNo.setEditable(b);
        tfSno.setEditable(b);
        tfId.setEditable(b);
        tfName.setEditable(b);
        tfphone.setEditable(b);
        tfregD.setEditable(b);
        tfexD.setEditable(b);
        tfperiod.setEditable(b);
        tfwarning.setEditable(b);

    }//initialTf()--------

    /*tf의 편집 가능 여부를 결정하는 메소드*/
    public void setEditable(int n) {

        boolean b = false;
        switch (n) {
            case ADD:
                tfId.setEditable(!b);
                tfSno.setEditable(!b);
                tfName.setEditable(!b);
                tfphone.setEditable(!b);
                tfregD.setEditable(!b);
                tfexD.setEditable(!b);
                tfwarning.setEditable(b);
                break;
            case UPDATE:
                tfId.setEditable(!b);
                tfregD.setEditable(!b);
                tfexD.setEditable(!b);
                break;
            case FIND://이름으로 검색
                tfName.setEditable(!b);
                break;
            case DEL:// 아이디로 삭제
                tfId.setEditable(!b);
                break;
            case NONE:
            case ALL:
                initialTf();
                clearTf();
                break;
        }

    }//setEditable()---------

    /**
     * 버튼의 활성화 여부를 결정하는 메소드
     */
    public void setEnabled(int n) {
        boolean b = false;
        this.intialBt(b);//모든 버튼을 비활성화
        switch (n) {//
            case ADD:
                btAdd.setEnabled(!b);//add버튼 활성화
                btCancel.setEnabled(!b);//cancel버튼 활성화
                cmd = ADD; //cmd = 1
                break;
            case UPDATE:
                btUpdate.setEnabled(!b);
                btCancel.setEnabled(!b);
                cmd = UPDATE;
                break;
            case DEL:
                btDel.setEnabled(!b);
                btCancel.setEnabled(!b);
                cmd = DEL;
                break;
            case FIND:
                btFind.setEnabled(!b);
                btCancel.setEnabled(!b);
                cmd = FIND;
                break;
            case ALL:
                btAll.setEnabled(!b);
                btCancel.setEnabled(!b);
                cmd = ALL;
                break;

            case NONE:
                this.intialBt(!b);//모든 버튼 활성화
                break;
        }
        this.setEditable(cmd);
        //tf의 활성화 여부 결정..

    }

    /**
     * 버튼 비활성화 메소드
     */
    public void intialBt(boolean b) {
        btAdd.setEnabled(b);
        btUpdate.setEnabled(b);
        btDel.setEnabled(b);
        btAll.setEnabled(b);
        btFind.setEnabled(b);
        btCancel.setEnabled(b);
    }

    /**
     * tf를 비워주는 메소드
     */
    public void clearTf() {
        tfNo.setText("");
        tfSno.setText("");
        tfId.setText("");
        tfName.setText("");
        tfphone.setText("");
        tfregD.setText("");
        tfexD.setText("");
        tfperiod.setText("");
        tfwarning.setText("");
    }

    public void add() {

        String msg = "";

        dto.setSno(tfSno.getText());//sno텍스트 필드에서 입력받은 데이터를 customer 객체의 sno에 설정
        dto.setId(tfId.getText());//id텍스트 필드에서 입력받은 데이터를 customer 객체의 id에 설정
        dto.setName(tfName.getText());//name텍스트 필드에서 입력받은 데이터를 customer 객체의 name에 설정
        dto.setPhone(tfphone.getText());//phone텍스트 필드에서 입력받은 데이터를 customer 객체의 phone에 설정
        dto.setRegD(tfregD.getText());//regD텍스트 필드에서 입력받은 데이터를 customer 객체의 regD에 설정
        dto.setExD(tfexD.getText());//exD텍스트 필드에서 입력받은 데이터를 cutomer 객체의 exD에 설정
        //tegd, exD텍스트 필드의 값을 calculateDateDifference 메소드를 사용해서 날짜의 차이를 period에 설정
        dto.setPeriod(calculateDateDifference(tfregD.getText() , tfexD.getText()));

        //유효성체크
        //아이디와 비밀번호의 데이터가 null이거나 공백이거나 아이디와 비밀번호에 공백이 존재하면
        if (dto.getId() == null || dto.getName() == null
                || dto.getId().trim().equals("")
                || dto.getName().trim().equals("")) {
            msg = "ID와 NAME값 입력하세요";
            JOptionPane.showMessageDialog(this, msg);
            return;//메소드를 종료시키고 호출한 곳으로 돌아간다
        }
        //중복성체크
        if (dao.duplicateCheck(dto)) {//중복이 있어 true를 받아오면
            msg = "중복된 ID입니다.";
            JOptionPane.showMessageDialog(this, msg);
            return;
        }

        int n = dao.insertMember(dto);//입력에 성공하면 1 실패하면 0
        if (n > 0) {
            msg = "회원가입성공";//입력받은 행의 수가 1이상이면 회원가입성공 메세지를 보냄
        } else {
            msg = "회원가입실패";//없을 시 회원가입 실패 메세지
        }
        JOptionPane.showMessageDialog(this, msg); //회원가입성공 메세지 팝업창 생성
        showData(ALL); //창에 회원가입 후 전체데이터 보여줌


    }

    public void update() {

        String msg = "";

        dto.setId(tfId.getText());
        dto.setRegD(tfregD.getText().replaceAll("-", ""));
        dto.setExD(tfexD.getText().replaceAll("-", ""));
        dto.setPeriod(calculateDateDifference(dto.getRegD() , dto.getExD()));

        int n = dao.updateMember(dto);
        if (n > 0) {
            msg = "회원정보 수정성공";
        } else {
            msg = "회원정보 수정실패";
        }
        JOptionPane.showMessageDialog(this, msg);
        showData(ALL);
    }

    public void showData(int n) {//all=4

        dao.setWarning(

        );

        CustomerDTO[] arr = null;//자료가 몇개가 될 지 모르므로 null 설정
        if (n == ALL) { //모두보기
            arr = dao.selectAll();
        } else if (n == FIND) { //이름 검색
            String name = tfName.getText();

            arr = dao.selectByName(name);
        }
        if (arr == null) {
            JOptionPane.showMessageDialog(this, "현재 등록된 회원 없음.");
            return;
        }
        String[] colNames = {"회원번호", "락커번호", "아이디", "이름", "전화번호",
                "등록일", "만료일", "남은기간", "만료임박"};
        String[][] data = new String[arr.length][9];
        //insert, delet 등등으로 길이가 항상 변하므로 길이가 [arr.length]가 됨

        for (int i = 0; i < data.length; i++) {
            data[i][0] = arr[i].getNo() + "";  //+"" 로 문자열로 만들어줌
            data[i][1] = arr[i].getSno();
            data[i][2] = arr[i].getId();
            data[i][3] = arr[i].getName();
            data[i][4] = arr[i].getPhone();
            data[i][5] = arr[i].getRegD();
            data[i][6] = arr[i].getExD();
            data[i][7] = arr[i].getPeriod() + "";
            data[i][8] = arr[i].getWarning();
        }
        model.setDataVector(data, colNames);
        table.setModel(model);


    }

    public void delete() {
        //삭제할 ID입력하지 않으면 경고창 생성    -> JOptionPane.showMessageDialog() 사용
        //정말 ??님의 정보를 삭제하시겠습니까? -> JOptionPane.showConfirmDialog() 사용

        String id = tfId.getText(); //화면에 입력한 아이디값 가져오기
        String msg = "";

        //유효성체크
        if (id == null || id.trim().equals("")) {  //trim = 공백제거
            msg = "삭제할 ID를 입력하세요";
            tfId.requestFocus();
            JOptionPane.showMessageDialog(this, msg);
            return; //여기서 끊어주기위해 반드시 입력해야함
        }
        msg = "정말 " + id + "님의 정보를 삭제하시겠습니까?";
        int yn = JOptionPane.showConfirmDialog(this, msg);
        // MessageDialog와 차이점 : 팝업 창에 예,아니오,취소 세가지 버튼이 추가됨
        if (yn == JOptionPane.YES_OPTION) {
            //삭제성공 혹은 삭제실패 확인
            int isDel = dao.deleteMember(id.trim()); //호출 후 삭제 성공 혹은 삭제 실패 메세지 생성
            if (isDel > 0) {
                msg = "회원삭제성공";
            } else {
                msg = "회원삭제실패";
            }
            JOptionPane.showMessageDialog(this, msg); //회원삭제성공 메세지 팝업창 생성
            showData(ALL); //창에 회원삭제 후 전체데이터 보여줌
            dao.deleteSeat(id);

        }
    }



    // 문자열을 LocalDate 형식으로 변환
    public LocalDate convertStringToLocalDate(String dateStr) {//문자열로된 yyyymmdd 형식의 날짜를 받아온다(입력값은 날짜 형식이여야함)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");//문자열을 yyyymmdd 날짜 형식으로 변환
        return LocalDate.parse(dateStr, formatter);//문자열을 local데이터 객체로 변환하고 변환된 날짜형식을 리턴한다
    }

    // 두 날짜의 차이를 계산
    public int calculateDateDifference(String startDateStr, String endDateStr) {//시작 날짜와 끝나는 날짜를 문자열로 받아온다
        LocalDate startDate = convertStringToLocalDate(startDateStr);//문자열로된 시작 날짜를 날짜형식으로 변환후 localdate 자료형으로 저장
        LocalDate endDate = convertStringToLocalDate(endDateStr);//문자열로된 끝나는 날짜를 날짜형식으로 변환후 localdate 자료형으로 저장
        return (int) ChronoUnit.DAYS.between(startDate, endDate);//시작날짜와 끝나는 날짜의 차이를 계산 후 정수형으로 형변환해서 리턴
    }
}
