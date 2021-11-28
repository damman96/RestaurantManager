package restaurantmanager.board;


import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import restaurantmanager.NotFoundException;

@Slf4j
@Service
class BoardService {
	
	private final BoardDao boardDao;
	
	BoardService(final BoardDao boardDao) {
		this.boardDao = boardDao;
	}
	
	List<BoardDto> getAllBoards() {
		final var boards = this.boardDao.findAll()
				.stream()
				.map(BoardMapper.INSTANCE::map)
				.collect(toUnmodifiableList());
		log.info("Received boards={}", boards);
		return boards;
	}
	
	BoardDto getBoardById(final Long id) {
		final var receivedBoard = BoardMapper.INSTANCE.map(this.getEntityFromDb(id));
		log.info("Received board={}", receivedBoard);
		return receivedBoard;
	}
	
	BoardDto addBoard(final ModifyBoardDto modifyBoardDto) {
		final var savedBoard = this.boardDao.save(BoardMapper.INSTANCE.mapToModify(modifyBoardDto));
		log.info("Saved board={}", savedBoard);
		return BoardMapper.INSTANCE.map(savedBoard);
	}
	
	BoardDto updateBoard(final Long id, final ModifyBoardDto modifyBoardDto) {
		final var boardFromDb = this.getEntityFromDb(id);
		final var modifiedBoard = Board.builder()
				.id(boardFromDb.getId())
				.numberOfSeats(modifyBoardDto.getNumberOfSeats())
				.boardDescription(modifyBoardDto.getBoardDescription()).build();
		return BoardMapper.INSTANCE.map(this.boardDao.save(modifiedBoard));
	}
	
	void deleteBoardById(final Long id) {
		final var removedBoard = this.getBoardById(id);
		this.boardDao.deleteById(removedBoard.getId());
		log.info("Removed board={}", removedBoard);
	}
	
	private Board getEntityFromDb(final Long id) {
		return this.boardDao.findById(id)
				.orElseThrow(() -> new NotFoundException("Board with id=" + id + " not found"));
	}
}
