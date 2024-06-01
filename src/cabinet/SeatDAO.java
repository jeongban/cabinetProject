package cabinet;

import javax.swing.text.html.parser.Parser;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

//import db.ConnectionTest;
//import db.Flight.SeatDTO;
//import util.DBClose;

public class SeatDAO {
    Connection con;//데이터베이스와의 연결을 해주는 객체
    Statement st;//정적 sql문을 실행하기 위한 객체
    PreparedStatement ps;//미리 컴파일 된 sql문을 실행하기 위한 객체
    ResultSet rs = null;//쿼리의 결과를 보유하는 객체


    public void dbConnect() throws SQLException{//sql과 연결해주는 메소드

        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cabinet", "root", "1234");
        System.out.println("DB Connected...");//연결에 성공하면 출력
    }

    public int updateSeatInfo(SeatDTO dto) {//좌석의 정보를 받아서 좌석 설정을 수정하는 기능
        String sql = "UPDATE STUDYROOM_2 SET STATE = ?, "//STUDYROOM_2 테이블에서 STATE, ID, PERIOD, SNUMBER를 입력 받아 STATE, ID, PERIOD값을 변경한다.
                +"ID = ?,PERIOD = ? WHERE SNUMBER = ?";
        int result = 0;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1,"사용중");//state -> 사용중
            ps.setString(2, dto.getId());//id -> 입력한 아이디
            ps.setString(3, dto.getPeriod()+"");//PERIOD -> 입력한 문자열 period
            ps.setString(4, dto.getSno());//SNUMBER -> 입력한 SNUMBER
            result = ps.executeUpdate();//쿼리문 업데이트
        } catch (SQLException e) {
            e.printStackTrace(); // 예외 메시지 기록
            // 예외를 다시 throw하여 호출자에게 전달
            throw new RuntimeException("좌석 정보 업데이트 중 오류 발생", e);
        } finally {
            //DBClose.close(ps);
        }
        return result;
    }

    public boolean duplicateCheck(SeatDTO dto) {

        String sql = "SELECT STATE FROM STUDYROOM_2 WHERE SNUMBER=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getSno());
            rs = ps.executeQuery();
            Seat_Manager[] sm = makeArraySeat(rs);

            for(int i=0; i<sm.length; i++) {
                if(sm[i].getState() != null) {
                    System.out.println(sm[i].getState());
                    return true;
                }//if
            }//for
        } catch (Exception e) {
            System.out.println("Seat검색 실패:"+e.getMessage());
            return false;
        }
        return false;
    }

    public boolean duplicateSeatCheck(SeatDTO dto) {

        String sql = "SELECT id FROM STUDYROOM_2 WHERE id=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getId());
            rs = ps.executeQuery();
            boolean isExist = rs.next();
            if(isExist)
                return true;
        } catch (SQLException e) {
        }
        return false;
    }

    public SeatDTO[] selectAll() {  //all
        String sql = "SELECT * FROM STUDYROOM_2";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            SeatDTO[] arr = makeArray(rs);
            return arr;
        } catch (Exception e) {
            System.out.println("검색 실패"+e.getMessage());
            return null;
        }finally {
            try {
                if(rs != null) rs.close();
                if(ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public SeatDTO[] selectById(String Id) { //find
        String sql= "SELECT * FROM STUDYROOM_2 WHERE ID=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, Id);
            rs = ps.executeQuery();
            SeatDTO[] arr = makeArray(rs);
            return arr;
        } catch (Exception e) {
            System.out.println("검색 실패"+e.getMessage());
            return null;
        }finally {
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            } catch (SQLException e) {
            }
        }
    }

    public SeatDTO[] makeArray(ResultSet rs) throws SQLException {
        Vector<SeatDTO> list = new Vector<SeatDTO>();
        //list.copyInto(memArr);를 쓰기위해 Arraylist가 아니라 Vector를씀
        SeatDTO dto = null;
        while(rs.next()) {

            dto=new SeatDTO();
            dto.setRno(rs.getString("RNUMBER"));
            dto.setSno(rs.getString("SNUMBER"));
            dto.setState(rs.getString("STATE"));
            dto.setId(rs.getString("ID"));
            if(rs.getString("PERIOD") == null){
                dto.setPeriod(0);
            }
            else{
                dto.setPeriod(Integer.parseInt(rs.getString("PERIOD")));
            }

            list.add(dto);
        }
        SeatDTO[] memArr = new SeatDTO[list.size()]; //사이즈정해짐.
        list.copyInto(memArr);
        return memArr;
    }


    public Seat_Manager[] makeArraySeat(ResultSet rs) throws SQLException {
        Vector<Seat_Manager> list = new Vector<Seat_Manager>();
        Seat_Manager sm = null;
        while(rs.next()) {
            sm=new Seat_Manager();
            sm.setState(rs.getString("state"));
            list.add(sm);
        }
        Seat_Manager[] memArr = new Seat_Manager[list.size()];
        list.copyInto(memArr);
        return memArr;
    }

    public int deleteSeatInfo(String Sno) {  //delete

        String sql = "UPDATE STUDYROOM_2 SET STATE = null, "
                +"ID = null, PERIOD = null WHERE SNUMBER = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, Sno);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("쿼리오류"+e.getMessage());
            return -1;
        }finally {
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            } catch (SQLException e) {}
        }
        return 1;
    }

    public void close() {
        try {
            if(con !=null)
                con.close();
        } catch (SQLException e) {
            System.out.println("닫기실패");}
    }
}
