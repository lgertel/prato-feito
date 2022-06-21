package br.com.pratofeito.query.model

import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.apache.commons.lang3.builder.ToStringBuilder
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class CustomerEntity(
  @Id var id: String,
  var aggregateVersion: Long,
  var firstName: String,
  val lastName: String,
  var orderLimit: BigDecimal
) {
  constructor() : this("", 0, "","", BigDecimal(0))

  override fun toString(): String = ToStringBuilder.reflectionToString(this)

  override fun equals(other: Any?): Boolean = EqualsBuilder.reflectionEquals(this, other)

  override fun hashCode(): Int = HashCodeBuilder.reflectionHashCode(this)
}