package br.com.pratofeito.courier.domain.api

import br.com.pratofeito.common.domain.api.AuditableAbstractEvent
import br.com.pratofeito.common.domain.api.model.AuditEntry
import br.com.pratofeito.common.domain.api.model.PersonName
import br.com.pratofeito.courier.domain.api.model.CourierId
import br.com.pratofeito.courier.domain.api.model.CourierOrderId

abstract class CourierEvent(open val aggregateIdentifier: CourierId, override val auditEntry: AuditEntry) :
  AuditableAbstractEvent(auditEntry)

abstract class CourierOrderEvent(open val aggregateIdentifier: CourierOrderId, override val auditEntry: AuditEntry) :
  AuditableAbstractEvent(auditEntry)

data class CourierCreatedEvent(
  val name: PersonName,
  val maxNumberOfActiveOrders: Int,
  override val aggregateIdentifier: CourierId,
  override val auditEntry: AuditEntry
) : CourierEvent(aggregateIdentifier, auditEntry)

data class CourierOrderAssignedEvent(
  override val aggregateIdentifier: CourierOrderId,
  val courierID: CourierId,
  override val auditEntry: AuditEntry
) : CourierOrderEvent(aggregateIdentifier, auditEntry)

data class CourierOrderCreatedEvent(
  override val aggregateIdentifier: CourierOrderId,
  override val auditEntry: AuditEntry
) : CourierOrderEvent(aggregateIdentifier, auditEntry)

data class CourierOrderDeliveredEvent(
  override val aggregateIdentifier: CourierOrderId,
  override val auditEntry: AuditEntry
) : CourierOrderEvent(aggregateIdentifier, auditEntry)

data class CourierOrderNotAssignedEvent(
  override val aggregateIdentifier: CourierOrderId,
  override val auditEntry: AuditEntry
) : CourierOrderEvent(aggregateIdentifier, auditEntry)