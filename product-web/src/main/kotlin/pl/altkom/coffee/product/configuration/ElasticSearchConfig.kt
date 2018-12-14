package pl.altkom.coffee.product.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories

@Configuration
@EnableElasticsearchRepositories("pl.altkom.coffee.product.query")
class ElasticSearchConfig
