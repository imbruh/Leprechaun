package br.edu.ifpb.pweb2.leprechaun.Model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "usuario")
@Data

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String nome;
    private String cpf;
    private LocalDate data_nascimento;
    private String login;
    private String senha;
    private TipoUsuario tipoUsuario = TipoUsuario.CLIENTE;
}
