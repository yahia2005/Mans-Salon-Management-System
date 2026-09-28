package data;
import java.sql.Date;
/**
 * 
 * Attendance
 */

public class Attendance {
    private int id;
    private int workerId;
    private Date workDate;
    private boolean isPresent;

    public Attendance() {}

    public Attendance(int workerId, Date workDate, boolean isPresent) {
        this.workerId = workerId;
        this.workDate = workDate;
        this.isPresent = isPresent;
    }

    public Attendance(int id, int workerId, Date workDate, boolean isPresent) {
        this.id = id;
        this.workerId = workerId;
        this.workDate = workDate;
        this.isPresent = isPresent;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getWorkerId() { return workerId; }
    public void setWorkerId(int workerId) { this.workerId = workerId; }

    public Date getWorkDate() { return workDate; }
    public void setWorkDate(Date workDate) { this.workDate = workDate; }

    public boolean isPresent() { return isPresent; }
    public void setPresent(boolean present) { isPresent = present; }
}
