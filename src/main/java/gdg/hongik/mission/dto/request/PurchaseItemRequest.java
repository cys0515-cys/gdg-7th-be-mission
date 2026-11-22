package gdg.hongik.mission.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

//구매 요청
public record PurchaseItemRequest(
        @NotNull(message = "상품 ID는 필수입니다.")
        @Positive(message = "상품 ID는 0보다 커야 합니다.")
        Long id,

        @NotNull(message = "구매 수량은 필수입니다.")
        @Positive(message = "구매 수량은 0보다 커야 합니다.")
        @JsonProperty("count")
        int quantity
) {
}
