package br.com.pratofeito.courier.domain

import br.com.pratofeito.courier.domain.model.*
import br.com.pratofeito.courier.domain.model.CourierOrderAssigningInitiatedInternalEvent
import br.com.pratofeito.courier.domain.model.MarkCourierOrderAsAssignedInternalCommand
import br.com.pratofeito.courier.domain.model.MarkCourierOrderAsNotAssignedInternalCommand
import br.com.pratofeito.courier.domain.model.ValidateOrderByCourierInternalCommand
import org.axonframework.commandhandling.callbacks.LoggingCallback
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.config.ProcessingGroup
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.springframework.beans.factory.annotation.Autowired

@Saga
@ProcessingGroup("courierordersaga")
internal class CourierOrderSaga {

  @Autowired
  @Transient
  private lateinit var commandGateway: CommandGateway

  @StartSaga
  @SagaEventHandler(associationProperty = "aggregateIdentifier")
  fun on(event: CourierOrderAssigningInitiatedInternalEvent) = commandGateway.send(ValidateOrderByCourierInternalCommand(event.aggregateIdentifier, event.courierId, event.auditEntry), LoggingCallback.INSTANCE)

  @EndSaga
  @SagaEventHandler(associationProperty = "orderId", keyName = "aggregateIdentifier")
  fun on(event: CourierNotFoundForOrderInternalEvent) = commandGateway.send(MarkCourierOrderAsNotAssignedInternalCommand(event.orderId, event.auditEntry), LoggingCallback.INSTANCE)

  @EndSaga
  @SagaEventHandler(associationProperty = "orderId", keyName = "aggregateIdentifier")
  fun on(event: CourierValidatedOrderWithSuccessInternalEvent) = commandGateway.send(MarkCourierOrderAsAssignedInternalCommand(event.orderId, event.aggregateIdentifier, event.auditEntry), LoggingCallback.INSTANCE)

  @EndSaga
  @SagaEventHandler(associationProperty = "orderId", keyName = "aggregateIdentifier")
  fun on(event: CourierValidatedOrderWithErrorInternalEvent) = commandGateway.send(MarkCourierOrderAsNotAssignedInternalCommand(event.orderId, event.auditEntry), LoggingCallback.INSTANCE)
}