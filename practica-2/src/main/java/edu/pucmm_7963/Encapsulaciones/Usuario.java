package edu.pucmm_7963.Encapsulaciones;

public class Usuario {
    private long id;
    private String username;
    private String nombre;
    private String password;

    private boolean admintrator;

    private boolean autor;

    public Usuario(long id,String username, String nombre, String password, boolean admintrator, boolean autor) {
        this.id = id;
        this.username = username;
        this.nombre = nombre;
        this.password = password;
        this.admintrator = admintrator;
        this.autor = autor;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmintrator() {
        return admintrator;
    }

    public void setAdmintrator(boolean admintrator) {
        this.admintrator = admintrator;
    }

    public boolean isAutor() {
        return autor;
    }

    public void setAutor(boolean autor) {
        this.autor = autor;
    }
}
