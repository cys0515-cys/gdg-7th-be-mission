package gdg.hongik.mission.controller;

import gdg.hongik.mission.dto.request.ProductCreateRequest;
import gdg.hongik.mission.dto.request.ProductDeleteRequest;
import gdg.hongik.mission.dto.request.StockUpdateRequest;
import gdg.hongik.mission.dto.response.ProductResponse;
import gdg.hongik.mission.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
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
            @Valid
            @RequestBody ProductCreateRequest request) {

        return productService.register(request);
    }

    // 재고 추가
    @PatchMapping("/stock")
    public ProductResponse addStock(
            @Valid
            @RequestBody StockUpdateRequest request){

        return productService.addStock(request);
    }

    // 상품명으로 상품 삭제
    @DeleteMapping
    public List<ProductResponse> delete(
            @Valid
            @RequestBody ProductDeleteRequest request) {

        return productService.delete(request);
    }
}