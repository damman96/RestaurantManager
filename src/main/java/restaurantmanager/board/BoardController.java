package restaurantmanager.board;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards")
public class BoardController {
	
	private final BoardService boardService;
	
	public BoardController(final BoardService boardService) {
		this.boardService = boardService;
	}
	
	@GetMapping
	public ResponseEntity<List<BoardDto>> getAllBoards() {
		return ResponseEntity.ok(this.boardService.getAllBoards());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<BoardDto> getBoardById(@PathVariable final Long id) {
		return ResponseEntity.ok(this.boardService.getBoardById(id));
	}
	
	@PostMapping
	public ResponseEntity<BoardDto> addBoard(@RequestBody final ModifyBoardDto modifyBoardDto) {
		return ResponseEntity.ok(this.boardService.addBoard(modifyBoardDto));
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<BoardDto> updateBoard(@PathVariable final Long id,
												@RequestBody final ModifyBoardDto modifyBoardDto) {
		return ResponseEntity.ok(this.boardService.updateBoard(id, modifyBoardDto));
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<BoardDto> deleteBoardById(@PathVariable final Long id) {
		return ResponseEntity.ok(this.boardService.deleteBoardById(id));
		
	}
}
