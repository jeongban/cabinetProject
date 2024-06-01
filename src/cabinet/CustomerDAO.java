package cabinet;

import java.beans.Statement;
import java.sql.*;
import java.util.Arrays;
import java.util.Vector;

//import db.ConnectionTest;
//import db.Flight.CustomerDTO;
//import util.DBClose;

public class CustomerDAO {
    Connection con;//db와 연결해주는 객체
    Statement st;//정적 sql문을 실행시켜주는 객체
    PreparedStatement ps;//미리 컴파일된 sql문을 실행시켜주는 객체
    ResultSet rs = null;//쿼리문의 결과를 보유하는 객체


    public void dbConnect() throws SQLException{//sql과 연결해주는 메소드
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cabinet", "root", "1234");
        System.out.println("DB Connected...");//연결 성공하면 출력
    }


    public int insertMember(CustomerDTO dto) {//회원을 추가하는 메소드

        String sql = "INSERT INTO STUDYROOM_1 VALUES(null, ?,?,?,?,?,?,?,?)";//null자리는 자동증가되는 기본키 필드이고, ? 자리는 입력값을 받을 자리 표시자

        int result = 0;
        try {
            ps = con.prepareStatement(sql);//sql문을 db에 연결
            ps.setString(1, dto.getSno());//좌석번호
            ps.setString(2, dto.getId());//아이디
            ps.setString(3, dto.getName());//이름
            ps.setString(4, dto.getPhone());//전화번호
            ps.setString(5, dto.getRegD());//등록일
            ps.setString(6, dto.getExD());//만료일
            ps.setInt(7, dto.getPeriod());//남은 기간;
            ps.setString(8, dto.getWarning());//만료임박
            result = ps.executeUpdate();//sql쿼리를 실행하고 영향을 받은 행의 수를 result에 저장

        } catch (Exception e) {
        } finally {
            //DBClose.close(ps);
        }

        return result;//영향을 받은 행의 수를 리턴
    }

    public boolean duplicateCheck(CustomerDTO dto) {//customer의 데이터를 받아온다

        String sql = "SELECT id FROM STUDYROOM_1 WHERE id=?";//아이디를 입력받아 STUDYROOM_1에서 id를 표시한다

        try {

            ps = con.prepareStatement(sql);//sql문을 실행시킬 준비를 한다.
            ps.setString(1, dto.getId());//customer id데이터를 쿼리의 입력값으로 설정한다
            rs = ps.executeQuery();//업데이트한 쿼리문을 rs에 저장한다.
            boolean isExist = rs.next();     /*rs의 위치에 있는지 없는지*/
            if(isExist)//같은 id가 있으면
                return true;//true리턴
        } catch (SQLException e) {
        }
        return false;//아무것도 없으면 false리턴
    }

    public int updateMember(CustomerDTO dto) {

        String sql = "UPDATE STUDYROOM_1 SET REG_DATE = ?, EX_DATE=?, period = ? WHERE id=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getRegD());
            ps.setString(2, dto.getExD());
            ps.setInt(3,dto.getPeriod());
            ps.setString(4, dto.getId());
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


    public Customer_DateManager[] dateManager() {

        String sql2 = "SELECT NO, PERIOD FROM STUDYROOM_1";//STUDYROOM_1에서 no와 period 찾기

        try {
            ps = con.prepareStatement(sql2);//sql문을 실행시킬 준비
            rs = ps.executeQuery();//쿼리문을 실행
            System.out.println(rs.getRow());
            Customer_DateManager[] cdm = makeArrayDate(rs);//데이터 크기만큼 배열 생성
            return cdm;
        } catch (Exception e) {
            System.out.println("검색 실패:"+e.getMessage());
            return null;
        }

    }
    public void setWarning() {

        Customer_DateManager[] cdm = null;
        cdm = dateManager();//회원 번호와 기간이 담긴 배열을 저장
        int [][] data = new int[cdm.length][2];//data에 cdm배열의 크기와 no와 period데이터를 담을 2차원 배열 생성
        for(int i=0; i< data.length; i++) {//data크기만큼 반복
            data[i][0] = cdm[i].getNo();//cdm[i] 번째 no를 data에 저장
            data[i][1] = cdm[i].getPeriod();//cdm[i] 번째 period를 data에 저장
        }

        for(int i=0; i<data.length; i++) {//data크기만큼 반복
            if(data[i][1] < 6) {//
                String sql3 = "UPDATE studyroom_1 set WARNING= '만료임박' WHERE NO = ?";
                try {
                    ps = con.prepareStatement(sql3);
                    ps.setInt(1, data[i][0]);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    System.out.println("만료임박 오류"+e.getMessage());
                }

            }//if
        }//for

    }

    public CustomerDTO[] selectAll() {
        String sql = "SELECT * FROM STUDYROOM_1";
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            CustomerDTO[] arr = makeArray(rs);
            return arr;
        } catch (Exception e) {
            System.out.println("검색 실패:"+e.getMessage());
            return null;
        }finally {
            //DBClose.close(rs);
            //DBClose.close(ps);
        }
    }

