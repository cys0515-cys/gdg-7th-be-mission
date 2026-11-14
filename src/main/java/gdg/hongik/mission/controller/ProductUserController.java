package gdg.hongik.mission.controller;

import gdg.hongik.mission.dto.ProductResponse;
import gdg.hongik.mission.dto.PurchaseRequest;
import gdg.hongik.mission.dto.PurchaseResponse;
import gdg.hongik.mission.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ProductResponse findByName(
            @RequestParam("name") String name) {

        return productService.findByName(name);
    }

    // 여러 상품 구매
    @PostMapping("/purchase")
    public PurchaseResponse purchase(
            @RequestBody List<PurchaseRequest> request) {
        return productService.purchase(request);
    }
}