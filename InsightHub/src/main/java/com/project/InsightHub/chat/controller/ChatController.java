package com.project.InsightHub.chat.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.InsightHub.chat.service.ChatService;
import com.project.InsightHub.user.entity.User;
import com.project.InsightHub.workspace.service.WorkspaceService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired private ChatService chatService;
    @Autowired private WorkspaceService workspaceService;

    @PostMapping("/ask")
    public ResponseEntity<String> ask(@RequestBody Map<String, String> req, Authentication auth) throws Exception {
        String question = req.get("question");
        User user = (User) auth.getPrincipal();

        Long workspaceId = workspaceService.getCurrentWorkspace(user)
                .orElseThrow(() -> new RuntimeException("No workspace")).getId();

        String answer = chatService.ask(question, workspaceId);
        return ResponseEntity.ok(answer);
    }
}
