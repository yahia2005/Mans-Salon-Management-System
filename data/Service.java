package data;
/**
 * 
 * Service
 * this is a table in data base hold the information about the service
 * 1- id
 * 2- name
 * 3- price
 * 
*/
public class Service {
    private int id;
    private String name;
    private  int price;
    public Service() {}

    public Service(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public Service(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    @Override
    public String toString() {
        return name + " (" + price + " L.E)";
    }
}
