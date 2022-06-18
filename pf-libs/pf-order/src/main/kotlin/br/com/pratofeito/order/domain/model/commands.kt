package br.com.pratofeito.order.domain.model

import br.com.pratofeito.common.domain.api.model.AuditEntry
import br.com.pratofeito.customer.domain.api.model.CustomerId
import br.com.pratofeito.order.domain.api.OrderCommand
import br.com.pratofeito.order.domain.api.model.OrderId
import br.com.pratofeito.restaurant.domain.api.model.RestaurantId
import org.axonframework.modelling.command.TargetAggregateIdentifier

internal data class MarkOrderAsDeliveredInternalCommand(
  @TargetAggregateIdentifier override val targetAggregateIdentifier: OrderId,
  override val auditEntry: AuditEntry
) : OrderCommand(targetAggregateIdentifier, auditEntry)

internal data class MarkOrderAsPreparedInternalCommand(
  @TargetAggregateIdentifier override val targetAggregateIdentifier: OrderId,
  override val auditEntry: AuditEntry
) : OrderCommand(targetAggregateIdentifier, auditEntry)

internal data class MarkOrderAsReadyForDeliveryInternalCommand(
  @TargetAggregateIdentifier override val targetAggregateIdentifier: OrderId,
  override val auditEntry: AuditEntry
) : OrderCommand(targetAggregateIdentifier, auditEntry)

internal data class MarkOrderAsRejectedInternalCommand(
  @TargetAggregateIdentifier override val targetAggregateIdentifier: OrderId,
  override val auditEntry: AuditEntry
) : OrderCommand(targetAggregateIdentifier, auditEntry)

internal data class MarkOrderAsVerifiedByCustomerInternalCommand(
  @TargetAggregateIdentifier override val targetAggregateIdentifier: OrderId,
  val customerId: CustomerId,
  override val auditEntry: AuditEntry
) : OrderCommand(targetAggregateIdentifier, auditEntry)

internal data class MarkOrderAsVerifiedByRestaurantInternalCommand(
  @TargetAggregateIdentifier override val targetAggregateIdentifier: OrderId,
  val restaurantId: RestaurantId,
  override val auditEntry: AuditEntry
) : OrderCommand(targetAggregateIdentifier, auditEntry)