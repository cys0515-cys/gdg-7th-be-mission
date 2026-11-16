package gdg.hongik.mission.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

//구매 요청
public record PurchaseItemRequest(
        Long id,

        @JsonProperty("count")
        int quantity
) {
}
