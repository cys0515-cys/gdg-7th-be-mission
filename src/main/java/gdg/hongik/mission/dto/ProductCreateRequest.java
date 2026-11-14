package gdg.hongik.mission.dto;

//상품 등록 요청
public record ProductCreateRequest(
        String name,
        int price,
        int stock) {
}
//ID는 DB에서 만드므로 요청 DTO에 x
