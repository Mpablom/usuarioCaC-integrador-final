package com.project.personal_management.validations;

import com.project.personal_management.entities.Usuario;
import com.project.personal_management.exceptions.ValidationException;

public class Validations {
    public static void validateUsuario(Usuario usuario) throws ValidationException {
        if (usuario.getUsername() == null || usuario.getUsername().isEmpty()) {
            throw new ValidationException("Nombre de usuario es requerido.");
        }
        if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
            throw new ValidationException("Contraseña es requerida.");
        }
        if (usuario.getNombre() == null || usuario.getNombre().isEmpty()) {
            throw new ValidationException("Nombre es requerido.");
        }
        if (usuario.getApellido() == null || usuario.getApellido().isEmpty()) {
            throw new ValidationException("Apellido es requerido.");
        }
        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            throw new ValidationException("Rol es requerido.");
        }
    }
}

