package com.project.InsightHub.workspace.entity;

import java.time.LocalDateTime;

import com.project.InsightHub.common.enums.Role;
import com.project.InsightHub.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserWorkspace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Workspace workspace;

    @Enumerated(EnumType.STRING)
    private Role role = Role.MEMBER;

    private boolean current = false; // is this the active workspace?

    private LocalDateTime joinedAt = LocalDateTime.now();
}
