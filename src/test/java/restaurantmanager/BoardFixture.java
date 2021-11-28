package restaurantmanager;

import restaurantmanager.board.Board;
import restaurantmanager.board.ModifyBoardDto;

public class BoardFixture {
	
	public static Board createBoardEntityWithNulls() {
		return Board.builder().build();
	}
	
	public static Board createBoardEntity(final Long id, final Long numberOfSeats, final String boardDescription) {
		return Board.builder()
				.id(id)
				.numberOfSeats(numberOfSeats)
				.boardDescription(boardDescription)
				.build();
	}
	
	public static ModifyBoardDto createModifyBoardDtoWithNulls() {
		return ModifyBoardDto.builder().build();
	}
	
	public static ModifyBoardDto createModifyBoardDto(final Long numberOfSeats, final String boardDescription) {
		return ModifyBoardDto.builder()
				.numberOfSeats(numberOfSeats)
				.boardDescription(boardDescription)
				.build();
	}
}
