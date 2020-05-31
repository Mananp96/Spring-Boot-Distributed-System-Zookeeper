package com.springdistributed.util;

import com.springdistributed.model.Note;

import java.util.ArrayList;
import java.util.List;

public final class NotesData {

    private static List<Note> noteList = new ArrayList<>();
    private static int idIncrementer = 0;

    private NotesData() {}

    public static List<Note> getAllNotesFromData() {
        return noteList;
    }

    public static void saveNoteIntoData(Note note) {
        Note storedNote = findById(note.getId());
        if (storedNote != null) {
            //update note
            updateNote(storedNote.getId(), note);
        } else {
            //store note
            int id = ++idIncrementer;
            note.setId(id);
            noteList.add(note);
        }

    }

    public static Note findById(int id) {
        Note note = null;
        for (Note storedNote : noteList) {
            if(storedNote.getId() == id) {
                note = storedNote;
            }
        }
        return note;
    }

    public static void updateNote(int id, Note note) {
        for (int i = 0 ; i < noteList.size(); i++) {
            if(noteList.get(i).getId() == id) {
                noteList.get(i).setName(note.getName());
                noteList.get(i).setDescription(note.getDescription());
            }
        }
    }

}
