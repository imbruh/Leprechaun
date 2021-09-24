package br.edu.ifpb.pweb2.leprechaun.Model;

public enum TipoUsuario {
    CLIENTE(0),
    CONTROLADOR(1);

    private int tipo;

    TipoUsuario(int tipo) {
        this.tipo = tipo;
    }

    public int getTipo() {
        return this.tipo;
    }
}
