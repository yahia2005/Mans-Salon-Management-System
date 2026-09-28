package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import data.Attendance;

public class AttendanceDAO extends BaseDAO {

    private static AttendanceDAO instanAttendanceDAO;

    private AttendanceDAO(){
        super();
    }

    public static synchronized AttendanceDAO getinstanOrderDao(){
        if(instanAttendanceDAO == null){
            instanAttendanceDAO = new AttendanceDAO();
        }
        return instanAttendanceDAO;
    }
    
    public boolean recordAttendance(Attendance worker){
        String sql = "INSERT INTO worker_attendance (worker_id, work_date, is_present) VALUES  (? , ? , ?) ON DUPLICATE KEY UPDATE is_present = ?";
        try(Connection conn =getConnection()){
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, worker.getWorkerId());
            stmt.setDate(2, worker.getWorkDate());
            stmt.setBoolean(3, worker.isPresent());
            stmt.setBoolean(4, worker.isPresent());
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public List<Attendance> getAttendanceByDate(Date date){
        String sql = "SELECT * FROM worker_attendance WHERE work_date = ?";
        List<Attendance> attendance = new ArrayList<>();
        try(Connection conn = getConnection()){
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDate(1, date);
            try(ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                Attendance worker = new Attendance();
                    worker.setId(rs.getInt("id"));
                    worker.setWorkerId(rs.getInt("worker_id"));
                    worker.setWorkDate(date);
                    worker.setPresent(rs.getBoolean("is_present"));
                    attendance.add(worker);
                }
            }
        }catch(SQLException e){
            System.err.println("Error " + e.getMessage());
            e.printStackTrace();
        }
        return attendance;
    }
    public int getPresentDaysCount(Date date1,Date date2,int id){
        String sql = "SELECT COUNT(*) AS days_count FROM worker_attendance "+
        "WHERE worker_id = ? AND is_present = 1 AND work_date BETWEEN ? AND ?";
        try(Connection conn = getConnection()){
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setDate(2, date1);
            stmt.setDate(3, date2);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return rs.getInt("days_count");
                }
            }
        }catch(SQLException e){
            System.err.println("Error " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    public boolean delete_attendance(Attendance attendance){
        String sql = "DELETE FROM worker_attendance WHERE worker_id = ? AND work_date = ?";
        try(Connection conn = getConnection()){
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, attendance.getWorkerId());
            stmt.setDate(2, attendance.getWorkDate());
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            System.err.println("Error " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
