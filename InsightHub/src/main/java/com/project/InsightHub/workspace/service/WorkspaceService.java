package com.project.InsightHub.workspace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.InsightHub.common.enums.Role;
import com.project.InsightHub.user.entity.User;
import com.project.InsightHub.workspace.entity.UserWorkspace;
import com.project.InsightHub.workspace.entity.Workspace;
import com.project.InsightHub.workspace.repository.UserWorkspaceRepository;
import com.project.InsightHub.workspace.repository.WorkspaceRepository;

@Service
public class WorkspaceService {
     @Autowired private WorkspaceRepository workspaceRepo;
    @Autowired private UserWorkspaceRepository userWorkspaceRepo;

    public Workspace createWorkspace(String name, User creator) {
        Workspace ws = new Workspace();
        ws.setName(name);
        ws.setCreatedBy(creator);
        workspaceRepo.save(ws);

        UserWorkspace uw = new UserWorkspace();
        uw.setUser(creator);
        uw.setWorkspace(ws);
        uw.setRole(Role.ADMIN);
        uw.setCurrent(true);
        userWorkspaceRepo.save(uw);

        // Set all others to false
        userWorkspaceRepo.findByUser(creator).stream()
            .filter(uws -> !uws.getWorkspace().getId().equals(ws.getId()))
            .forEach(uws -> {
                uws.setCurrent(false);
                userWorkspaceRepo.save(uws);
            });

        return ws;
    }

    public List<Workspace> getUserWorkspaces(User user) {
        return userWorkspaceRepo.findByUser(user)
                .stream()
                .map(UserWorkspace::getWorkspace)
                .toList();
    }

    public void switchWorkspace(User user, Long workspaceId) {
        Workspace ws = workspaceRepo.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        UserWorkspace uw = userWorkspaceRepo.findByUserAndWorkspace(user, ws)
                .orElseThrow(() -> new RuntimeException("Not a member of this workspace"));

        userWorkspaceRepo.findByUser(user).forEach(u -> {
            u.setCurrent(false);
            userWorkspaceRepo.save(u);
        });

        uw.setCurrent(true);
        userWorkspaceRepo.save(uw);
    }

    public Optional<Workspace> getCurrentWorkspace(User user) {
        return userWorkspaceRepo.findByUserAndCurrentTrue(user)
                .map(UserWorkspace::getWorkspace);
    }
}
