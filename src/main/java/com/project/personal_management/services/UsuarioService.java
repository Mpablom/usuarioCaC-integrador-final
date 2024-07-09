package com.project.personal_management.services;

import java.util.List;
import java.util.Map;

import com.project.personal_management.entities.Usuario;
import com.project.personal_management.repositories.UsuarioRepository;

public class UsuarioService {

    private UsuarioRepository usuarioRepository = new UsuarioRepository();

    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.obtenerTodos();
    }

    public Usuario obtenerUsuarioPorId(Long id) {
      return usuarioRepository.obtenerPorId(id);
    }
    
    public List<Usuario> obtenerUsuariosFiltrados(String nombre, String apellido, String username) {
        return usuarioRepository.findUsuariosFiltrados(nombre, apellido, username);
    }

    
    public void crearUsuario(Usuario usuario) {
        usuarioRepository.crear(usuario);
    }

    public void actualizarUsuario(Long id, Usuario usuario) {
        usuario.setId(id);
        usuarioRepository.actualizar(id, usuario);
    }

    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.obtenerPorId(id);
        if (usuario != null) {
            usuario.setEstaEliminado(true);
            usuarioRepository.actualizar(id, usuario);
        }
    }
    public void restaurarUsuario(Long id) {
        Usuario usuario = usuarioRepository.obtenerPorId(id);
        if (usuario != null) {
            System.out.println("Usuario antes de restaurar: " + usuario);
            usuario.setEstaEliminado(false);
            usuarioRepository.actualizar(id, usuario);
            System.out.println("Usuario después de restaurar: " + usuario);
        }else {
            System.out.println("Usuario no encontrado con ID: " + id);
		}
    }
}
