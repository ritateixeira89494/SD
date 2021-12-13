package com.sd.ssutilizadores.utilizadores;

public abstract class Utilizador {
    /**
     * Limites de tamanho para o username.
     */
    public static final int USERNAME_LENGTH_MINIMO = 4;
    public static final int USERNAME_LENGTH_MAXIMO = 20;

    /**
     * Limites de tamanho para a password.
     */
    public static final int PASSWORD_LENGTH_MINIMO = 5;
    public static final int PASSWORD_LENGTH_MAXIMO = 30;

    private String username;
    private String password;
    
    public Utilizador(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return this.username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return this.password;
    }
}
