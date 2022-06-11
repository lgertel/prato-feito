package br.com.pratofeito.restaurant.domain

import br.com.pratofeito.common.domain.api.model.AuditEntry
import br.com.pratofeito.common.domain.api.model.Money
import br.com.pratofeito.restaurant.domain.api.CreateRestaurantCommand
import br.com.pratofeito.restaurant.domain.api.CreateRestaurantOrderCommand
import br.com.pratofeito.restaurant.domain.api.RestaurantCreatedEvent
import br.com.pratofeito.restaurant.domain.api.RestaurantOrderCreatedEvent
import br.com.pratofeito.restaurant.domain.api.model.*
import org.axonframework.messaging.interceptors.BeanValidationInterceptor
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.util.Calendar

class RestaurantAggregateTest {

  private lateinit var fixture: FixtureConfiguration<Restaurant>
  private val who = "lgertel"
  private var auditEntry: AuditEntry = AuditEntry(who, Calendar.getInstance().time)
  private var orderId: RestaurantOrderId = RestaurantOrderId("orderId")
  private var restaurantId: RestaurantId = RestaurantId("restaurantId")
  private var lineItem: RestaurantOrderLineItem = RestaurantOrderLineItem(1, "menuItemId", "name")
  private var lineItems: MutableList<RestaurantOrderLineItem> = ArrayList()
  private var orderDetails: RestaurantOrderDetails = RestaurantOrderDetails(lineItems)

  @Before
  fun setUp() {
    fixture = AggregateTestFixture(Restaurant::class.java)
    fixture.registerCommandDispatchInterceptor(BeanValidationInterceptor())

    lineItems.add(lineItem)
  }

  @Test
  fun createRestaurantTest() {
    val name = "GordoBar"
    val menuItems = ArrayList<MenuItem>()
    val item = MenuItem("id", "name", Money(BigDecimal.valueOf(100)))
    menuItems.add(item)
    val menu = RestaurantMenu(menuItems, "v1")

    val createRestaurantCommand = CreateRestaurantCommand(name, menu, auditEntry)
    val restaurantCreatedEvent = RestaurantCreatedEvent(name, menu, createRestaurantCommand.targetAggregateIdentifier, auditEntry)

    fixture.given().`when`(createRestaurantCommand).expectEvents(restaurantCreatedEvent)
  }

  @Test
  fun createRestaurantOrder() {
    val name = "GordoBar"
    val menuItems = ArrayList<MenuItem>()
    val item = MenuItem("menuItemId", "name", Money(BigDecimal.valueOf(100)))
    menuItems.add(item)
    val menu = RestaurantMenu(menuItems, "v1")
    val restaurantCreatedEvent = RestaurantCreatedEvent(name, menu,restaurantId, auditEntry)

    val createRestaurantOrderCommand = CreateRestaurantOrderCommand(restaurantId, orderDetails, orderId, auditEntry)
    val restaurantOrderCreatedEvent = RestaurantOrderCreatedEvent(lineItems, orderId, restaurantId, auditEntry)

    fixture.given(restaurantCreatedEvent)
      .`when`(createRestaurantOrderCommand)
      .expectEvents(restaurantOrderCreatedEvent)
  }

  @Test
  fun createRestaurantOrderFailTest() {
    val name = "GordoBar"
    val menuItems = ArrayList<MenuItem>()
    val item = MenuItem("WRONG", "name", Money(BigDecimal.valueOf(100)))
    menuItems.add(item)
    val menu = RestaurantMenu(menuItems, "v1")
    val restaurantCreatedEvent = RestaurantCreatedEvent(name, menu,restaurantId, auditEntry)

    val createRestaurantOrderCommand = CreateRestaurantOrderCommand(restaurantId, orderDetails, orderId, auditEntry)
    val restaurantOrderCreatedEvent = RestaurantOrderCreatedEvent(lineItems, orderId, restaurantId, auditEntry)

    fixture.given(restaurantCreatedEvent)
      .`when`(createRestaurantOrderCommand)
      .expectException(UnsupportedOperationException::class.java)
  }
}