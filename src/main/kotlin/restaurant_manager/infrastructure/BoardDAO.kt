package restaurant_manager.infrastructure

import org.springframework.data.repository.CrudRepository

interface BoardDAO : CrudRepository<Board, Long> {

    fun countBoardsByEmployeeId(employeeId: Long): Long
}
