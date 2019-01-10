package pl.altkom.coffee.product.web.controller

import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.altkom.coffee.product.api.dto.CancelProductPreparationRequest
import pl.altkom.coffee.product.api.dto.ChangeProductReceiverRequest
import pl.altkom.coffee.product.api.dto.RegisterProductPreparationRequest
import pl.altkom.coffee.product.domain.CancelProductPreparationCommand
import pl.altkom.coffee.product.domain.ChangeProductReceiverCommand
import pl.altkom.coffee.product.domain.RegisterProductPreparationCommand
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/product")
class ProductController(private val commandGateway: CommandGateway) {

    @PreAuthorize("hasAuthority('BARISTA')")
    @PostMapping("/register")
    fun registerProductPreparation(@RequestBody request: RegisterProductPreparationRequest): Mono<Void> {
        return Mono.fromFuture(commandGateway.send<Void>(
                RegisterProductPreparationCommand(
                        request.id,
                        request.productDefId,
                        request.productReceiverId,
                        request.productExecutorId
                )))
    }

    @PreAuthorize("hasAuthority('BARISTA')")
    @PostMapping("/cancel")
    fun cancelProductPreparation(@RequestBody request: CancelProductPreparationRequest): Mono<Void> {
        return Mono.fromFuture(commandGateway.send<Void>(
                CancelProductPreparationCommand(
                        request.id,
                        request.productDefId
                )))
    }

    @PreAuthorize("hasAnyAuthority('MEMBER','BARISTA')")
    @PostMapping("/changeReceiver")
    fun changeReceiver(@RequestBody request: ChangeProductReceiverRequest): Mono<Void> {
        return Mono.fromFuture(commandGateway.send<Void>(
                ChangeProductReceiverCommand(
                        request.id,
                        request.productReceiverNewId,
                        request.executorId
                )))
    }
}
