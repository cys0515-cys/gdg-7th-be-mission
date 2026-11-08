package gdg.hongik.mission.controller;

import gdg.hongik.mission.entity.Product;
import gdg.hongik.mission.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public Product register(
            @RequestBody Product product) {

        return productService.register(product);
    }

    // 재고 추가
    @PatchMapping("/stock")
    public Product addStock(
            @RequestBody
            Map<String, Long> request) {

        return productService.addStock(request);
    }

    // 여러 상품 삭제
    @DeleteMapping
    public List<Product> delete(
            @RequestBody List<Long> ids) {

        return productService.delete(ids);
    }
}