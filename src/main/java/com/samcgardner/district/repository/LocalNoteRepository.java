package com.samcgardner.district.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samcgardner.district.model.Note;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

// A note repository implementation that persists notes to the local disk
public class LocalNoteRepository implements NotesRepository {

    private final Path storePath;
    private final ObjectMapper mapper = new ObjectMapper();

    public LocalNoteRepository(Path path) {
        this.storePath = path;
    }

    @Override
    public List<Note> getNotes(int limit) throws IOException {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(storePath)) {
            return StreamSupport.stream(
                    Spliterators.spliteratorUnknownSize(dirStream.iterator(), Spliterator.ORDERED),
                    false)
                    .map(path -> {
                        try {
                            return new FileInputStream(path.toFile());
                        } catch (FileNotFoundException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .map(stream -> {
                        try {
                            return mapper.readValue(stream, Note.class);
                        } catch (IOException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .limit(limit)
                    .collect(Collectors.toList());
        }

    }

    @Override
    public Optional<Note> getNote(int id) throws IOException {

        String location = getLocationForId(id);

        if (Files.notExists(Paths.get(location))) {
            return Optional.empty();
        }

        try (FileInputStream stream = new FileInputStream(location)) {
            return Optional.of(mapper.readValue(stream, Note.class));
        }
    }

    @Override
    public void putNote(Note note, int id) throws IOException {

        String location = getLocationForId(id);

        Files.write(Paths.get(location), mapper.writeValueAsBytes(note));

    }

    @Override
    public void deleteNote(int id) throws IOException {

        String location = getLocationForId(id);

        Files.deleteIfExists(Paths.get(location));

    }


    private String getLocationForId(int id) {
        return storePath.toString() + "/" + id + ".json";
    }

}
