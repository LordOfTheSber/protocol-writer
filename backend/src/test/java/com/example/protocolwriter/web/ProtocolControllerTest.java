package com.example.protocolwriter.web;

import com.example.protocolwriter.domain.ProtocolStatus;
import com.example.protocolwriter.persistence.ProtocolEntity;
import com.example.protocolwriter.service.ProtocolNotFoundException;
import com.example.protocolwriter.service.ProtocolService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProtocolController.class)
class ProtocolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProtocolService service;

    private ProtocolEntity sampleEntity() {
        return new ProtocolEntity(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "Протокол №1", "Иванов", ProtocolStatus.DRAFT, "# Заголовок\n\nТекст.");
    }

    @Test
    void createReturns201() throws Exception {
        when(service.create(any())).thenReturn(sampleEntity());

        var body = """
                {
                  "title": "Протокол №1",
                  "author": "Иванов",
                  "body": "# Заголовок\\n\\nТекст."
                }
                """;

        mockMvc.perform(post("/api/protocols").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Протокол №1"))
                .andExpect(jsonPath("$.body").value("# Заголовок\n\nТекст."));
    }

    @Test
    void createWithBlankTitleReturns400() throws Exception {
        var body = """
                { "title": "  " }
                """;

        mockMvc.perform(post("/api/protocols").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMissingReturns404() throws Exception {
        var id = UUID.randomUUID();
        when(service.findById(id)).thenThrow(new ProtocolNotFoundException(id));

        mockMvc.perform(get("/api/protocols/{id}", id))
                .andExpect(status().isNotFound());
    }
}
