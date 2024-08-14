package board.board_spring.domain.board.entity;

import board.board_spring.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity()
public class Board extends BaseEntity {
    @Column(length = 50, nullable = false)
    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
}
