package br.com.fiap.gerente_carrinho.dominio;

public enum UserRole {
    ADMIN("admin"),
    USER("user"),
    CLIENTE("cliente");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
