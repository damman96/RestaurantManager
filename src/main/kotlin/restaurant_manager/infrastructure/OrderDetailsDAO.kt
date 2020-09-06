package restaurant_manager.infrastructure

import org.springframework.data.repository.CrudRepository

interface OrderDetailsDAO : CrudRepository<OrderDetails, Long>
