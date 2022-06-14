package br.com.pratofeito.courier.domain

import br.com.pratofeito.common.domain.api.model.AuditEntry
import br.com.pratofeito.courier.domain.api.CourierEvent
import br.com.pratofeito.courier.domain.api.CourierOrderEvent
import br.com.pratofeito.courier.domain.api.model.CourierId
import br.com.pratofeito.courier.domain.api.model.CourierOrderId

internal data class CourierNotFoundForOrderInternalEvent(
  override val aggregateIdentifier: CourierId,
  val orderId: CourierOrderId,
  override val auditEntry: AuditEntry
) : CourierEvent(aggregateIdentifier, auditEntry)

internal data class CourierOrderAssigningInitiatedInternalEvent(
  val courierID: CourierId,
  override val aggregateIdentifier: CourierOrderId,
  override val auditEntry: AuditEntry
) : CourierOrderEvent(aggregateIdentifier, auditEntry)

internal data class CourierValidateOrderWithErrorInternalEvent(
  override val aggregateIdentifier: CourierId,
  val orderId: CourierOrderId,
  override val auditEntry: AuditEntry
) : CourierEvent(aggregateIdentifier, auditEntry)

internal data class CourierValidatedOrderWithSuccesInternalEvent(
  override val aggregateIdentifier: CourierId,
  val orderId: CourierOrderId,
  override val auditEntry: AuditEntry
) : CourierEvent(aggregateIdentifier, auditEntry)