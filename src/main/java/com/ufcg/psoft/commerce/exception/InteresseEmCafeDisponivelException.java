package com.ufcg.psoft.commerce.exception;

public class InteresseEmCafeDisponivelException extends RuntimeException {
    public InteresseEmCafeDisponivelException() {
        super("Não é possível demonstrar interesse em um café disponível");
    }
}