package restaurantmanager.board;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.assertj.core.api.Assertions.assertThat;
import static restaurantmanager.utils.BoardFixture.createBoardEntityWithNulls;
import static restaurantmanager.utils.BoardFixture.createModifyBoardDto;
import static restaurantmanager.utils.BoardFixture.createModifyBoardDtoWithNulls;
import static restaurantmanager.utils.RandomUtilsFixture.createRandomLong;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BoardControllerTest {
	
	private static final String HTTP_LOCAL_HOST = "http://localhost:";
	
	private static final String SLASH = "/";
	
	private static final String BOARDS = "boards";
	private static final String UPDATE = "update";
	private static final String DELETE = "delete";
	
	@Autowired
	private BoardDao boardDao;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@LocalServerPort
	private int randomServerPort;
	
	@BeforeEach
	void setUp() {
		this.boardDao.deleteAll();
	}
	
	@Test
	void getAllBoards_Should_ReturnStatusCode200AndEmptyList_When_DatabaseIsEmpty() {
		// given
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOARDS;
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				new ParameterizedTypeReference<List<BoardDto>>() {
				});
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull().isEmpty();
	}
	
	@Test
	void getAllBoards_Should_ReturnStatusCode200AndResultList_When_DatabaseIsNotEmpty() {
		//given
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOARDS;
		final var boards = List.of(
				createBoardEntityWithNulls(1L),
				createBoardEntityWithNulls(2L),
				createBoardEntityWithNulls(3L));
		this.boardDao.saveAll(boards);
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				new ParameterizedTypeReference<List<BoardDto>>() {
				});
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull().isNotEmpty();
		assertThat(result.getBody()).usingRecursiveComparison()
				.isEqualTo(boards.stream().map(BoardMapper.INSTANCE::map).collect(toUnmodifiableList()));
	}
	
	@Test
	void getBoardById_Should_ReturnStatusCode200AndResult_When_EntityWithGivenIdExists() {
		// given
		final var saved = this.boardDao.save(createBoardEntityWithNulls());
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOARDS + SLASH + saved.getId();
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				BoardDto.class);
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody())
				.isNotNull()
				.isEqualTo(BoardMapper.INSTANCE.map(saved));
	}
	
	@Test
	void getBoardById_Should_ReturnStatusCode404_When_EntityWithGivenIdNotExist() {
		// given
		final var id = createRandomLong();
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOARDS + SLASH + id;
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				BoardDto.class);
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	void addBoard_Should_ReturnStatusCode200AndResult_When_SuccessfullyAddedBoard() {
		// given
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOARDS;
		final var body = new HttpEntity<>(createModifyBoardDtoWithNulls());
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.POST,
				body,
				BoardDto.class);
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isNotNull();
		assertThat(result.getBody().getNumberOfSeats()).isNull();
		assertThat(result.getBody().getBoardDescription()).isNull();
	}
	
	@Test
	void updateBoard_Should_ReturnStatusCode200_When_SuccessfullyUpdatedEntity() {
		// given
		final var saved = this.boardDao.save(createBoardEntityWithNulls());
		final var modifyBoardDto = createModifyBoardDto(2L, "Double");
		
		final var body = new HttpEntity<>(modifyBoardDto);
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOARDS + SLASH + UPDATE + SLASH + saved.getId();
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.PUT,
				body,
				BoardDto.class);
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isEqualTo(saved.getId());
		assertThat(result.getBody().getNumberOfSeats()).isEqualTo(modifyBoardDto.getNumberOfSeats());
		assertThat(result.getBody().getBoardDescription()).isEqualTo(modifyBoardDto.getBoardDescription());
	}
	
	@Test
	void updateBoard_Should_ReturnStatusCode404_When_EntityWithGivenIdNotExist() {
		// given
		final var id = createRandomLong();
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOARDS + SLASH + UPDATE + SLASH + id;
		final var body = new HttpEntity<>(createModifyBoardDtoWithNulls());
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.PUT,
				body,
				BoardDto.class);
		
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	void deleteBoardById_Should_ReturnStatusCode200AndResult_When_EntityWithGivenIdWasDeleted() {
		// given
		final var saved = this.boardDao.save(createBoardEntityWithNulls());
		final var baseUrl = HTTP_LOCAL_HOST + this.randomServerPort + SLASH + BOARDS + SLASH + DELETE + SLASH + saved.getId();
		
		// when
		final var result = this.restTemplate.exchange(
				baseUrl,
				HttpMethod.DELETE,
				HttpEntity.EMPTY,
				BoardDto.class);
		
		// then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody())
				.isNotNull()
				.isEqualTo(BoardMapper.INSTANCE.map(saved));
	}
	
}