package com.springdistributed.controller;

import com.springdistributed.model.Note;
import com.springdistributed.util.NotesData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class ZKNotesController {

    @GetMapping("/notes")
    public ResponseEntity<List<Note>> getAllNotes() {
        return ResponseEntity.ok(NotesData.getAllNotesFromData());
    }

    @PostMapping("/notes")
    public ResponseEntity<String> addNote(@RequestBody Note note) {
        NotesData.saveNoteIntoData(note);
        return ResponseEntity.ok().body("Note saved!");
    }

    @PutMapping("note/{id}")
    public ResponseEntity<String> updateNote(@PathVariable(value="id") int id, @RequestBody Note note) {
        Note storedNote = NotesData.findById(id);
        if(storedNote == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found!");
        }
        NotesData.saveNoteIntoData(note);
        return ResponseEntity.ok().body("Note updadted!");
    }

}
