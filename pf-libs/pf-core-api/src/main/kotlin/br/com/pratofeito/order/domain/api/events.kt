package br.com.pratofeito.order.domain.api

import br.com.pratofeito.common.domain.api.AuditableAbstractEvent
import br.com.pratofeito.common.domain.api.model.AuditEntry
import br.com.pratofeito.customer.domain.api.model.CustomerId
import br.com.pratofeito.order.domain.api.model.OrderDetails
import br.com.pratofeito.order.domain.api.model.OrderId
import br.com.pratofeito.restaurant.domain.api.model.RestaurantId
import org.springframework.core.annotation.Order


abstract class OrderEvent(open val aggregateIdentifier: OrderId, override val auditEntry: AuditEntry) :
  AuditableAbstractEvent(auditEntry)

class OrderCreationInitiatedEvent(
  val orderDetails: OrderDetails,
  override val aggregateIdentifier: OrderId,
  override val auditEntry: AuditEntry
) : OrderEvent(aggregateIdentifier, auditEntry)

class OrderDeliveredEvent(
  override val aggregateIdentifier: OrderId,
  override val auditEntry: AuditEntry
) : OrderEvent(aggregateIdentifier, auditEntry)

class OrderPreparedEvent(
  override val aggregateIdentifier: OrderId,
  override val auditEntry: AuditEntry
) : OrderEvent(aggregateIdentifier, auditEntry)

class OrderReadyForDeliveryEvent(
  override val aggregateIdentifier: OrderId,
  override val auditEntry: AuditEntry
) : OrderEvent(aggregateIdentifier, auditEntry)

class OrderRejectedEvent(
  override val aggregateIdentifier: OrderId,
  override val auditEntry: AuditEntry
) : OrderEvent(aggregateIdentifier, auditEntry)

class OrderVerifiedByCustomerEvent(
  override val aggregateIdentifier: OrderId,
  val customerId: CustomerId,
  override val auditEntry: AuditEntry
) : OrderEvent(aggregateIdentifier, auditEntry)

class OrderVerifiedByRestaurantEvent(
  override val aggregateIdentifier: OrderId,
  val restaurantId: RestaurantId,
  override val auditEntry: AuditEntry
) : OrderEvent(aggregateIdentifier, auditEntry)