package gdg.hongik.mission.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Product {
    @Id //상품을 구별하는 PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) //새 상품을 저장할 때 DB가 ID를 자동으로 만들도록 설정
    //IDENTITY = DB의 자동 증가 기능으로 1, 2, 3... 같은 ID 생성
    private Long id;

    private String name;
    private int price;
    private int stock;

    @Enumerated(EnumType.STRING) // 반드시 STRING! (생략하면 ORDINAL이 되어 0, 1, 2 같은 순번이 저장됩니다)
    @Column(nullable = false)
    private Category category;

    public void addStock(int quantity) {
        this.stock += quantity;
    }

    public void decreaseStock(int quantity) {
        this.stock -= quantity;
    }
}
