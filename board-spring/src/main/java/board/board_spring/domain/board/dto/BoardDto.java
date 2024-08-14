package board.board_spring.domain.board.dto;

import board.board_spring.domain.board.entity.Board;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardDto {
    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Board toEntity() {
        Board board = Board.builder()
                .title(title)
                .content(content)
                .build();

        return board;
    }
}
