package com.ufcg.psoft.commerce.controller;


import com.ufcg.psoft.commerce.service.pedido.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        value = "/pedidos",
        produces = MediaType.APPLICATION_JSON_VALUE
)

public class PedidoController {

    @Autowired
    PedidoService pedidoService;
}