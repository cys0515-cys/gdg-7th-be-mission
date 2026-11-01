package gdg.hongik.mission.controller;


import gdg.hongik.mission.Product;
import gdg.hongik.mission.ProductStore;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Tag(
        name = "소비자 상품 API",
        description = "상품 조회 및 여러 상품 구매 API"
)
@RestController
@RequestMapping("/products")
public class ProductUserController {
    @Operation(
            summary = "상품명으로 상품 조회",
            description = "상품명을 전달받아 일치하는 상품 정보를 조회합니다."
    )
    @GetMapping
    public Product findByName(@RequestParam("name") String name) {
        synchronized (ProductStore.class) {
            return ProductStore.products.stream()
                    .filter(p -> p.getName().equals(name))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다: " + name));
        }
    }

    // 예: [{"id": 1, "quantity": 2}, {"id": 2, "quantity": 1}]
    @Operation(
            summary = "여러 상품 구매",
            description = "상품 ID와 구매 수량 목록을 받아 재고를 확인하고 상품을 구매합니다."
    )
    @PostMapping("/purchase")
    public Map<String, Object>
    purchase(@RequestBody List<Map<String, Long>> request) {
        synchronized (ProductStore.class) {
            if (request == null || request.isEmpty()) throw new RuntimeException("구매할 상품이 필요합니다.");
            Map<Long, Integer> quantities = new LinkedHashMap<>();
            for (Map<String, Long> item : request) {
                if (item == null || item.get("id") == null || item.get("quantity") == null) {
                    throw new RuntimeException("id와 quantity가 필요합니다.");
                }
                Long id = item.get("id");
                long quantity = item.get("quantity");
                if (quantity <= 0 || quantity > Integer.MAX_VALUE) {
                    throw new RuntimeException("구매 수량이 올바르지 않습니다.");
                }
                long sum = quantities.getOrDefault(id, 0) + quantity;
                if (sum > Integer.MAX_VALUE) throw new RuntimeException("구매 수량이 너무 큽니다.");
                quantities.put(id, (int) sum);
            }
            long totalPrice = 0;
            for (Map.Entry<Long, Integer> entry : quantities.entrySet()) {
                Product product = findById(entry.getKey());
                if (product.getStock() < entry.getValue()) throw new RuntimeException("재고가 부족합니다.");
                totalPrice = Math.addExact(totalPrice, (long) product.getPrice() * entry.getValue());
            }
            // 중복 ID 수량도 합산해 검증한 다음 한 번에 차감합니다.
            quantities.forEach((id, quantity) -> findById(id).decreaseStock(quantity));
            return Map.of("totalPrice", totalPrice, "products",
                    quantities.keySet().stream().map(ProductUserController::findById).toList());
        }
    }
    private static Product findById(Long id) {
        return ProductStore.products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다: " + id));
    }
}
