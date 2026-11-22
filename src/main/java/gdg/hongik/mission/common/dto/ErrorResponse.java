package gdg.hongik.mission.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 모든 에러 응답의 JSON 형식을 통일하는 DTO
@Getter
@AllArgsConstructor
public class ErrorResponse{
        // 클라이언트에게 전달할 에러 메시지
        private String errorMessage;
}
