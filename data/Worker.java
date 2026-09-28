package data;
/**
 * 
 * Worker
 *  this is a table in data base hold the information about the name and salry of worker
 * 1- id
 * 2- name
 * 3- daily salary
 * 
 */
public class Worker{
    private int id;
    private String name;
    private int dailySalary;

    public Worker() {}

     public Worker(int id,String name) {
        this.id = id;
        this.name = name;
    }

    public Worker(String name, int dailySalary) {
        this.name = name;
        this.dailySalary = dailySalary;
    }

    public Worker(int id, String name, int dailySalary) {
        this.id = id;
        this.name = name;
        this.dailySalary = dailySalary;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getDailySalary() { return dailySalary; }
    public void setDailySalary(int dailySalary) { this.dailySalary = dailySalary; }

    @Override
    public String toString() {
        return name;
    }
}
