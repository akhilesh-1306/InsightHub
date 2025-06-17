package com.project.InsightHub.knowledge.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.InsightHub.knowledge.entity.KnowledgeItem;
import com.project.InsightHub.knowledge.repository.KnowledgeItemRepository;
import com.project.InsightHub.user.entity.User;
import com.project.InsightHub.workspace.entity.Workspace;

@Service
public class KnowledgeService {
    @Autowired private KnowledgeItemRepository knowledgeRepo;

    public void saveKnowledge(MultipartFile file, User user, Workspace workspace) {
        String content;
        try {
            content = extractContent(file);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract file content: " + e.getMessage());
        }

        KnowledgeItem item = new KnowledgeItem();
        item.setTitle(file.getOriginalFilename());
        item.setFileName(file.getOriginalFilename());
        item.setFileType(file.getContentType());
        item.setContent(content);
        item.setWorkspace(workspace);
        item.setUploadedBy(user);

        knowledgeRepo.save(item);
    }

    private String extractContent(MultipartFile file) throws Exception {
        String fileType = Objects.requireNonNull(file.getContentType()).toLowerCase();

        if (fileType.contains("pdf")) {
            return extractPdf(file);
        } else if (fileType.contains("word") || fileType.contains("doc")) {
            return extractDocx(file);
        } else if (fileType.contains("text") || fileType.contains("plain")) {
            return new String(file.getBytes(), StandardCharsets.UTF_8);
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
    }

    private String extractPdf(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            return new PDFTextStripper().getText(document);
        }
    }

    private String extractDocx(MultipartFile file) throws IOException {
        try (XWPFDocument doc = new XWPFDocument(file.getInputStream())) {
            return doc.getParagraphs().stream()
                .map(XWPFParagraph::getText)
                .collect(Collectors.joining("\n"));
        }
    }

    @Transactional(readOnly = true)
    public List<KnowledgeItem> getAllForWorkspace(Workspace ws) {
        return knowledgeRepo.findByWorkspace(ws);
    }

    @Transactional(readOnly = true)
    public List<KnowledgeItem> search(String query, Workspace ws) {
        return knowledgeRepo.searchByKeyword(ws.getId(), "%" + query.toLowerCase() + "%");
    }
}
