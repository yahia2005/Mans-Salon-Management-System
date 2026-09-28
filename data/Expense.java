package data;
import java.sql.Date;
/**
 * 
 * Expense
 */

public class Expense {
    private int id;
    private Date expenseDate;
    private String description;
    private int amount;

    public Expense() {}

    public Expense(Date expenseDate, String description, int amount) {
        this.expenseDate = expenseDate;
        this.description = description;
        this.amount = amount;
    }

    public Expense(int id, Date expenseDate, String description, int amount) {
        this.id = id;
        this.expenseDate = expenseDate;
        this.description = description;
        this.amount = amount;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getExpenseDate() { return expenseDate; }
    public void setExpenseDate(Date expenseDate) { this.expenseDate = expenseDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
}
