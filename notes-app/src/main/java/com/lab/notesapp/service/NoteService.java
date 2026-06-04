package com.lab.notesapp.service;

import com.lab.notesapp.exception.ObjectNotFoundException;
import com.lab.notesapp.model.Note;
import com.lab.notesapp.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Optional<Note> getNoteById(Integer id) {
        return noteRepository.findById(id);
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    public Note updateNote(Note note, Integer id) throws ObjectNotFoundException {
        Optional<Note> existingNoteOpt = this.getNoteById(id);
        if (existingNoteOpt.isPresent()) {
            Note existingNote = existingNoteOpt.get();
            existingNote.setTitle(note.getTitle());  // Użyj istniejącego obiektu
            return this.noteRepository.save(existingNote); // Zapisz zmiany w istniejącym obiekcie
        }
        throw new ObjectNotFoundException(id);
    }

    public void deleteNote(Integer id) throws ObjectNotFoundException {
        if (this.getNoteById(id).isEmpty()) {
            throw new ObjectNotFoundException(id);
        }
        noteRepository.deleteById(id);
    }
}
