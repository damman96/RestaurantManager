package restaurant_manager.employee

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import restaurant_manager.exceptions.BadRequestException
import restaurant_manager.exceptions.NotFoundException
import restaurant_manager.infrastructure.Employee
import restaurant_manager.infrastructure.EmployeeDAO
import java.util.stream.Collectors

@Component
class EmployeeApplicationService(
        private val employeeDAO: EmployeeDAO,
        private val repository: EmployeeRepository) {

    private val LOGGER = LoggerFactory.getLogger(EmployeeApplicationService::class.java)

    fun getEmployees(): List<EmployeeDetailDto> =
            employeeDAO.findAllByOrderByIdAsc()
                    .stream()
                    .map(repository::mapEmployeeToEmployeeDetailDto)
                    .collect(Collectors.toList())

    fun getEmployeeById(employeeId: Long): EmployeeDetailDto =
            employeeDAO.findById(employeeId)
                    .map(repository::mapEmployeeToEmployeeDetailDto)
                    .orElseThrow { throwNotFoundException(employeeId) }

    fun addEmployee(dto: EmployeeAddDto): String =
            when {
                dto.isValid() -> saveNewEmployee(dto)
                else -> this.throwBadRequestException(dto)
            }

    fun editEmployee(dto: EmployeeEditDto, employeeId: Long): String =
            when {
                dto.isValid() -> saveEditedEmployee(dto, employeeId)
                else -> this.throwBadRequestException(dto)
            }

    private fun saveNewEmployee(dto: EmployeeAddDto): String {
        employeeDAO.save(repository.mapEmployeeAddDtoToEmployee(dto))
        return "Employee was added!"
    }

    private fun saveEditedEmployee(dto: EmployeeEditDto, employeeId: Long): String {
        employeeDAO.findById(employeeId)
                .ifPresentOrElse(
                        { employee: Employee -> employeeDAO.save(repository.mapEmployeeEditDtoToEmployee(employee, dto)) },
                        { throw NotFoundException("Employee with id: $employeeId was not found!") })
        return "Employee was edited!"
    }

    private fun throwNotFoundException(employeeId: Long): NotFoundException {
        LOGGER.warn("Employee with id: {} was not found!", employeeId)
        return NotFoundException("Employee with id: $employeeId was not found!")
    }

    private fun throwBadRequestException(dto: EmployeeAddDto): String {
        LOGGER.warn("addEmployee() Wrong input parameters for dto = {}", dto)
        throw BadRequestException("addEmployee() Wrong input parameters for dto = $dto")
    }

    private fun throwBadRequestException(dto: EmployeeEditDto): String {
        LOGGER.warn("editEmployee() Wrong input parameters for dto = {}", dto)
        throw BadRequestException("editEmployee() Wrong input parameters for dto = $dto")
    }


}
