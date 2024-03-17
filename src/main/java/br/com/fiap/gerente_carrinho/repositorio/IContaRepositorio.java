package br.com.fiap.gerente_carrinho.repositorio;

import br.com.fiap.gerente_carrinho.dominio.Carrinho;
import br.com.fiap.gerente_carrinho.dominio.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IContaRepositorio extends JpaRepository<Conta, Long> {


}
