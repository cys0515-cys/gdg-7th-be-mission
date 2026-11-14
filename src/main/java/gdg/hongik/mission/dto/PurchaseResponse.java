package gdg.hongik.mission.dto;

import java.util.List;

//구매 응답
public record PurchaseResponse(
        long totalPrice,
        List<ProductResponse> products) {
}
