package br.com.pratofeito.courier.domain

import br.com.pratofeito.common.domain.api.model.PersonName
import br.com.pratofeito.courier.domain.api.CourierCreatedEvent
import br.com.pratofeito.courier.domain.api.CreateCourierCommand
import br.com.pratofeito.courier.domain.api.model.CourierID
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
internal class Courier {

  @AggregateIdentifier
  private lateinit var id: CourierID
  private lateinit var name: PersonName
  private var maxNumberOfActiveOrders: Int = 5
  private var numberOfActiveOrders: Int = 0

  constructor()

  @CommandHandler
  constructor(command: CreateCourierCommand) {
    AggregateLifecycle.apply(CourierCreatedEvent(command.personName,
      command.maxNumberOfActiveOrders,
      command.targetAggregateIdentifier,
      command.auditEntry)
    )

    @EventSourcingHandler
    fun on(event: CourierCreatedEvent) {
      id = event.aggregateIdentifier
      name = event.name
      maxNumberOfActiveOrders = event.maxNumberOfActiveOrders
      numberOfActiveOrders += 1
    }
  }











}