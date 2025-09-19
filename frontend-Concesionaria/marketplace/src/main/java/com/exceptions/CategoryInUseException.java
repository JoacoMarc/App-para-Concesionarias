package com.exceptions;

public class CategoryInUseException extends Exception {
    public CategoryInUseException() {
        super("No se puede eliminar la categoría porque tiene productos asociados");
    }
}