package org.example.services;

import org.example.encapsulacion.RolesApp;
import org.example.encapsulacion.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FakeServices {

    private static FakeServices instancia;
    private List<Usuario> listaUsuarios = new ArrayList<>();

    /**
     * Constructor privado.
     */
    private FakeServices(){
        //anadiendo los usuarios.
        listaUsuarios.add(new Usuario("admin", "admin", "1234", Set.of(RolesApp.LOGUEADO)));
    }

    public static FakeServices getInstancia(){
        if(instancia==null){
            instancia = new FakeServices();
        }
        return instancia;
    }

    /**
     * Permite autenticar los usuarios. Lo ideal es sacar en
     * @param usuario
     * @param password
     * @return
     */
    public Usuario autheticarUsuario(String usuario, String password){
        //simulando la busqueda en la base de datos.
        Usuario user = new Usuario(usuario, "Usuario "+usuario, password, Set.of(RolesApp.LOGUEADO));
        listaUsuarios.add(user);
        return user;
    }
    public List<Usuario> getListaUsuarios(){
        return listaUsuarios;
    }

}