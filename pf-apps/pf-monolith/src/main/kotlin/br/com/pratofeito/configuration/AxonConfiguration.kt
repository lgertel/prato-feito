package br.com.pratofeito.configuration

import org.axonframework.commandhandling.CommandBus
import org.axonframework.messaging.interceptors.BeanValidationInterceptor
import org.axonframework.spring.eventsourcing.SpringAggregateSnapshotterFactoryBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class AxonConfiguration {

  /* Register a command interceptor */
  @Autowired
  fun registerInterceptors(commandBus: CommandBus) {
    commandBus.registerDispatchInterceptor(BeanValidationInterceptor())
  }

  @Bean
  open fun snapshotterFactoryBean() = SpringAggregateSnapshotterFactoryBean()
}