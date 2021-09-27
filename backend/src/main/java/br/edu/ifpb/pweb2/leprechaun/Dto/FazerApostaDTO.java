package br.edu.ifpb.pweb2.leprechaun.Dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
public class FazerApostaDTO {

	private String n1;
	private String n2;
	private String n3;
	private String n4;
	private String n5;
	private String n6;
	private String n7;
	private String n8;
	private String n9;
	private String n10;

	private List<String> apostasFavoritas = new ArrayList<>();
	
	private String apostaFav = ""; 
	
	private Boolean msgSucesso = false;
}
