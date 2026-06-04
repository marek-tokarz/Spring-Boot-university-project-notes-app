package service;

import com.lab.notesapp.exception.ObjectNotFoundException;
import com.lab.notesapp.model.Note;
import com.lab.notesapp.repository.NoteRepository;
import com.lab.notesapp.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return a note by id")
    void testGetNoteById() {
        Integer noteId = 1;
        Note note = new Note();
        note.setId(noteId);
        note.setTitle("Test Note");

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(note));

        Optional<Note> result = noteService.getNoteById(noteId);

        assertEquals(note.getTitle(), result.get().getTitle());
        verify(noteRepository, times(1)).findById(noteId);
    }

    @Test
    @DisplayName("Should return all notes")
    void testGetAllNotes() {
        Note note1 = new Note();
        note1.setTitle("Note 1");

        Note note2 = new Note();
        note2.setTitle("Note 2");

        when(noteRepository.findAll()).thenReturn(Arrays.asList(note1, note2));

        List<Note> notes = noteService.getAllNotes();

        assertEquals(2, notes.size());
        verify(noteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a note")
    void testCreateNote() {
        Note newNote = new Note();
        newNote.setTitle("New Task");

        when(noteRepository.save(newNote)).thenReturn(newNote);

        Note createdNote = noteService.createNote(newNote);

        assertEquals(newNote.getTitle(), createdNote.getTitle());
        verify(noteRepository, times(1)).save(newNote);
    }

    @Test
    @DisplayName("Should update a note")
    void testUpdateNote() throws ObjectNotFoundException {
        Integer noteId = 1;
        Note existingNote = new Note();
        existingNote.setId(noteId);
        existingNote.setTitle("Existing Task");

        Note updatedNote = new Note();
        updatedNote.setTitle("Updated Task");

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(existingNote));
        when(noteRepository.save(existingNote)).thenReturn(existingNote); // To wywołanie zostanie zatwierdzone

        Note result = noteService.updateNote(updatedNote, noteId);

        assertEquals(existingNote.getTitle(), result.getTitle());
        verify(noteRepository, times(1)).findById(noteId);
        verify(noteRepository, times(1)).save(existingNote); // Teraz to wywołanie będzie prawidłowe
    }

    @Test
    @DisplayName("Should throw ObjectNotFoundException when deleting a non-existing note")
    void testDeleteNoteNotFound() {
        Integer noteId = 1;

        when(noteRepository.findById(noteId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> noteService.deleteNote(noteId));
        verify(noteRepository, times(0)).deleteById(noteId);
    }

    @Test
    @DisplayName("Should delete a note")
    void testDeleteNote() throws ObjectNotFoundException {
        Integer noteId = 1;
        Note note = new Note();
        note.setId(noteId);

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(note));

        noteService.deleteNote(noteId);

        verify(noteRepository, times(1)).deleteById(noteId);
    }
}