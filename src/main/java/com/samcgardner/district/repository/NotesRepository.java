package com.samcgardner.district.repository;

import com.samcgardner.district.model.Note;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface NotesRepository {

    List<Note> getNotes(int limit) throws IOException;

    Optional<Note> getNote(int id) throws IOException;

    void putNote(Note note, int id) throws IOException;

    void deleteNote(int id) throws IOException;
}
