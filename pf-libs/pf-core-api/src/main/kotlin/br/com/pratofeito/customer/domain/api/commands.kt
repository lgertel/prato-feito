package br.com.pratofeito.customer.domain.api

import br.com.pratofeito.common.domain.api.AuditableAbstractCommand
import br.com.pratofeito.common.domain.api.model.AuditEntry
import br.com.pratofeito.common.domain.api.model.Money
import br.com.pratofeito.common.domain.api.model.PersonName
import br.com.pratofeito.customer.domain.api.model.CustomerId
import br.com.pratofeito.customer.domain.api.model.CustomerOrderId
import org.axonframework.modelling.command.TargetAggregateIdentifier
import javax.validation.Valid


abstract class CustomerCommand(open val targetAggregateIdentifier: CustomerId, override val auditEntry: AuditEntry)
  : AuditableAbstractCommand(auditEntry)

abstract class CustomerOrderCommand(open val targetAggregateIdentifier: CustomerOrderId, override val auditEntry: AuditEntry)
  : AuditableAbstractCommand(auditEntry)

data class CreateCustomerCommand(@TargetAggregateIdentifier override val targetAggregateIdentifier: CustomerId,
                                 @field:Valid val name: PersonName,
                                 val orderLimit: Money,
                                 override val auditEntry: AuditEntry) :
CustomerCommand(targetAggregateIdentifier, auditEntry) {
  constructor(name: PersonName, orderLimit: Money, auditEntry: AuditEntry) : this(CustomerId(), name, orderLimit, auditEntry)
}

data class CreateCustomerOrderCommand(@TargetAggregateIdentifier override val targetAggregateIdentifier: CustomerId,
                                      val customerOrderId: CustomerOrderId,
                                      @field:Valid val orderTotal: Money,
                                      override val auditEntry: AuditEntry)
  : CustomerCommand(targetAggregateIdentifier, auditEntry) {
    constructor(targetAggregateIdentifier: CustomerId, orderTotal: Money, auditEntry: AuditEntry) :
        this(targetAggregateIdentifier, CustomerOrderId(), orderTotal, auditEntry)
  }

data class MarkCustomerOrderAsDeliveredCommand(@TargetAggregateIdentifier override val targetAggregateIdentifier: CustomerOrderId,
                                               override val auditEntry: AuditEntry)
  : CustomerOrderCommand(targetAggregateIdentifier, auditEntry)