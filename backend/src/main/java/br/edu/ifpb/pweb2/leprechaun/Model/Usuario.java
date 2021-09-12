package br.edu.ifpb.pweb2.leprechaun.Model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Table(name = "usuario")
@Data

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(nullable = false)
    @NotBlank
    private String nome;
    
    @NotNull(message = "cpf nao pode ser null")
    private String cpf;
    
    @NotNull
    private LocalDate data_nascimento;
    
    @NotNull
    private String login;
    
    @NotNull
    private String senha;
    
    @NotNull
    private TipoUsuario tipoUsuario;

    public Usuario(){};

    public Usuario(String nome, String cpf, LocalDate data_nascimento, String login, String senha, TipoUsuario tipoUsuario){
        this.nome = nome;
        this.cpf = cpf;
        this.data_nascimento = data_nascimento;
        this.login = login;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
    }
}
