package br.com.pratofeito.order.domain

import br.com.pratofeito.courier.domain.api.CourierOrderCreatedEvent
import br.com.pratofeito.courier.domain.api.CourierOrderDeliveredEvent
import br.com.pratofeito.courier.domain.api.CreateCourierOrderCommand
import br.com.pratofeito.courier.domain.api.model.CourierOrderId
import br.com.pratofeito.customer.domain.api.CreateCustomerOrderCommand
import br.com.pratofeito.customer.domain.api.CustomerOrderCreatedEvent
import br.com.pratofeito.customer.domain.api.CustomerOrderRejectedEvent
import br.com.pratofeito.customer.domain.api.model.CustomerId
import br.com.pratofeito.customer.domain.api.model.CustomerOrderId
import br.com.pratofeito.order.domain.api.OrderCreationInitiatedEvent
import br.com.pratofeito.order.domain.api.OrderPreparedEvent
import br.com.pratofeito.order.domain.api.OrderVerifiedByCustomerEvent
import br.com.pratofeito.order.domain.api.model.OrderDetails
import br.com.pratofeito.order.domain.api.model.OrderId
import br.com.pratofeito.order.domain.model.*
import br.com.pratofeito.order.domain.model.MarkOrderAsPreparedInternalCommand
import br.com.pratofeito.order.domain.model.MarkOrderAsReadyForDeliveryInternalCommand
import br.com.pratofeito.order.domain.model.MarkOrderAsVerifiedByCustomerInternalCommand
import br.com.pratofeito.order.domain.model.MarkOrderAsVerifiedByRestaurantInternalCommand
import br.com.pratofeito.restaurant.domain.api.CreateRestaurantOrderCommand
import br.com.pratofeito.restaurant.domain.api.RestaurantOrderCreatedEvent
import br.com.pratofeito.restaurant.domain.api.RestaurantOrderPreparedEvent
import br.com.pratofeito.restaurant.domain.api.RestaurantOrderRejectedEvent
import br.com.pratofeito.restaurant.domain.api.model.RestaurantId
import br.com.pratofeito.restaurant.domain.api.model.RestaurantOrderDetails
import br.com.pratofeito.restaurant.domain.api.model.RestaurantOrderId
import br.com.pratofeito.restaurant.domain.api.model.RestaurantOrderLineItem
import org.axonframework.commandhandling.callbacks.LoggingCallback
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.config.ProcessingGroup
import org.axonframework.modelling.saga.EndSaga
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

  @SagaEventHandler(associationProperty = "restaurantOrderId")
  fun on(event: RestaurantOrderCreatedEvent) = commandGateway.send(
    MarkOrderAsVerifiedByRestaurantInternalCommand(orderId, restaurantId, event.auditEntry),
    LoggingCallback.INSTANCE
  )

  @SagaEventHandler(associationProperty = "aggregateIdentifier", keyName = "restaurantOrderId")
  fun on(event: RestaurantOrderPreparedEvent) = commandGateway.send(
    MarkOrderAsPreparedInternalCommand(orderId, event.auditEntry),
    LoggingCallback.INSTANCE
  )

  @SagaEventHandler(associationProperty = "aggregateIdentifier")
  fun on(event: OrderPreparedEvent) {
    var courierOrderId = CourierOrderId("courierOrder_$orderId")
    commandGateway.send(
      CreateCourierOrderCommand(courierOrderId, event.auditEntry),
      LoggingCallback.INSTANCE
    )
  }

  @SagaEventHandler(associationProperty = "aggregateIdentifier", keyName = "courierOrderId")
  fun on(event: CourierOrderCreatedEvent) = commandGateway.send(
    MarkOrderAsReadyForDeliveryInternalCommand(orderId, event.auditEntry),
    LoggingCallback.INSTANCE
  )

  @EndSaga
  @SagaEventHandler(associationProperty = "aggregateIdentifier", keyName = "courierOrderId")
  fun on(event: CourierOrderDeliveredEvent) = commandGateway.send(
    MarkOrderAsDeliveredInternalCommand(orderId, event.auditEntry),
    LoggingCallback.INSTANCE
  )

  @EndSaga
  @SagaEventHandler(associationProperty = "aggregateIdentifier", keyName = "customerOrderId")
  fun on(event: CustomerOrderRejectedEvent) = commandGateway.send(
    MarkOrderAsRejectedInternalCommand(orderId, event.auditEntry),
    LoggingCallback.INSTANCE
  )

  @EndSaga
  @SagaEventHandler(associationProperty = "aggregateIdentifier", keyName = "restaurantOrderId")
  fun on(event: RestaurantOrderRejectedEvent) = commandGateway.send(
    MarkOrderAsRejectedInternalCommand(orderId, event.auditEntry),
    LoggingCallback.INSTANCE
  )
}

