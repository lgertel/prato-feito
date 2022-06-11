package br.com.pratofeito.courier.domain.api.model

import java.io.Serializable
import java.util.*

enum class CourierOrderState {
  CREATED, ASSIGN_PENDING, ASSIGNED, DELIVERED, CANCEL_PENDING, CANCELLED
}

data class CourierID(val identifier: String) : Serializable {
  constructor() : this(UUID.randomUUID().toString())

  override fun toString(): String = identifier
}

data class CourierOrderId(val identifier: String) {
  constructor() : this(UUID.randomUUID().toString())

  override fun toString(): String = identifier
}