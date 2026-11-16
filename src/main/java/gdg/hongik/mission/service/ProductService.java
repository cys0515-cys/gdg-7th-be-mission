package gdg.hongik.mission.service;

import gdg.hongik.mission.dto.request.ProductCreateRequest;
import gdg.hongik.mission.dto.request.PurchaseItemRequest;
import gdg.hongik.mission.dto.request.StockUpdateRequest;
import gdg.hongik.mission.dto.request.ProductDeleteRequest;
import gdg.hongik.mission.dto.response.ProductResponse;
import gdg.hongik.mission.dto.response.PurchaseResponse;
import gdg.hongik.mission.dto.response.PurchasedProductResponse;

import gdg.hongik.mission.entity.Product;
import gdg.hongik.mission.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ID로 상품 찾기
    private Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("상품을 찾을 수 없습니다."));
    }

    // 상품명 단건 조회
    @Transactional(readOnly = true)
    public ProductResponse findByName(String name) {
        Product product = productRepository.findByName(name);

        if (product == null) {
            throw new RuntimeException("상품을 찾을 수 없습니다.");
        }

        return ProductResponse.from(product);
    }

    //전체 상품 조회
    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        List<ProductResponse> responses =
                new ArrayList<>(); //빈 목록 만들기 ex) responses = []

        for (Product product
                : productRepository.findAll()) {

            responses.add(
                    ProductResponse.from(product));
        }

        return responses;
    }
    // 상품 등록
    @Transactional
    public ProductResponse register(ProductCreateRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new RuntimeException("상품명이 필요합니다.");
        }

        if (request.price() < 0 || request.stock() < 0) {
            throw new RuntimeException("가격과 재고는 0 이상이어야 합니다.");
        }

        if (request.category() == null) {
            throw new RuntimeException(
                    "카테고리가 필요합니다.");
        }

        if (productRepository.findByName(request.name()) != null) {
            throw new RuntimeException("이미 등록된 상품명입니다.");
        }

        Product product = new Product();
        product.setName(request.name());
        product.setPrice(request.price());
        product.setStock(request.stock());

        product.setCategory(request.category());

        Product savedProduct =
                productRepository.save(product);

        return ProductResponse.from(savedProduct);
    }

    // 재고 추가
    @Transactional
    public ProductResponse addStock(StockUpdateRequest request) {
        //상품명 검사
        if (request.name() == null ||
                request.name().isBlank() ||
                request.quantity() <= 0) {

            throw new RuntimeException(
                    "상품명과 1 이상의 수량이 필요합니다.");
        }

        Product product =
                productRepository.findByName(
                        request.name());

        //상품명으로 조회
        if (product == null) {
            throw new RuntimeException(
                    "상품을 찾을 수 없습니다.");
        }

        product.addStock(request.quantity());

        return ProductResponse.from(product);
    }

    // 여러 상품 구매
    @Transactional
    public PurchaseResponse purchase(
            List<PurchaseItemRequest> requests) {

        if (requests == null || requests.isEmpty()) {
            throw new RuntimeException("구매할 상품이 없습니다.");
        }

        Map<Long, Integer> quantities = new HashMap<>();

        // 먼저 모든 구매 요청과 재고를 확인
        for (PurchaseItemRequest request : requests) {
            if (request.id() == null || request.quantity() <= 0) {
                throw new RuntimeException(
                        "상품 ID와 1 이상의 수량이 필요합니다.");
            }

            Product product = findById(request.id());

            int totalQuantity =
                    quantities.getOrDefault(request.id(), 0)
                            + request.quantity();

            if (product.getStock() < totalQuantity) {
                throw new RuntimeException("재고가 부족합니다.");
            }

            quantities.put(request.id(), totalQuantity);
        }

        long totalPrice = 0;
        List<PurchasedProductResponse>
                purchasedProducts =
                new ArrayList<>();

        // 검사가 끝난 상품들의 재고 차감
        for (Map.Entry<Long, Integer> entry
                : quantities.entrySet()) {

            Product product = findById(entry.getKey());
            int quantity = entry.getValue();

            product.decreaseStock(quantity);

            //해당 상품의 총 소비 금액
            long itemPrice =
                    (long) product.getPrice()
                            * quantity;

            totalPrice += itemPrice;

            //구매 상품명,수량,소비 금액 응답
            purchasedProducts.add(
                    new PurchasedProductResponse(
                            product.getName(),
                            quantity,
                            itemPrice
                    )
            );
        }

        return new PurchaseResponse(
                totalPrice,
                purchasedProducts
        );
    }

    //상품명으로 상품 하나 삭제
    @Transactional
    public List<ProductResponse> delete(
            ProductDeleteRequest request) {

        if (request.name() == null ||
                request.name().isBlank()) {

            throw new RuntimeException(
                    "삭제할 상품명이 필요합니다.");
        }

        Product product =
                productRepository.findByName(
                        request.name());

        if (product == null) {
            throw new RuntimeException(
                    "상품을 찾을 수 없습니다.");
        }

        productRepository.delete(product);

        // 삭제 후 남은 전체 상품 목록 반환
        return findAll();
    }
}