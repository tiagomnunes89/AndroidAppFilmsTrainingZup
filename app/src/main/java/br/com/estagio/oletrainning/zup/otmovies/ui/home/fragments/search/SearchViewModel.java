package br.com.estagio.oletrainning.zup.otmovies.ui.home.fragments.search;

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
import br.com.estagio.oletrainning.zup.otmovies.ui.home.adapters.SearchDataSourceFactory;

public class SearchViewModel extends BaseViewModel {

    private final static String FIRST_PAGE = "1";
    private final static String FILTER_NAME = "name";
    private Integer PAGE_SIZE = 10;
    private Integer INITIAL_LOAD_SIZE_HINT = 5;
    private Integer PREFETCH_DISTANCE_VALUE = 5;
    private FilmRepository filmRepository = new FilmRepository();
    private FavoriteListRepository favoriteListRepository = new FavoriteListRepository();
    private String SERVICE_OR_CONNECTION_ERROR = "Falha ao receber filmes. Verifique a conexão e tente novamente.";
    private LiveData<PagedList<FilmResponse>> itemPagedList;
    private LiveData<PageKeyedDataSource<Integer, FilmResponse>> liveDataSource;
    private LiveData<ResponseModel<FilmsResults>> filmsResults;
    private MutableLiveData<FilterIDAndPageSize> receiverAPageSizeAndGenreIDService = new MutableLiveData<>();
    private MutableLiveData<FilmsResults> fragmentTellerThereIsFilmResults = new MutableLiveData<>();
    private MutableLiveData<Boolean> fragmentTellerIsSessionExpired = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSearchEmpty = new MutableLiveData<>();
    private String queryMovies;

    public MutableLiveData<Boolean> getIsSearchEmpty() {
        return isSearchEmpty;
    }

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
            SearchDataSourceFactory itemDataSourceFactory =
                    new SearchDataSourceFactory(filterIDAndPageSize.getPageSize(),
                            filterIDAndPageSize.getFilterID(),FILTER_NAME);
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

    public void executeServiceGetFilmResultsSearch(String queryMovies) {
        this.queryMovies = queryMovies;
        isLoading.setValue(true);

        setupObserversForever();

        if(queryMovies != null){
            filmsResults = filmRepository.getFilmsResults(FIRST_PAGE,queryMovies,FILTER_NAME);
            filmsResults.observeForever(filmsResultsObserverSearch);
        }
    }

    private Observer<ResponseModel<FilmsResults>> filmsResultsObserverSearch = new Observer<ResponseModel<FilmsResults>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<FilmsResults> responseModel) {
            if (responseModel != null) {
                if (responseModel.getCode() == SUCCESS_CODE) {
                    if(responseModel.getResponse().getTotal_results() !=0 && SearchViewModel.this.queryMovies != null){
                        FilterIDAndPageSize filterIDAndPageSize = new FilterIDAndPageSize(responseModel.getResponse().getTotal_pages(),
                                SearchViewModel.this.queryMovies);
                        receiverAPageSizeAndGenreIDService.setValue(filterIDAndPageSize);
                        fragmentTellerThereIsFilmResults.setValue(responseModel.getResponse());
                        isSearchEmpty.setValue(false);
                    } else {
                        isSearchEmpty.setValue(true);
                    }
                } else if(responseModel.getCode() == SESSION_EXPIRED_CODE){
                    fragmentTellerIsSessionExpired.setValue(true);
                }
            } else {
                isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR);
            }
        }
    };


    private void setupObserversForever(){
        filmRepository.getViewModelTellerIsSessionExpiredPagination().observeForever(isSessionExpiredPaginationObserver);
        filmRepository.getThereIsPaginationError().observeForever(thereIsPaginationErrorObserve);
        receiverAPageSizeAndGenreIDService.observeForever(receiverAPageSizeAndGenreIDServiceObserver);
        favoriteListRepository.getViewModelTellerIsSessionExpired().observeForever(isSessionExpiredPaginationObserver);
    }
    
    private Observer<ErrorMessage> thereIsPaginationErrorObserve = new Observer<ErrorMessage>() {
        @Override
        public void onChanged(@Nullable ErrorMessage errorMessage) {
            if(errorMessage != null){
                isErrorMessageForToast.setValue(errorMessage.getMessage());
            }
        }
    };

    private Observer<Boolean> isSessionExpiredPaginationObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isSessionExpired) {
            if(isSessionExpired){
                fragmentTellerIsSessionExpired.setValue(true);
            }
        }
    };

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (filmsResults != null && filmRepository.getThereIsPaginationError() != null
                &&  receiverAPageSizeAndGenreIDService != null
                && filmRepository.getViewModelTellerIsSessionExpiredPagination() != null
                && favoriteListRepository.getViewModelTellerIsSessionExpired() != null
                && getAddFavoriteFilm() != null
                && getRemoveFavoriteFilm() != null)  {
            filmsResults.removeObserver(filmsResultsObserverSearch);
            filmRepository.getThereIsPaginationError().removeObserver(thereIsPaginationErrorObserve);
            receiverAPageSizeAndGenreIDService.removeObserver(receiverAPageSizeAndGenreIDServiceObserver);
            filmRepository.getViewModelTellerIsSessionExpiredPagination().removeObserver(isSessionExpiredPaginationObserver);
            favoriteListRepository.getViewModelTellerIsSessionExpired().removeObserver(isSessionExpiredPaginationObserver);
            getAddFavoriteFilm().removeObserver(addFavoriteFilmObserver);
            getRemoveFavoriteFilm().removeObserver(removeFavoriteFilmObserver);
        }
    }
}
