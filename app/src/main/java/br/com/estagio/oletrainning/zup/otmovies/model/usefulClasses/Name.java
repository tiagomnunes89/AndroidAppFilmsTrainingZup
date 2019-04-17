package br.com.estagio.oletrainning.zup.otmovies.model.usefulClasses;

public class Name {

    private final String name;
    private String REGEX_FOR_NAME = "^[\\p{L} .'-]+$";
    private final Integer MAX_SIZE_NAME = 50;

    public Name(String name) {
        this.name = name;
    }

    public boolean isValidName() {
        return validateName();
    }

    public boolean validateName() {
        return (!name.isEmpty() && validateNameFormat());
    }

    private boolean validateNameFormat() {
        return name.length() <= MAX_SIZE_NAME && name.matches(REGEX_FOR_NAME);
    }
}
