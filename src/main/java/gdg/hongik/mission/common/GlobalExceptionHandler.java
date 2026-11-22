package gdg.hongik.mission.common;

import gdg.hongik.mission.common.dto.ErrorResponse;
import gdg.hongik.mission.common.exception.BadRequestException;
import gdg.hongik.mission.common.exception.NotFoundException;
import gdg.hongik.mission.common.message.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// 모든 Controller에서 발생하는 예외를 한곳에서 처리
@ControllerAdvice
public class GlobalExceptionHandler {

    // @Valid 유효성 검사 실패 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse>
    handleValidationException(
            MethodArgumentNotValidException exception) {

        String message = exception
                .getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        ErrorResponse errorResponse =
                new ErrorResponse(message);

        // 400 Bad Request
        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }

    // 잘못된 JSON 형식이나 타입 처리
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse>
    handleMessageNotReadableException(
            HttpMessageNotReadableException exception) {

        ErrorResponse errorResponse =
                new ErrorResponse(
                        ErrorMessage.INVALID_REQUEST);

        // 400 Bad Request
        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }

    // 요청한 상품을 찾지 못한 경우
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse>
    handleNotFoundException(
            NotFoundException exception) {

        ErrorResponse errorResponse =
                new ErrorResponse(
                        exception.getMessage());

        // 404 Not Found
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    // 재고 부족 등 잘못된 요청
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse>
    handleBadRequestException(
            BadRequestException exception) {

        ErrorResponse errorResponse =
                new ErrorResponse(
                        exception.getMessage());

        // 400 Bad Request
        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }

    // 별도로 처리하지 못한 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse>
    handleUnknownException(
            Exception exception) {

        ErrorResponse errorResponse =
                new ErrorResponse(
                        ErrorMessage.INTERNAL_SERVER_ERROR);

        // 500 Internal Server Error
        return ResponseEntity
                .internalServerError()
                .body(errorResponse);
    }
}