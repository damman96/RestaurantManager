package restaurantmanager.board;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static restaurantmanager.utils.BoardFixture.createBoardEntity;
import static restaurantmanager.utils.BoardFixture.createBoardEntityWithNulls;
import static restaurantmanager.utils.BoardFixture.createModifyBoardDto;
import static restaurantmanager.utils.BoardFixture.createModifyBoardDtoWithNulls;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import restaurantmanager.NotFoundException;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BoardServiceTestIT {
	
	@Autowired
	private BoardDao boardDao;
	
	@Autowired
	private BoardService boardService;
	
	@BeforeEach
	void setUp() {
		this.boardDao.deleteAll();
	}
	
	@Test
	void getAllBoards_Should_ReturnEmptyList_When_EntitiesAreNotPresentInDb() {
		// when
		final var result = this.boardService.getAllBoards();
		
		// then
		assertThat(result).isEmpty();
	}
	
	@Test
	void getAllBoards_Should_ReturnResultList_When_EntitiesArePresentInDb() {
		// given
		final var boardsToAdd = List.of(createBoardEntity(1L, 1L, "Single"),
										createBoardEntity(2L, 2L, "Double"),
										createBoardEntity(3L, 3L, "Triple"));
		this.boardDao.saveAll(boardsToAdd);
		
		// when
		final var result = this.boardService.getAllBoards();
		
		// then
		assertThat(result).isNotEmpty();
		assertThat(result).extracting(BoardDto::getId)
				.containsAll(boardsToAdd.stream().map(Board::getId).collect(toUnmodifiableList()));
		assertThat(result).extracting(BoardDto::getBoardDescription)
				.containsAll(boardsToAdd.stream().map(Board::getBoardDescription).collect(toUnmodifiableList()));
		assertThat(result).extracting(BoardDto::getNumberOfSeats)
				.containsAll(boardsToAdd.stream().map(Board::getNumberOfSeats).collect(toUnmodifiableList()));
	}
	
	@Test
	void getBoardById_Should_ReturnResult_When_EntityExists() {
		// given
		final var board = createBoardEntity(1L, 1L, "Single");
		this.boardDao.save(board);
		
		// when
		final var result = this.boardService.getBoardById(board.getId());
		
		// then
		assertThat(result).isEqualTo(BoardMapper.INSTANCE.map(board));
	}
	
	@Test
	void getBoardById_Should_ThrowNotFoundException_When_EntityWithGivenIdNotExist() {
		// given
		final var id = new Random().nextLong();
		
		// when
		final var throwable = catchThrowable(() -> this.boardService.getBoardById(id));
		
		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class)
				.hasMessage("Board with id=" + id + " not found");
	}
	
	@Test
	void addBoard_Should_SaveEntity() {
		// given
		final var boardToAdd = createModifyBoardDto(1L, "Single");
		
		// when
		final var result = this.boardService.addBoard(boardToAdd);
		
		// then
		assertThat(result.getId()).isNotNull().isPositive();
		assertThat(result.getNumberOfSeats()).isEqualTo(boardToAdd.getNumberOfSeats());
		assertThat(result.getBoardDescription()).isEqualTo(boardToAdd.getBoardDescription());
	}
	
	@Test
	void updateBoard_Should_UpdateBoard() {
		// given
		final var existingBoard = createBoardEntity(1L, 1L, "Single");
		this.boardDao.save(existingBoard);
		
		final var modifyBoardDto = createModifyBoardDto(2L, "Double");
		
		// when
		final var result = this.boardService.updateBoard(existingBoard.getId(), modifyBoardDto);
		
		// then
		assertThat(result.getId()).isEqualTo(existingBoard.getId());
		assertThat(result.getNumberOfSeats()).isEqualTo(modifyBoardDto.getNumberOfSeats());
		assertThat(result.getBoardDescription()).isEqualTo(modifyBoardDto.getBoardDescription());
	}
	
	@Test
	void updateBoard_Should_ThrowNotFoundException_When_EntityExists() {
		// given
		final var id = new Random().nextLong();
		
		// when
		final var throwable = catchThrowable(() -> this.boardService.updateBoard(id, createModifyBoardDtoWithNulls()));
		
		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class)
				.hasMessage("Board with id=" + id + " not found");
	}
	
	@Test
	void deleteBoardById_Should_RemoveEntity_When_EntityExists() {
		// given
		final var board = createBoardEntityWithNulls();
		this.boardDao.save(board);
		
		// when
		final var result = this.boardService.deleteBoardById(board.getId());
		
		//then
		assertThat(this.boardDao.findById(board.getId())).isNotPresent();
		assertThat(result).isEqualTo(BoardMapper.INSTANCE.map(board));
	}
	
	@Test
	void deleteBoardById_Should_ThrowNotFoundException_When_EntityExists() {
		// given
		final var id = new Random().nextLong();
		
		// when
		final var throwable = catchThrowable(() -> this.boardService.deleteBoardById(id));
		
		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class)
				.hasMessage("Board with id=" + id + " not found");
	}
}