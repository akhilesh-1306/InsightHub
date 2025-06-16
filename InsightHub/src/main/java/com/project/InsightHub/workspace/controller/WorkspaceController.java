package com.project.InsightHub.workspace.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.InsightHub.user.entity.User;
import com.project.InsightHub.workspace.entity.Workspace;
import com.project.InsightHub.workspace.service.WorkspaceService;

@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {
    @Autowired private WorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<Workspace> create(@RequestBody Map<String, String> request, Authentication auth) {
        User user = (User) auth.getPrincipal();
        Workspace ws = workspaceService.createWorkspace(request.get("name"), user);
        return ResponseEntity.ok(ws);
    }

    @GetMapping
    public ResponseEntity<List<Workspace>> getAll(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return ResponseEntity.ok(workspaceService.getUserWorkspaces(user));
    }

    @PostMapping("/switch")
    public ResponseEntity<String> switchWs(@RequestBody Map<String, Long> req, Authentication auth) {
        User user = (User) auth.getPrincipal();
        workspaceService.switchWorkspace(user, req.get("workspaceId"));
        return ResponseEntity.ok("Switched workspace");
    }

    @GetMapping("/current")
    public ResponseEntity<Workspace> getCurrent(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return workspaceService.getCurrentWorkspace(user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
