package br.com.estagio.oletrainning.zup.otmovies.ui.home.adapters;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;

import br.com.estagio.oletrainning.zup.otmovies.server.repositories.SearchDataSource;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmResponse;

public class SearchDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, FilmResponse>> itemLiveDataSource = new MutableLiveData<>();
    private int pageSize;
    private String ID;
    private String FILTER;

    public SearchDataSourceFactory(Integer pageSize, String ID, String filter) {
        this.pageSize = pageSize;
        this.ID = ID;
        this.FILTER = filter;
    }

    public SearchDataSourceFactory() {
    }

    @Override
    public DataSource create() {
        SearchDataSource searchDataSource = new SearchDataSource(pageSize, ID,FILTER);
        itemLiveDataSource.postValue(searchDataSource);
        return searchDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, FilmResponse>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}