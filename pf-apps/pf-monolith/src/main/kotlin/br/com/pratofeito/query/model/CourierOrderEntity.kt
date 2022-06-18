package br.com.pratofeito.query.model

import br.com.pratofeito.courier.domain.api.model.CourierOrderState
import javax.persistence.*

@Entity
class CourierOrderEntity(
  @Id var id: String,
  var aggregateVersion: Long,
  @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "COURIER_ID") var courier: CourierEntity,
  @Enumerated(EnumType.STRING) var state: CourierOrderState
)
