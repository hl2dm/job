package dm.school.suck.job;


public  class DB_df {

    String id;
    String time;
    String price;
    String qty;
    String status;

    public DB_df(String id, String time, String price, String qty, String status) {
        this.id = id;
        this.time = time;
        this.price = price;
        this.qty = qty;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}