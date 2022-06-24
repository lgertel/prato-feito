package br.com.pratofeito.query.model

import br.com.pratofeito.order.domain.api.model.OrderState
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.apache.commons.lang3.builder.ToStringBuilder
import java.math.BigDecimal
import java.util.Collections
import javax.persistence.*

@Entity
class OrderEntity(
  @Id var id: String,
  var aggregateVersion: Long,
  @ElementCollection var lineItems: List<OrderItemEmbedable>,
  @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "RESTAURANT_ID") var restaurant: RestaurantEntity?,
  @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "CUSTOMER_ID") var customer: CustomerEntity?,
  @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "COURIER_ID") var courier: CourierEntity?,
  @Enumerated var state: OrderState?
) {
  constructor() : this("", 0, Collections.emptyList(), null, null, null, null)


  override fun toString(): String = ToStringBuilder.reflectionToString(this)

  override fun equals(other: Any?): Boolean = EqualsBuilder.reflectionEquals(this, other)

  override fun hashCode(): Int = HashCodeBuilder.reflectionHashCode(this)
}

@Embeddable
@Access(AccessType.FIELD)
data class OrderItemEmbedable(var menuId: String, var name: String, var price: BigDecimal, var quantity: Int) {
  constructor() : this("", "", BigDecimal(0), 0)

  override fun toString(): String = ToStringBuilder.reflectionToString(this)

  override fun equals(other: Any?): Boolean = EqualsBuilder.reflectionEquals(this, other)

  override fun hashCode(): Int = HashCodeBuilder.reflectionHashCode(this)
}