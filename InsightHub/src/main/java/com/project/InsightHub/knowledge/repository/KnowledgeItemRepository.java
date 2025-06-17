package com.project.InsightHub.knowledge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.InsightHub.knowledge.entity.KnowledgeItem;
import com.project.InsightHub.workspace.entity.Workspace;

public interface KnowledgeItemRepository extends JpaRepository<KnowledgeItem, Long> {
    List<KnowledgeItem> findByWorkspace(Workspace workspace);
}
