package com.project.personal_management.databaseConexion;


import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;
import com.project.personal_management.controllers.UsuarioController;

public class SimpleHttpServer {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/usuarios", new UsuarioController());
        server.setExecutor(null);
        server.start();
        System.out.println("Servidor iniciado en el puerto 8080");
    }
}