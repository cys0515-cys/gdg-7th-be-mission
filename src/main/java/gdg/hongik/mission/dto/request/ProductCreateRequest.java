package gdg.hongik.mission.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import gdg.hongik.mission.entity.Category;
import jakarta.validation.constraints.*;

//상품 등록 요청
public record ProductCreateRequest(
        //ID는 DB가 자동 생성
        @NotBlank(message = "상품명은 필수입니다.")
        @Size(max = 30,
                message = "상품명은 30자 이하여야 합니다.")
        @JsonProperty("itemName")
        String name,

        @NotNull(message = "가격은 필수입니다.")
        @Positive(message = "가격은 0보다 커야 합니다.")
        Integer price,

        @NotNull(message = "재고는 필수입니다.")
        @PositiveOrZero(
                message = "재고는 0 이상이어야 합니다.")
        @JsonProperty("quantity")
        Integer stock,

        @NotNull(message = "카테고리는 필수입니다.")
        Category category
) {
}