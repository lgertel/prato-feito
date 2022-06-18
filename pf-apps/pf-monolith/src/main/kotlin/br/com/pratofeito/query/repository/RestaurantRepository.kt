package br.com.pratofeito.query.repository

import br.com.pratofeito.query.model.RestaurantEntity
import org.springframework.data.repository.PagingAndSortingRepository

interface RestaurantRepository : PagingAndSortingRepository<RestaurantEntity, String>