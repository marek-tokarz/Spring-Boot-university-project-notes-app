package com.lab.notesapp.integration;

import com.lab.notesapp.model.Note;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class NoteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Note buildNote(String noteTitle, String noteContent) throws Exception {
        Note noteRequest = new Note();
        noteRequest.setTitle(noteTitle);
        noteRequest.setContent(noteContent);

        String noteResponse = mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noteRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        long noteId = ((Number) JsonPath.read(noteResponse, "$.id")).longValue();

        Note savedNote = new Note();
        savedNote.setId((int) noteId);
        savedNote.setTitle(noteTitle);
        savedNote.setContent(noteContent);

        return savedNote;
    }

    @Test
    public void shouldRetrieveAllNotes() throws Exception {
        buildNote("Note 1", "Content for Note 1");
        buildNote("Note 2", "Content for Note 2");

        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", isA(List.class)));
    }

    @Test
    public void shouldCreateAndRetrieveNote() throws Exception {
        // Tworzymy nową notatkę
        Note newNote = buildNote("New Note", "Content for New Note");

        // Weryfikujemy, czy notatka została poprawnie utworzona i zwrócona
        mockMvc.perform(get("/api/notes/" + newNote.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("New Note")) // Zmiana oczekiwania na "New Note"
                .andExpect(jsonPath("$.content").value("Content for New Note")); // Dodano weryfikację dla content
    }

    @Test
    public void shouldUpdateExistingNote() throws Exception {
        Note existingNote = buildNote("Initial Note Title", "Initial content for Note 1");

        existingNote.setTitle("Updated Note Title");

        mockMvc.perform(put("/api/notes/" + existingNote.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingNote)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Note Title"));
    }

    @Test
    public void shouldDeleteNote() throws Exception {
        Note noteToDelete = buildNote("Note to Delete", "Content for Note 1");

        mockMvc.perform(delete("/api/notes/" + noteToDelete.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/notes/" + noteToDelete.getId()))
                .andExpect(status().isNotFound());
    }
}
