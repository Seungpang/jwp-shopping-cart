package woowacourse.shoppingcart.dto.response;

import woowacourse.shoppingcart.domain.CartItem;

public class CartItemResponse {

    private Long id;
    private int quantity;
    private ProductResponse product;

    public CartItemResponse() {
    }

    public CartItemResponse(Long id, int quantity, ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public static CartItemResponse of(Long cartItemId, ProductResponse product, int productQuantity) {
        return new CartItemResponse(
            cartItemId, productQuantity, product
        );
    }

    public static CartItemResponse of(CartItem cartItem) {
        return new CartItemResponse(
            cartItem.getId(),
            cartItem.getQuantity(),
            ProductResponse.of(cartItem.getProduct())
        );
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
