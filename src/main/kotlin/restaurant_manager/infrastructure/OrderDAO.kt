package restaurant_manager.infrastructure

import org.springframework.data.repository.CrudRepository

interface OrderDAO : CrudRepository<Order, Long> {

    fun countOrdersByBoardId(boardId: Long): Long

    fun countOrdersByEmployeeId(employeeId: Long): Long

}
