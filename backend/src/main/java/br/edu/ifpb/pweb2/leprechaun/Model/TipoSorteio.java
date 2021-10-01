package br.edu.ifpb.pweb2.leprechaun.Model;

public enum TipoSorteio {
	   ALEATORIO("aleatorio"),
	   NAO_ALEATORIO("nao aleatorio");
	    private String tipo;

	    TipoSorteio(String tipo) {
	        this.tipo = tipo;
	    }

	    public String getTipo() {
	        return this.tipo;
	    }
}
