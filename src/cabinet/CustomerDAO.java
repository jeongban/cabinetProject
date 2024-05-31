package cabinet;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

//import db.ConnectionTest;
//import db.Flight.CustomerDTO;
//import util.DBClose;

public class CustomerDAO {
    Connection con;
    Statement st;
    PreparedStatement ps;
    ResultSet rs = null;


    public void dbConnect() throws SQLException{
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cabinet", "root", "1234");
        System.out.println("DB Connected...");
    }


    public int insertMember(CustomerDTO dto) {

        String sql = "INSERT INTO STUDYROOM_1 VALUES(null, ?,?,?,?,?,?,?,?)";

        int result = 0;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getSno());
            ps.setString(2, dto.getId());
            ps.setString(3, dto.getName());
            ps.setString(4, dto.getPhone());
            ps.setString(5, dto.getRegD());
            ps.setString(6, dto.getExD());
            ps.setInt(7, dto.getPeriod());
            ps.setString(8, dto.getWarning());
            result = ps.executeUpdate();

        } catch (Exception e) {
        } finally {
            //DBClose.close(ps);
        }

        return result;
    }

    public boolean duplicateCheck(CustomerDTO dto) {

        String sql = "SELECT id FROM STUDYROOM_1 WHERE id=?";

        try {

            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getId());
            rs = ps.executeQuery();
            boolean isExist = rs.next();     /*rs의 위치에 있는지 없는지*/
            if(isExist)
                return true;
        } catch (SQLException e) {
        }
        return false;
    }

    public int updateMember(CustomerDTO dto) {

        String sql = "UPDATE STUDYROOM_1 SET REG_DATE = ?, EX_DATE=? WHERE id=?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, dto.getRegD());
            ps.setString(2, dto.getExD());
            ps.setString(3, dto.getId());
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

    public void setPeriod(CustomerDTO dto) {

        String sql = "UPDATE studyroom_1 set PERIOD = Round(EX_DATE-REG_DATE, 0)";
        try {
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("PERIOD계산 오류"+e.getMessage());
        }
    }

    public Customer_DateManager[] dateManager() {

        String sql2 = "SELECT NO, PERIOD FROM STUDYROOM_1";

        try {
            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            Customer_DateManager[] cdm = makeArrayDate(rs);
            return cdm;
        } catch (Exception e) {
            System.out.println("검색 실패:"+e.getMessage());
            return null;
        }

    }
    public void setWarning() {

        Customer_DateManager[] cdm = null;
        cdm = dateManager();
        int [][] data = new int[cdm.length][2];
        for(int i=0; i< data.length; i++) {
            data[i][0] = cdm[i].getNo();
            data[i][1] = cdm[i].getPeriod();
        }

        for(int i=0; i<data.length; i++) {
            if(data[i][1] < 6) {
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
        String sql= "SELECT * FROM STUDYROOM_1 WHERE name=?";

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
        Vector<Customer_DateManager> list = new Vector<Customer_DateManager>();
        Customer_DateManager cdm = null;
        while(rs.next()) {
            cdm=new Customer_DateManager();
            cdm.setNo(rs.getInt("no"));
            cdm.setPeriod(rs.getInt("period"));
            list.add(cdm);
        }
        Customer_DateManager[] memArr = new Customer_DateManager[list.size()];
        list.copyInto(memArr);
        return memArr;
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
