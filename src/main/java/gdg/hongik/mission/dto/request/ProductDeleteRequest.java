package gdg.hongik.mission.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record ProductDeleteRequest(

        @NotBlank(message = "상품명은 필수입니다.")
        @JsonProperty("itemName")
        String name
) {
}