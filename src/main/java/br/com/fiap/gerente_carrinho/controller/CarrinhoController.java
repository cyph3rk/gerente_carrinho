package br.com.fiap.gerente_carrinho.controller;

import br.com.fiap.gerente_carrinho.dominio.Carrinho;
import br.com.fiap.gerente_carrinho.utils.CodigoResposta;
import br.com.fiap.gerente_carrinho.facade.CarrinhoFacade;
import br.com.fiap.gerente_carrinho.repositorio.ICarrinhoRepositorio;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/carrinho")
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

    @DeleteMapping
    public ResponseEntity<Object> deleteItensPorId(@RequestBody Carrinho carrinho) {

        carrinhoFacade.remove(carrinho);
        return ResponseEntity.ok("Item DELETADO do carrinho com sucesso.");
    }

    @GetMapping("/{id_usuario}")
    public ResponseEntity<Object> listaItensCarrinhoUsuario(@PathVariable Long id_usuario) {

        List<Carrinho> carrinhos = carrinhoFacade.listaItensCarrinhoUsuario(id_usuario);

        if (carrinhos.size() == 0) {
            return ResponseEntity.badRequest().body("{\"Erro\": \"Item NÃO cadastrado.\"}");
        }

        return ResponseEntity.status(HttpStatus.OK).body(carrinhos);
    }

}
