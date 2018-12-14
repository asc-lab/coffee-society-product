package pl.altkom.coffee.product.web.controller

import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.altkom.coffee.product.api.dto.BeginProductPreparationRequest
import pl.altkom.coffee.product.api.dto.ChangeProductReceiverRequest
import pl.altkom.coffee.product.domain.BeginProductPreparationCommand
import pl.altkom.coffee.product.domain.ChangeProductReceiverCommand

@RestController
@RequestMapping("/api/product")
class ProductController(private val commandGateway: CommandGateway) {

    @PreAuthorize("hasAuthority('BARISTA')")
    @PostMapping("/begin")
    fun beginProductPreparation(@RequestBody request: BeginProductPreparationRequest): ResponseEntity<Void> {
        commandGateway.send<Void>(
                BeginProductPreparationCommand(request.id, request.productDefId, request.productReceiverName, request.productName))

        return ResponseEntity.ok().build()
    }

    @PreAuthorize("hasAuthority('BARISTA')")
    @PostMapping("/changeReceiver")
    fun changeReciver(@RequestBody request: ChangeProductReceiverRequest): ResponseEntity<Void> {
        commandGateway.send<Void>(
                ChangeProductReceiverCommand(request.id, request.productReceiverNewName))

        return ResponseEntity.ok().build()
    }
}