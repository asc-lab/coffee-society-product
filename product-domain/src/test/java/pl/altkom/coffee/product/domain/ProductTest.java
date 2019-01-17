package pl.altkom.coffee.product.domain;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.Test;
import pl.altkom.coffee.product.api.ProductPreparationCancelledEvent;
import pl.altkom.coffee.product.api.ProductPreparationRegisteredEvent;
import pl.altkom.coffee.product.api.ProductReceiverChangedEvent;
import pl.altkom.coffee.product.api.enums.ProductState;

import static org.junit.Assert.assertSame;

public class ProductTest {

    private AggregateTestFixture<Product> fixture;

    @Before
    public void setUp() {
        fixture = new AggregateTestFixture<>(Product.class);
    }

    @Test
    public void shouldCreateNewProduct() {
        fixture
                .when(new RegisterProductPreparationCommand("123", "product_def", "receiver", "executor"))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new ProductPreparationRegisteredEvent("123", "product_def", "receiver", "executor"))
                .expectState(product -> {
                    assertSame("product_def", product.productDefId);
                    assertSame("receiver", product.receiverId);
                    assertSame("executor", product.executorId);

                    assertSame(ProductState.PREPARED, product.state);
                });
    }

    @Test
    public void shouldCancelProductPreparation() {
        fixture
                .given(new ProductPreparationRegisteredEvent("123", "product_def", "receiver", "executor"))
                .when(new CancelProductPreparationCommand("123"))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new ProductPreparationCancelledEvent("123", "product_def"))
                .expectState(product -> assertSame(ProductState.CANCELLED, product.state));
    }

    @Test
    public void shouldThrowExceptionForCancelWhenPreparationAlreadyCanceled() {
        fixture
                .given(new ProductPreparationRegisteredEvent("123", "product_def", "receiver", "executor"),
                        new ProductPreparationCancelledEvent("123", "product_def"))
                .when(new CancelProductPreparationCommand("123"))
                .expectNoEvents()
                .expectException(IllegalStateException.class);
    }

    @Test
    public void shouldChangeProductReceiverWithExecutor() {
        fixture
                .given(new ProductPreparationRegisteredEvent("123", "product_def", "receiver", "executor"))
                .when(new ChangeProductReceiverCommand("123", "new_receiver", "executor"))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new ProductReceiverChangedEvent("123", "product_def", "new_receiver"))
                .expectState(product -> assertSame("new_receiver", product.receiverId));
    }

    @Test
    public void shouldChangeProductReceiverWithReceiver() {
        fixture
                .given(new ProductPreparationRegisteredEvent("123", "product_def", "receiver", "executor"))
                .when(new ChangeProductReceiverCommand("123", "new_receiver", "receiver"))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new ProductReceiverChangedEvent("123", "product_def", "new_receiver"))
                .expectState(product -> assertSame("new_receiver", product.receiverId));
    }

    @Test
    public void shouldThrowExceptionWhenProductReceiverChangedWithOtherUser() {
        fixture
                .given(new ProductPreparationRegisteredEvent("123", "product_def", "receiver", "executor"))
                .when(new ChangeProductReceiverCommand("123", "new_receiver", "other_user"))
                .expectNoEvents()
                .expectException(IllegalStateException.class);
    }

    @Test
    public void shouldThrowExceptionWhenProductReceiverChangedOnCancelledProduct() {
        fixture
                .given(new ProductPreparationRegisteredEvent("123", "product_def", "receiver", "executor"),
                        new ProductPreparationCancelledEvent("123", "product_def"))
                .when(new ChangeProductReceiverCommand("123", "new_receiver", "executor"))
                .expectNoEvents()
                .expectException(IllegalStateException.class);
    }

}
