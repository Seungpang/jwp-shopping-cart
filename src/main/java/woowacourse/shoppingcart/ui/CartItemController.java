package woowacourse.shoppingcart.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import woowacourse.auth.support.AuthenticationPrincipal;
import woowacourse.shoppingcart.dto.CartResponse;
import woowacourse.shoppingcart.dto.LoginCustomer;
import woowacourse.shoppingcart.dto.ProductIdRequest;
import woowacourse.shoppingcart.dto.ProductRequest;
import woowacourse.shoppingcart.dto.QuantityRequest;
import woowacourse.shoppingcart.dto.Request;
import woowacourse.shoppingcart.application.CartService;

import java.util.List;

@RestController
@RequestMapping("/customers/carts")
public class CartItemController {
    private final CartService cartService;

    public CartItemController(final CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<CartResponse>> getCartItems(@AuthenticationPrincipal LoginCustomer loginCustomer) {
        return ResponseEntity.ok().body(cartService.findCartsByCustomerName(loginCustomer.getUsername()));
    }

    @PostMapping
    public ResponseEntity<CartResponse> addCartItem(@Validated(Request.id.class) @RequestBody final ProductIdRequest productIdRequest,
                                      @AuthenticationPrincipal LoginCustomer loginCustomer) {
        final CartResponse cartResponse = cartService.addCart(productIdRequest.getProductId(), loginCustomer.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartResponse);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCartItem(@AuthenticationPrincipal LoginCustomer loginCustomer,
                                         @PathVariable final Long cartId) {
        cartService.deleteCart(loginCustomer.getUsername(), cartId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<CartResponse> updateCartItemQuantity(
        @AuthenticationPrincipal LoginCustomer loginCustomer,
        @PathVariable final Long cartId, @RequestBody QuantityRequest quantityRequest) {
        CartResponse cartResponse = cartService.updateCardItemQuantity(cartId, quantityRequest.getQuantity());
        return ResponseEntity.ok().body(cartResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllCartItem(@AuthenticationPrincipal LoginCustomer loginCustomer) {
        cartService.deleteAllCart(loginCustomer.getUsername());
        return ResponseEntity.noContent().build();
    }

}
