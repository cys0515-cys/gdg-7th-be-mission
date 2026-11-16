package gdg.hongik.mission.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StockUpdateRequest(

        @JsonProperty("itemName")
        String name,

        int quantity
) {
}