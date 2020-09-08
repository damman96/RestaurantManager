package restaurant_manager.board

import org.springframework.stereotype.Component
import restaurant_manager.exceptions.NotFoundException
import restaurant_manager.infrastructure.*

@Component
class BoardRepository(
        private val orderDAO: OrderDAO,
        private val bookingDAO: BookingDAO,
        private val employeeDAO: EmployeeDAO) {

    fun mapBoardToBoardDetailDto(board: Board): BoardDetailDto =
            BoardDetailDto(
                    board.numberOfSeats,
                    board.description,
                    board.employee.id,
                    getNumberOfBookings(board.id!!),
                    getNumberOfOrders(board.id!!)
            )

    fun mapBoardAddDtoToBoard(dto: BoardAddDto): Board =
            Board(
                    id = null,
                    numberOfSeats = dto.numberOfSeats!!,
                    description = dto.description!!,
                    employee = getEmployee(dto.employeeId!!)
            )

    fun mapBoardEditDtoToBoard(board: Board, dto: BoardEditDto): Board =
            board.copy(
                    id = board.id,
                    numberOfSeats = dto.numberOfSeats!!,
                    description = dto.description!!,
                    employee = getEmployee(dto.employeeId!!)
            )

    private fun getNumberOfOrders(boardId: Long): Long {
        return orderDAO.countOrdersByBoardId(boardId)
    }

    private fun getNumberOfBookings(boardId: Long): Long {
        return bookingDAO.countBookingsByBoardId(boardId)
    }

    private fun getEmployee(employeeId: Long): Employee {
        return employeeDAO.findById(employeeId)
                .orElseThrow { NotFoundException("Employee with id: $employeeId was not found!") }
    }
}
