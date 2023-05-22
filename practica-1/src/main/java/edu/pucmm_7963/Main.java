package edu.pucmm_7963;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private static final HttpClient cliente = HttpClient.newHttpClient();
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese una URL válida: ");

        try {
            String url = scanner.nextLine();
            procesoURL(url);
        } catch (NoSuchElementException e) {
            System.out.println("No se encontró ninguna línea de entrada.");
        }
    }

    public static void procesoURL(String url){
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

        try{
            HttpResponse<String> response = cliente.send(request, HttpResponse.BodyHandlers.ofString());
            String cuerpoResponse = response.body();

            //a) En este apartado es donde indicamos el tipo de recurso seleccionado
            String tipoRecurso = response.headers().firstValue("Content-Type").orElse("desconocido");
            System.out.println("Tipo de recurso: " + tipoRecurso);

            //b) En este apartado vamos a chequear si el documento es un html
            if(tipoRecurso.contains("text/html")){
                //1. A continuacion, vamos a contar la cantidad de lineas del recurso retornado
                int contLineas = cuerpoResponse.split(System.lineSeparator()).length;
                System.out.println("Cantidad de lineas: " + contLineas);

                //2. Vamos a indicar la cantidad de parrafos que contiene el documento HTML
                int contParrafos = contarTag(cuerpoResponse, "<p");
                System.out.println("Cantidad de parrafos: " + contParrafos);

                //3. Vamos a indicar la cantidad de imagenes dentro de los parrafos que contiene el documento HTML
                int contImagen = contarTag(cuerpoResponse, "<img");
                System.out.println("Cantidad de imagenes dentro de parrafos: " + contImagen);

                //4. indicar la cantidad de formularios (form) que contiene el HTML por
                //categorizando por el método implementado POST o GET.
                int contForm = contarTag(cuerpoResponse,"<form");
                System.out.println("Cantidad de formularios: " + contForm);

                //5. Para cada formulario mostrar los campos del tipo input y su
                //respectivo tipo que contiene en el documento HTML.
                ShowInputForm(cuerpoResponse);

                // 6. Para cada formulario parseado,identificar que el método de
                //envío del formulario sea POST y enviar una petición al servidor
                //con el parámetro llamado asignatura y valor practica1 y un
                //header llamado matricula-id con el valor correspondiente a
                //matrícula o id asignado.

                parseForm(cuerpoResponse);

            }
        } catch (Exception e){
            System.out.println("Error al procesar la URL: " + e.getMessage());
        }


    }

    private static int contarTag(String html, String tag) {
        return html.split(tag).length - 1;
    }

    private static void ShowInputForm(String html) {
        Document doc = Jsoup.parse(html);
        Elements forms = doc.select("form");

        for(Element form : forms){
            System.out.println("Formulario: ");

            //En esta parte vamos a obtene el metodo del formulario (POST o GET)
            String metodo = form.attr("method");
            System.out.println("Metodo: " + metodo);

            //Obtenemos los campos de tipos input y sus tipos
            Elements inputFields = form.select("input");
            for(Element input : inputFields){
                String nombre = input.attr("name");
                String tipo = input.attr("type");
                System.out.println("Input Nombre: " + nombre +" Tipo: " + tipo);
            }
        }
    }

    private static void parseForm(String html) {
        Document doc = Jsoup.parse(html);
        Elements forms = doc.select("form");

        for(Element form : forms){
            //Verificamos que el metodo del formulario sea POST
            String metodo = form.attr("method");
            if(metodo.equalsIgnoreCase("post")){
                String action = form.attr("action");

                //Enviar la peticion al servidor con los parametros asignatura = practica1 y mi matricula
                try{
                    HttpRequest request = HttpRequest.newBuilder().uri(new URI(action))
                            .POST(HttpRequest.BodyPublishers.ofString("asignatura=practica1&matricula=20190416"))
                            .header("Content-Type","application/x-www-form-urlencoded").build();

                    HttpResponse<String> response = cliente.send(request, HttpResponse.BodyHandlers.ofString());
                    System.out.println("Respuesta del servidor: " + response.body());
                } catch (IOException | InterruptedException | URISyntaxException e){
                    System.out.println("Error al enviar la peticion del formulario: " + e.getMessage());
                }

            }
        }
    }
}
