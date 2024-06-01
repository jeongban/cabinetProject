package cabinet;

public class Customer_DateManager {

    private int no;//회원번호
    private int period;//남은기간


    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }


    public Customer_DateManager() {
    }

    public Customer_DateManager(int no, int period) {
        super();
        this.no=no;
        this.period=period;

    }

}
