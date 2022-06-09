package br.com.pratofeito.query.repository

import br.com.pratofeito.query.model.CustomerEntity
import org.springframework.data.repository.PagingAndSortingRepository

interface CustomerRepository : PagingAndSortingRepository<CustomerEntity, String>