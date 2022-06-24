package br.com.pratofeito.query.handler

import br.com.pratofeito.query.model.MenuItemEmbedable
import br.com.pratofeito.query.model.RestaurantEntity
import br.com.pratofeito.query.model.RestaurantMenuEmbedable
import br.com.pratofeito.query.repository.RestaurantRepository
import br.com.pratofeito.restaurant.domain.api.RestaurantCreatedEvent
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.AllowReplay
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
import org.axonframework.eventhandling.SequenceNumber
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Component

@Component
@ProcessingGroup("restaurant")
class RestaurantHandler(
  private val repository: RestaurantRepository,
  private val messagingTemplate: SimpMessageSendingOperations
) {

  @EventHandler
  @AllowReplay(true)
  fun handle(event: RestaurantCreatedEvent, @SequenceNumber aggregateVersion: Long) {
    val menuItems = ArrayList<MenuItemEmbedable>()
    for (item in event.menu.menuItems) {
      val menuItem = MenuItemEmbedable(item.id, item.name, item.price.amount)
      menuItems.add(menuItem)
    }

    val menu = RestaurantMenuEmbedable(menuItems, event.menu.menuVersion)

    repository.save(
      RestaurantEntity(
        event.aggregateIdentifier.identifier,
        aggregateVersion,
        event.name,
        menu,
        emptyList()
      )
    )

    broadcastUpdates()
  }

  @ResetHandler
  fun onReset() = repository.deleteAll()

  private fun broadcastUpdates() = messagingTemplate.convertAndSend("/topic/restaurants.updates", repository.findAll())
}