    public CustomerDTO[] selectByName(String name) {
        // 검색은 부분검색으로 like로 조건검색해야 사용성 측면에서 좋음 그리고 모든 검색창은 부분검색으로도 검색이 가능하게
        String sql= "SELECT * FROM STUDYROOM_1 WHERE name LIKE ?";
        name = STR."%\{name}%";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, name);
            rs = ps.executeQuery();
            CustomerDTO[] arr = makeArray(rs);
            return arr;
        } catch (Exception e) {
            System.out.println("검색 실패:"+e.getMessage());
            return null;
        }finally {
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            } catch (SQLException e) {
            }
        }
    }

    public CustomerDTO[] makeArray(ResultSet rs) throws SQLException {
        Vector<CustomerDTO> list = new Vector<CustomerDTO>();
        CustomerDTO dto = null;
        while(rs.next()) {
            dto=new CustomerDTO();
            dto.setNo(rs.getInt("no"));
            dto.setSno(rs.getString("SNUMBER"));
            dto.setId(rs.getString("id"));
            dto.setName(rs.getString("name"));
            dto.setPhone(rs.getString("phone"));
            dto.setRegD(rs.getString("reg_date"));
            dto.setExD(rs.getString("ex_date"));
            dto.setPeriod(rs.getInt("period"));
            dto.setWarning(rs.getString("warning"));
            list.add(dto);
        }
        CustomerDTO[] memArr = new CustomerDTO[list.size()]; //사이즈정해짐.
        list.copyInto(memArr);
        return memArr;
    }

    public Customer_DateManager[] makeArrayDate(ResultSet rs) throws SQLException {
        Vector<Customer_DateManager> list = new Vector<Customer_DateManager>();//회원 번호와 기간의 정보가담긴 객체를 배열로 선언한다
        while(rs.next()) {//저장된 다음 쿼리문이 있을 때 까지
            Customer_DateManager cdm =new Customer_DateManager();//cdm에 회원 번호와 기간의 데이터를 저장하는 객체 생성
            cdm.setNo(rs.getInt("no"));//저장된 쿼리문에서 컬럼이 no인 데이터를 cdm에 설정
            cdm.setPeriod(rs.getInt("period"));//저장된 쿼리문에서 컬럼이 period인 데이터를 cdm에 설정
            list.add(cdm);//list에 설정한 데이터를 삽입
        }
        //memarr에 no와 perido의 입력된 데이터 수 만큼의 배열을 가진 객체 생성
        Customer_DateManager[] memArr = new Customer_DateManager[list.size()];
        list.copyInto(memArr);//vector에 있는 자료의 크기만큼 배열을 지정
        return memArr;//memarr에 no와 perido의 입력된 데이터 수 만큼의 배열을 리턴
    }

    public int deleteMember(String id) {

        String sql = "DELETE FROM STUDYROOM_1 WHERE id = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Member 삭제오류"+e.getMessage());
            return -1;
        }finally {
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            } catch (SQLException e) {}
        }
        return 1;
    }



    public void deleteSeat(String id) {

        String sql = "UPDATE STUDYROOM_2 SET STATE = null, ID = null, PERIOD = null WHERE ID = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Seat 삭제오류"+e.getMessage());
        }finally {
            try {
                if(rs!=null) rs.close();
                if(ps!=null) ps.close();
            } catch (SQLException e) {}
        }

    }

    public void close() {
        try {
            if(con !=null)
                con.close();
        } catch (SQLException e) {
            System.out.println("닫기실패");}
    }
}
