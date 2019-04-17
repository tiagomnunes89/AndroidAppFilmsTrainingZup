package br.com.estagio.oletrainning.zup.otmovies.ui.home.adapters;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;

import br.com.estagio.oletrainning.zup.otmovies.server.repositories.FilmDataSource;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmResponse;

public class FilmDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, FilmResponse>> itemLiveDataSource = new MutableLiveData<>();
    private int pageSize;
    private String ID;
    private String FILTER;

    public FilmDataSourceFactory(Integer pageSize, String ID, String filter) {
        this.pageSize = pageSize;
        this.ID = ID;
        this.FILTER = filter;
    }

    public FilmDataSourceFactory() {
    }

    @Override
    public DataSource create() {
        FilmDataSource filmDataSource = new FilmDataSource(pageSize, ID,FILTER);
        itemLiveDataSource.postValue(filmDataSource);
        return filmDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, FilmResponse>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}