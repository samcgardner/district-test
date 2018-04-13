package com.samcgardner.district.resources;

import com.samcgardner.district.model.Link;
import com.samcgardner.district.model.Note;
import com.samcgardner.district.model.NotesResponse;
import com.samcgardner.district.repository.NotesRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotesResource {

    private final NotesRepository repository;
    private static final int LIMIT = 5;

    public NotesResource(NotesRepository repository) {
        this.repository = repository;
    }

    @HEAD
    @Path("/notes.json")
    public Response rootHead() {
        return Response.ok().build();
    }

    @GET
    @Path("/notes.json")
    public NotesResponse rootGet() {
        List<Note> notes;
        try {
            notes = repository.getNotes(LIMIT);
        } catch (IOException e) {
            throw new InternalServerErrorException();
        }

        notes.forEach(note -> note.setLink(buildLinkForNote(note)));

        Map<String, Link> links = new HashMap<>();
        Link self = new Link();
        self.setHref("http://localhost/notes");
        links.put("self", self);
        Link note = new Link();
        note.setHref("http://localhost/notes/{id}");
        note.setTemplated(true);
        links.put("notes", note);


        NotesResponse response = new NotesResponse();
        response.setLimit(LIMIT);
        response.setNotes(notes);
        response.setLinks(links);

        return response;
    }

    @GET
    @Path("/notes/{id}.json")
    public Note getNote(@PathParam("id") int id) {
        try {
            return repository.getNote(id).orElseThrow(NotFoundException::new);
        } catch (IOException e) {
            throw new InternalServerErrorException();
        }
    }

    @PUT
    @Path("/notes/{id}.json")
    public Response putNote(Note input, @PathParam("id") int id) throws URISyntaxException {
        if (input.getMessage().isEmpty()) {
            return Response.status(400)
                    .entity("{\"code\":400,\"message\":\"Validation Failed\",\"errors\":" +
                            "{\"children\":{\"message\":{\"errors\":[\"This value should not be blank.\"]}}}}")
                    .build();
        }
        try {
            Optional<Note> currentValue = repository.getNote(id);
            repository.putNote(input, id);
            if (currentValue.isPresent()) {
                return Response.noContent()
                        .header("location", "http://locahost/notes/" + id)
                        .build();
            } else {
                return Response.created(new URI("http://locahost/notes/" + id))
                        .build();
            }
        } catch (IOException e) {
            throw new InternalServerErrorException();
        }
    }

    @POST
    @Path("/notes/{id}.json")
    public Response postNote(Note input, @PathParam("id") int id) throws URISyntaxException {
        if (input.getMessage().isEmpty()) {
            return Response.status(400)
                    .entity("{\"code\":400,\"message\":\"Validation Failed\",\"errors\":" +
                            "{\"children\":{\"message\":{\"errors\":[\"This value should not be blank.\"]}}}}")
                    .build();
        }
        try {
            repository.putNote(input, id);
            return Response.created(new URI("http://locahost/notes/" + id))
                    .build();
        } catch (IOException e) {
            throw new InternalServerErrorException();
        }
    }

    @DELETE
    @Path("/notes/{id}.json")
    public Response deleteNote(@PathParam("id") int id) {
        try {
            repository.deleteNote(id);
        } catch (IOException e) {
            throw new InternalServerErrorException();
        }
        return Response.noContent()
                .header("location", "http://locahost/notes")
                .build();
    }



    private Map<String, Link> buildLinkForNote(Note note) {
        Map<String, Link> result = new HashMap<>();

        Link link = new Link();
        link.setHref("http://localhost/notes/" + note.getId());
        link.setTemplated(false);

        result.put("self", link);
        return result;
    }

}
