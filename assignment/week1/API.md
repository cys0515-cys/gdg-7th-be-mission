# Week 1 API 명세서

## 1. 상품명으로 상품 조회

상품명과 정확히 일치하는 상품을 조회

### Request

- HTTP Method: `GET`
- URL: `/products?name={상품명}`

예시:

```http
GET /products?name=맥북
```

### Response

```json
{
  "id": 1,
  "name": "맥북",
  "price": 2000000,
  "stock": 10
}
```

상품이 존재하지 않으면 `RuntimeException`이 발생

---

## 2. 여러 상품 구매

여러 상품과 구매 수량을 전달하여 한 번에 구매

### Request

- HTTP Method: `POST`
- URL: `/products/purchase`
- Content-Type: `application/json`

```json
[
  {
    "id": 1,
    "quantity": 2
  },
  {
    "id": 2,
    "quantity": 1
  }
]
```

### Response

```json
{
  "totalPrice": 4050000,
  "products": [
    {
      "id": 1,
      "name": "맥북",
      "price": 2000000,
      "stock": 8
    },
    {
      "id": 2,
      "name": "마우스",
      "price": 50000,
      "stock": 9
    }
  ]
}
```

상품이 존재하지 않거나 재고가 부족하면 `RuntimeException`이 발생

---

## 3. 상품 등록

관리자가 새로운 상품을 등록, 상품 ID는 서버에서 자동으로 생성

### Request

- HTTP Method: `POST`
- URL: `/admin/products`
- Content-Type: `application/json`

```json
{
  "name": "맥북",
  "price": 2000000,
  "stock": 10
}
```

### Response

```json
{
  "id": 1,
  "name": "맥북",
  "price": 2000000,
  "stock": 10
}
```

---

## 4. 상품 재고 추가

관리자가 상품 ID와 추가할 수량을 전달하여 재고를 증가

### Request

- HTTP Method: `PATCH`
- URL: `/admin/products/stock`
- Content-Type: `application/json`

```json
{
  "id": 1,
  "quantity": 5
}
```

### Response

```json
{
  "id": 1,
  "name": "맥북",
  "price": 2000000,
  "stock": 15
}
```

상품이 존재하지 않으면 `RuntimeException`이 발생

---

## 5. 여러 상품 삭제

관리자가 상품 ID 목록을 전달하여 여러 상품을 삭제

### Request

- HTTP Method: `DELETE`
- URL: `/admin/products`
- Content-Type: `application/json`

```json
[
  1,
  2
]
```

### Response

삭제 후 남아 있는 상품 목록을 반환

```json
[
  {
    "id": 3,
    "name": "키보드",
    "price": 100000,
    "stock": 5
  }
]
```

삭제할 상품이 존재하지 않으면 `RuntimeException`이 발생