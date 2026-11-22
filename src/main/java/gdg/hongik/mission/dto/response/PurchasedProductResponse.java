package gdg.hongik.mission.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PurchasedProductResponse(

        @JsonProperty("itemName")
        String name,

        @JsonProperty("count")
        int quantity,

        long price
) {
}