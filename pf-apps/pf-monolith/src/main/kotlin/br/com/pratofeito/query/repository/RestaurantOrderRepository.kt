package br.com.pratofeito.query.repository

import br.com.pratofeito.query.model.RestaurantOrderEntity
import org.springframework.data.repository.PagingAndSortingRepository

interface RestaurantOrderRepository : PagingAndSortingRepository<RestaurantOrderEntity, String>