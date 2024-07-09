package com.project.personal_management.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.project.personal_management.entities.Usuario;
import com.project.personal_management.exceptions.ValidationException;
import com.project.personal_management.repositories.UsuarioRepository;
import com.project.personal_management.services.UsuarioService;
import com.project.personal_management.validations.Validations;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class UsuarioController implements HttpHandler {
    private UsuarioRepository usuarioRepository = new UsuarioRepository();
    private UsuarioService usuarioService = new UsuarioService();
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
        	String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            switch (method) {
                case "GET":
                	 if (path.equals("/usuarios")) {
                         handleGetUsuarios(exchange);
                     } else if (path.matches("/usuarios/\\d+")) {
                         handleGetUsuarioById(exchange);
                     } else {
                    	 JsonObject responseJson = new JsonObject();
                         responseJson.addProperty("message", "Endpoint no encontrado");
                         sendResponse(exchange, 404, responseJson.toString());
                     }
                    break;
                case "POST":
                    handleCrearUsuario(exchange);
                    break;
                case "PUT":
                    handleActualizarUsuario(exchange);
                    break;
                case "DELETE":
                    handleEliminarUsuario(exchange);
                    break;
                case "PATCH":
                	handleRestaurarUsuario(exchange);
                	break;
                default:
                	JsonObject responseJson = new JsonObject();
                    responseJson.addProperty("message", "Method Not Allowed");
                    sendResponse(exchange, 405, responseJson.toString());
                    break;
            }
		} catch (ValidationException e) {
			JsonObject responseJson = new JsonObject();
            responseJson.addProperty("error", e.getMessage());
            sendResponse(exchange, 400, responseJson.toString());
		} catch (Exception e) {
			JsonObject responseJson = new JsonObject();
            responseJson.addProperty("error", "Internal Server Error");
            sendResponse(exchange, 500, responseJson.toString());
            e.printStackTrace();
		}
    	
    }

    private void handleGetUsuarios(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> queryParams = parseQueryParams(query);

        List<Usuario> usuarios;
        if (queryParams.isEmpty()) {
            usuarios = usuarioService.obtenerUsuarios();
        } else {
            usuarios = usuarioService.obtenerUsuariosFiltrados(
                queryParams.get("nombre"),
                queryParams.get("apellido"),
                queryParams.get("username")
            );
        }

        String response = new Gson().toJson(usuarios);
        sendResponse(exchange, 200, response);
    }
    
    private void handleGetUsuarioById(HttpExchange exchange) throws IOException {
        Long id = Long.parseLong(exchange.getRequestURI().getPath().split("/")[2]);
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        if (usuario != null) {
            String response = new Gson().toJson(usuario);    
            sendResponse(exchange, 200, response);
        } else {
        	JsonObject responseJson = new JsonObject();
            responseJson.addProperty("message", "Usuario no encontrado");
            sendResponse(exchange, 404, responseJson.toString());
        }
    }
    
    private Map<String, String> parseQueryParams(String query) {
        if (query == null || query.isEmpty()) {
            return Collections.emptyMap();
        }
        return Arrays.stream(query.split("&"))
            .map(param -> param.split("="))
            .collect(Collectors.toMap(
                arr -> arr[0], arr -> arr.length > 1 ? arr[1] : ""
            ));
    }

    private void handleCrearUsuario(HttpExchange exchange) throws IOException, ValidationException {
        String requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                .lines().collect(Collectors.joining("\n"));
        Usuario nuevoUsuario = new Gson().fromJson(requestBody, Usuario.class);

        Validations.validateUsuario(nuevoUsuario);
        usuarioService.crearUsuario(nuevoUsuario);

        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("message", "Usuario creado correctamente");
        sendResponse(exchange, 201, responseJson.toString());
    }

    private void handleActualizarUsuario(HttpExchange exchange) throws IOException, ValidationException {
        try {
            String requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                    .lines().collect(Collectors.joining("\n"));

            Usuario usuarioActualizado = new Gson().fromJson(requestBody, Usuario.class);

            String[] paths = exchange.getRequestURI().getPath().split("/");
            if (paths.length < 3) {
                throw new ValidationException("Formato de URI incorrecto para actualizar usuario");
            }

            Long id = Long.parseLong(paths[2]); 
            usuarioActualizado.setId(id);

            Validations.validateUsuario(usuarioActualizado);
            usuarioService.actualizarUsuario(id, usuarioActualizado);

            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("message", "Usuario actualizado correctamente");
            sendResponse(exchange, 200, responseJson.toString());
        } catch (ValidationException e) {
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("error", e.getMessage());
            sendResponse(exchange, 400, responseJson.toString());
        } catch (Exception e) {
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("error", "Internal Server Error");
            sendResponse(exchange, 500, responseJson.toString());
            e.printStackTrace();
        }
    }

    private void handleEliminarUsuario(HttpExchange exchange) throws IOException {
        Long id = Long.parseLong(exchange.getRequestURI().getPath().split("/")[2]);
        usuarioService.eliminarUsuario(id);
        
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("message", "Usuario eliminado correctamente");
        sendResponse(exchange, 200, responseJson.toString());
    }
    
    private void handleRestaurarUsuario(HttpExchange exchange) throws IOException {
            Long id = Long.parseLong(exchange.getRequestURI().getPath().split("/")[2]);
            usuarioService.restaurarUsuario(id);

            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("message", "Usuario restaurado correctamente");
            sendResponse(exchange, 200, responseJson.toString());
       
    }


    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
    	exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

}
