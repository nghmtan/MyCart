package hanu.a2_1901040237.model;

public class Product {
    private int id;
    private String thumbnail;
    private String name;
    private Double unitPrice;
    private int quantity;

    public Product(int id, String thumbnail, String name, double unitPrice) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.unitPrice = unitPrice;
    }
    public Product(int id, String thumbnail, String name, double unitPrice, int quantity) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", thumbnail='" + thumbnail + '\'' +
                ", name='" + name + '\'' +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                '}';
    }

}
