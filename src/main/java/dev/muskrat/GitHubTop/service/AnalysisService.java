package dev.muskrat.GitHubTop.service;

import dev.muskrat.GitHubTop.models.Contributor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalysisService {

    public List<Contributor> findBestContributors(String profile, String repository) throws HttpClientErrorException {
        Contributor[] contributors = findContributorsForRepo(profile, repository);

        if (contributors == null)
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Repository not found");

        return Arrays.stream(contributors)
            .sorted(Comparator.comparingInt(Contributor::getContributions).reversed())
            .limit(3)
            .collect(Collectors.toList());
    }

    public Contributor[] findContributorsForRepo(String profile, String repository) {
        String path = "repos/" + profile + "/" + repository + "/contributors";
        URI url = UriComponentsBuilder.newInstance()
            .scheme("https")
            .host("api.github.com")
            .path(path)
            .build().toUri();

        try {
            RestTemplate template = new RestTemplate();
            ResponseEntity<Contributor[]> response = template.getForEntity(url, Contributor[].class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND))
                return null;
            else
                throw e;
        }
    }
}
