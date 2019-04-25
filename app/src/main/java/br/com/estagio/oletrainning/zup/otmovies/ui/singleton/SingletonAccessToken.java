package br.com.estagio.oletrainning.zup.otmovies.ui.singleton;

public enum SingletonAccessToken {

    INSTANCE;

    private String accessToken;

    private void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public static void setAccessTokenReceived(String token){
        SingletonAccessToken singletonAccessToken = SingletonAccessToken.INSTANCE;
        singletonAccessToken.setAccessToken(token);
    }

    public String getLastestAuth(){
        if(accessToken != null && !accessToken.isEmpty()){
            return accessToken;
        }
        return null;
    }
}
