package edu.pucmm_7963.Controlador;

import edu.pucmm_7963.util.BaseControlador;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ControladorSesion extends BaseControlador {
    public ControladorSesion(Javalin app){super(app);}

    public void aplicarRutas(){
        app.routes(() ->{
            path("/",() -> {
                /*
                    Validando si existe el usuario
                 */
                before("/",ctxContext -> {
                    if(ctxContext.sessionAttribute("usuario") == null){
                        ctxContext.redirect("formulario.html");
                    }
                });
                // Redirecciona al html para la pagina de inicio
                get(ctxContext -> {
                    ctxContext.redirect("/inicio.html");
                });

                //
                post("/login", ctxContext -> {
                    String usuario = ctxContext.formParam("usuario");
                    String contrasena = ctxContext.formParam("password");
                    //Se autentifica si el usuario y contrasena digitado son los que se pide
                    if(usuario.equals("admin") && contrasena.equals("admin")){
                        ctxContext.sessionAttribute("usuario",usuario);
                        ctxContext.redirect("/");
                    } // Si no se cumple la condicion vuelve al formulario
                    else{
                        ctxContext.redirect("formulario.html");
                    }
                });
            });
        });


    }
}
