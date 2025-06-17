package com.project.InsightHub.knowledge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.InsightHub.knowledge.entity.KnowledgeItem;
import com.project.InsightHub.workspace.entity.Workspace;

public interface KnowledgeItemRepository extends JpaRepository<KnowledgeItem, Long> {
    List<KnowledgeItem> findByWorkspace(Workspace workspace);

    @Query(value = "SELECT * FROM knowledge_item k WHERE k.workspace_id = :workspaceId AND " +
       "(LOWER(k.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(CAST(k.content AS TEXT)) LIKE LOWER(CONCAT('%', :query, '%')))",
       nativeQuery = true)
    List<KnowledgeItem> searchByKeyword(@Param("workspaceId") Long workspaceId,
                                    @Param("query") String query);
}
