package data;

public enum OrderStatus {
    IN_QUEUE("in_queue"),
    ACCEPTED("accepted"),
    REJECTED("rejected");
    private final String dbValue;

    OrderStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }
    public static OrderStatus fromstring(String value){
        if(value != null){
            for(OrderStatus status: OrderStatus.values()){
                if(status.dbValue.equalsIgnoreCase(value)){
                    return status;
                }
            }
        }
        return IN_QUEUE;
    }
    @Override
    public String toString() {
        switch (this) {
            case IN_QUEUE:
                return "in_queue";
            case ACCEPTED :
                return "accepted";
            case REJECTED :
                return "rejected";
            default:
                return name();
        }
    }
}
