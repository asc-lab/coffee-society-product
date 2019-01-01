import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.Test;
import pl.altkom.coffee.product.api.enums.ProductState;
import pl.altkom.coffee.product.api.ProductPreparationCancelledEvent;
import pl.altkom.coffee.product.api.ProductPreparationEndedEvent;
import pl.altkom.coffee.product.api.ProductPreparationStartedEvent;
import pl.altkom.coffee.product.api.ProductReceiverChangedEvent;
import pl.altkom.coffee.product.domain.*;

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
                .when(new BeginProductPreparationCommand("123", "product_def", "receiver","executor"))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new ProductPreparationStartedEvent("123", "product_def", "receiver", "executor"))
                .expectState(product -> {
                    assertSame("product_def", product.productDefId);
                    assertSame("receiver", product.receiverName);
                    assertSame("executor", product.executorName);

                    assertSame(ProductState.IN_PREPARATION, product.state);
                });
    }

    @Test
    public void shouldEndProductPreparation() {
        fixture
                .given(new ProductPreparationStartedEvent("123", "product_def", "receiver", "executor"))
                .when(new EndProductPreparationCommand("123"))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new ProductPreparationEndedEvent("123"))
                .expectState(product -> {
                    assertSame("product_def", product.productDefId);
                    assertSame("receiver", product.receiverName);
                    assertSame("executor", product.executorName);

                    assertSame(ProductState.PREPARED, product.state);
                });
    }

    @Test
    public void shouldThrowExceptionForEndWhenPreparationAlreadyEnded() {
        fixture
                .given(new ProductPreparationStartedEvent("123", "product_def", "receiver", "executor"),
                        new ProductPreparationEndedEvent("123"))
                .when(new EndProductPreparationCommand("123"))
                .expectNoEvents()
                .expectException(IllegalStateException.class);
    }

    @Test
    public void shouldCancelProductPreparation() {
        fixture
                .given(new ProductPreparationStartedEvent("123", "product_def", "receiver", "executor"))
                .when(new CancelProductPreparationCommand("123"))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new ProductPreparationCancelledEvent("123"))
                .expectState(product -> assertSame(ProductState.CANCELLED, product.state));
    }

    @Test
    public void shouldThrowExceptionForCancelWhenPreparationAlreadyEnded() {
        fixture
                .given(new ProductPreparationStartedEvent("123", "product_def", "receiver", "executor"),
                        new ProductPreparationEndedEvent("123"))
                .when(new CancelProductPreparationCommand("123"))
                .expectNoEvents()
                .expectException(IllegalStateException.class);
    }

    @Test
    public void shouldThrowExceptionForCancelWhenPreparationAlreadyCanceled() {
        fixture
                .given(new ProductPreparationStartedEvent("123", "product_def", "receiver", "executor"),
                        new ProductPreparationCancelledEvent("123"))
                .when(new CancelProductPreparationCommand("123"))
                .expectNoEvents()
                .expectException(IllegalStateException.class);
    }

    @Test
    public void shouldChangeProductReceiverWithExecutor() {
        fixture
                .given(new ProductPreparationStartedEvent("123", "product_def", "receiver", "executor"))
                .when(new ChangeProductReceiverCommand("123", "new_receiver", "executor"))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new ProductReceiverChangedEvent("123", "new_receiver"))
                .expectState(product -> assertSame("new_receiver", product.receiverName));
    }

    @Test
    public void shouldChangeProductReceiverWithReceiver() {
        fixture
                .given(new ProductPreparationStartedEvent("123", "product_def", "receiver", "executor"))
                .when(new ChangeProductReceiverCommand("123", "new_receiver", "receiver"))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new ProductReceiverChangedEvent("123", "new_receiver"))
                .expectState(product -> assertSame("new_receiver", product.receiverName));
    }

    @Test
    public void shouldThrowExceptionWhenProductReceiverChangedWithOtherUser() {
        fixture
                .given(new ProductPreparationStartedEvent("123", "product_def", "receiver", "executor"))
                .when(new ChangeProductReceiverCommand("123", "new_receiver", "other_user"))
                .expectNoEvents()
                .expectException(IllegalStateException.class);
    }

    @Test
    public void shouldThrowExceptionWhenProductReceiverChangedOnCancelledProduct() {
        fixture
                .given(new ProductPreparationStartedEvent("123", "product_def", "receiver", "executor"),
                        new ProductPreparationCancelledEvent("123"))
                .when(new ChangeProductReceiverCommand("123", "new_receiver", "executor"))
                .expectNoEvents()
                .expectException(IllegalStateException.class);
    }

}