package br.com.pratofeito.order.domain.api

import br.com.pratofeito.common.domain.api.AuditableAbstractCommand
import br.com.pratofeito.common.domain.api.AuditableAbstractEvent
import br.com.pratofeito.common.domain.api.model.AuditEntry
import br.com.pratofeito.order.domain.api.model.OrderId
import br.com.pratofeito.order.domain.api.model.OrderInfo

abstract class OrderCommand(open val targetAggregateIdentifier: OrderId, override val auditEntry: AuditEntry) :
  AuditableAbstractCommand(auditEntry)

data class CreateOrderCommand(
  override val targetAggregateIdentifier: OrderId,
  val orderInfo: OrderInfo,
  override val auditEntry: AuditEntry
) : OrderCommand(targetAggregateIdentifier, auditEntry) {
  constructor(orderInfo: OrderInfo, auditEntry: AuditEntry) : this(OrderId(), orderInfo, auditEntry)
}
