package br.com.pratofeito.restaurant.domain.api.model

import br.com.pratofeito.common.domain.api.model.Money
import java.io.Serializable
import java.util.*

data class MenuItem(val id: String, val name: String, val price: Money)

data class RestaurantMenu(val menuItems: MutableList<MenuItem>, val menuVersion: String)

data class RestaurantOrderLineItem(val quantity: Int, val menuItemId: String, val name: String)

data class RestaurantOrderDetails(val lineItems: List<RestaurantOrderLineItem>)

enum class RestaurantOrderState {
  CREATED, PREPARED, CANCELLED
}

enum class RestaurantState {
  OPEN, CLOSED
}

data class RestaurantId(val identifier: String) : Serializable {
  constructor() : this(UUID.randomUUID().toString())

  override fun toString(): String = identifier
}

data class RestaurantOrderId(val identifier: String) {
  constructor() : this(UUID.randomUUID().toString())

  override fun toString(): String = identifier
}