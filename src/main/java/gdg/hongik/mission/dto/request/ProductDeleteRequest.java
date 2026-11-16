package gdg.hongik.mission.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductDeleteRequest(

        @JsonProperty("itemName")
        String name
) {
}