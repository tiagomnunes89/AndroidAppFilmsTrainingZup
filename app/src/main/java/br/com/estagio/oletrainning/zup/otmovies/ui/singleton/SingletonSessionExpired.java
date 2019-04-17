package br.com.estagio.oletrainning.zup.otmovies.ui.singleton;

import android.util.Log;

public enum SingletonSessionExpired {

    INSTANCE;

    private Boolean isSessionExpired;

    private void setIsSessionExpired(boolean isSessionExpired) {
        this.isSessionExpired = isSessionExpired;
    }

    public static void setIsSessionExpiredEntered(boolean isSessionExpired){
        SingletonSessionExpired singletonSessionExpired = SingletonSessionExpired.INSTANCE;
        singletonSessionExpired.setIsSessionExpired(isSessionExpired);
        if (singletonSessionExpired.isSessionExpired != null){
            Log.d("SINGLETON_SESSION_SAVED", "Salvou alteração sessão");
        }
    }

    public boolean getIsSessionExpired(){
        if(isSessionExpired != null){
            return isSessionExpired;
        }
        return Boolean.parseBoolean(null);
    }
}
