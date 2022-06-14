package br.com.pratofeito.courier.domain.api

import br.com.pratofeito.common.domain.api.AuditableAbstractCommand
import br.com.pratofeito.common.domain.api.model.AuditEntry
import br.com.pratofeito.common.domain.api.model.PersonName
import br.com.pratofeito.courier.domain.api.model.CourierId
import br.com.pratofeito.courier.domain.api.model.CourierOrderId
import org.axonframework.modelling.command.TargetAggregateIdentifier
import javax.validation.Valid

abstract class CourierCommand(open val targetAggregateIdentifier: CourierId, override val auditEntry: AuditEntry)
  : AuditableAbstractCommand(auditEntry)

abstract class CourierOrderCommand(open val targetAggregateIdentifier: CourierOrderId, override val auditEntry: AuditEntry)
  : AuditableAbstractCommand(auditEntry)

data class CreateCourierCommand(@TargetAggregateIdentifier override val targetAggregateIdentifier: CourierId,
                                @field:Valid val personName: PersonName,
                                val maxNumberOfActiveOrders: Int,
                                override val auditEntry: AuditEntry)
  : CourierCommand(targetAggregateIdentifier, auditEntry) {
    constructor(name: PersonName, maxNumberOfActiveOrders: Int, auditEntry: AuditEntry)
        : this(CourierId(), name, maxNumberOfActiveOrders, auditEntry)
  }

data class CreateCourierOrderCommand(@TargetAggregateIdentifier override val targetAggregateIdentifier: CourierOrderId,
                                     override val auditEntry: AuditEntry)
  : CourierOrderCommand(targetAggregateIdentifier, auditEntry) {
    constructor(auditEntry: AuditEntry) : this(CourierOrderId(), auditEntry)
  }

data class AssignCourierOrderToCourierCommand(@TargetAggregateIdentifier override val targetAggregateIdentifier: CourierOrderId,
                                              val courierID: CourierId, override val auditEntry: AuditEntry)
  : CourierOrderCommand(targetAggregateIdentifier, auditEntry)

data class MarkCourierOrderAsDeliveredCommand(@TargetAggregateIdentifier override val targetAggregateIdentifier: CourierOrderId,
                                              override val auditEntry: AuditEntry)
  : CourierOrderCommand(targetAggregateIdentifier, auditEntry)











