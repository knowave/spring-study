package board.board_spring.domain.board.repository;

import board.board_spring.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Board getBoardById(Long id);
}
