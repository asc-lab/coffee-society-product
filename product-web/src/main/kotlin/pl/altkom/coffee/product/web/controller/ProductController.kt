package pl.altkom.coffee.product.web.controller

import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.altkom.coffee.product.api.dto.BeginProductPreparationRequest
import pl.altkom.coffee.product.api.dto.CancelProductPreparationRequest
import pl.altkom.coffee.product.api.dto.ChangeProductReceiverRequest
import pl.altkom.coffee.product.api.dto.EndProductPreparationRequest
import pl.altkom.coffee.product.domain.BeginProductPreparationCommand
import pl.altkom.coffee.product.domain.CancelProductPreparationCommand
import pl.altkom.coffee.product.domain.ChangeProductReceiverCommand
import pl.altkom.coffee.product.domain.EndProductPreparationCommand
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/product")
class ProductController(private val commandGateway: CommandGateway) {

    @PreAuthorize("hasAuthority('BARISTA')")
    @PostMapping("/begin")
    fun beginProductPreparation(@RequestBody request: BeginProductPreparationRequest): Mono<Void> {
        val executingUser = SecurityContextHolder.getContext().authentication.principal as String

        return Mono.fromFuture(commandGateway.send<Void>(
                BeginProductPreparationCommand(request.id, request.productDefId, request.productReceiverName, executingUser)))
    }

    @PreAuthorize("hasAuthority('BARISTA')")
    @PostMapping("/end")
    fun endProductPreparation(@RequestBody request: EndProductPreparationRequest): Mono<Void> {
        return Mono.fromFuture(commandGateway.send<Void>(
                EndProductPreparationCommand(request.id)))
    }

    @PreAuthorize("hasAuthority('BARISTA')")
    @PostMapping("/cancel")
    fun cancelProductPreparation(@RequestBody request: CancelProductPreparationRequest): Mono<Void> {
        return Mono.fromFuture(commandGateway.send<Void>(
                CancelProductPreparationCommand(request.id)))
    }

    @PreAuthorize("hasAnyAuthority('MEMBER','BARISTA')")
    @PostMapping("/changeReceiver")
    fun changeReciver(@RequestBody request: ChangeProductReceiverRequest): Mono<Void> {
        val executingUser = SecurityContextHolder.getContext().authentication.principal as String

        return Mono.fromFuture(commandGateway.send<Void>(
                ChangeProductReceiverCommand(request.id, request.productReceiverNewName, executingUser)))
    }
}