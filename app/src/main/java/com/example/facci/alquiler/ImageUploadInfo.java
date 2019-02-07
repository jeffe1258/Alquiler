package com.example.facci.alquiler;

public class ImageUploadInfo {
String title;
String description;
String image;
String search;
String usuario;
String telefono;
String email;
String estado;

    public ImageUploadInfo() {
    }

    public ImageUploadInfo(String title, String description, String image, String search, String usuario, String telefono, String email, String estado) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.search = search;
        this.usuario = usuario;
        this.telefono = telefono;
        this.email = email;
        this.estado = estado;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
