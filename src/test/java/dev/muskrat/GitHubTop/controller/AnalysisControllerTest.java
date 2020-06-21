package dev.muskrat.GitHubTop.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muskrat.GitHubTop.models.Contributor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AnalysisControllerTest {

    private static String REAL_PROFILE = "sugarisboy";
    private static String REAL_REPO = "delivery";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @SneakyThrows
    void successTest() {
        String content = mockMvc.perform(
            get(String.format("/analysis/best/%s/%s", REAL_PROFILE, REAL_REPO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        List<Contributor> contributors = mapper.readValue(content, new TypeReference<>() {});

        assertNotNull(contributors);
        assertFalse(contributors.isEmpty());
    }

    @Test
    @SneakyThrows
    void incorrectDataTest1() {
        mockMvc.perform(
            get(String.format("/analysis/best/%s/%s", REAL_PROFILE, REAL_REPO + "()_"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound());
    }
}