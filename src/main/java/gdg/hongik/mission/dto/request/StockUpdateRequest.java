package gdg.hongik.mission.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockUpdateRequest(

        @NotBlank(message = "상품명은 필수입니다.")
        @JsonProperty("itemName")
        String name,

        @NotNull(message = "추가 수량은 필수입니다.")
        @Positive(message = "추가 수랑은 0보다 커야합니다.")
        int quantity //Integer?
) {
}