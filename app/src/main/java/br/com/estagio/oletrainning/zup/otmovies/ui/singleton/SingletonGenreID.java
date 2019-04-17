package br.com.estagio.oletrainning.zup.otmovies.ui.singleton;

import android.util.Log;

public enum SingletonGenreID {

    INSTANCE;

    private String genreID;

    private void setGenreID(String genreID) {
        this.genreID = genreID;
    }

    public static void setGenreIDEntered(String genreID){
        SingletonGenreID singletonGenreID = SingletonGenreID.INSTANCE;
        singletonGenreID.setGenreID(genreID);
        if (singletonGenreID.genreID != null){
            Log.d("SINGLETON_GENRE_ID", singletonGenreID.genreID);
        }

    }

    public String getGenreID(){
        if(genreID != null && !genreID.isEmpty()){
            return genreID;
        }
        return null;
    }
}
