package data;
import java.sql.Date;
/**
 * 
 * Order
 * 
 */


public class Order {
    private int id;
    private int workerId;
    private int serviceId;
    private Date orderDate;
    private int price;
    private OrderStatus status;
    /**
     * InnerOrder
     */ 
    
    public Order() {}

    public Order(int workerId, int serviceId, int price,String status, Date orderDate) {
        this.workerId = workerId;
        this.serviceId = serviceId;
        this.price = price;
        this.status = OrderStatus.fromstring(status);
        this.orderDate = orderDate;
    }

    public Order(int id, int workerId, int serviceId, int price, String status, Date orderDate) {
        this.id = id;
        this.workerId = workerId;
        this.serviceId = serviceId;
        this.price = price;
        this.status = OrderStatus.fromstring(status);
        this.orderDate = orderDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getWorkerId() { return workerId; }
    public void setWorkerId(int workerId) { this.workerId = workerId; }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getStatus() { return status.toString(); }
    public void setStatus(String status) { this.status = OrderStatus.fromstring(status);}

    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
}
