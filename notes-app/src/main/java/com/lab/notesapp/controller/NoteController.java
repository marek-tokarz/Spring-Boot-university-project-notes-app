package com.lab.notesapp.controller;

import com.lab.notesapp.exception.ObjectNotFoundException;
import com.lab.notesapp.model.Note;
import com.lab.notesapp.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
@Tag(name = "Note", description = "Note Controller")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    @Operation(method = "GET", summary = "Get all notes", description = "Get all notes from the Database")
    public ResponseEntity<List<Note>> getNotes() {
        return new ResponseEntity<>(noteService.getAllNotes(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(method = "GET", summary = "Get note by id", description = "Get a note by its id")
    public ResponseEntity<Note> getNoteById(
            @Parameter(name = "id", description = "ID of the note to retrieve") @PathVariable Integer id) {

        Optional<Note> noteOpt = noteService.getNoteById(id);
        return noteOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(method = "POST", summary = "Create a note", description = "Create new note")
    public ResponseEntity<Note> createNote(
            @Parameter(name = "Note", description = "Note to create") @RequestBody Note note) {
        return new ResponseEntity<>(noteService.createNote(note), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(method = "PUT", summary = "Update a note", description = "Update an existing note by id")
    public ResponseEntity<Note> updateNote(
            @Parameter(name = "id", description = "ID of the note to update") @PathVariable Integer id,
            @Parameter(name = "Note", description = "Updated note data") @RequestBody Note note) throws ObjectNotFoundException {

        Note updatedNote = noteService.updateNote(note, id);
        return ResponseEntity.ok(updatedNote);
    }

    @DeleteMapping("/{id}")
    @Operation(method = "DELETE", summary = "Delete a note", description = "Delete a note by id")
    public ResponseEntity<Void> deleteNote(
            @Parameter(name = "id", description = "ID of the note to delete") @PathVariable Integer id) {

        try {
            noteService.deleteNote(id);
            return ResponseEntity.noContent().build();
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
