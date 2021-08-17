package br.edu.ifpb.pweb2.leprechaun.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "apostas_favoritas")
@Data
public class ApostasFavoritas {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@ManyToOne
	@JoinColumn(name="cliente_id")
	private Usuario cliente;
	
	@ManyToOne
	@JoinColumn(name="aposta_id")
	private Aposta aposta;
}