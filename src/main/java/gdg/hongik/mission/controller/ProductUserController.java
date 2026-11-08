package gdg.hongik.mission.controller;

import gdg.hongik.mission.entity.Product;
import gdg.hongik.mission.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductUserController {

    private final ProductService productService;

    public ProductUserController(
            ProductService productService) {

        this.productService = productService;
    }

    // 상품명 조회
    @GetMapping
    public Product findByName(
            @RequestParam("name") String name) {

        return productService.findByName(name);
    }

    // 여러 상품 구매
    @PostMapping("/purchase")
    public Map<String, Object> purchase(
            @RequestBody
            List<Map<String, Long>> request) {

        return productService.purchase(request);
    }
}