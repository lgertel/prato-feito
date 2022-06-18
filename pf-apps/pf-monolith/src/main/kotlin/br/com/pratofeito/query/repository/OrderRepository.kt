package br.com.pratofeito.query.repository

import br.com.pratofeito.query.model.OrderEntity
import org.springframework.data.repository.PagingAndSortingRepository

interface OrderRepository: PagingAndSortingRepository<OrderEntity, String>