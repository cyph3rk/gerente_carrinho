package br.com.fiap.gerente_carrinho.dominio;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@AllArgsConstructor
@Table(name = "conta")
public class Conta {

    @JsonProperty
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private long id;

    @JsonProperty
    private LocalDateTime data;

    @JsonProperty
    private String cliente;

    @JsonProperty
    private String produto;

    @JsonProperty
    private String valor;

    @JsonProperty
    private String quantidade;

    @JsonProperty
    private String total;

    @JsonProperty
    private String situacao;

    public Conta() { };

}
