package com.project.InsightHub.knowledge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.InsightHub.knowledge.service.KnowledgeService;
import com.project.InsightHub.user.entity.User;
import com.project.InsightHub.workspace.entity.Workspace;
import com.project.InsightHub.workspace.service.WorkspaceService;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeUploadController {
    @Autowired private KnowledgeService knowledgeService;
    @Autowired private WorkspaceService workspaceService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadKnowledge(
            @RequestParam("file") MultipartFile file,
            Authentication auth) {

        User user = (User) auth.getPrincipal();
        Workspace workspace = workspaceService.getCurrentWorkspace(user)
                .orElseThrow(() -> new RuntimeException("No active workspace"));

        knowledgeService.saveKnowledge(file, user, workspace);
        return ResponseEntity.ok("File uploaded and parsed successfully");
    }
}
