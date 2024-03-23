package br.com.fiap.gerente_carrinho.dominio;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "carrinho")
public class Carrinho {

    @JsonProperty
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private long id;

    @JsonProperty
    private Long id_usuario;

    @JsonProperty
    private Long id_itens;

    @JsonProperty
    private String quantidade;

}
