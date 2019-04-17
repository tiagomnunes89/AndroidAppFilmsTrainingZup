package br.com.estagio.oletrainning.zup.otmovies.model.usefulClasses;

public class Username {

    private final String username;
    private final Integer MAX_SIZE_USERNAME = 15;
    private String REGEX_ONLY_NUMBER_OR_LETTER = "[a-zA-Z0-9]+";

    public Username(String username) {
        this.username = username;
    }

    public boolean isValidUserName() {
        return validateUserName();
    }

    private boolean validateUserNameFormat() {
        return username.length() <= MAX_SIZE_USERNAME && username.matches(REGEX_ONLY_NUMBER_OR_LETTER);
    }

    public boolean validateUserName() {
        return (!username.isEmpty() && validateUserNameFormat());
    }
}
