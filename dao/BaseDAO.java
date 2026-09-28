package dao;
import java.sql.Connection;
import java.sql.SQLException;
import util.DatabaseConection;
/**
 * 
 * BaseDAO
 */

public abstract class BaseDAO {
    protected Connection getConnection() throws SQLException{
        return DatabaseConection.getConnection();
    }
    protected boolean deleteById(String table_name,int id){
        String sql = "DELETE FROM "+table_name + " WHERE ID = ?";
        try(Connection conn = getConnection()){
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
