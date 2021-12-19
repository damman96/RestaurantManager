package restaurantmanager.board;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardDaoTestIT {
	
	@Autowired
	private BoardDao boardDao;
	
	@BeforeEach
	void setUp() {
		this.boardDao.deleteAll();
	}
	
	@Test
	void findAll_Should_ReturnEmptyResult_When_NoEntitiesArePresentInDb() {
		// when
		final var result = this.boardDao.findAll();
		
		// then
		assertThat(result).isEmpty();
	}
	
	@Test
	void save_Should_SaveEntity() {
		// given
		final var boardToAdd = Board.builder().build();
		
		// when
		final var result = this.boardDao.save(boardToAdd);
		
		// then
		assertThat(result).isEqualTo(boardToAdd);
	}
	
	@Test
	void findById_Should_ReturnEntity_When_EntityIsPresent() {
		// given
		final var boardToAdd = Board.builder().build();
		this.boardDao.save(boardToAdd);
		
		// when
		final var result = this.boardDao.findById(boardToAdd.getId());
		
		// then
		assertThat(result)
				.isPresent()
				.contains(boardToAdd);
	}
	
	@Test
	void findById_Should_ReturnEmptyResult_When_EntityIsNotPresent() {
		// given
		final var notExistingId = new Random().nextLong();
		// when
		final var result = this.boardDao.findById(notExistingId);
		
		// then
		assertThat(result)
				.isNotPresent();
	}
	
	@Test
	void deleteAll_Should_ReturnEmptyResult_When_AllEntitiesWasRemoved() {
		// given
		this.boardDao.save(Board.builder().build());
		this.boardDao.save(Board.builder().build());
		this.boardDao.save(Board.builder().build());
		
		// when
		this.boardDao.deleteAll();
		final var result = this.boardDao.findAll();
		
		// then
		assertThat(result).isEmpty();
	}
	
}