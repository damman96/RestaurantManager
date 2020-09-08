package restaurant_manager.board

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import restaurant_manager.exceptions.BadRequestException
import restaurant_manager.exceptions.NotFoundException
import restaurant_manager.infrastructure.Board
import restaurant_manager.infrastructure.BoardDAO
import java.util.stream.Collectors

@Component
class BoardApplicationService(
        private val boardDAO: BoardDAO,
        private val repository: BoardRepository) {

    private val LOGGER = LoggerFactory.getLogger(BoardApplicationService::class.java)

    fun getBoards(): List<BoardDetailDto> =
            boardDAO.findAllByOrderByIdAsc()
                    .stream()
                    .map(repository::mapBoardToBoardDetailDto)
                    .collect(Collectors.toList())

    fun getBoardById(boardId: Long): BoardDetailDto =
            boardDAO.findById(boardId)
                    .map(repository::mapBoardToBoardDetailDto)
                    .orElseThrow { throwNotFoundException(boardId) }

    fun addBoard(dto: BoardAddDto): String =
            when {
                dto.isValid() -> saveNewBoard(dto)
                else -> this.throwBadRequestException(dto)
            }

    fun editBoard(dto: BoardEditDto, boardId: Long): String =
            when {
                dto.isValid() -> saveEditedEmployee(dto, boardId)
                else -> this.throwBadRequestException(dto)
            }

    private fun saveNewBoard(dto: BoardAddDto): String {
        boardDAO.save(repository.mapBoardAddDtoToBoard(dto))
        return "Board was added!"
    }

    private fun saveEditedEmployee(dto: BoardEditDto, boardId: Long): String {
        boardDAO.findById(boardId)
                .ifPresentOrElse(
                        { board: Board -> boardDAO.save(repository.mapBoardEditDtoToBoard(board, dto)) },
                        { throw NotFoundException("Board with id: $boardId was not found!") })
        return "Board was edited!"
    }

    private fun throwNotFoundException(boardId: Long): NotFoundException =
            NotFoundException("Board with id: $boardId was not found!")

    private fun throwBadRequestException(dto: BoardAddDto): String {
        LOGGER.warn("addBoard() Wrong input parameters for dto = {}", dto)
        throw BadRequestException("addBoard() Wrong input parameters for dto = $dto")
    }

    private fun throwBadRequestException(dto: BoardEditDto): String {
        LOGGER.warn("editBoard() Wrong input parameters for dto = {}", dto)
        throw BadRequestException("editBoard() Wrong input parameters for dto = $dto")
    }

}
