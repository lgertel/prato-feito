package br.com.pratofeito.query.handler

import br.com.pratofeito.courier.domain.api.CourierCreatedEvent
import br.com.pratofeito.query.model.CourierEntity
import br.com.pratofeito.query.repository.CourierRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.AllowReplay
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
import org.axonframework.eventhandling.SequenceNumber
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Component

@Component
@ProcessingGroup("courier")
internal class CourierHandler(private val repository: CourierRepository,
                              private val messagingTemplate: SimpMessageSendingOperations) {

	@EventHandler
	@AllowReplay(true)
	fun handle(event: CourierCreatedEvent, @SequenceNumber aggregateVersion: Long) {
		repository.save(CourierEntity(event.aggregateIdentifier.identifier,
			aggregateVersion, event.name.firstName, event.name.lastName,
			event.maxNumberOfActiveOrders, emptyList()))

		broadcastUpdates()
	}

	@ResetHandler
	fun onReset() = repository.deleteAll()

	private fun broadcastUpdates() =
		messagingTemplate.convertAndSend("/topic/couriers.updates", repository.findAll())
}