package org.example.controladores;

import org.example.encapsulaciones.Estudiante;
import org.example.servicios.EstudianteServices;
import org.example.util.BaseControlador;
import io.javalin.Javalin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;
import static java.lang.Integer.parseInt;

/**
 * Representa las rutas para manejar las operaciones de petición - respuesta.
 */
public class CrudTradicionalControlador extends BaseControlador {

    EstudianteServices estudianteServices = EstudianteServices.getInstancia();

    public CrudTradicionalControlador(Javalin app) {
        super(app);
    }

    /**
     * Las clases que implementan el sistema de plantilla están agregadas en PlantillasControlador.
     * http://localhost:7000/crud-simple/listar
     */
    @Override
    public void aplicarRutas() {
        app.routes(() -> {
            path("/crud-simple/", () -> {


                get("/", ctx -> {
                    ctx.redirect("/crud-simple/listar");
                });

                get("/listar", ctx -> {
                    //tomando el parametro utl y validando el tipo.
                    List<Estudiante> lista = estudianteServices.findAll();
                    //
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Listado de Estudiante");
                    modelo.put("lista", lista);
                    //enviando al sistema de plantilla.
                    ctx.render("/templates/crud-tradicional/listar.html", modelo);
                });

                get("/crear", ctx -> {
                    //
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Formulario Creación Estudiante");
                    modelo.put("accion", "/crud-simple/crear");
                    //enviando al sistema de plantilla.
                    ctx.render("/templates/crud-tradicional/crearEditarVisualizar.html", modelo);
                });

                /**
                 * manejador para la creación del estudiante, una vez creado
                 * pasa nuevamente al listado.
                 */
                post("/crear", ctx -> {
                    //obteniendo la información enviada.
                    Estudiante insertar = new Estudiante();
                    insertar.setMatricula(parseInt(ctx.formParam("matricula")));
                    insertar.setNombre(ctx.formParam("nombre"));
                    insertar.setApellido(ctx.formParam("apellido"));
                    insertar.setTelefono(ctx.formParam("telefono"));
                    insertar.setCarrera(ctx.formParam("carrera"));
                    //realizar algún tipo de validación...
                    estudianteServices.crear(insertar); //puedo validar, existe un error enviar a otro vista.
                    ctx.redirect("/crud-simple/");
                });

                get("/visualizar/{matricula}", ctx -> {
                    Estudiante estudiante = estudianteServices.find(ctx.pathParamAsClass("matricula", Integer.class).get());
                    //
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Formulario Visaulizar Estudiante "+estudiante.getMatricula());
                    modelo.put("estudiante", estudiante);
                    modelo.put("visualizar", true); //para controlar en el formulario si es visualizar
                    modelo.put("accion", "/crud-simple/");

                    //enviando al sistema de ,plantilla.
                    ctx.render("/templates/crud-tradicional/crearEditarVisualizar.html", modelo);
                });

                get("/editar/{matricula}", ctx -> {
                    Estudiante estudiante = estudianteServices.find(ctx.pathParamAsClass("matricula", Integer.class).get());
                    //
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Formulario Editar Estudiante "+estudiante.getMatricula());
                    modelo.put("estudiante", estudiante);
                    modelo.put("accion", "/crud-simple/editar");

                    //enviando al sistema de ,plantilla.
                    ctx.render("/templates/crud-tradicional/crearEditarVisualizar.html", modelo);
                });

                /**
                 * Proceso para editar un estudiante.
                 */
                post("/editar", ctx -> {

                    Estudiante actualizar = new Estudiante();
                    actualizar.setMatricula(parseInt(ctx.formParam("matricula")));
                    actualizar.setNombre(ctx.formParam("nombre"));
                    actualizar.setApellido(ctx.formParam("apellido"));
                    actualizar.setTelefono(ctx.formParam("telefono"));
                    actualizar.setCarrera(ctx.formParam("carrera"));
                    //realizar algún tipo de validación...
                    estudianteServices.editar(actualizar);
                    ctx.redirect("/crud-simple/");
                });

                /**
                 * Puede ser implementando por el metodo post, por simplicidad utilizo el get. ;-D
                 */
                get("/eliminar/{matricula}", ctx -> {
                    estudianteServices.eliminar(ctx.pathParamAsClass("matricula", Integer.class).get());
                    ctx.redirect("/crud-simple/");
                });

            });
        });
    }
}
