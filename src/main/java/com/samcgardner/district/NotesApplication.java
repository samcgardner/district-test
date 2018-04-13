package com.samcgardner.district;

import com.samcgardner.district.repository.LocalNoteRepository;
import com.samcgardner.district.repository.NotesRepository;
import com.samcgardner.district.resources.NotesResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

import java.nio.file.Path;
import java.nio.file.Paths;

public class NotesApplication extends Application<NotesConfiguration> {

    @Override
    public void run(NotesConfiguration configuration, Environment environment) throws Exception {

        Path storePath = Paths.get(configuration.getStorepath());
        NotesRepository repository = new LocalNoteRepository(storePath);
        environment.jersey().register(new NotesResource(repository));

    }

    public static void main(String[] args) throws Exception {
        new NotesApplication().run(args);
    }
}
