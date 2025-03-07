package com.ufcg.psoft.commerce.exception;

public class CafeNaoEInteresseDeClienteException extends CommerceException {
  public CafeNaoEInteresseDeClienteException() {
    super("Esse cafe nao esta na lista de interesses do cliente!");
  }
}
