package com.project.personal_management.entities;
import java.util.Objects;

public class Usuario {
	private Long id;
    private String username;
    private String password;
    private String nombre;
    private String apellido;
    private String rol;
    private boolean estaEliminado;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	public boolean isEstaEliminado() {
		return estaEliminado;
	}
	public void setEstaEliminado(boolean estaEliminado) {
		this.estaEliminado = estaEliminado;
	}
	
	public Usuario(Long id, String username, String password, String nombre, String apellido, String rol, boolean estaEliminad) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.nombre = nombre;
		this.apellido = apellido;
		this.rol = rol;
		this.estaEliminado = estaEliminado;
	}
	
	public Usuario() {
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(apellido, id, nombre, password, rol, username, estaEliminado);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(apellido, other.apellido) && Objects.equals(id, other.id)
				&& Objects.equals(nombre, other.nombre) && Objects.equals(password, other.password)
				&& Objects.equals(rol, other.rol) && Objects.equals(username, other.username) 
				&& Objects.equals(estaEliminado, other.estaEliminado);
	}
	@Override
	public String toString() {
		return "Usuario [id= " + id + ", username= " + username + ", password= " + password + ", nombre=" + nombre
				+ ", apellido= " + apellido + ", rol= " + rol + "estaEliminado= "+ estaEliminado +"]";
	}    
}
