package org.example;

import org.example.controladores.*;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.example.servicios.BootStrapServices;
import org.example.servicios.DataBaseServices;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {


    public static void main(String[] args) throws SQLException {

        //Creando la instancia del servidor y configurando.
        Javalin app = Javalin.create(config -> {
                    //configurando los documentos estaticos.
                    config.staticFiles.add(staticFileConfig -> {
                        staticFileConfig.hostedPath = "/";
                        staticFileConfig.directory = "/publico";
                        staticFileConfig.location = Location.CLASSPATH;
                        staticFileConfig.precompress = false;
                        staticFileConfig.aliasCheck = null;
                    });

                    //Habilitando el CORS. Ver: https://javalin.io/plugins/cors#getting-started para más opciones.
                    config.plugins.enableCors(corsContainer -> {
                        corsContainer.add(corsPluginConfig -> {
                            corsPluginConfig.anyHost();
                        });
                    });

                    //habilitando el plugins de las rutas definidas.
                    config.plugins.enableRouteOverview("/rutas");

                    //Configurando el servicio SOAP en nuestro proyecto.
                    //config.jetty.server(() -> new SoapControlador().agregarWebServicesSoap());
        });

        BootStrapServices.startDb();

        //Prueba de Conexión.
        DataBaseServices.getInstancia().testConexion();

        BootStrapServices.crearTablas();
        app.start(7000);

        //creando el manejador
        app.get("/", ctx -> ctx.result("Hola Mundo en Javalin :-D"));

        new CrudTradicionalControlador(app).aplicarRutas();

    }

}
