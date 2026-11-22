package gdg.hongik.mission.common.message;

// 여러 곳에서 사용하는 에러 메시지를 한곳에서 관리
public final class ErrorMessage {

    // 요청한 상품이 DB에 없을 때
    public static final String PRODUCT_NOT_FOUND =
            "상품을 찾을 수 없습니다.";

    //이미 상품명이 등록됐을 때
    public static final String PRODUCT_ALREADY_EXISTS =
            "이미 등록된 상품명입니다.";
    
    // 구매 요청 수량보다 재고가 부족할 때
    public static final String INSUFFICIENT_STOCK =
            "상품의 재고가 부족합니다.";

    public static final String EMPTY_PURCHASE_REQUEST =
            "구매할 상품이 없습니다.";

    // JSON 형식이나 값이 올바르지 않을 때
    public static final String INVALID_REQUEST =
            "잘못된 요청입니다.";

    // 예상하지 못한 서버 오류가 발생했을 때
    public static final String INTERNAL_SERVER_ERROR =
            "서버 내부 오류가 발생했습니다.";

    // 객체 생성을 막기 위한 private 생성자
    private ErrorMessage() {
    }
}
