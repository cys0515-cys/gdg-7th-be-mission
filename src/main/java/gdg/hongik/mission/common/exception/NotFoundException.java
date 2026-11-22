package gdg.hongik.mission.common.exception;

// 상품 등 요청한 데이터를 찾지 못했을 때 사용하는 예외
public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        // 부모 RuntimeException에 에러 메시지 전달
        super(message);
    }
}
