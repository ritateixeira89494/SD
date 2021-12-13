package com.sd.ssutilizadores;

import com.sd.ssutilizadores.exceptions.CredenciaisErradasException;
import com.sd.ssutilizadores.exceptions.PasswordInvalidaException;
import com.sd.ssutilizadores.exceptions.UsernameInvalidoException;
import com.sd.ssutilizadores.exceptions.UtilizadorExisteException;

public interface ISSUtilizador {
    public boolean autenticar(String username, String password) throws CredenciaisErradasException;
    public void registar(String username, String password, int authority) throws UtilizadorExisteException, UsernameInvalidoException, PasswordInvalidaException;
}