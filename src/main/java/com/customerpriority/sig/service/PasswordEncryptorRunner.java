package com.customerpriority.sig.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptorRunner implements CommandLineRunner{

    private final UsuarioService usuarioService;

    public PasswordEncryptorRunner(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void run(String... args) throws Exception {
        //usuarioService.updatePasswords(); // Encripta las contraseñas de la base de datos.
    }

}
