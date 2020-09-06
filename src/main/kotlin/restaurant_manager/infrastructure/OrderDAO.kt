package restaurant_manager.infrastructure

import org.springframework.data.repository.CrudRepository

interface OrderDAO : CrudRepository<Order, Long>
