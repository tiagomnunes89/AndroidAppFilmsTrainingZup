package br.com.estagio.oletrainning.zup.otmovies.ui.home.fragments.movieList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.model.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.model.FilterIDAndPageSize;
import br.com.estagio.oletrainning.zup.otmovies.model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.server.repositories.FavoriteListRepository;
import br.com.estagio.oletrainning.zup.otmovies.server.repositories.FilmRepository;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmResponse;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.ui.BaseViewModel;
import br.com.estagio.oletrainning.zup.otmovies.ui.home.adapters.FilmDataSourceFactory;

public class MovieListViewModel extends BaseViewModel {

    private Integer INITIAL_LOAD_SIZE_HINT = 10;
    private Integer PREFETCH_DISTANCE_VALUE = 10;
    private Integer PAGE_SIZE = 10;
    protected FilmRepository filmRepository = new FilmRepository();
    protected FavoriteListRepository favoriteListRepository = new FavoriteListRepository();
    protected String SERVICE_OR_CONNECTION_ERROR = "Falha ao receber filmes. Verifique a conexão e tente novamente.";
    protected String FILTER_GENRES = "genres";
    protected LiveData<PagedList<FilmResponse>> itemPagedList;
    protected LiveData<PageKeyedDataSource<Integer, FilmResponse>> liveDataSource;
    protected LiveData<ResponseModel<FilmsResults>> filmsResults;
    protected MutableLiveData<FilterIDAndPageSize> receiverAPageSizeAndGenreIDService = new MutableLiveData<>();
    protected MutableLiveData<FilmsResults> fragmentTellerThereIsFilmResults = new MutableLiveData<>();
    protected MutableLiveData<Boolean> fragmentTellerIsSessionExpired = new MutableLiveData<>();
    private String genreID;

    public MutableLiveData<Boolean> getFragmentTellerIsSessionExpired() {
        return fragmentTellerIsSessionExpired;
    }

    public LiveData<PagedList<FilmResponse>> getItemPagedList() {
        return itemPagedList;
    }

    public MutableLiveData<FilmsResults> getFragmentTellerThereIsFilmResults() {
        return fragmentTellerThereIsFilmResults;
    }

    private Observer<FilterIDAndPageSize> receiverAPageSizeAndGenreIDServiceObserver = new Observer<FilterIDAndPageSize>() {
        @Override
        public void onChanged(FilterIDAndPageSize filterIDAndPageSize) {
            FilmDataSourceFactory itemDataSourceFactory =
                    new FilmDataSourceFactory(filterIDAndPageSize.getPageSize(),
                            filterIDAndPageSize.getFilterID(), FILTER_GENRES);
            liveDataSource = itemDataSourceFactory.getItemLiveDataSource();
            PagedList.Config config =
                    (new PagedList.Config.Builder())
                            .setEnablePlaceholders(false)
                            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
                            .setPrefetchDistance(PREFETCH_DISTANCE_VALUE)
                            .setPageSize(PAGE_SIZE)
                            .build();

            itemPagedList = (new LivePagedListBuilder(itemDataSourceFactory, config)).build();
        }
    };

    protected Observer<ResponseModel<FilmsResults>> filmsResultsObserver = new Observer<ResponseModel<FilmsResults>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<FilmsResults> responseModel) {
            if (responseModel != null) {
                if (responseModel.getCode() == SUCCESS_CODE) {
                        FilterIDAndPageSize filterIDAndPageSize = new FilterIDAndPageSize(responseModel.getResponse().getTotal_pages(),
                                MovieListViewModel.this.genreID);
                        receiverAPageSizeAndGenreIDService.setValue(filterIDAndPageSize);
                        fragmentTellerThereIsFilmResults.setValue(responseModel.getResponse());
                } else if (responseModel.getCode() == SESSION_EXPIRED_CODE) {
                    fragmentTellerIsSessionExpired.setValue(true);
                }
            } else {
                isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR);
            }
        }
    };

    protected void setupObserversForever() {
        filmRepository.getViewModelTellerIsSessionExpiredPagination().observeForever(isSessionExpiredPaginationObserver);
        filmRepository.getThereIsPaginationError().observeForever(thereIsPaginationErrorObserve);
        receiverAPageSizeAndGenreIDService.observeForever(receiverAPageSizeAndGenreIDServiceObserver);
        favoriteListRepository.getViewModelTellerIsSessionExpired().observeForever(isSessionExpiredPaginationObserver);
    }


    public void executeServiceGetFilmResults(String page, String genreID, String filter) {
        isLoading.setValue(true);
        this.genreID = genreID;
        setupObserversForever();
        filmsResults = filmRepository.getFilmsResults(page, this.genreID, filter);
        filmsResults.observeForever(filmsResultsObserver);
    }

    private Observer<ErrorMessage> thereIsPaginationErrorObserve = new Observer<ErrorMessage>() {
        @Override
        public void onChanged(@Nullable ErrorMessage errorMessage) {
            if (errorMessage != null) {
                isErrorMessageForToast.setValue(errorMessage.getMessage());
            }
        }
    };

    private Observer<Boolean> isSessionExpiredPaginationObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isSessionExpired) {
            if (isSessionExpired) {
                fragmentTellerIsSessionExpired.setValue(true);
            }
        }
    };

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (filmsResults != null && filmRepository.getThereIsPaginationError() != null
                && receiverAPageSizeAndGenreIDService != null
                && filmRepository.getViewModelTellerIsSessionExpiredPagination() != null
                && favoriteListRepository.getViewModelTellerIsSessionExpired() != null
        && getAddFavoriteFilm() != null
        && getRemoveFavoriteFilm() != null) {
            filmsResults.removeObserver(filmsResultsObserver);
            filmRepository.getThereIsPaginationError().removeObserver(thereIsPaginationErrorObserve);
            receiverAPageSizeAndGenreIDService.removeObserver(receiverAPageSizeAndGenreIDServiceObserver);
            filmRepository.getViewModelTellerIsSessionExpiredPagination().removeObserver(isSessionExpiredPaginationObserver);
            favoriteListRepository.getViewModelTellerIsSessionExpired().removeObserver(isSessionExpiredPaginationObserver);
            getAddFavoriteFilm().removeObserver(addFavoriteFilmObserver);
            getRemoveFavoriteFilm().removeObserver(removeFavoriteFilmObserver);
        }
    }
}