package com.lab.notesapp.exception;

public class ObjectNotFoundException extends Exception {

    public ObjectNotFoundException() {
        super();
    }

    public ObjectNotFoundException(String message) {
        super(message);
    }

    public ObjectNotFoundException(long id) {
        super(String.format("Object with id: %d not found.", id));
    }

    public ObjectNotFoundException(int id) {
        super(String.format("Object with id: %d not found.", id));
    }
}