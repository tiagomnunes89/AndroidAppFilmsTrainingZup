package br.com.estagio.oletrainning.zup.otmovies.ui.singleton;

import android.util.Log;

public enum SingletonEmail {

    INSTANCE;

    private String email;

    private void setEmail(String email) {
        this.email = email;
    }

    public static void setEmailEntered(String name){
        SingletonEmail singletonEmail = SingletonEmail.INSTANCE;
        singletonEmail.setEmail(name);
        if (singletonEmail.email != null){
            Log.d("SINGLETON_EMAIL_SAVED", singletonEmail.email);
        }

    }

    public String getEmail(){
        if(email != null && !email.isEmpty()){
            return email;
        }
        return null;
    }
}
