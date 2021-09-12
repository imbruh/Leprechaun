package br.edu.ifpb.pweb2.leprechaun.Dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class SorteioSentDTO {

	private String[] dezenasSorteadas;
	private LocalDateTime dataHora;
	private double valorPremio;
	
	private List<String> ganhador;
	
}
