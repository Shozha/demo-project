package com.technokratos.agona.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "task")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task extends AbstractEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "starter_code")
    private String starterCode;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "review_deadline")
    private LocalDateTime reviewDeadline;

    @Column(name = "required_reviewers")
    private Integer requiredReviewers;

    @Column(name = "max_score")
    private Integer maxScore;

}
