package br.edu.ifpb.pweb2.leprechaun.Dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SorteioDTO {
	
	private Long idControlador;
	private LocalDateTime dataHora;
	private double valorPremio;
	
}
