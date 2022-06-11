package br.com.pratofeito.courier.domain

import br.com.pratofeito.courier.domain.api.AssignCourierOrderToCourierCommand
import br.com.pratofeito.courier.domain.api.CourierOrderCreatedEvent
import br.com.pratofeito.courier.domain.api.CreateCourierCommand
import br.com.pratofeito.courier.domain.api.CreateCourierOrderCommand
import br.com.pratofeito.courier.domain.api.model.CourierID
import br.com.pratofeito.courier.domain.api.model.CourierOrderId
import br.com.pratofeito.courier.domain.api.model.CourierOrderState
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
internal class CourierOrder {

  private lateinit var id: CourierOrderId
  private lateinit var courierID: CourierID
  private lateinit var state: CourierOrderState

  constructor()

  @CommandHandler
  constructor(command: CreateCourierOrderCommand) {
    AggregateLifecycle.apply(CourierOrderCreatedEvent(command.targetAggregateIdentifier, command.auditEntry))
  }

  @EventSourcingHandler
  fun on(event: CourierOrderCreatedEvent) {
    id = event.aggregateIdentifier
    state = CourierOrderState.CREATED
  }

//  fun assignToCourier(command: AssignCourierOrderToCourierCommand) {
//  }

  // SAGA
  // Inicializar atribuição de pedido
  // validar entrega de pedido com sucesso
  // validar entrega de pedido com erro
  //


}