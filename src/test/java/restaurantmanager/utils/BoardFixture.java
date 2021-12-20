package restaurantmanager.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static restaurantmanager.utils.RandomUtilsFixture.createRandomLong;
import static restaurantmanager.utils.RandomUtilsFixture.createRandomString;

import restaurantmanager.board.Board;
import restaurantmanager.board.BoardDto;
import restaurantmanager.board.ModifyBoardDto;

public abstract class BoardFixture {
	
	public static Board createBoardEntityWithNulls() {
		return Board.builder().build();
	}
	
	public static Board createBoardEntity(final Long id) {
		return Board.builder()
				.id(id)
				.numberOfSeats(createRandomLong())
				.boardDescription(createRandomString())
				.build();
	}
	
	public static Board createBoardEntityFromModifyDto(final Long id, final ModifyBoardDto modifyBoardDto) {
		return Board.builder()
				.id(id)
				.numberOfSeats(modifyBoardDto.getNumberOfSeats())
				.boardDescription(modifyBoardDto.getBoardDescription())
				.build();
	}
	
	public static ModifyBoardDto createModifyBoardDtoWithNulls() {
		return ModifyBoardDto.builder().build();
	}
	
	public static ModifyBoardDto createModifyBoardDto() {
		return ModifyBoardDto.builder()
				.numberOfSeats(createRandomLong())
				.boardDescription(createRandomString())
				.build();
	}
	
	public static void assertBoard(final BoardDto result, final ModifyBoardDto modifyBoardDto) {
		assertThat(result.getNumberOfSeats()).isEqualTo(modifyBoardDto.getNumberOfSeats());
		assertThat(result.getBoardDescription()).isEqualTo(modifyBoardDto.getBoardDescription());
	}
}
