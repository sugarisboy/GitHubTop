package dev.muskrat.GitHubTop.service;

import dev.muskrat.GitHubTop.models.Contributor;
import dev.muskrat.GitHubTop.service.AnalysisService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AnalysisServiceTest {

    private static String REAL_PROFILE = "sugarisboy";
    private static String REAL_REPO = "delivery";

    @Autowired
    private AnalysisService service;

    @Test
    void successTest() {
        Contributor first, second, third;

        Contributor[] contrs = {
            new Contributor("fourth", 2),
            second = new Contributor("second", 4),
            new Contributor("fifth", 1),
            third = new Contributor("third", 3),
            first = new Contributor("first", 5),
        };

        AnalysisService service = mock(AnalysisService.class);
        when(service.findContributorsForRepo(anyString(), anyString()))
            .thenReturn(contrs);

        when(service.findBestContributors(anyString(), anyString())).thenCallRealMethod();

        List<Contributor> bestContributors = service.findBestContributors("", "");

        assertEquals(first, bestContributors.get(0));
        assertEquals(second, bestContributors.get(1));
        assertEquals(third, bestContributors.get(2));
    }

    @Test
    void emptyArrayTest() {
        Contributor[] contrs = {};

        AnalysisService service = mock(AnalysisService.class);
        when(service.findContributorsForRepo(anyString(), anyString()))
            .thenReturn(contrs);

        when(service.findBestContributors(anyString(), anyString())).thenCallRealMethod();

        List<Contributor> bestContributors = service.findBestContributors("", "");

        assertNotNull(bestContributors);
        assertTrue(bestContributors.isEmpty());
    }

    @Test
    void realRepoTest() {
        List<Contributor> response = service.findBestContributors(REAL_PROFILE, REAL_REPO);
        assertNotNull(response);
        assertFalse(response.isEmpty());

        List<String> names = response.stream()
            .map(Contributor::getLogin)
            .collect(Collectors.toList());

        assertTrue(names.contains(REAL_PROFILE));
    }

    @Test
    void lessThen3Test() {
        Contributor first, second;

        Contributor[] contrs = {
            second = new Contributor("second", 4),
            first = new Contributor("first", 5),
        };

        AnalysisService service = mock(AnalysisService.class);
        when(service.findContributorsForRepo(anyString(), anyString()))
            .thenReturn(contrs);

        when(service.findBestContributors(anyString(), anyString())).thenCallRealMethod();

        List<Contributor> bestContributors = service.findBestContributors("", "");

        assertEquals(first, bestContributors.get(0));
        assertEquals(second, bestContributors.get(1));
    }


    @Test
    void nonexistentRepoTest() {
        HttpStatusCodeException exception1 = Assertions.assertThrows(HttpStatusCodeException.class,
            () -> service.findBestContributors(REAL_PROFILE, "abcde")
        );

        String nonexistingProfile = "unsopperted#@!_chars";
        HttpStatusCodeException exception2 = Assertions.assertThrows(HttpStatusCodeException.class,
            () -> service.findBestContributors(nonexistingProfile, "abcde")
        );

        assertEquals("Repository not found", exception1.getStatusText());
        assertEquals("Repository not found", exception2.getStatusText());
    }
}