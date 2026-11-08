package gdg.hongik.mission.service;

import gdg.hongik.mission.entity.Product;
import gdg.hongik.mission.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(
            ProductRepository productRepository) {

        this.productRepository = productRepository;
    }

    // ID로 상품 조회
    private Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "상품을 찾을 수 없습니다."));
    }

    // 상품명으로 조회
    public Product findByName(String name) {

        Product product =
                productRepository.findByName(name);

        if (product == null) {
            throw new RuntimeException(
                    "상품을 찾을 수 없습니다.");
        }

        return product;
    }

    // 상품 등록
    public Product register(Product product) {

        if (product.getName() == null
                || product.getName().isBlank()) {

            throw new RuntimeException(
                    "상품명이 필요합니다.");
        }

        if (product.getPrice() < 0
                || product.getStock() < 0) {

            throw new RuntimeException(
                    "가격과 재고는 0 이상이어야 합니다.");
        }

        return productRepository.save(product);
    }

    // 재고 추가
    public Product addStock(
            Map<String, Long> request) {

        Long id = request.get("id");
        Long quantity = request.get("quantity");

        if (id == null
                || quantity == null
                || quantity <= 0) {

            throw new RuntimeException(
                    "id와 양수 quantity가 필요합니다.");
        }

        Product product = findById(id);

        product.addStock(quantity.intValue());

        return product;
    }

    // 여러 상품 구매
    public Map<String, Object> purchase(
            List<Map<String, Long>> request) {

        if (request == null || request.isEmpty()) {
            throw new RuntimeException(
                    "구매할 상품이 필요합니다.");
        }

        long totalPrice = 0;

        List<Product> purchasedProducts =
                new ArrayList<>();

        // 먼저 모든 상품의 재고 확인과 가격 계산
        for (Map<String, Long> item : request) {

            Long id = item.get("id");
            Long quantity = item.get("quantity");

            if (id == null
                    || quantity == null
                    || quantity <= 0) {

                throw new RuntimeException(
                        "id와 양수 quantity가 필요합니다.");
            }

            Product product = findById(id);

            // 재고 확인
            if (product.getStock() < quantity) {
                throw new RuntimeException(
                        "재고가 부족합니다.");
            }

            // 가격 계산
            totalPrice +=
                    (long) product.getPrice()
                            * quantity;

            purchasedProducts.add(product);
        }

        // 모든 검사가 끝난 후 재고 차감
        for (int i = 0; i < request.size(); i++) {

            Map<String, Long> item =
                    request.get(i);

            int quantity =
                    item.get("quantity").intValue();

            Product product =
                    purchasedProducts.get(i);

            // 상품 구매 처리
            product.decreaseStock(quantity);
        }

        return Map.of(
                "totalPrice", totalPrice,
                "products", purchasedProducts
        );
    }

    // 여러 상품 삭제
    public List<Product> delete(List<Long> ids) {

        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException(
                    "삭제할 ID가 필요합니다.");
        }

        for (Long id : ids) {
            findById(id);
        }

        productRepository.deleteAllById(ids);

        return productRepository.findAll();
    }
}