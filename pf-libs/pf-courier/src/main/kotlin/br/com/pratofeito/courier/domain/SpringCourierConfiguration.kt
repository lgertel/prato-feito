package br.com.pratofeito.courier.domain

import org.axonframework.eventhandling.EventBus
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition
import org.axonframework.eventsourcing.Snapshotter
import org.axonframework.spring.config.AxonConfiguration
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal open class SpringCourierConfiguration {

	@Bean
	open fun courierCommandHandler(axonConfiguration: AxonConfiguration, eventBus: EventBus) = CourierCommandHandler(axonConfiguration.repository(Courier::class.java), eventBus)

	@Value("\${axon.snapshot.trigger.treshold.courier}")
	private val snapshotTriggerTresholdCourier: Int = 100

	@Value("\${axon.snapshot.trigger.treshold.courierorder}")
	private val snapshotTriggerTresholdCourierOrder: Int = 100

	@Bean("courierSnapshotTriggerDefinition")
	open fun courierSnapshotTriggerDefinition(snapshotter: Snapshotter) = EventCountSnapshotTriggerDefinition(snapshotter, snapshotTriggerTresholdCourier)

	@Bean("courierOrderSnapshotTriggerDefinition")
	open fun courierOrderSnapshotTriggerDefinition(snapshotter: Snapshotter) = EventCountSnapshotTriggerDefinition(snapshotter, snapshotTriggerTresholdCourierOrder)
}