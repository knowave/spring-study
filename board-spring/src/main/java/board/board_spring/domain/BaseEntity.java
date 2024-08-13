package board.board_spring.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass()
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp()
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp()
    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @Column()
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onInsert() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate()
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PreRemove()
    protected void onDeleted() {
        this.deletedAt = LocalDateTime.now();
    }
}
