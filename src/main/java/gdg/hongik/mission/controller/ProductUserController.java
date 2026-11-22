package gdg.hongik.mission.controller;

import gdg.hongik.mission.dto.request.PurchaseItemRequest;
import gdg.hongik.mission.dto.response.ProductResponse;
import gdg.hongik.mission.dto.response.PurchaseResponse;
import gdg.hongik.mission.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/products")
public class ProductUserController {

    private final ProductService productService;

    public ProductUserController(
            ProductService productService) {

        this.productService = productService;
    }
    // 전체 상품 조회
    @GetMapping
    public List<ProductResponse> findAll() {
        //요청 DTO x
        return productService.findAll();
    }
    // 상품명 단건 조회
    @GetMapping(params = "itemName")
    public ProductResponse findByName(
            @RequestParam("itemName")
            String itemName) {

        return productService.findByName(itemName);
    }

    // 여러 상품 구매
    @PostMapping("/purchase")
    public PurchaseResponse purchase(
            @Valid
            @RequestBody List<PurchaseItemRequest> request) {
        return productService.purchase(request);
    }
}