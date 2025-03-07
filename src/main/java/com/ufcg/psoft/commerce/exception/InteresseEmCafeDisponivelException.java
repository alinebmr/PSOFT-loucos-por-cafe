package com.ufcg.psoft.commerce.exception;

public class InteresseEmCafeDisponivelException extends CommerceException {
    public InteresseEmCafeDisponivelException() {
        super("Nao e possivel demonstrar interesse em um cafe disponivel");
    }
}