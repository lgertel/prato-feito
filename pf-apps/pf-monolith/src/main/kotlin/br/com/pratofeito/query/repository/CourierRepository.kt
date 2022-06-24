package br.com.pratofeito.query.repository

import br.com.pratofeito.query.model.CourierEntity
import org.springframework.data.repository.PagingAndSortingRepository

interface CourierRepository : PagingAndSortingRepository<CourierEntity, String>