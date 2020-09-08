package restaurant_manager.infrastructure

import org.springframework.data.repository.CrudRepository

interface BookingDAO : CrudRepository<Booking, Long> {

    fun countBookingsByBoardId(boardId: Long): Long

    fun countBookingsByEmployeeId(employeeId: Long): Long

}
