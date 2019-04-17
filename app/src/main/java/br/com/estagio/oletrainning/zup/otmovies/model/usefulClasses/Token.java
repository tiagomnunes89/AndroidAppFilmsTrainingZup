package br.com.estagio.oletrainning.zup.otmovies.model.usefulClasses;

public class Token {

    private final String token;
    private final int MAX_SIZE_TOKEN = 6;

    public Token(String token) {
        this.token = token;
    }

    public boolean isValidToken(){
        return validateTokenSize();
    }

    public boolean validateTokenSize() {
        return (token.length() == MAX_SIZE_TOKEN);
    }
}
