package gdg.hongik.mission.service;

import gdg.hongik.mission.common.exception.BadRequestException;
import gdg.hongik.mission.common.exception.NotFoundException;
import gdg.hongik.mission.common.message.ErrorMessage;
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


import java.util.*;

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
                        new NotFoundException(ErrorMessage.PRODUCT_NOT_FOUND));
    }

    private Product findProductByName(
            String name) {

        Product product =
                productRepository.findByName(name);

        if (product == null) {
            throw new NotFoundException(
                    ErrorMessage.PRODUCT_NOT_FOUND
            );
        }

        return product;
    }

    // 상품명 단건 조회
    @Transactional(readOnly = true)
    public ProductResponse findByName(
            String name) {

        Product product =
                findProductByName(name);

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

        if (productRepository.findByName(request.name()) != null) {
            throw new BadRequestException(
                    ErrorMessage.PRODUCT_ALREADY_EXISTS
            );
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
    public ProductResponse addStock(
            StockUpdateRequest request) {

        Product product =
                findProductByName(request.name());

        product.addStock(request.quantity());

        return ProductResponse.from(product);
    }

    private Map<Long, Integer> sumQuantities(
            List<PurchaseItemRequest> requests) {

        Map<Long, Integer> quantities =
                new LinkedHashMap<>();

        for (PurchaseItemRequest request
                : requests) {

            quantities.merge(
                    request.id(),
                    request.quantity(),
                    Integer::sum
            );
        }

        return quantities;
    }
    private PurchasedProductResponse purchaseProduct(
            Long id,
            int quantity) {

        // 상품 조회
        Product product =
                findById(id);

        // 재고 확인
        if (product.getStock() < quantity) {
            throw new BadRequestException(
                    ErrorMessage.INSUFFICIENT_STOCK
            );
        }

        // 재고 차감
        product.decreaseStock(quantity);

        // 해당 상품의 총 구매 가격
        long itemPrice =
                (long) product.getPrice()
                        * quantity;

        // 구매한 상품 하나의 응답 생성
        return new PurchasedProductResponse(
                product.getName(),
                quantity,
                itemPrice
        );
    }
    // 여러 상품 구매
    @Transactional
    public PurchaseResponse purchase(
            List<PurchaseItemRequest> requests) {

        //구매 목록 비었는지 확인
        if (requests == null || requests.isEmpty()) {
            throw new BadRequestException(
                    ErrorMessage.EMPTY_PURCHASE_REQUEST
            );
        }

        // 동일 상품의 구매 수량 합산
        Map<Long, Integer> quantities =
                sumQuantities(requests);

        List<PurchasedProductResponse>
                purchasedProducts =
                new ArrayList<>();

        long totalPrice = 0;

        // 상품별 구매 처리
        for (Map.Entry<Long, Integer> entry
                : quantities.entrySet()) {

            PurchasedProductResponse
                    purchasedProduct =
                    purchaseProduct(
                            entry.getKey(),
                            entry.getValue()
                    );

            purchasedProducts.add(
                    purchasedProduct
            );

            totalPrice +=
                    purchasedProduct.price();
        }

        // 전체 구매 결과 반환
        return new PurchaseResponse(
                totalPrice,
                purchasedProducts
        );
    }

    //상품명으로 상품 하나 삭제
    @Transactional
    public List<ProductResponse> delete(
            ProductDeleteRequest request) {

        Product product =
                findProductByName(request.name());

        productRepository.delete(product);

        return findAll();
    }
}