package com.example.SplitLoop.user.domain.port;

public interface PasswordEncoderPort {

    /**
     * Encripta una contraseña en texto plano.
     *
     * @param rawPassword Contraseña sin cifrar
     * @return Contraseña cifrada
     */
    String encode(CharSequence rawPassword);

    /**
     * Compara una contraseña en texto plano con una contraseña cifrada.
     *
     * @param rawPassword Contraseña sin cifrar
     * @param encodedPassword Contraseña cifrada de la base de datos
     * @return true si coinciden, false en caso contrario
     */
    boolean matches(CharSequence rawPassword, String encodedPassword);
}
