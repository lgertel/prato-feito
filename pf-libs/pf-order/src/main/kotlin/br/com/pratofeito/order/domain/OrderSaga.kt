package br.com.pratofeito.order.domain

import br.com.pratofeito.customer.domain.api.CreateCustomerOrderCommand
import br.com.pratofeito.customer.domain.api.CustomerOrderCreatedEvent
import br.com.pratofeito.customer.domain.api.model.CustomerId
import br.com.pratofeito.customer.domain.api.model.CustomerOrderId
import br.com.pratofeito.order.domain.api.OrderCreationInitiatedEvent
import br.com.pratofeito.order.domain.api.OrderVerifiedByCustomerEvent
import br.com.pratofeito.order.domain.api.model.OrderDetails
import br.com.pratofeito.order.domain.api.model.OrderId
import br.com.pratofeito.order.domain.model.MarkOrderAsVerifiedByCustomerInternalCommand
import br.com.pratofeito.restaurant.domain.api.CreateRestaurantOrderCommand
import br.com.pratofeito.restaurant.domain.api.model.RestaurantId
import br.com.pratofeito.restaurant.domain.api.model.RestaurantOrderDetails
import br.com.pratofeito.restaurant.domain.api.model.RestaurantOrderId
import br.com.pratofeito.restaurant.domain.api.model.RestaurantOrderLineItem
import org.axonframework.commandhandling.callbacks.LoggingCallback
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.config.ProcessingGroup
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.SagaLifecycle
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.springframework.beans.factory.annotation.Autowired


@Saga
@ProcessingGroup("ordersaga")
internal class OrderSaga {

  @Autowired
  @Transient
  private lateinit var commandGateway: CommandGateway
  private lateinit var restaurantId: RestaurantId
  private lateinit var customerId: CustomerId
  private lateinit var orderDetails: OrderDetails
  private lateinit var orderId: OrderId

  @StartSaga
  @SagaEventHandler(associationProperty = "aggregateIdentifier")
  fun on(event: OrderCreationInitiatedEvent) {
    orderId = event.aggregateIdentifier
    restaurantId = RestaurantId(event.orderDetails.restaurantId)
    customerId = CustomerId(event.orderDetails.customerId)
    orderDetails = event.orderDetails

    var customerOrderId = CustomerOrderId("customerOrder_$orderId")
    SagaLifecycle.associateWith("customerOrderId", customerOrderId.toString())

    commandGateway.send(
      CreateCustomerOrderCommand(
        customerId,
        customerOrderId,
        orderDetails.orderTotal,
        event.auditEntry
      ), LoggingCallback.INSTANCE
    )
  }

  @SagaEventHandler(associationProperty = "customerOrderId")
  fun on(event: CustomerOrderCreatedEvent) = commandGateway.send(
    MarkOrderAsVerifiedByCustomerInternalCommand(orderId, customerId, event.auditEntry),
    LoggingCallback.INSTANCE
  )

  @SagaEventHandler(associationProperty = "aggregateIdentifier")
  fun on(event: OrderVerifiedByCustomerEvent) {
    val restaurantOrderId = RestaurantOrderId("restaurantOrder_$orderId")
    SagaLifecycle.associateWith("restaurantOrderId", restaurantOrderId.toString())

    var restaurantLineItems = ArrayList<RestaurantOrderLineItem>()
    for (oli in orderDetails.lineItems) {
      var roli = RestaurantOrderLineItem(oli.quantity, oli.menuItemId, oli.name)
      restaurantLineItems.add(roli)
    }

    val restaurantOrderDetails = RestaurantOrderDetails(restaurantLineItems)
    commandGateway.send(
      CreateRestaurantOrderCommand(
        restaurantId,
        restaurantOrderDetails,
        restaurantOrderId,
        event.auditEntry
      ), LoggingCallback.INSTANCE
    )
  }



}