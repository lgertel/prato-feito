package br.com.pratofeito.courier.domain.model

import br.com.pratofeito.common.domain.api.model.AuditEntry
import br.com.pratofeito.courier.domain.api.CourierEvent
import br.com.pratofeito.courier.domain.api.CourierOrderEvent
import br.com.pratofeito.courier.domain.api.model.CourierId
import br.com.pratofeito.courier.domain.api.model.CourierOrderId

/**
 * Internal events, scoped to 'courier' bounded context only
 */


/**
 * Courier aggregate event, noting that `courier` was not found
 */
internal data class CourierNotFoundForOrderInternalEvent(override val aggregateIdentifier: CourierId,
                                                         val orderId: CourierOrderId,
                                                         override val auditEntry: AuditEntry) : CourierEvent(aggregateIdentifier, auditEntry)

/**
 * CourierOrder aggregate event, noting that 'courier order' assigning to `courier' has been initiated
 */
internal data class CourierOrderAssigningInitiatedInternalEvent(
  val courierId: CourierId,
  override val aggregateIdentifier: CourierOrderId,
  override val auditEntry: AuditEntry) : CourierOrderEvent(aggregateIdentifier, auditEntry)

/**
 * Courier aggregate event, noting that `courier` validated the `courier order` with error
 */
internal data class CourierValidatedOrderWithErrorInternalEvent(override val aggregateIdentifier: CourierId, val orderId: CourierOrderId, override val auditEntry: AuditEntry) : CourierEvent(aggregateIdentifier, auditEntry)

/**
 * Courier aggregate event, noting that `courier` validated the `courier order` with error
 */
internal data class CourierValidatedOrderWithSuccessInternalEvent(override val aggregateIdentifier: CourierId, val orderId: CourierOrderId, override val auditEntry: AuditEntry) : CourierEvent(aggregateIdentifier, auditEntry)