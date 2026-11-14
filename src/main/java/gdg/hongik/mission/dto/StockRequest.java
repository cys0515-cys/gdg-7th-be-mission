package gdg.hongik.mission.dto;

//재고 추가 요청
public record StockRequest(
        Long id,
        int quantity
) {
}


//재고삭제는 ㄱㄷ