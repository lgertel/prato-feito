package br.com.pratofeito.restaurant.domain.api

import br.com.pratofeito.common.domain.api.AuditableAbstractCommand
import br.com.pratofeito.common.domain.api.model.AuditEntry
import br.com.pratofeito.restaurant.domain.api.model.RestaurantId
import br.com.pratofeito.restaurant.domain.api.model.RestaurantMenu
import br.com.pratofeito.restaurant.domain.api.model.RestaurantOrderDetails
import br.com.pratofeito.restaurant.domain.api.model.RestaurantOrderId
import org.axonframework.modelling.command.TargetAggregateIdentifier
import javax.validation.Valid

abstract class RestaurantCommand(open val targetAggregateIdentifier: RestaurantId, override val auditEntry: AuditEntry)
  : AuditableAbstractCommand(auditEntry)

abstract class RestaurantOrderCommand(open val targetAggregateIdentifier: RestaurantOrderId, override val auditEntry: AuditEntry)
  : AuditableAbstractCommand(auditEntry)

data class CreateRestaurantCommand(val name: String,
                                   @field:Valid val menu: RestaurantMenu,
                                   @TargetAggregateIdentifier override val targetAggregateIdentifier: RestaurantId,
                                   override val auditEntry: AuditEntry)
  : RestaurantCommand(targetAggregateIdentifier, auditEntry) {
    constructor(name: String, menu: RestaurantMenu, auditEntry: AuditEntry)
        : this(name, menu, RestaurantId(), auditEntry)
  }

data class CreateRestaurantOrderCommand(@TargetAggregateIdentifier override val targetAggregateIdentifier: RestaurantId,
                                        @field:Valid val orderDetails: RestaurantOrderDetails,
                                        val restaurantOrderId: RestaurantOrderId,
                                        override val auditEntry: AuditEntry)
  : RestaurantCommand(targetAggregateIdentifier, auditEntry) {
    constructor(targetAggregateIdentifier: RestaurantId, orderDetails: RestaurantOrderDetails, auditEntry: AuditEntry)
        : this(targetAggregateIdentifier, orderDetails, RestaurantOrderId(), auditEntry)
  }

data class MarkRestaurantOrderAsPreparedCommand(@TargetAggregateIdentifier override val targetAggregateIdentifier: RestaurantOrderId,
                                       override val auditEntry: AuditEntry)
  : RestaurantOrderCommand(targetAggregateIdentifier, auditEntry)
















