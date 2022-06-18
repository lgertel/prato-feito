package br.com.pratofeito.query.repository

import br.com.pratofeito.query.model.CourierOrderEntity
import org.springframework.data.repository.PagingAndSortingRepository

interface CourierOrderRepository : PagingAndSortingRepository<CourierOrderEntity, String>