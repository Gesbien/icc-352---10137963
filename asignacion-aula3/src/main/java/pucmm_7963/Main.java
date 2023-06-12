package pucmm_7963;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import pucmm_7963.controladores.CrudTradicionalControlador;
import pucmm_7963.servicios.BootStrapServices;
import pucmm_7963.servicios.DataBaseServices;

import java.sql.SQLException;


public class Main {
    private static String modoConexion = "";
    public static void main(String[] args) throws SQLException {
        if(args.length >= 1){
            modoConexion = args[0];
            System.out.println("Modo de Operacion: "+modoConexion);
        }

        //Iniciando la base de datos.
        if(modoConexion.isEmpty()) {
            BootStrapServices.getInstancia().init();
        }

        Javalin app = Javalin.create(config -> {
            //configurando los documentos estaticos.
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/publico";
                staticFileConfig.location = Location.CLASSPATH;
                staticFileConfig.precompress = false;
                staticFileConfig.aliasCheck = null;
            });
        });

        app.start(getHerokuAssignedPort());

        //Prueba de Conexión.
        DataBaseServices.getInstancia().testConexion();

        new CrudTradicionalControlador(app).aplicarRutas();

    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 7000; //Retorna el puerto por defecto en caso de no estar en Heroku.
    }

    public static String getModoConexion(){
        return modoConexion;
    }

}