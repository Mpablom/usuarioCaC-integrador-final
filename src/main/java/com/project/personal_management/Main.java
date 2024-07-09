package com.project.personal_management;

import com.project.personal_management.databaseConexion.SimpleHttpServer;

public class Main {

	public static void main(String[] args) {
		 try {
	            SimpleHttpServer.main(args); 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}

}
