package restaurant_manager.employee

import restaurant_manager.others.PhoneNumber
import java.math.BigDecimal
import java.time.LocalDate

data class EmployeeDetailDto(

        var firstName: String? = null,
        var lastName: String? = null,
        var email: String? = null,
        var position: String? = null,
        var salary: BigDecimal? = null,
        var startDate: LocalDate? = null,
        var phoneNumber: PhoneNumber? = null,
        var numberOfBoards: Long? = null,
        var numberOfOrders: Long? = null,
        var numberOfBookings: Long? = null

) {
    override fun toString(): String {
        return "EmployeeDetailDto(" +
                "firstName=$firstName, " +
                "lastName=$lastName, " +
                "email=$email, " +
                "position=$position, " +
                "salary=$salary, " +
                "startDate=$startDate, " +
                "phoneNumber=$phoneNumber, " +
                "numberOfBoards=$numberOfBoards, " +
                "numberOfOrders=$numberOfOrders, " +
                "numberOfBookings=$numberOfBookings" +
                ")"
    }
}
