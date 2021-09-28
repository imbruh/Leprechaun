package br.edu.ifpb.pweb2.leprechaun.Dto;

import java.util.List;

import lombok.Data;

@Data
public class ConferirApostaDTO {
	private Long idSorteio;
	private List<String> dezenasSorteadas;
	private List<String> numerosApostados;
	private int acertos;	
}
