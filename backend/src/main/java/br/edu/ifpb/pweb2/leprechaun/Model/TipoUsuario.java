package br.edu.ifpb.pweb2.leprechaun.Model;

public enum TipoUsuario {
    CLIENTE("cliente"),
    CONTROLADOR("controlador");

    private String tipo;

    TipoUsuario(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return this.tipo;
    }
}
