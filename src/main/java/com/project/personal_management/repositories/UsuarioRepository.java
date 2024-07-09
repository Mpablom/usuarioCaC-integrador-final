package com.project.personal_management.repositories;


import com.project.personal_management.databaseConexion.ConexionDB;
import com.project.personal_management.entities.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {
    private Connection conexion;

    public UsuarioRepository() {
        try {
            this.conexion = ConexionDB.conectar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Usuario> obtenerTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE estaEliminado = 0";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getLong("id"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setRol(rs.getString("rol"));
                usuario.setEstaEliminado(rs.getBoolean("estaEliminado"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public Usuario obtenerPorId(Long id) {
        String sql = "SELECT * FROM usuarios WHERE id = ? ";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getLong("id"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setRol(rs.getString("rol"));
                usuario.setEstaEliminado(rs.getBoolean("estaEliminado"));
                return usuario;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Usuario> findUsuariosFiltrados(String nombre, String apellido, String username) {
        List<Usuario> usuarios = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM usuarios WHERE 1=1");
        if (nombre != null && !nombre.isEmpty()) sql.append(" AND nombre LIKE ?");
        if (apellido != null && !apellido.isEmpty()) sql.append(" AND apellido LIKE ?");
        if (username != null && !username.isEmpty()) sql.append(" AND username LIKE ?");

        try (PreparedStatement stmt = conexion.prepareStatement(sql.toString())) {
            int index = 1;
            if (nombre != null && !nombre.isEmpty()) stmt.setString(index++, "%" + nombre + "%");
            if (apellido != null && !apellido.isEmpty()) stmt.setString(index++, "%" + apellido + "%");
            if (username != null && !username.isEmpty()) stmt.setString(index++, "%" + username + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                usuarios.add(new Usuario(
                    rs.getLong("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("rol"),
                    rs.getBoolean("estaEliminado")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }


    public void crear(Usuario usuario) {
        String sql = "INSERT INTO usuarios (username, password, nombre, apellido, rol, estaEliminado) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, usuario.getUsername());
            stmt.setString(2, usuario.getPassword());
            stmt.setString(3, usuario.getNombre());
            stmt.setString(4, usuario.getApellido());
            stmt.setString(5, usuario.getRol());
            stmt.setBoolean(6, usuario.isEstaEliminado());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizar(Long id, Usuario usuario) {
        String sql = "UPDATE usuarios SET username=?, password=?, nombre=?, apellido=?, rol=?, estaEliminado=? WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
        	stmt.setString(1, usuario.getUsername());
            stmt.setString(2, usuario.getPassword());
            stmt.setString(3, usuario.getNombre());
            stmt.setString(4, usuario.getApellido());
            stmt.setString(5, usuario.getRol());
            stmt.setBoolean(6, usuario.isEstaEliminado());
            stmt.setLong(7, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void eliminar(Long id) {
        String sql = "UPDATE usuarios SET estaEliminado = TRUE WHERE id=?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
            conexion.commit(); 
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conexion.rollback(); 
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
