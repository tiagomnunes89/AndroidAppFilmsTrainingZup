package br.com.estagio.oletrainning.zup.otmovies.model.usefulClasses;

public class Password {

    protected final String password;
    private final Integer MIN_SIZE_PASS = 6;
    private final Integer MAX_SIZE_PASS = 10;
    private String REGEX_ONLY_NUMBER_AND_LETTER = "(?:\\d+[a-z]|[a-z]+\\d)[a-z\\d]*";

    public Password(String password) {
        this.password = password;
    }

    public boolean isValidPassword() {
        return validatePassword();
    }

    public boolean validatePassword() {
        return (!password.isEmpty() && validatePasswordFormat());
    }

    public boolean validatePassword(String password) {
        return (!password.isEmpty() && validatePasswordFormat());
    }

    private boolean validatePasswordFormat() {
        return password.length() >= MIN_SIZE_PASS && password.length() <= MAX_SIZE_PASS && password.matches(REGEX_ONLY_NUMBER_AND_LETTER);
    }

    public String getPassword() {
        return password;
    }

    public boolean isValidConfirmPassword(String confirmPassword){
        return validateConfirmPassword(confirmPassword);
    }

    public boolean validateMatchNewPassword(String confirmPassword) {
        return (password.equals(confirmPassword));
    }

    public boolean validateConfirmPassword(String confirmPassword) {
        return (!confirmPassword.isEmpty() && validateMatchNewPassword(confirmPassword));
    }
}
