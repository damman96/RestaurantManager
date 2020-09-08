package restaurant_manager.board

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/boards")
class BoardController(private val applicationService: BoardApplicationService) {

    @GetMapping
    fun getBoards(): ResponseEntity<List<BoardDetailDto>> =
            ResponseEntity.ok(applicationService.getBoards())

    @GetMapping("/{boardId}")
    fun getBoardById(@PathVariable boardId: Long): ResponseEntity<BoardDetailDto> =
            ResponseEntity.ok(applicationService.getBoardById(boardId))

    @PostMapping
    fun addBoard(@RequestBody boardAddDto: BoardAddDto): ResponseEntity<String> =
            ResponseEntity.ok(applicationService.addBoard(boardAddDto))

    @PutMapping("/edit/{boardId}")
    fun editBoard(@RequestBody boardEditDto: BoardEditDto, @PathVariable boardId: Long): ResponseEntity<String> =
            ResponseEntity.ok(applicationService.editBoard(boardEditDto, boardId))
}
