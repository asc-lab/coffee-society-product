import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.Test;
import pl.altkom.coffee.product.api.ProductPreparationStartedEvent;
import pl.altkom.coffee.product.domain.BeginProductPreparationCommand;
import pl.altkom.coffee.product.domain.Product;


public class ProductTest {

    private AggregateTestFixture<Product> fixture;

    @Before
    public void setUp() {
        fixture = new AggregateTestFixture<>(Product.class);
    }

    @Test
    public void shouldCreateNewProduct() {
        fixture
                .when(new BeginProductPreparationCommand("123", "some_name", "ss", "ddd"))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new ProductPreparationStartedEvent("123", "some_name", "ss", "ddd"));

    }
}