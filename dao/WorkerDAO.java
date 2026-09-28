package dao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import data.Worker;
public class WorkerDAO extends BaseDAO{

    private static WorkerDAO instanWorkerDAO;

    private WorkerDAO(){
        super();
    }

    public static synchronized WorkerDAO getInstance(){
        if(instanWorkerDAO == null){
            instanWorkerDAO = new WorkerDAO();
        }
        return instanWorkerDAO;
    }
    public boolean insertworker(Worker worker){
        String sql = "INSERT INTO workers (name ,daily_salary ) VALUES (? , ?)";
        try(Connection conn = getConnection()){
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, worker.getName());
            stmt.setInt(2, worker.getDailySalary());
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
        public  List<Worker> getAllWorker(){
        String sql = "SELECT * FROM workers";
        List<Worker> Workers = new ArrayList<>();
        try(Connection conn = getConnection()){

            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int daily_salary = rs.getInt("daily_salary");
                Worker worker = new Worker(id,name,daily_salary);
                Workers.add(worker);
            }
        }catch(SQLException e){
            System.err.println("Error " + e.getMessage());
            e.printStackTrace();
        }
        return Workers;

    }
     public Worker getWorkerByid(int id){
        String sql = "SELECT * FROM workers WHERE id = ?";
        try(Connection conn = getConnection()){
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new Worker(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("daily_salary")
                );
            }
        }
        }catch(SQLException e){
            System.err.println("Error "+ e.getMessage());
            e.printStackTrace();
        }
        return null;
        
    }
    public boolean updateworker(Worker worker){
        String sql = "UPDATE workers SET name = ? , daily_salary = ? WHERE id = ?";
        try(Connection conn = getConnection()){
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, worker.getName());
            stmt.setInt(2, worker.getDailySalary());
            stmt.setInt(3, worker.getId());
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            System.err.println("Error "+ e.getMessage());
            e.printStackTrace(); 
            return false;   
        }
    }
    public boolean deleteworker(int id){
        return deleteById("workers",id);
    }
    public int getworkerId(String name){
        String sql = "SELECT * FROM workers WHERE name = ?";
        try(Connection conn = getConnection()){
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return rs.getInt("id");
                }
            }            
        }catch(SQLException e){
            System.err.println("Error "+ e.getMessage());
            e.printStackTrace(); 
        }
        return -1;   
    }
}
