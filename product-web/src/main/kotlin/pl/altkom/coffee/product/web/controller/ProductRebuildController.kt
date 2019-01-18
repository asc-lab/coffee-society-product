package pl.altkom.coffee.product.web.controller

import org.axonframework.config.EventProcessingModule
import org.axonframework.eventhandling.TrackingEventProcessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/product/rebuild")
class ProductRebuildController {

    @Autowired
    lateinit var eventProcessingConfiguration: EventProcessingModule

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("productEntry")
    fun rebuildProductEntryProjection() {
        with(eventProcessingConfiguration.eventProcessor<TrackingEventProcessor>("ProductEntryProjection").get()) {
            shutDown()
            resetTokens()
            start()
        }
    }

}