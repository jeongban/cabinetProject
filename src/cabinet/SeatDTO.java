package cabinet;

public class SeatDTO {
    private String rno;//방 번호
    private String sno;//좌석번호
    private String state;//사용유무
    private String id;//id
    private int period;//남은기간


    public String getRno() {
        return rno;
    }

    public void setRno(String rno) {
        this.rno = rno;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(String Period) {//문자열로 받은 남은 기간을 정수형으로 변환해서 설정
        this.period = Integer.parseInt(Period);
    }

    public void setPeriod(int Period) {
        this.period = Period;
    }

    public SeatDTO() {
    }

    public SeatDTO(String rno, String sno, String state,String id, int period ) {
        super();
        this.rno = rno;
        this.sno=sno;
        this.state=state;
        this.id=id;
        this.period=period;
    }

}
