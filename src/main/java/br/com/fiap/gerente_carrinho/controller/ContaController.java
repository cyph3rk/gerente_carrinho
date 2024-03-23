package br.com.fiap.gerente_carrinho.controller;

import br.com.fiap.gerente_carrinho.dominio.Conta;
import br.com.fiap.gerente_carrinho.facade.ContaFacade;
import br.com.fiap.gerente_carrinho.utils.CodigoResposta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/conta")
public class ContaController {

    public static final String ERRO_IEM = "Item NÃO encontrado.";
    public static final String ERRO_CLIENTE = "Cliente NÃO encontrado.";
    public static final String SUCESSO = "Conta ADICIONADO com sucesso.";
    private static final String DELETE_SUCESSO = "Item DELETADO da conta com sucesso.";

    private final ContaFacade contaFacade;

    @Autowired
    public ContaController(ContaFacade contaFacade) {
        this.contaFacade = contaFacade;
    }

    @PostMapping
    public ResponseEntity<Object> salva(@RequestBody Conta conta) {

        CodigoResposta resp = contaFacade.salvar(conta);
        if ( resp == CodigoResposta.INTEM_NAO_EXISTE) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERRO_IEM);
        }
        if ( resp == CodigoResposta.CLIENTE_NAO_EXISTE) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERRO_CLIENTE);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(SUCESSO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePorId(@PathVariable Long id) {

        contaFacade.remove(id);
        return ResponseEntity.ok(DELETE_SUCESSO);
    }

    @GetMapping("/{data}")
    public ResponseEntity<Object> listaItensCarrinhoUsuario(@PathVariable LocalDateTime data) {

        List<Conta> conta = contaFacade.listaContaUsuario(data);

        if (conta.isEmpty()) {
            return ResponseEntity.badRequest().body(ERRO_IEM);
        }

        return ResponseEntity.status(HttpStatus.OK).body(conta);
    }

}
