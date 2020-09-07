package restaurant_manager.employee

import org.springframework.stereotype.Component
import restaurant_manager.enums.Position
import restaurant_manager.infrastructure.BoardDAO
import restaurant_manager.infrastructure.BookingDAO
import restaurant_manager.infrastructure.Employee
import restaurant_manager.infrastructure.OrderDAO
import restaurant_manager.others.Salary

@Component
class EmployeeRepository(
        private val boardDAO: BoardDAO,
        private val orderDAO: OrderDAO,
        private val bookingDAO: BookingDAO) {

    fun mapEmployeeToEmployeeDetailDto(employee: Employee): EmployeeDetailDto {
        return EmployeeDetailDto(
                employee.firstName,
                employee.lastName,
                employee.email,
                employee.position.position,
                employee.salary.salary,
                employee.startDate,
                employee.phoneNumber,
                getNumberOfBoards(employee.id!!),
                getNumberOfOrders(employee.id!!),
                getNumberOfBookings(employee.id!!)
        )
    }

    fun mapEmployeeAddDtoToEmployee(dto: EmployeeAddDto): Employee {
        return Employee(id = null,
                firstName = dto.firstName!!,
                lastName = dto.lastName!!,
                email = dto.email!!,
                position = Position.getByValue(dto.position!!),
                salary = Salary(dto.salary!!),
                startDate = dto.startDate!!,
                phoneNumber = dto.phoneNumber!!)
    }

    fun mapEmployeeEditDtoToEmployee(employee: Employee, dto: EmployeeEditDto): Employee {
        return employee.copy(id = employee.id,
                firstName = dto.firstName!!,
                lastName = dto.lastName!!,
                email = dto.email!!,
                position = Position.getByValue(dto.position!!),
                salary = Salary(dto.salary!!),
                startDate = employee.startDate,
                phoneNumber = dto.phoneNumber!!)
    }

    private fun getNumberOfBoards(employeeId: Long): Long {
        return boardDAO.countBoardsByEmployeeId(employeeId)
    }

    private fun getNumberOfOrders(employeeId: Long): Long {
        return orderDAO.countOrdersByEmployeeId(employeeId)
    }

    private fun getNumberOfBookings(employeeId: Long): Long {
        return bookingDAO.countBookingsByEmployeeId(employeeId)
    }
}
