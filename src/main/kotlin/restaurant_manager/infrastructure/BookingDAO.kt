package restaurant_manager.infrastructure

import org.springframework.data.repository.CrudRepository

interface BookingDAO : CrudRepository<Booking, Long> {

    fun countBookingsByEmployeeId(employeeId: Long): Long

}
