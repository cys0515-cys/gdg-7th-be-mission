package gdg.hongik.mission.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

//구매 응답
public record PurchaseResponse(
        long totalPrice,

        @JsonProperty("items")
        List<PurchasedProductResponse> purchasedProducts
) {
}