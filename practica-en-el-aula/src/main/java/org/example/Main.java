package org.example;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.example.encapsulacion.RolesApp;
import org.example.encapsulacion.Usuario;
import org.example.services.FakeServices;

public class Main {
    public static void main(String[] args) {

        //Creando la instancia del servidor y configurando.
        Javalin app = Javalin.create(config ->{
            //configurando los documentos estaticos.
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/publico";
                staticFileConfig.location = Location.CLASSPATH;
                staticFileConfig.precompress=false;
                staticFileConfig.aliasCheck=null;
            });
        });

        FakeServices fakeServices = FakeServices.getInstancia();

        JavalinRenderer.register(new JavalinThymeleaf(), ".html");

        app.cfg.accessManager((handler, ctx, permittedRoles) -> {
            //para obtener el usuario estaré utilizando el contexto de sesion.
            final Usuario usuario = ctx.sessionAttribute("usuario");
            System.out.println("Los roles permitidos: "+permittedRoles.toString());
            if(permittedRoles.isEmpty()){
                handler.handle(ctx);
                return;
            }
            //validando si existe el usuario.
            if(usuario == null){
                System.out.println("Atributo de sesion no existe..");
                ctx.redirect("/login.html");
                return;
            }
            //buscando el permiso del usuario.
            Usuario usuarioTmp = fakeServices.getListaUsuarios().stream()
                    .filter(u -> u.getUsuario().equalsIgnoreCase(usuario.getUsuario()))
                    .findAny()
                    .orElse(null);

            if(usuarioTmp==null){
                System.out.println("Existe el usuario pero sin roles para acceder.");
                //System.out.println(usuarioTmp);
                ctx.status(401).result("No tiene roles para acceder...");
                return;
            }

            //validando que el usuario registrando tiene el rol permitido.
            for(RolesApp role : usuarioTmp.getListaRoles() ) {
                if (permittedRoles.contains(role)) {
                    System.out.println(String.format("El Usuario: %s - con el Rol: %s tiene permiso", usuarioTmp.getUsuario(), role.name()));
                    handler.handle(ctx);
                    break;
                }
            }

        });

        app.post("/login", ctx -> {
            //Obteniendo la informacion de la petion. Pendiente validar los parametros.
            String nombreUsuario = ctx.formParam("usuario");
            String password = ctx.formParam("password");
            if(nombreUsuario != null && password != null) {
                //Autenticando el usuario para nuestro ejemplo siempre da una respuesta correcta.
                Usuario usuario = fakeServices.autheticarUsuario(nombreUsuario, password);
                //agregando el usuario en la session... se puede validar si existe para solicitar el cambio.-
                ctx.sessionAttribute("usuario", usuario);
                //redireccionando la vista con autorizacion.
                ctx.redirect("/");
            }
            else{
                ctx.status(400);
            }
        });
        app.get("/", ctx -> {
            ctx.render("/publico/index.html");
        }, RolesApp.LOGUEADO);

        app.get("/test", ctx -> {
            ctx.result("Pagina secundaria");
        }, RolesApp.LOGUEADO);

        app.start(7000);
    }
}