package br.com.pratofeito.query.model

import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.apache.commons.lang3.builder.ToStringBuilder
import java.math.BigDecimal
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class CourierEntity(
  @Id var id: String,
  var aggregateVersion: Long,
  var firstName: String,
  var lastName: String,
  var maxNumberOfActiveOrder: Int,
  @OneToMany(mappedBy = "courier") var orders: List<CourierOrderEntity>
) {
  constructor() : this("", 0, "", "", 5, Collections.emptyList()) {
  }


  override fun toString(): String = ToStringBuilder.reflectionToString(this)

  override fun equals(other: Any?): Boolean = EqualsBuilder.reflectionEquals(this, other)

  override fun hashCode(): Int = HashCodeBuilder.reflectionHashCode(this)
}