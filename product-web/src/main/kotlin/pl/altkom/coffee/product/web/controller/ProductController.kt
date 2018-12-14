package pl.altkom.coffee.product.web.controller

import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
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

@RestController
@RequestMapping("/api/product")
class ProductController(private val commandGateway: CommandGateway) {

    @PreAuthorize("hasAuthority('BARISTA')")
    @PostMapping("/begin")
    fun beginProductPreparation(@RequestBody request: BeginProductPreparationRequest): ResponseEntity<Void> {
        commandGateway.send<Void>(
                BeginProductPreparationCommand(request.id, request.productDefId, request.productReceiverName))

        return ResponseEntity.ok().build()
    }

    @PreAuthorize("hasAuthority('BARISTA')")
    @PostMapping("/end")
    fun endProductPreparation(@RequestBody request: EndProductPreparationRequest): ResponseEntity<Void> {
        commandGateway.send<Void>(
                EndProductPreparationCommand(request.id))

        return ResponseEntity.ok().build()
    }

    @PreAuthorize("hasAuthority('BARISTA')")
    @PostMapping("/cancel")
    fun cancelProductPreparation(@RequestBody request: CancelProductPreparationRequest): ResponseEntity<Void> {
        commandGateway.send<Void>(
                CancelProductPreparationCommand(request.id))

        return ResponseEntity.ok().build()
    }

    @PreAuthorize("hasAnyAuthority('MEMBER','BARISTA')")
    @PostMapping("/changeReceiver")
    fun changeReciver(@RequestBody request: ChangeProductReceiverRequest): ResponseEntity<Void> {
        commandGateway.send<Void>(
                ChangeProductReceiverCommand(request.id, request.productReceiverNewName))

        return ResponseEntity.ok().build()
    }
}