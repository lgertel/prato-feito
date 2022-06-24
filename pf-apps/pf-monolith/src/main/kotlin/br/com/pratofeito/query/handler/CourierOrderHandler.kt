package br.com.pratofeito.query.handler

import br.com.pratofeito.courier.domain.api.CourierOrderAssignedEvent
import br.com.pratofeito.courier.domain.api.CourierOrderCreatedEvent
import br.com.pratofeito.courier.domain.api.CourierOrderDeliveredEvent
import br.com.pratofeito.courier.domain.api.CourierOrderNotAssignedEvent
import br.com.pratofeito.courier.domain.api.model.CourierOrderState
import br.com.pratofeito.query.model.CourierOrderEntity
import br.com.pratofeito.query.repository.CourierOrderRepository
import br.com.pratofeito.query.repository.CourierRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.AllowReplay
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
import org.axonframework.eventhandling.SequenceNumber
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Component

@Component
@ProcessingGroup("courierorder")
internal class CourierOrderHandler(private val repository: CourierOrderRepository,
                                   private val courierRepository: CourierRepository,
                                   private val messagingTemplate: SimpMessageSendingOperations) {

	@EventHandler
	@AllowReplay(true)
	fun handle(event: CourierOrderCreatedEvent, @SequenceNumber aggregateVersion: Long) {
		val record = CourierOrderEntity(event.aggregateIdentifier.identifier,
			aggregateVersion, null, CourierOrderState.CREATED)
		repository.save(record)
		broadcastUpdates()
	}

	@EventHandler
	@AllowReplay(true)
	fun handle(event: CourierOrderAssignedEvent, @SequenceNumber aggregateVersion: Long) {
		val courierEntity = courierRepository.findById(event.courierId.identifier).get()
		val record = repository.findById(event.aggregateIdentifier.identifier).get()
		record.state = CourierOrderState.ASSIGNED
		record.courier = courierEntity
		repository.save(record)
		broadcastUpdates()
	}

	@EventHandler
	@AllowReplay(true)
	fun handle(event: CourierOrderNotAssignedEvent, @SequenceNumber aggregateVersion: Long) {
		// Do nothing
		broadcastUpdates()
	}

	@EventHandler
	@AllowReplay(true)
	fun handle(event: CourierOrderDeliveredEvent, @SequenceNumber aggregateVersion: Long) {
		val record = repository.findById(event.aggregateIdentifier.identifier).get()
		record.state = CourierOrderState.DELIVERED
		repository.save(record)
		broadcastUpdates()
	}

	@ResetHandler
	fun onReset() = repository.deleteAll()

	private fun broadcastUpdates() = messagingTemplate.convertAndSend("/topic/couriers/orders.updates", repository.findAll())
}