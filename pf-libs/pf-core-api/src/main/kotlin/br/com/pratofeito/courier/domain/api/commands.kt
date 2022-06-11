package br.com.pratofeito.courier.domain.api

import br.com.pratofeito.common.domain.api.AuditableAbstractCommand
import br.com.pratofeito.common.domain.api.AuditableAbstractEvent
import br.com.pratofeito.common.domain.api.model.AuditEntry
import br.com.pratofeito.common.domain.api.model.PersonName
import br.com.pratofeito.courier.domain.api.model.CourierID
import br.com.pratofeito.courier.domain.api.model.CourierOrderId
import org.axonframework.modelling.command.TargetAggregateIdentifier
import javax.validation.Valid
import kotlin.math.max

abstract class CourierCommand(open val targetAggregateIdentifier: CourierID, override val auditEntry: AuditEntry)
  : AuditableAbstractCommand(auditEntry)

abstract class CourierOrderCommand(open val targetAggregateIdentifier: CourierOrderId, override val auditEntry: AuditEntry)
  : AuditableAbstractCommand(auditEntry)

data class CreateCourierCommand(@TargetAggregateIdentifier override val targetAggregateIdentifier: CourierID,
                                @field:Valid val personName: PersonName,
                                val maxNumberOfActiveOrders: Int,
                                override val auditEntry: AuditEntry)
  : CourierCommand(targetAggregateIdentifier, auditEntry) {
    constructor(name: PersonName, maxNumberOfActiveOrders: Int, auditEntry: AuditEntry)
        : this(CourierID(), name, maxNumberOfActiveOrders, auditEntry)
  }

data class CreateCourierOrderCommand(@TargetAggregateIdentifier override val targetAggregateIdentifier: CourierOrderId,
                                     override val auditEntry: AuditEntry)
  : CourierOrderCommand(targetAggregateIdentifier, auditEntry) {
    constructor(auditEntry: AuditEntry) : this(CourierOrderId(), auditEntry)
  }

data class AssignCourierOrderToCourierCommand(@TargetAggregateIdentifier override val targetAggregateIdentifier: CourierOrderId,
                                              val courierID: CourierID, override val auditEntry: AuditEntry)
  : CourierOrderCommand(targetAggregateIdentifier, auditEntry)

data class MarkCourierOrderAsDeliveredCommand(@TargetAggregateIdentifier override val targetAggregateIdentifier: CourierOrderId,
                                              override val auditEntry: AuditEntry)
  : CourierOrderCommand(targetAggregateIdentifier, auditEntry)











