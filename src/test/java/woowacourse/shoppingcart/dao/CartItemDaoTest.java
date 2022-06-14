package woowacourse.shoppingcart.dao;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import woowacourse.shoppingcart.domain.CartItem;
import woowacourse.shoppingcart.domain.Product;

import java.util.List;
import woowacourse.shoppingcart.dto.request.ProductRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiChracters")
@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql", "classpath:test.sql"})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CartItemDaoTest {

    private final CartItemDao cartItemDao;
    private final ProductDao productDao;
    private final JdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    public CartItemDaoTest(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        cartItemDao = new CartItemDao(dataSource);
        productDao = new ProductDao(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        productDao.save(new ProductRequest("banana", 1_000, "woowa1.com"));
        productDao.save(new ProductRequest("apple", 2_000, "woowa2.com"));

        jdbcTemplate.update(
            "INSERT INTO cart_item(customer_id, product_id, quantity) VALUES(?, ?, ?)", 1L, 1L, 5);
        jdbcTemplate.update(
            "INSERT INTO cart_item(customer_id, product_id,quantity) VALUES(?, ?, ?)", 1L, 2L, 6);
    }

    @DisplayName("카트에 아이템을 담으면, 담긴 카트를 반환한다.")
    @Test
    void addCartItem() {
        // given
        final Long customerId = 1L;
        final Product product = new Product(4L, "banana", 1_000, "woowa1.com");

        CartItem cartItem = cartItemDao.addCartItem(customerId, product);

        assertAll(() -> {
            assertThat(cartItem.getId()).isEqualTo(5L);
            assertThat(cartItem.getQuantity()).isEqualTo(1);
            assertThat(cartItem.getProduct())
                .extracting("id", "name", "price", "imageUrl")
                .containsExactly(4L, "banana", 1_000, "woowa1.com");
        });
    }

    @Test
    void findIdsByCustomerId_메서드는_고객id로_장바구니_id들을_반환한다() {
        final Long customerId = 1L;

        List<Long> productIds = cartItemDao.findIdsByCustomerId(customerId);

        assertThat(productIds).containsExactly(3L, 4L);
    }

    @Test
    void findProductIdById_메서드는_장바구니상품_id로_상품_id를_반환한다() {
        final Long cartItemId = 1L;

        Long productId = cartItemDao.findProductIdById(cartItemId);

        assertThat(productId).isEqualTo(1L);
    }

    @Test
    void deleteCartItem_메서드는_장바구니_id로_장바구니를_삭제한다() {

        // given
        final Long cartItemId = 1L;

        // when
        cartItemDao.deleteCartItem(cartItemId);

        // then
        final Long customerId = 25L;
        final List<Long> productIds = cartItemDao.findIdsByCustomerId(customerId);

        assertThat(productIds).containsExactly(2L);
    }

    @Test
    void findByCartId메서드는_장바구니_id로_장바구니를_조회한다() {
        final Long cartItemId = 1L;

        CartItem cartItem = cartItemDao.findByCartItemId(cartItemId);

        assertAll(() -> {
            assertThat(cartItem.getId()).isEqualTo(1L);
            assertThat(cartItem.getQuantity()).isEqualTo(5);
            assertThat(cartItem.getProduct())
                .extracting("id", "name", "price", "imageUrl")
                .containsExactly(1L, "[승팡] 칠레산 코호 냉동 연어필렛 trim D(껍질있음) 1.1~1.3kg", 24_500,
                    "https://cdn-mart.baemin.com/sellergoods/main/92438f0e-0c4b-425e-b03b-999cee7cdca2.jpg");
        });
    }

    @Test
    void updateCartItemQuantity메서드는_장바구니의_상품_개수를_업데이트한다() {
        final Long cartItemId = 1L;
        final Long customerId = 1L;

        cartItemDao.updateCartItemQuantity(customerId, cartItemId, 5);

        assertThat(cartItemDao.findQuantityById(cartItemId)).isEqualTo(5);
    }
}
