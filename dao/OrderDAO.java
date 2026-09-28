package dao;
/**
 * 
 * OrderDAO
 */

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import data.Order;

public class OrderDAO extends BaseDAO{
    private static OrderDAO instanOrderDao;

    private OrderDAO(){
        super();
    }

    public static synchronized OrderDAO getinstanOrderDao(){
        if(instanOrderDao == null){
            instanOrderDao = new OrderDAO();
        }
        return instanOrderDao;
    }

    public boolean insertOrder(Order order){
        String sql = "INSERT INTO orders (worker_id, service_id, price ,status, order_date)"+
        " VALUES (?,?,?,?,?) ";
        try(Connection conn = getConnection()) {
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, order.getWorkerId());
            stmt.setInt(2, order.getServiceId());
            stmt.setInt(3, order.getPrice());
            stmt.setString(4, order.getStatus());
            stmt.setDate(5, order.getOrderDate());
            return stmt.executeUpdate() > 0;
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public List<Order> getOrdersBydate(Date date){
        String sql = "SELECT * FROM orders WHERE order_date = ?";
        List<Order> orders = new ArrayList<>();
        try(Connection conn = getConnection()){
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDate(1, date);
            try(ResultSet rs = stmt.executeQuery() ){
                while (rs.next()) {
                    Order order = new Order(rs.getInt("id"),
                        rs.getInt("worker_id"),
                        rs.getInt("service_id"),
                        rs.getInt("price"),
                        rs.getString("status"),
                        rs.getDate("order_date")
                    );
                    orders.add(order);
                }
            }
        }catch(SQLException e){
            System.err.println("Error " + e.getMessage());
            e.printStackTrace();
        }
        return orders;
    }
    public int getTotaleIncomeByDate(Date date){
        String sql = "SELECT SUM(price) AS income FROM orders WHERE order_date = ? AND status = ? ";
        try(Connection conn = getConnection()){
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDate(1, date);
            stmt.setString(2, "accepted");
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return rs.getInt("income");
                }
            }
        }catch(SQLException e){
            System.err.println("Error " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public boolean updateStatus(String status, int id){
        String sql = "UPDATE ORDERS SET status = ? WHERE id = ?";
        try(Connection conn = getConnection()){
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            System.err.println("Error " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOrder(int id){
        return deleteById("orders", id);
    }
}
