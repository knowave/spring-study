package board.board_spring.service;

import board.board_spring.domain.board.dto.BoardDto;
import board.board_spring.domain.board.entity.Board;
import board.board_spring.domain.board.repository.BoardRepository;
import board.board_spring.domain.board.service.BoardService;
import board.board_spring.global.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    @DisplayName("게시글 ID로 조회 - 성공")
    public void testGetBoardByIdSuccess() {
        // Given
        Long boardId = 1L;
        Board boardEntity = Board.builder()
                .title("test title")
                .content("test content")
                .build();

        // Mocking: findById() 호출 시 해당 ID의 Board 반환, id는 setter를 통해 수동 설정
        boardEntity.setId(boardId);
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(boardEntity));

        // When
        Board result = boardService.getBoardById(boardId);

        // Then
        verify(boardRepository, times(1)).findById(boardId);
        // 원하는 결과를 확인할 수 있는 assert 구문 추가 가능 (e.g., assertEquals)
    }

    @Test
    @DisplayName("게시글 ID로 조회 - 실패 (존재하지 않는 경우)")
    public void testGetBoardByIdFailure() {
        // Given
        Long boardId = 1L;

        // Mocking: findById() 호출 시 빈 Optional 반환
        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CustomException.class, () -> {
            boardService.getBoardById(boardId);
        });

        verify(boardRepository, times(1)).findById(boardId);
    }

    @Test
    @DisplayName("게시글 수정 - 성공")
    public void testUpdateBoardSuccess() {
        // Given
        Long boardId = 1L;
        BoardDto boardDto = BoardDto.builder()
                .title("test title")
                .content("test content")
                .build();
        Board board = Board.builder()
                .title("exist title")
                .content("exist content")
                .build();
        board.setId(boardId);

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        when(boardRepository.save(any(Board.class))).thenReturn(board);

        // When
        Board updatedBoard = boardService.updateBoard(boardId, boardDto);

        // Then
        verify(boardRepository, times(1)).findById(boardId);
        verify(boardRepository, times(1)).save(board);

        assertEquals(boardDto.getTitle(), updatedBoard.getTitle());
        assertEquals(boardDto.getContent(), updatedBoard.getContent());
    }

    @Test
    @DisplayName("게시글 업데이트 - 실패 (존재하지 않는 ID)")
    public void testUpdateBoardFailure() {
        // Given
        Long boardId = 1L;
        BoardDto boardDto = BoardDto.builder()
                .title("updated title")
                .content("updated content")
                .build();

        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CustomException.class, () -> {
            boardService.updateBoard(boardId, boardDto);
        });

        verify(boardRepository, times(1)).findById(boardId);
        verify(boardRepository, times(0)).save(any(Board.class));
    }
}
