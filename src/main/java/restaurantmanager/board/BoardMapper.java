package restaurantmanager.board;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BoardMapper {
	
	BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);
	
	BoardDto map(Board board);
	
	Board mapToModify(ModifyBoardDto modifyBoardDto);
	
}
