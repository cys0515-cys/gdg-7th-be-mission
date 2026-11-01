package gdg.hongik.mission.controller;

import gdg.hongik.mission.Product;
import gdg.hongik.mission.ProductStore;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;

@Tag(
        name = "관리자 상품 API",
        description = "상품 등록, 재고 추가 및 상품 삭제 API"
)
@RestController
@RequestMapping("/admin/products")
public class ProductAdminController {
    @Operation(
            summary = "상품 등록",
            description = "상품명, 가격, 초기 재고를 받아 새로운 상품을 등록합니다."
    )
    @PostMapping
    public Product register(@RequestBody Product request) {
        synchronized (ProductStore.class) {
            Product product = new Product(ProductStore.sequence, request.getName(),
                    request.getPrice(), request.getStock());
            if (ProductStore.products.stream().anyMatch(p -> p.getName().equals(product.getName()))) {
                throw new RuntimeException("이미 등록된 상품명입니다.");
            }
            ProductStore.sequence++;
            ProductStore.products.add(product);
            return product;
        }
    }
    // 예: {"id": 1, "quantity": 5}
    @Operation(
            summary = "상품 재고 추가",
            description = "상품 ID와 추가 수량을 받아 해당 상품의 재고를 증가시킵니다."
    )
    @PatchMapping("/stock")
    public Product addStock(@RequestBody Map<String, Long> request) {
        synchronized (ProductStore.class) {
            Long id = request.get("id");
            Long quantity = request.get("quantity");
            if (id == null || quantity == null || quantity <= 0 || quantity > Integer.MAX_VALUE) {
                throw new RuntimeException("id와 양수 quantity가 필요합니다.");
            }
            Product product = findById(id);
            product.addStock(quantity.intValue());
            return product;
        }
    }

    // 예: [1, 2]
    @Operation(
            summary = "여러 상품 삭제",
            description = "상품 ID 목록을 받아 여러 상품을 삭제하고 남은 상품 목록을 반환합니다."
    )
    @DeleteMapping
    public List<Product>
    delete(@RequestBody List<Long> ids) {
        synchronized (ProductStore.class) {
            if (ids == null || ids.isEmpty()) throw new RuntimeException("삭제할 ID가 필요합니다.");
            // 모든 ID를 먼저 확인하여 실패 시 일부만 삭제되는 것을 방지합니다.
            ids.forEach(ProductAdminController::findById);
            ProductStore.products.removeIf(p -> ids.contains(p.getId()));
            return List.copyOf(ProductStore.products);
        }
    }
    private static Product findById(Long id) {
        return ProductStore.products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다: " + id));
    }
}
