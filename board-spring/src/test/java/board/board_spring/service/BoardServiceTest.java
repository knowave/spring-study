package board.board_spring.service;

import board.board_spring.domain.board.dto.BoardDto;
import board.board_spring.domain.board.entity.Board;
import board.board_spring.domain.board.repository.BoardRepository;
import board.board_spring.domain.board.service.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardService boardService;

    @Test
    @DisplayName("게시글 생성 테스트")
    public void testCreateBoard() {
        // Given
        BoardDto boardDto = BoardDto.builder()
                .title("test title")
                .content("test content")
                .build();
        Board boardEntity = boardDto.toEntity();

        // Mocking: boardRepository.save() 호출 시 실제 DB에 저장하지 않고 반환만 진행.
        when(boardRepository.save(any(Board.class))).thenReturn(boardEntity);

        // When
        boardService.createBoard(boardDto);

        // Then
        verify(boardRepository, times(1)).save(any(Board.class));
    }
}
