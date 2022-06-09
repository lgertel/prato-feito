package br.com.pratofeito.customer.domain

import br.com.pratofeito.common.domain.api.model.Money
import br.com.pratofeito.customer.domain.api.*
import br.com.pratofeito.customer.domain.api.model.CustomerId
import br.com.pratofeito.customer.domain.api.model.CustomerOrderId
import br.com.pratofeito.customer.domain.api.model.CustomerOrderState
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.apache.commons.lang3.builder.ToStringBuilder
import org.axonframework.commandhandling.CommandHandler

import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate(snapshotTriggerDefinition = "customerOrderSnapshotTriggerDefinition")
internal class CustomerOrder {

  @AggregateIdentifier
  private lateinit var id: CustomerOrderId
  private lateinit var customerId: CustomerId
  private lateinit var state: CustomerOrderState
  private lateinit var orderTotal: Money

  constructor()

  constructor(command: CreateCustomerOrderCommand) {
    AggregateLifecycle.apply(CustomerOrderCreatedEvent(command.orderTotal,
      command.targetAggregateIdentifier,
      command.customerOrderId,
      command.auditEntry))
  }

  @EventSourcingHandler
  fun on(event: CustomerOrderCreatedEvent) {
    this.id = event.customerOrderId
    this.customerId = event.aggregateIdentifier
    this.orderTotal = event.orderTotal
    this.state = CustomerOrderState.CREATED
  }

  @CommandHandler
  fun handle(command: MarkCustomerOrderAsDeliveredCommand) {
    if (CustomerOrderState.CREATED == state) {
      AggregateLifecycle.apply(CustomerOrderDeliveredEvent(command.targetAggregateIdentifier, command.auditEntry))
    } else {
      throw UnsupportedOperationException("The current state is not CREATED")
    }
  }

  @EventSourcingHandler
  fun on(event: CustomerOrderDeliveredEvent) {
    this.state = CustomerOrderState.DELIVERED
  }

  override fun toString(): String = ToStringBuilder.reflectionToString(this)

  override fun equals(other: Any?): Boolean = EqualsBuilder.reflectionEquals(this, other)

  override fun hashCode(): Int = HashCodeBuilder.reflectionHashCode(this)
}