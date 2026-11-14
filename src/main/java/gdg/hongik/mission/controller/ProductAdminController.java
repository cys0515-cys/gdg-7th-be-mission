package gdg.hongik.mission.controller;

import gdg.hongik.mission.dto.ProductCreateRequest;
import gdg.hongik.mission.dto.ProductResponse;
import gdg.hongik.mission.dto.StockRequest;
import gdg.hongik.mission.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/products")
public class ProductAdminController {

    private final ProductService productService;

    public ProductAdminController(
            ProductService productService) {

        this.productService = productService;
    }

    // 상품 등록
    @PostMapping
    public ProductResponse register(
            @RequestBody ProductCreateRequest request) {

        return productService.register(request);
    }

    // 재고 추가
    @PatchMapping("/stock")
    public ProductResponse addStock(
            @RequestBody StockRequest request){

        return productService.addStock(request);
    }

    // 여러 상품 삭제
    @DeleteMapping
    public List<ProductResponse> delete(
            @RequestBody List<Long> ids) {

        return productService.delete(ids);
    }
}