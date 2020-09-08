package restaurant_manager.board

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
import restaurant_manager.infrastructure.BoardDAO
import java.util.stream.Collectors

@TestInstance(PER_CLASS)
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class BoardControllerTestIT(
        @Autowired private val restTemplate: TestRestTemplate,
        @Autowired private val boardDAO: BoardDAO,
        @Autowired private val boardRepository: BoardRepository) {

    @LocalServerPort
    private val port = 0

    private val headers = HttpHeaders()
    private val request: HttpEntity<*> = HttpEntity<Any>(headers)

    companion object {
        private const val ROOT_URL = "http://localhost:"
        private const val API_PATH = "/boards/"
        private const val EDIT = "edit/"
    }

    @BeforeAll
    fun beforeAll() {
        headers["Content-Type"] = "application/json"
    }

    @Test
    fun getBoards_Should_ReturnListOfBoardDetailDto_When_BoardsExist() {

        val result: ResponseEntity<List<BoardDetailDto>> = restTemplate.exchange(
                ROOT_URL + port + API_PATH,
                HttpMethod.GET,
                request,
                object : ParameterizedTypeReference<List<BoardDetailDto>>() {})

        val expectedResult = boardDAO.findAllByOrderByIdAsc()
                .stream()
                .map(boardRepository::mapBoardToBoardDetailDto)
                .collect(Collectors.toList())

        assertEquals(HttpStatus.OK, result.statusCode)
        assertNotNull(result.body)
        assertEquals(expectedResult.size, result.body!!.size)
    }

    @Test
    fun getBoardById_Should_ReturnBoardDetailDto_When_BoardWithGivenIdExists() {
        val boardId = 1L

        val result = restTemplate.exchange(
                ROOT_URL + port + API_PATH + boardId,
                HttpMethod.GET,
                request,
                BoardDetailDto::class.java)

        val expectedResult = boardDAO.findById(boardId)
                .map(boardRepository::mapBoardToBoardDetailDto)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertNotNull(result.body)
        assertFalse(expectedResult.isEmpty)
        assertEquals(expectedResult.get().toString(), result.body.toString())
    }

    @Test
    fun getBoardById_Should_ThrowNotFoundException_When_BoardWithGivenIdDoesNotExist() {
        val boardId = 99L

        val result = restTemplate.exchange(
                ROOT_URL + port + API_PATH + boardId,
                HttpMethod.GET,
                request,
                NotFoundException::class.java)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        assertNotNull(result.body)
        assertTrue(result.body!!.message!!.contains("Board with id: $boardId was not found!"))
    }

    @Test
    fun addBoard_Should_ReturnBoardWasAddedString_When_CorrectInputParameters() {

        val result = restTemplate.exchange(
                ROOT_URL + port + API_PATH,
                HttpMethod.POST,
                HttpEntity(boardAddDto, headers),
                String::class.java)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertNotNull(result.body)
        assertEquals("Board was added!", result.body)
    }

    @Test
    fun addBoard_Should_ThrowBadRequestException_When_IncorrectInputParameters() {

        val result = restTemplate.exchange(
                ROOT_URL + port + API_PATH,
                HttpMethod.POST,
                HttpEntity(BoardAddDto(), headers),
                BadRequestException::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
        assertNotNull(result.body)
        assertTrue(result.body!!.message!!.contains("addBoard() Wrong input parameters for dto = ${BoardAddDto()}"))
    }

    @Test
    fun editBoard_Should_ReturnBoardWasEditedString_When_CorrectInputParameters() {
        val boardId = 1L

        val result = restTemplate.exchange(
                ROOT_URL + port + API_PATH + EDIT + boardId,
                HttpMethod.PUT,
                HttpEntity(boardEditDto, headers),
                String::class.java)

        val expectedResult = boardDAO.findById(1L)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertNotNull(result.body)
        assertEquals("Board was edited!", result.body)
        assertFalse(expectedResult.isEmpty)
        assertEquals(expectedResult.get().numberOfSeats, this.boardEditDto.numberOfSeats)
        assertEquals(expectedResult.get().description, this.boardEditDto.description)
        assertEquals(expectedResult.get().employee.id, this.boardEditDto.employeeId)
    }

    @Test
    fun editBoard_Should_ThrowBadRequestException_When_IncorrectInputParameters() {
        val boardId = 1L

        val result = restTemplate.exchange(
                ROOT_URL + port + API_PATH + EDIT + boardId,
                HttpMethod.PUT,
                HttpEntity(BoardEditDto(), headers),
                BadRequestException::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
        assertNotNull(result.body)
        assertTrue(result.body!!.message!!.contains("editBoard() Wrong input parameters for dto = ${BoardEditDto()}"))
    }

    @Test
    fun editBoard_Should_ThrowNotFoundException_When_NotExistingBoardId() {
        val boardId = 99L

        val result = restTemplate.exchange(
                ROOT_URL + port + API_PATH + EDIT + boardId,
                HttpMethod.PUT,
                HttpEntity(boardEditDto, headers),
                NotFoundException::class.java)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
        assertNotNull(result.body)
        assertTrue(result.body!!.message!!.contains("Board with id: $boardId was not found!"))
    }

    private val boardAddDto: BoardAddDto
        get() = BoardAddDto(numberOfSeats = 3,
                description = "Stolik trzyosobowy",
                employeeId = 2L)

    private val boardEditDto: BoardEditDto
        get() = BoardEditDto(numberOfSeats = 3,
                description = "Stolik trzyosobowy",
                employeeId = 2L)

}
