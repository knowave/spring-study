package board.board_spring.domain.board.service;

import board.board_spring.domain.board.dto.BoardDto;
import board.board_spring.domain.board.entity.Board;
import board.board_spring.domain.board.repository.BoardRepository;
import board.board_spring.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service()
public class BoardService {
    private final BoardRepository boardRepository;

    public void createBoard(BoardDto boardDto) {
        boardRepository.save(boardDto.toEntity());
    }

    public Board getBoardById(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new CustomException("Not Found Board", HttpStatus.NOT_FOUND));
        return board;
    }

    public Board updateBoard(Long id, BoardDto boardDto) {
        Board board = this.getBoardById(id);

        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());

        return boardRepository.save(board);
    }
}
