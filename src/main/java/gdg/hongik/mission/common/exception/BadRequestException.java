package gdg.hongik.mission.common.exception;

// 재고 부족 등 잘못된 요청을 처리하는 예외
public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        // 부모 RuntimeException에 에러 메시지 전달
        super(message);
    }
}
