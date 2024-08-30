package board.board_spring.domain.board.controller;

import board.board_spring.domain.board.dto.BoardDto;
import board.board_spring.domain.board.entity.Board;
import board.board_spring.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("")
    public ResponseEntity<String> createBoard(@RequestBody BoardDto boardDto) {
        boardService.createBoard(boardDto);
        return ResponseEntity.ok().body("create board success!");
    }

    @GetMapping("")
    public ResponseEntity<Board> getBoardById(@RequestParam Long id) {
        Board board = boardService.getBoardById(id);
        return ResponseEntity.ok().body(board);
    }

    @PutMapping("")
    public ResponseEntity<Board> updateBoard(@RequestParam Long id, @RequestBody BoardDto boardDto) {
        Board board = boardService.updateBoard(id, boardDto);
        return ResponseEntity.ok(board);
    }
}
