package restaurant_manager.employee

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.test.context.junit.jupiter.SpringExtension
import restaurant_manager.exceptions.BadRequestException
import restaurant_manager.exceptions.NotFoundException
import restaurant_manager.infrastructure.EmployeeDAO
import restaurant_manager.others.Defaults
import restaurant_manager.others.PhoneNumber
import java.math.BigDecimal
import java.time.LocalDate
import java.util.stream.Collectors

@TestInstance(PER_CLASS)
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class EmployeeControllerTestIT(
        @Autowired private val restTemplate: TestRestTemplate,
        @Autowired private val employeeDAO: EmployeeDAO,
        @Autowired private val employeeRepository: EmployeeRepository) {

    @LocalServerPort
    private val port = 0

    private val headers = HttpHeaders()
    private val request: HttpEntity<*> = HttpEntity<Any>(headers)

    companion object {
        private const val ROOT_URL = "http://localhost:"
        private const val API_PATH = "/employees/"
        private const val EDIT = "edit/"
    }

    @BeforeAll
    fun beforeAll() {
        headers["Content-Type"] = "application/json"
    }

    @Test
    fun getEmployees_Should_ReturnListOfEmployeeDetailDto_When_EmployeesExist() {

        val result: ResponseEntity<List<EmployeeDetailDto>> = restTemplate.exchange(
                ROOT_URL + port + API_PATH,
                HttpMethod.GET,
                request,
                object : ParameterizedTypeReference<List<EmployeeDetailDto>>() {})

        val expectedResult = employeeDAO.findAllByOrderByIdAsc()
                .stream()
                .map(employeeRepository::mapEmployeeToEmployeeDetailDto)
                .collect(Collectors.toList())
        assertEquals(HttpStatus.OK, result.statusCode)

        assertNotNull(result.body)
        assertEquals(expectedResult.size, result.body!!.size)
    }

    @Test
    fun getEmployeeById_Should_ReturnEmployeeDetailDto_When_EmployeeWithGivenIdExists() {
        val employeeId = 1L

        val result = restTemplate.exchange(
                ROOT_URL + port + API_PATH + employeeId,
                HttpMethod.GET,
                request,
                EmployeeDetailDto::class.java)

        val expectedResult = employeeDAO.findById(employeeId)
                .map(employeeRepository::mapEmployeeToEmployeeDetailDto)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertNotNull(result.body)
        assertFalse(expectedResult.isEmpty)
        assertEquals(expectedResult.get().toString(), result.body.toString())
    }

    @Test
    fun getEmployeeById_Should_ThrowNotFoundException_When_EmployeeWithGivenIdDoesNotExist() {
        val employeeId = 99L

        val result = restTemplate.exchange(
                ROOT_URL + port + API_PATH + employeeId,
                HttpMethod.GET,
                request,
                NotFoundException::class.java)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        assertNotNull(result.body)
        assertTrue(result.body!!.message!!.contains("Employee with id: $employeeId was not found!"))
    }

    @Test
    fun addEmployee_Should_ReturnEmployeeWasAddedString_When_CorrectInputParameters() {

        val result = restTemplate.exchange(
                ROOT_URL + port + API_PATH,
                HttpMethod.POST,
                HttpEntity(employeeAddDto, headers),
                String::class.java)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertNotNull(result.body)
        assertEquals("Employee was added!", result.body)
    }

    @Test
    fun addEmployee_Should_ThrowBadRequestException_When_IncorrectInputParameters() {

        val result = restTemplate.exchange(
                ROOT_URL + port + API_PATH,
                HttpMethod.POST,
                HttpEntity(EmployeeAddDto(), headers),
                BadRequestException::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
        assertNotNull(result.body)
        assertTrue(result.body!!.message!!.contains("addEmployee() Wrong input parameters for dto = ${EmployeeAddDto()}"))
    }

    @Test
    fun editEmployee_Should_ReturnEmployeeWasEditedString_When_CorrectInputParameters() {
        val employeeId = 1L

        val result = restTemplate.exchange(
                ROOT_URL + port + API_PATH + EDIT + employeeId,
                HttpMethod.PUT,
                HttpEntity(this.employeeEditDto, headers),
                String::class.java)

        val expectedResult = employeeDAO.findById(1L)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertNotNull(result.body)
        assertEquals("Employee was edited!", result.body)
        assertFalse(expectedResult.isEmpty)
        assertEquals(expectedResult.get().firstName, this.employeeEditDto.firstName)
        assertEquals(expectedResult.get().lastName, this.employeeEditDto.lastName)
        assertEquals(expectedResult.get().email, this.employeeEditDto.email)
        assertEquals(expectedResult.get().phoneNumber.phoneNumber, this.employeeEditDto.phoneNumber!!.phoneNumber)
        assertEquals(expectedResult.get().position.position, this.employeeEditDto.position)
        assertEquals(expectedResult.get().salary.salary, this.employeeEditDto.salary)
    }

    @Test
    fun editEmployee_Should_ThrowBadRequestException_When_IncorrectInputParameters() {
        val employeeId = 1L

        val result = restTemplate.exchange(
                ROOT_URL + port + API_PATH + EDIT + employeeId,
                HttpMethod.PUT,
                HttpEntity(EmployeeEditDto(), headers),
                BadRequestException::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
        assertNotNull(result.body)
        assertTrue(result.body!!.message!!.contains("editEmployee() Wrong input parameters for dto = ${EmployeeEditDto()}"))
    }

    @Test
    fun editEmployee_Should_ThrowNotFoundException_When_NotExistingEmployeeId() {
        val employeeId = 99L

        val result = restTemplate.exchange(
                ROOT_URL + port + API_PATH + EDIT + employeeId,
                HttpMethod.PUT,
                HttpEntity(employeeEditDto, headers),
                NotFoundException::class.java)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        assertNotNull(result.body)
        assertTrue(result.body!!.message!!.contains("Employee with id: $employeeId was not found!"))
    }

    private val employeeAddDto: EmployeeAddDto
        get() = EmployeeAddDto(
                firstName = "FirstName",
                lastName = "LastName",
                email = "email@mail.com",
                position = "Waiter",
                salary = BigDecimal(Defaults.DEFAULT_SALARY),
                startDate = LocalDate.now(),
                phoneNumber = PhoneNumber("123456789")
        )

    private val employeeEditDto: EmployeeEditDto
        get() = EmployeeEditDto(
                firstName = "Test",
                lastName = "Test",
                email = "test@mail.com",
                position = "Manager",
                salary = BigDecimal("3000.00"),
                phoneNumber = PhoneNumber("123456789")
        )

}
