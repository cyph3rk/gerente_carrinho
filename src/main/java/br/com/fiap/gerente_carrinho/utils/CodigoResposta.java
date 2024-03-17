package br.com.fiap.gerente_carrinho.utils;

public class CodigoResposta {

    public static final CodigoResposta OK = new CodigoResposta(4001, "Ok");
    public static final CodigoResposta INTEM_NAO_EXISTE = new CodigoResposta(5002, "Item não cadastrado");
    public static final CodigoResposta CLIENTE_NAO_EXISTE = new CodigoResposta(5003, "cliente não cadastrado");
    public static final CodigoResposta ERRO_GENERICO = new CodigoResposta(5004, "Erro interno Sistema");


    private final int codigo;
    private final String descricao;

    CodigoResposta(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

}
