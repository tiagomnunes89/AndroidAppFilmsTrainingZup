package br.com.estagio.oletrainning.zup.otmovies.ui.singleton;

public enum SingletonEmail {

    INSTANCE;

    private String email;

    private void setEmail(String email) {
        this.email = email;
    }

    public static void setEmailEntered(String name){
        SingletonEmail singletonEmail = SingletonEmail.INSTANCE;
        singletonEmail.setEmail(name);
    }

    public String getEmail(){
        if(email != null && !email.isEmpty()){
            return email;
        }
        return null;
    }
}
