package com.grepo.grepobackend.service;

import com.grepo.grepobackend.dto.CodeSearchResultDto;
import com.grepo.grepobackend.model.CodeElement;
import com.grepo.grepobackend.model.ElementType;
import com.grepo.grepobackend.repository.CodeElementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final CodeElementRepository codeElementRepository;

    public List<CodeSearchResultDto> search(String query, String type, String repoName) {
        ElementType typeEnum = null;
        if (type != null && !type.isBlank()) {
            typeEnum = ElementType.valueOf(type.toUpperCase());
        }

        List<CodeElement> results = codeElementRepository.search(query, typeEnum, repoName);

        return results.stream()
                .map(this::toDto)
                .toList();
    }

    private CodeSearchResultDto toDto(CodeElement el) {
        return new CodeSearchResultDto(
                el.getId(),
                el.getSourceFile().getRepository().getRepoName(),
                el.getSourceFile().getFilePath(),
                el.getStartLine(),
                el.getType().name(),
                el.getName(),
                el.getSignature(),
                el.getSourceFile().getId()
        );
    }
}