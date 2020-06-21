package dev.muskrat.GitHubTop.controller;

import dev.muskrat.GitHubTop.models.Contributor;
import dev.muskrat.GitHubTop.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@RestController
@RequestMapping("/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService service;

    @GetMapping("/best/{profile}/{repository}")
    public ResponseEntity findBestContributors(
        @PathVariable String profile,
        @PathVariable String repository
    ) {
        try {
            List<Contributor> response = service.findBestContributors(profile, repository);
            return ResponseEntity.ok(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ResponseEntity
                .status(e.getStatusCode())
                .body(e.getStatusText());
        }
    }
}
