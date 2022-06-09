package br.com.pratofeito.query.handler

import br.com.pratofeito.customer.domain.api.CustomerCreatedEvent
import br.com.pratofeito.query.model.CustomerEntity
import br.com.pratofeito.query.repository.CustomerRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.AllowReplay
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
import org.axonframework.eventhandling.SequenceNumber
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Component

@Component
@ProcessingGroup("customer")
internal class CustomerHandler(
  private val repository: CustomerRepository,
  private val messagingTemplate: SimpMessageSendingOperations
) {

  @EventHandler
  @AllowReplay(true)
  fun handle(event: CustomerCreatedEvent, @SequenceNumber aggregateVersion: Long) {
    repository.save(
      CustomerEntity(
        event.aggregateIdentifier.identifier,
        aggregateVersion,
        event.name.firstName,
        event.name.lastName,
        event.orderLimit.amount)
    )

    broadcastUpdates()
  }

  @ResetHandler
  fun onReset() = repository.deleteAll()

  private fun broadcastUpdates() = messagingTemplate.convertAndSend("/topic/customers.updates", repository.findAll())
}