package board.board_spring.domain.board.service;

import board.board_spring.domain.board.dto.BoardDto;
import board.board_spring.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service()
public class BoardService {
    private final BoardRepository boardRepository;

    public void createBoard(BoardDto boardDto) {
        boardRepository.save(boardDto.toEntity());
    }
}
