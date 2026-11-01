package gdg.hongik.mission;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Product {
    private Long id;
    private String name;
    private int price;
    private int stock;

    public Product(Long id,String name,int price,int stock){
        this.id=id;
        this.name=name;
        this.price=price;
        this.stock=stock;
    }

    public Long getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public int getPrice(){
        return price;
    }
    public int getStock(){
        return stock;
    }

    public void addStock(int quantity){
        this.stock+=quantity;
    }
    public void decreaseStock(int quantity){
        this.stock-=quantity;
    }
}
