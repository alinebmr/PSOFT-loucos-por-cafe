package com.ufcg.psoft.commerce.exception;

public class FornecedorNaoForneceCafeException extends CommerceException {
    public FornecedorNaoForneceCafeException() {
        super("O fornecedor nao fornece esse cafe!");
    }
}