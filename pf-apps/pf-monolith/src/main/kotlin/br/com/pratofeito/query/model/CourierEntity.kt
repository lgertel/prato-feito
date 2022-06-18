package br.com.pratofeito.query.model

import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class CourierEntity(
  @Id var id: String,
  var aggregateVersion: Long,
  var firstName: String,
  var lastName: String,
  var orderLimit: BigDecimal
)