package com.project.InsightHub.knowledge.entity;

import java.time.LocalDateTime;

import com.project.InsightHub.user.entity.User;
import com.project.InsightHub.workspace.entity.Workspace;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KnowledgeItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String fileName;
    private String fileType;

    @Lob
    private String content;

    private LocalDateTime uploadedAt = LocalDateTime.now();

    @ManyToOne
    private Workspace workspace;

    @ManyToOne
    private User uploadedBy;
}
