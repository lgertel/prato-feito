package br.com.pratofeito.restaurant.domain

import br.com.pratofeito.restaurant.domain.api.CreateRestaurantCommand
import br.com.pratofeito.restaurant.domain.api.CreateRestaurantOrderCommand
import br.com.pratofeito.restaurant.domain.api.RestaurantCreatedEvent
import br.com.pratofeito.restaurant.domain.api.model.RestaurantId
import br.com.pratofeito.restaurant.domain.api.model.RestaurantMenu
import br.com.pratofeito.restaurant.domain.api.model.RestaurantState
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.apache.commons.lang3.builder.ToStringBuilder
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import java.util.stream.Collector
import java.util.stream.Collectors

@Aggregate
internal class Restaurant {

  @AggregateIdentifier
  private lateinit var id: RestaurantId
  private lateinit var name: String
  private lateinit var menu: RestaurantMenu
  private lateinit var state: RestaurantState

  constructor()

  @CommandHandler
  constructor(command: CreateRestaurantCommand) {
    AggregateLifecycle.apply(
      RestaurantCreatedEvent(command.name, command.menu, command.targetAggregateIdentifier, command.auditEntry)
    )
  }

  @EventSourcingHandler
  fun on(event: RestaurantCreatedEvent) {
    id = event.aggregateIdentifier
    name = event.name
    menu = event.menu
    state = RestaurantState.OPEN
  }

  @CommandHandler
  fun handle(command: CreateRestaurantOrderCommand) {
    if (
      menu.menuItems.stream().map { mi -> mi.id }.collect(Collectors.toList()).containsAll(
        command.orderDetails.lineItems.stream().map { li -> li.menuItemId }.collect(Collectors.toList())
      )
    ) {
      AggregateLifecycle.createNew(RestaurantOrder::class.java) { RestaurantOrder(command) }
    } else {
      throw UnsupportedOperationException("Pedido inválido - não existe no menu")
    }
  }

  override fun toString(): String = ToStringBuilder.reflectionToString(this)

  override fun equals(other: Any?): Boolean = EqualsBuilder.reflectionEquals(this, other)

  override fun hashCode(): Int = HashCodeBuilder.reflectionHashCode(this)
}