package br.com.pratofeito.customer.domain

import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition
import org.axonframework.eventsourcing.Snapshotter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal open class SpringCustomerConfiguration {

  @Value("\${axon.snapshot.trigger.treshold.customer}")
  private val snapshotTriggerTresholdCustomer: Int = 100

  @Value("\${axon.snapshot.trigger.treshold.customerorder}")
  private val snapshotTriggerTresholdCustomerOrder: Int = 100

  @Bean("customerSnapshotTriggerDefinition")
  open fun customerSnapshotTriggerDefinition(snapshotter: Snapshotter) = EventCountSnapshotTriggerDefinition(snapshotter, snapshotTriggerTresholdCustomer)

  @Bean("customerOrderSnapshotTriggerDefinition")
  open fun customerOrderSnapshotTriggerDefinition(snapshotter: Snapshotter) = EventCountSnapshotTriggerDefinition(snapshotter, snapshotTriggerTresholdCustomerOrder)

}