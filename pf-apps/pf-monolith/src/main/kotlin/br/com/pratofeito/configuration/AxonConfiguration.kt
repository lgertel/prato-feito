package br.com.pratofeito.configuration

import org.axonframework.commandhandling.CommandBus
import org.axonframework.messaging.interceptors.BeanValidationInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
class AxonConfiguration {

  @Autowired
  fun registerInterceptors(commandBus: CommandBus) {
    commandBus.registerDispatchInterceptor(BeanValidationInterceptor())
  }
}