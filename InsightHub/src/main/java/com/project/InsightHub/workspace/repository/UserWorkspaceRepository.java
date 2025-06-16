package com.project.InsightHub.workspace.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.InsightHub.user.entity.User;
import com.project.InsightHub.workspace.entity.UserWorkspace;
import com.project.InsightHub.workspace.entity.Workspace;

public interface UserWorkspaceRepository extends JpaRepository<UserWorkspace, Long> {
    List<UserWorkspace> findByUser(User user);
    Optional<UserWorkspace> findByUserAndCurrentTrue(User user);
    Optional<UserWorkspace> findByUserAndWorkspace(User user, Workspace workspace); 
}
