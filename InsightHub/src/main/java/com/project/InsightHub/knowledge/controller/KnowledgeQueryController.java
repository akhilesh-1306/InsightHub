package com.project.InsightHub.knowledge.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.InsightHub.knowledge.entity.KnowledgeItem;
import com.project.InsightHub.knowledge.service.KnowledgeService;
import com.project.InsightHub.user.entity.User;
import com.project.InsightHub.workspace.entity.Workspace;
import com.project.InsightHub.workspace.service.WorkspaceService;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeQueryController {
     @Autowired private KnowledgeService knowledgeService;
    @Autowired private WorkspaceService workspaceService;

    @GetMapping("/")
    public ResponseEntity<List<KnowledgeItem>> getAll(Authentication auth) {
        User user = (User) auth.getPrincipal();
        Workspace ws = workspaceService.getCurrentWorkspace(user)
                .orElseThrow(() -> new RuntimeException("No current workspace"));
        return ResponseEntity.ok(knowledgeService.getAllForWorkspace(ws));
    }

    // NOT working, needs checking
    @GetMapping("/search")
    public ResponseEntity<List<KnowledgeItem>> search(@RequestParam String query, Authentication auth) {
        User user = (User) auth.getPrincipal();
        Workspace ws = workspaceService.getCurrentWorkspace(user)
                .orElseThrow(() -> new RuntimeException("No current workspace"));
        return ResponseEntity.ok(knowledgeService.search(query, ws));
    }
}
