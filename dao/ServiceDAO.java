package dao;
/**
 * ServiceDAO
 * 
 */
import data.Service;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class ServiceDAO extends BaseDAO{

    private static ServiceDAO instanServiceDAO;

    private ServiceDAO(){
        super();
    }

    public static synchronized ServiceDAO getinstanServiceDAO(){
        if(instanServiceDAO == null){
            instanServiceDAO = new ServiceDAO();
        }
        return instanServiceDAO;
    }

    public boolean getInstance(Service service){
        String sql = "INSERT INTO service (name, price) VALUES (? , ?)" ;
        try(Connection conn = getConnection()){
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, service.getName());
            stmt.setInt(2, service.getPrice());
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            System.err.println("Error " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public  List<Service> getAllservice(){
        String sql = "SELECT * FROM service";
        List<Service> services = new ArrayList<>();
        try(Connection conn = getConnection()){

            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int price = rs.getInt("price");
                Service service = new Service(id,name,price);
                services.add(service);
            }
        }catch(SQLException e){
            System.err.println("Error " + e.getMessage());
            e.printStackTrace();
        }
        return services;
    }
    public Service getServiceByid(int id){
        String sql = "SELECT * FROM service WHERE id = ?";
        try(Connection conn = getConnection()){
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new Service(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("price")
                );
            }
        }
        }catch(SQLException e){
            System.err.println("Error "+ e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    public boolean updateService(Service service){
        String sql = "UPDATE Service SET name = ? , price = ? WHERE id = ?";
        try(Connection conn = getConnection()){
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, service.getName());
            stmt.setInt(2, service.getPrice());
            stmt.setInt(3, service.getId());
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            System.err.println("Error "+ e.getMessage());
            e.printStackTrace(); 
            return false;   
        }
    }
    public boolean deleteService(int id){
        return deleteById("service",id);
    }
    public int getServiceId(String name){
        String sql = "SELECT * FROM service WHERE name = ?";
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