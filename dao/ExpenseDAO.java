package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import data.Expense;

public class ExpenseDAO extends BaseDAO {


    private static ExpenseDAO instanExpenseDAO;

    private ExpenseDAO(){
        super();
    }

    public static synchronized ExpenseDAO getinstanOrderDao(){
        if(instanExpenseDAO == null){
            instanExpenseDAO = new ExpenseDAO();
        }
        return instanExpenseDAO;
    }
    
    public boolean insertexpense (Expense expense){
        String sql = "INSERT INTO expenses ( expense_date, description ,amount ) VALUE (? ,? , ?)";
        try(Connection conn = getConnection()){
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDate(1, expense.getExpenseDate());
            stmt.setString(2, expense.getDescription());
            stmt.setInt(3, expense.getAmount());
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public  List<Expense> getAllexpenses(){
        String sql = "SELECT * FROM expenses";
        List<Expense> expenses = new ArrayList<>();
        try(Connection conn = getConnection()){
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                Date expense_date = rs.getDate("expense_date");
                String description = rs.getString("description");
                int amount = rs.getInt("amount");
                Expense expense = new Expense(id,expense_date,description,amount);
                expenses.add(expense);
            }
        }catch(SQLException e){
            System.err.println("Error " + e.getMessage());
            e.printStackTrace();
        }
        return expenses;
    }
    public List<Expense> getExpensBydate(Date date){
        String sql = "SELECT * FROM expenses WHERE expense_date = ?";
        List<Expense> expenses = new ArrayList<>();
        try(Connection conn = getConnection()){
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDate(1, date);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                Date expense_date = rs.getDate("expense_date");
                String description = rs.getString("description");
                int amount = rs.getInt("amount");
                Expense expense = new Expense(id,expense_date,description,amount);
                expenses.add(expense);
            }
        }catch(SQLException e){
            System.err.println("Error "+ e.getMessage());
            e.printStackTrace();
        }
        return expenses;
    }
    public boolean updateExpense(Expense expense){
        String sql = "UPDATE expenses SET expense_date = ? , description = ? , amount = ? WHERE id = ?";
        try(Connection conn = getConnection()){
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDate(1, expense.getExpenseDate());
            stmt.setString(2, expense.getDescription());
            stmt.setInt(3, expense.getAmount());
            stmt.setInt(4, expense.getId());
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
             System.err.println("Error "+ e.getMessage());
            e.printStackTrace(); 
            return false;
        }
    }
    public boolean deleteExpense(int id){
        return deleteById("expenses", id);      
    }
    public int getTotalExpensesByDate(Date date){
        String sql = "SELECT SUM(amount) AS total FROM expenses WHERE expense_date = ?";
        try(Connection conn = getConnection()){
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDate(1, date);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return  rs.getInt("total");
                }
            } 
        }catch(SQLException e){
            System.err.println("Error "+ e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
}
