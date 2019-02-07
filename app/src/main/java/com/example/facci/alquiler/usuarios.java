package com.example.facci.alquiler;

public class usuarios {
    private String email;
    private String nombre;
    private String telefono;
    private String pass;
    private String imagen;

    public usuarios() {
    }

    public usuarios(String email, String nombre, String telefono, String pass, String imagen) {
        this.email = email;
        this.nombre = nombre;
        this.telefono = telefono;
        this.pass = pass;
        this.imagen = imagen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}

