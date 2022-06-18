package br.com.pratofeito.web

import br.com.pratofeito.common.domain.api.model.AuditEntry
import br.com.pratofeito.common.domain.api.model.Money
import br.com.pratofeito.common.domain.api.model.PersonName
import br.com.pratofeito.courier.domain.api.AssignCourierOrderToCourierCommand
import br.com.pratofeito.courier.domain.api.CreateCourierCommand
import br.com.pratofeito.courier.domain.api.MarkCourierOrderAsDeliveredCommand
import br.com.pratofeito.courier.domain.api.model.CourierId
import br.com.pratofeito.courier.domain.api.model.CourierOrderId
import br.com.pratofeito.customer.domain.api.CreateCustomerCommand
import br.com.pratofeito.order.domain.api.CreateOrderCommand
import br.com.pratofeito.order.domain.api.model.OrderInfo
import br.com.pratofeito.order.domain.api.model.OrderLineItem
import br.com.pratofeito.query.model.*
import br.com.pratofeito.query.repository.*
import br.com.pratofeito.restaurant.domain.api.CreateRestaurantCommand
import br.com.pratofeito.restaurant.domain.api.MarkRestaurantOrderAsPreparedCommand
import br.com.pratofeito.restaurant.domain.api.model.MenuItem
import br.com.pratofeito.restaurant.domain.api.model.RestaurantMenu
import br.com.pratofeito.restaurant.domain.api.model.RestaurantOrderId
import org.axonframework.commandhandling.callbacks.LoggingCallback
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.annotation.SubscribeMapping
import org.springframework.stereotype.Controller
import java.math.BigDecimal
import java.util.Calendar

@Controller
class WebController(
  private val commandGateway: CommandGateway,
  private val customerRepository: CustomerRepository,
  private val restaurantRepository: RestaurantRepository,
  private val courierRepository: CourierRepository,
  private val orderRepository: OrderRepository,
  private val restaurantOrderRepository: RestaurantOrderRepository,
  private val courierOrderRepository: CourierOrderRepository
) {

  private val auditEntry: AuditEntry
    get() = AuditEntry("TEST", Calendar.getInstance().time)

  // CUSTOMERS
  @MessageMapping("/customers/createcommand")
  fun createCustomer(request: CreateCustomerDTO) =
    commandGateway.send(
      CreateCustomerCommand(
        PersonName(request.firstName, request.lastName),
        Money(request.orderLimit),
        auditEntry
      ),
      LoggingCallback.INSTANCE
    )

  @SubscribeMapping("/customers")
  fun allCustomers(): Iterable<CustomerEntity> = customerRepository.findAll()

  @SubscribeMapping("/customers/{id}")
  fun getCustomer(@DestinationVariable id: String) =
    customerRepository.findById(id)

  // RESTAURANTS
  @MessageMapping("/restaurant/createcommand")
  fun CreateRestaurant(request: CreateRestaurantDTO) {

    val menuItems = ArrayList<MenuItem>()
    for ((id, name, price) in request.menuItems) {
      val item = MenuItem(id, name, Money(price))
      menuItems.add(item)
    }

    val menu = RestaurantMenu(menuItems, "ver.0")
    val command = CreateRestaurantCommand(request.name, menu, auditEntry)
    commandGateway.send(
      command,
      LoggingCallback.INSTANCE
    )
  }

  @SubscribeMapping("/restaurants")
  fun allRestaurants(): Iterable<RestaurantEntity> = restaurantRepository.findAll()

  @SubscribeMapping("/restaurants/{id}")
  fun getRestaurant(@DestinationVariable id: String) =
    restaurantRepository.findById(id)

  // COURIER
  @MessageMapping("/couriers/createcommand")
  fun createCourier(request: CreateCourierDTO) = commandGateway.send(
    CreateCourierCommand(
      PersonName(request.firstName, request.lastName),
      request.maxNumberOfActiveOrders,
      auditEntry
    ),
    LoggingCallback.INSTANCE
  )

  @SubscribeMapping("/couriers")
  fun allCouriers(): Iterable<CourierEntity> =
    courierRepository.findAll()

  @SubscribeMapping("/couriers/{id}")
  fun getCourier(@DestinationVariable id: String) = courierRepository.findById(id)

  // ORDERS
  @MessageMapping("/orders/createcommand")
  fun createOrder(request: CreateOrderDTO) {
    val lineItems = ArrayList<OrderLineItem>()
    for ((id, name, price, quantity) in request.orderItems) {
      val item = OrderLineItem(id, name, Money(price), quantity)
      lineItems.add(item)
    }

    val orderInfo = OrderInfo(request.customerId, request.restaurantId, lineItems)
    val command = CreateOrderCommand(orderInfo, auditEntry)
    commandGateway.send(
      command,
      LoggingCallback.INSTANCE
    )
  }

  @SubscribeMapping("/orders")
  fun allOrders(): Iterable<OrderEntity> = orderRepository.findAll()

  @SubscribeMapping("/orders/{id}")
  fun getOrder(@DestinationVariable id: String) = orderRepository.findById(id)

  // RESTAURANT ORDERS
  @MessageMapping("/restaurants/orders/markpreparedcommand")
  fun markRestaurantOrderAsPrepared(id: String) = commandGateway.send(
    MarkRestaurantOrderAsPreparedCommand(RestaurantOrderId(id), auditEntry),
    LoggingCallback.INSTANCE
  )

  @SubscribeMapping("/restaurants/orders")
  fun allRestaurantOrders(): Iterable<RestaurantOrderEntity> = restaurantOrderRepository.findAll()

  @SubscribeMapping("/restaurants/orders/{id}")
  fun getRestaurantOrder(@DestinationVariable id: String) = restaurantOrderRepository.findById(id)

  // COURIER ORDERS
  @MessageMapping("/couriers/orders/assigncommmand")
  fun assignOrderToCourier(request: AssignOrderToCourierDTO) = commandGateway.send(
    AssignCourierOrderToCourierCommand(
      CourierOrderId(request.courierOrderId),
      CourierId(request.courierId),
      auditEntry
    ),
    LoggingCallback.INSTANCE
  )

  @MessageMapping("couriers/orders/markdeliveredcommand")
  fun markCourierOrderAsDelivered(id: String) = commandGateway.send(
    MarkCourierOrderAsDeliveredCommand(
      CourierOrderId(id),
      auditEntry
    ),
    LoggingCallback.INSTANCE
  )

  @SubscribeMapping("/couriers/orders")
  fun allCourierOrders(): Iterable<CourierOrderEntity> = courierOrderRepository.findAll()

  @SubscribeMapping("/couriers/orders/{id}")
  fun getCourierOrder(@DestinationVariable id: String) =
    courierOrderRepository.findById(id)
}


data class CreateCustomerDTO(val firstName: String, val lastName: String, val orderLimit: BigDecimal)

data class CreateRestaurantDTO(val name: String, val menuItems: List<MenuItemDTO>)

data class MenuItemDTO(val id: String, val name: String, val price: BigDecimal)

data class CreateCourierDTO(val firstName: String, val lastName: String, val maxNumberOfActiveOrders: Int)

data class CreateOrderDTO(
  val customerId: String,
  val restaurantId: String,
  val orderItems: List<OrderItemDTO>
)

data class OrderItemDTO(val id: String, val name: String, val price: BigDecimal, val quantity: Int)

data class AssignOrderToCourierDTO(val courierOrderId: String, val courierId: String)