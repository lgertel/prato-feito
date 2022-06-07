package br.com.pratofeito.web

import br.com.pratofeito.common.domain.api.model.AuditEntry
import br.com.pratofeito.common.domain.api.model.Money
import br.com.pratofeito.common.domain.api.model.PersonName
import br.com.pratofeito.customer.domain.api.CreateCustomerCommand
import br.com.pratofeito.query.model.CustomerEntity
import br.com.pratofeito.query.repository.CustomerRepository
import org.axonframework.commandhandling.callbacks.LoggingCallback
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.annotation.SubscribeMapping
import org.springframework.stereotype.Controller
import java.math.BigDecimal
import java.util.Calendar

@Controller
class WebController(
  private val commandGateway: CommandGateway,
  private val customerRepository: CustomerRepository
) {

  private val auditEntry: AuditEntry
    get() = AuditEntry("TEST", Calendar.getInstance().time)

  @MessageMapping("/customers/createcommand")
  fun createCustomer(request: CreateCustomerDTO) =
    commandGateway.send(
      CreateCustomerCommand(
        PersonName(request.firstName, request.lastName),
        Money(request.orderLimit),
        auditEntry
      ),
      LoggingCallback.INSTANCE
    )

  @SubscribeMapping("/customers")
  fun allCustomers(): Iterable<CustomerEntity> = customerRepository.findAll()

  @SubscribeMapping("/customers/{id}")
  fun getCustomer(@DestinationVariable id: String) =
    customerRepository.findById(id)

}

data class CreateCustomerDTO(val firstName: String, val lastName: String, val orderLimit: BigDecimal)