package restaurantmanager.board;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public class ModifyBoardDto {
	
	private Long numberOfSeats;
	private String boardDescription;
}
