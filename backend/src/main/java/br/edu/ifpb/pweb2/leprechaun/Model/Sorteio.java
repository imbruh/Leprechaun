package br.edu.ifpb.pweb2.leprechaun.Model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Table(name = "sorteio")
@Data
public class Sorteio {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private String[] dezenasSorteadas;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
	private LocalDateTime dataHora;
	private double valorPremio;
	private TipoSorteio tipo;
	
	@ManyToOne
	@JoinColumn(name="controlador_id")
	private Usuario controlador;
	
	@ManyToOne
	@JoinColumn(name="aposta_id")
	private Aposta aposta;
}
