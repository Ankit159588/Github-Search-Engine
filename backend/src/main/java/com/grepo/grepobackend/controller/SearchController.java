package com.grepo.grepobackend.controller;

import com.grepo.grepobackend.dto.CodeSearchResultDto;
import com.grepo.grepobackend.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/api/search")
    public List<CodeSearchResultDto> search(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String repo
    ) {
        return searchService.search(query, type, repo);
    }
}