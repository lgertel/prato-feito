package br.com.pratofeito.restaurant.domain.api

import br.com.pratofeito.common.domain.api.AuditableAbstractEvent
import br.com.pratofeito.common.domain.api.model.AuditEntry
import br.com.pratofeito.restaurant.domain.api.model.RestaurantId
import br.com.pratofeito.restaurant.domain.api.model.RestaurantMenu
import br.com.pratofeito.restaurant.domain.api.model.RestaurantOrderId
import br.com.pratofeito.restaurant.domain.api.model.RestaurantOrderLineItem

abstract class RestaurantEvent(open val aggregateIdentifier: RestaurantId, override val auditEntry: AuditEntry)
  : AuditableAbstractEvent(auditEntry)

abstract class RestaurantOrderEvent(open val aggregateIdentifier: RestaurantOrderId, override val auditEntry: AuditEntry)
  : AuditableAbstractEvent(auditEntry)

data class RestaurantCreatedEvent(val name: String,
                                  val menu: RestaurantMenu,
                                  override val aggregateIdentifier: RestaurantId,
                                  override val auditEntry: AuditEntry)
  : RestaurantEvent(aggregateIdentifier, auditEntry)

data class RestaurantOrderCreatedEvent(val lineItems: List<RestaurantOrderLineItem>,
                                       val restaurantOrderId: RestaurantOrderId,
                                       override val aggregateIdentifier: RestaurantId,
                                       override val auditEntry: AuditEntry)
  : RestaurantEvent(aggregateIdentifier, auditEntry)

data class RestaurantOrderPreparedEvent(override val aggregateIdentifier: RestaurantOrderId,
                                        override val auditEntry: AuditEntry)
  : RestaurantOrderEvent(aggregateIdentifier, auditEntry)

data class RestaurantOrderRejectedEvent(override val aggregateIdentifier: RestaurantOrderId,
                                        override val auditEntry: AuditEntry)
  : RestaurantOrderEvent(aggregateIdentifier, auditEntry)