package br.com.estagio.oletrainning.zup.otmovies.ui.singleton;

import android.util.Log;

public enum SingletonName {

    INSTANCE;

    private String name;

    private void setName(String name) {
        this.name = name;
    }

    public static void setCompleteName(String name){
        SingletonName singletonName = SingletonName.INSTANCE;
        singletonName.setName(name);
        if (singletonName.name != null){
            Log.d("SINGLETON_NAME_SAVED", singletonName.name);
        }

    }

    public String getUsername(){
        if(name != null && !name.isEmpty()){
            return name;
        }
        return null;
    }
}
