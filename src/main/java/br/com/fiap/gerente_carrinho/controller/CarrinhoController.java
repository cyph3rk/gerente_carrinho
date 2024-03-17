package br.com.fiap.gerente_carrinho.controller;

import br.com.fiap.gerente_carrinho.dominio.Carrinho;
import br.com.fiap.gerente_carrinho.utils.CodigoResposta;
import br.com.fiap.gerente_carrinho.facade.CarrinhoFacade;
import br.com.fiap.gerente_carrinho.repositorio.ICarrinhoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carrinho")
public class CarrinhoController {

    public static final String ERRO_IEM = "Item NÃO encontrado.";
    public static final String ERRO_CLIENTE = "Cliente NÃO encontrado.";
    public static final String SUCESSO = "Item ADICIONADO ao carrinho com sucesso.";

    private final ICarrinhoRepositorio carrinhoRepositorio;

    private final CarrinhoFacade carrinhoFacade;

    @Autowired
    public CarrinhoController(ICarrinhoRepositorio servicosRepositorio, CarrinhoFacade carrinhoFacade) {
        this.carrinhoRepositorio = servicosRepositorio;
        this.carrinhoFacade = carrinhoFacade;
    }

    @PostMapping
    public ResponseEntity<?> salva(@RequestBody Carrinho carrinho) {

        CodigoResposta resp = carrinhoFacade.salvar(carrinho);
        if ( resp == CodigoResposta.INTEM_NAO_EXISTE) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERRO_IEM);
        }
        if ( resp == CodigoResposta.CLIENTE_NAO_EXISTE) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERRO_CLIENTE);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(SUCESSO);
    }

}
