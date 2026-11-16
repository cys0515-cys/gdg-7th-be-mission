package gdg.hongik.mission.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import gdg.hongik.mission.entity.Category;
import gdg.hongik.mission.entity.Product;

//구매 응답
public record ProductResponse(
        Long id,

        @JsonProperty("itemName")
        String name,

        int price,

        @JsonProperty("quantity")
        int stock,

        Category category
) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getCategory()
        );
    }
}