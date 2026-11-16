package gdg.hongik.mission.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import gdg.hongik.mission.entity.Category;

//상품 등록 요청
public record ProductCreateRequest(

        // ID는 DB가 자동 생성
        @JsonProperty("itemName")
        String name,

        int price,

        @JsonProperty("quantity")
        int stock,

        Category category
) {
}