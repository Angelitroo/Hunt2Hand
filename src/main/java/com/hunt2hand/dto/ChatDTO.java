package com.hunt2hand.dto;

import lombok.Data;

@Data
public class ChatDTO {
    private Long id;
    private Long id_creador;
    private Long id_receptor;
    private String nombre_receptor;
    private String imagen_receptor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_creador() {
        return id_creador;
    }

    public void setId_creador(Long id_creador) {
        this.id_creador = id_creador;
    }

    public Long getId_receptor() {
        return id_receptor;
    }

    public void setId_receptor(Long id_receptor) {
        this.id_receptor = id_receptor;
    }

    public String getNombre_receptor() {
        return nombre_receptor;
    }

    public void setNombre_receptor(String nombre_receptor) {
        this.nombre_receptor = nombre_receptor;
    }

    public String getImagen_receptor() {
        return imagen_receptor;
    }

    public void setImagen_receptor(String imagen_receptor) {
        this.imagen_receptor = imagen_receptor;
    }
}
