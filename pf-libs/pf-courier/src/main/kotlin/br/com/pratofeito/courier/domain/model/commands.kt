package br.com.pratofeito.courier.domain.model

import br.com.pratofeito.common.domain.api.model.AuditEntry
import br.com.pratofeito.courier.domain.api.CourierOrderCommand
import br.com.pratofeito.courier.domain.api.model.CourierId
import br.com.pratofeito.courier.domain.api.model.CourierOrderId
import org.axonframework.modelling.command.TargetAggregateIdentifier

internal data class MarkCourierOrderAsAssignedInternalCommand(@TargetAggregateIdentifier override val targetAggregateIdentifier: CourierOrderId, val courierId: CourierId, override val auditEntry: AuditEntry) : CourierOrderCommand(targetAggregateIdentifier, auditEntry)

internal data class MarkCourierOrderAsNotAssignedInternalCommand(
  @TargetAggregateIdentifier override val targetAggregateIdentifier: CourierOrderId,
  override val auditEntry: AuditEntry) : CourierOrderCommand(targetAggregateIdentifier, auditEntry)

internal data class ValidateOrderByCourierInternalCommand(
  @TargetAggregateIdentifier override val targetAggregateIdentifier: CourierOrderId,
  val courierId: CourierId, override val auditEntry: AuditEntry) :
  CourierOrderCommand(targetAggregateIdentifier, auditEntry)