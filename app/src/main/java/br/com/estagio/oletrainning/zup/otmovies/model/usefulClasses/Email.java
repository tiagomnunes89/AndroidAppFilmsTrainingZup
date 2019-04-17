package br.com.estagio.oletrainning.zup.otmovies.model.usefulClasses;

public class Email {

    private final String email;

    public Email(String email) {
        this.email = email;
    }

    public boolean isValidEmail() {
        return validateEmail();
    }

    public boolean validateEmail() {
        return (!email.isEmpty() && validateEmailFormat());
    }

    private boolean validateEmailFormat() {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
