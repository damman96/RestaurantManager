package restaurantmanager.board;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static restaurantmanager.utils.BoardFixture.createBoardEntity;
import static restaurantmanager.utils.BoardFixture.createBoardEntityWithNulls;
import static restaurantmanager.utils.BoardFixture.createModifyBoardDto;
import static restaurantmanager.utils.BoardFixture.createModifyBoardDtoWithNulls;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import restaurantmanager.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {
	
	private final BoardDao boardDao = Mockito.mock(BoardDao.class);
	
	@InjectMocks
	private BoardService boardService;
	
	@BeforeEach
	void setUp() {
		this.boardDao.deleteAll();
	}
	
	@Test
	void getAllBoards_Should_ReturnEmptyList_When_EntitiesAreNotPresentInDb() {
		// given
		when(this.boardDao.findAll()).thenReturn(emptyList());
		
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
		
		when(this.boardDao.findAll()).thenReturn(boardsToAdd);
		
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
		final var id = 1L;
		final var board = createBoardEntity(id, 1L, "Single");
		when(this.boardDao.findById(id)).thenReturn(Optional.of(board));
		
		// when
		final var result = this.boardService.getBoardById(id);
		
		// then
		assertThat(result).isEqualTo(BoardMapper.INSTANCE.map(board));
	}
	
	@Test
	void getBoardById_Should_ThrowNotFoundException_When_EntityWithGivenIdNotExist() {
		// given
		final var id = new Random().nextLong();
		when(this.boardDao.findById(id)).thenReturn(Optional.empty());
		
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
		final var entity = Board.builder()
				.id(1L)
				.numberOfSeats(boardToAdd.getNumberOfSeats())
				.boardDescription(boardToAdd.getBoardDescription())
				.build();
		
		when(this.boardDao.save(BoardMapper.INSTANCE.mapFromModify(boardToAdd))).thenReturn(entity);
		final var result = this.boardService.addBoard(boardToAdd);
		
		// then
		assertThat(result.getId()).isNotNull().isPositive();
		assertThat(result.getNumberOfSeats()).isEqualTo(boardToAdd.getNumberOfSeats());
		assertThat(result.getBoardDescription()).isEqualTo(boardToAdd.getBoardDescription());
	}
	
	@Test
	void updateBoard_Should_UpdateBoard() {
		// given
		final var id = 1L;
		final var existingBoard = createBoardEntity(id, 1L, "Single");
		when(this.boardDao.findById(id)).thenReturn(Optional.of(existingBoard));
		
		final var modifyBoardDto = createModifyBoardDto(2L, "Double");
		
		// when
		final var entity = Board.builder()
				.id(id)
				.numberOfSeats(modifyBoardDto.getNumberOfSeats())
				.boardDescription(modifyBoardDto.getBoardDescription())
				.build();
		
		when(this.boardDao.save(entity)).thenReturn(entity);
		final var result = this.boardService.updateBoard(id, modifyBoardDto);
		
		// then
		assertThat(result.getId()).isEqualTo(id);
		assertThat(result.getNumberOfSeats()).isEqualTo(modifyBoardDto.getNumberOfSeats());
		assertThat(result.getBoardDescription()).isEqualTo(modifyBoardDto.getBoardDescription());
	}
	
	@Test
	void updateBoard_Should_ThrowNotFoundException_When_EntityExists() {
		// given
		final var id = new Random().nextLong();
		when(this.boardDao.findById(id)).thenReturn(Optional.empty());
		
		// when
		final var throwable = catchThrowable(() -> this.boardService.updateBoard(id, createModifyBoardDtoWithNulls()));
		
		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class)
				.hasMessage("Board with id=" + id + " not found");
	}
	
	@Test
	void deleteBoardById_Should_RemoveEntity_When_EntityExists() {
		// given
		final var id = 1L;
		final var board = createBoardEntityWithNulls(id);
		when(this.boardDao.findById(id)).thenReturn(Optional.of(board));
		
		// when
		this.boardService.deleteBoardById(id);
		
		//then
		verify(this.boardDao, times(1)).deleteById(id);
	}
	
	@Test
	void deleteBoardById_Should_ThrowNotFoundException_When_EntityExists() {
		// given
		final var id = new Random().nextLong();
		when(this.boardDao.findById(id)).thenReturn(Optional.empty());
		
		// when
		final var throwable = catchThrowable(() -> this.boardService.deleteBoardById(id));
		
		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class)
				.hasMessage("Board with id=" + id + " not found");
	}
}
