import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import pl.altkom.coffee.product.api.ProductPreparationCancelledEvent;
import pl.altkom.coffee.product.api.ProductPreparationEndedEvent;
import pl.altkom.coffee.product.api.ProductPreparationStartedEvent;
import pl.altkom.coffee.product.api.ProductReceiverChangedEvent;
import pl.altkom.coffee.product.api.enums.ProductState;
import pl.altkom.coffee.product.domain.*;

import java.util.ArrayList;

import static org.junit.Assert.assertSame;


public class ProductTest {

    private AggregateTestFixture<Product> fixture;

    @Before
    public void setUp() {
        fixture = new AggregateTestFixture<>(Product.class);
    }

    @Test
    public void shouldCreateNewProduct() {
        withUser("executor");

        fixture
                .when(new BeginProductPreparationCommand("123", "product_def", "receiver"))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new ProductPreparationStartedEvent("123", "product_def", "receiver", "executor"))
                .expectState(product -> {
                    assertSame(product.productDefId, "product_def");
                    assertSame(product.receiverName, "receiver");
                    assertSame(product.executorName, "executor");

                    assertSame(product.state, ProductState.IN_PREPARATION);
                });
    }

    @Test
    public void shouldEndProductPreparation() {
        withUser("executor");

        fixture
                .given(new ProductPreparationStartedEvent("123", "product_def", "receiver", "executor"))
                .when(new EndProductPreparationCommand("123"))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new ProductPreparationEndedEvent("123"))
                .expectState(product -> {
                    assertSame(product.productDefId, "product_def");
                    assertSame(product.receiverName, "receiver");
                    assertSame(product.executorName, "executor");

                    assertSame(product.state, ProductState.PREPARED);
                });
    }

    @Test
    public void shouldThrowExceptionForEndWhenPreparationAlreadyEnded() {
        withUser("executor");

        fixture
                .given(new ProductPreparationStartedEvent("123", "product_def", "receiver", "executor"),
                        new ProductPreparationEndedEvent("123"))
                .when(new EndProductPreparationCommand("123"))
                .expectNoEvents()
                .expectException(IllegalStateException.class);
    }

    @Test
    public void shouldCancelProductPreparation() {
        withUser("executor");

        fixture
                .given(new ProductPreparationStartedEvent("123", "product_def", "receiver", "executor"))
                .when(new CancelProductPreparationCommand("123"))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new ProductPreparationCancelledEvent("123"))
                .expectState(product -> assertSame(product.state, ProductState.CANCELLED));
    }

    @Test
    public void shouldThrowExceptionForCancelWhenPreparationAlreadyEnded() {
        withUser("executor");

        fixture
                .given(new ProductPreparationStartedEvent("123", "product_def", "receiver", "executor"),
                        new ProductPreparationEndedEvent("123"))
                .when(new CancelProductPreparationCommand("123"))
                .expectNoEvents()
                .expectException(IllegalStateException.class);
    }

    @Test
    public void shouldThrowExceptionForCancelWhenPreparationAlreadyCanceled() {
        withUser("executor");

        fixture
                .given(new ProductPreparationStartedEvent("123", "product_def", "receiver", "executor"),
                        new ProductPreparationCancelledEvent("123"))
                .when(new CancelProductPreparationCommand("123"))
                .expectNoEvents()
                .expectException(IllegalStateException.class);
    }

    @Test
    public void shouldChangeProductReceiverWithExecutor() {
        withUser("executor");

        fixture
                .given(new ProductPreparationStartedEvent("123", "product_def", "receiver", "executor"))
                .when(new ChangeProductReceiverCommand("123", "new_receiver"))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new ProductReceiverChangedEvent("123", "new_receiver"))
                .expectState(product -> assertSame(product.receiverName, "new_receiver"));
    }

    @Test
    public void shouldChangeProductReceiverWithReceiver() {
        withUser("receiver");

        fixture
                .given(new ProductPreparationStartedEvent("123", "product_def", "receiver", "executor"))
                .when(new ChangeProductReceiverCommand("123", "new_receiver"))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new ProductReceiverChangedEvent("123", "new_receiver"))
                .expectState(product -> assertSame(product.receiverName, "new_receiver"));
    }

    @Test
    public void shouldThrowExceptionWhenProductReceiverChangedWithOtherUser() {
        withUser("other_user");

        fixture
                .given(new ProductPreparationStartedEvent("123", "product_def", "receiver", "executor"))
                .when(new ChangeProductReceiverCommand("123", "new_receiver"))
                .expectNoEvents()
                .expectException(IllegalStateException.class);
    }

    @Test
    public void shouldThrowExceptionWhenProductReceiverChangedOnCancelledProduct() {
        withUser("executor");

        fixture
                .given(new ProductPreparationStartedEvent("123", "product_def", "receiver", "executor"),
                        new ProductPreparationCancelledEvent("123"))
                .when(new ChangeProductReceiverCommand("123", "new_receiver"))
                .expectNoEvents()
                .expectException(IllegalStateException.class);
    }


    private void withUser(String userName) {
        Authentication authenticatedUser = new UsernamePasswordAuthenticationToken(
                new User(userName, "", new ArrayList<>()), null);
        SecurityContextHolder.setContext(
                new SecurityContextImpl(authenticatedUser));
    }
}