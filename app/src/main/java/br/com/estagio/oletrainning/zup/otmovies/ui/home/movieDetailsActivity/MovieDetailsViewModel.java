package br.com.estagio.oletrainning.zup.otmovies.ui.home.movieDetailsActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.model.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.model.MovieDetailsModel;
import br.com.estagio.oletrainning.zup.otmovies.model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.server.repositories.FilmRepository;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmResponse;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.ui.BaseViewModel;
import br.com.estagio.oletrainning.zup.otmovies.ui.home.adapters.FilmDataSourceFactory;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonFilmID;

public class MovieDetailsViewModel extends BaseViewModel {

    private FilmRepository filmRepository = new FilmRepository();

    private LiveData<ResponseModel<MovieDetailsModel>> getMovieDetails;

    private MutableLiveData<MovieDetailsModel> thereIsMovieDetails = new MutableLiveData<>();


    private String SERVICE_OR_CONNECTION_ERROR = "Falha ao receber detalhes do filme. Verifique a conexão e tente novamente.";
    private String FILTER_SIMILARITY = "similarity";
    private Integer INITIAL_LOAD_SIZE_HINT = 10;
    private Integer PREFETCH_DISTANCE_VALUE = 10;
    private Integer PAGE_SIZE = 10;
    private int ERROR_UNEXPECTED_CODE = 500;
    private String MESSAGE_ERROR_RECURRENT = "Erro inesperado ao receber filmes. Feche o aplicativo e tente novamente mais tarde";

    private MutableLiveData<Boolean> activityTellerIsSessionExpired = new MutableLiveData<>();

    private LiveData<PagedList<FilmResponse>> itemPagedList;
    private LiveData<PageKeyedDataSource<Integer, FilmResponse>> liveDataSource;
    private MutableLiveData<Integer> receiverPageSizeService = new MutableLiveData<>();
    private MutableLiveData<FilmsResults> activityTellerThereIsFilmResults = new MutableLiveData<>();
    private LiveData<ResponseModel<FilmsResults>> filmsResults;
    private MutableLiveData<Boolean> similarMoviesListEmpty = new MutableLiveData<>();

    public MutableLiveData<Boolean> getSimilarMoviesListEmpty() {
        return similarMoviesListEmpty;
    }

    public LiveData<PagedList<FilmResponse>> getItemPagedList() {
        return itemPagedList;
    }

    public MutableLiveData<FilmsResults> getActivityTellerThereIsFilmResults() {
        return activityTellerThereIsFilmResults;
    }

    public MutableLiveData<Boolean> getActivityTellerIsSessionExpired() {
        return activityTellerIsSessionExpired;
    }

    public MutableLiveData<MovieDetailsModel> getThereIsMovieDetails() {
        return thereIsMovieDetails;
    }

    public void executeServiceGetMovieDetails(int id) {
        isLoading.setValue(true);
        getMovieDetails = filmRepository.getMovieDetails(id);
        getMovieDetails.observeForever(getMovieDetailsObserver);
    }

    private Observer<ResponseModel<MovieDetailsModel>> getMovieDetailsObserver = new Observer<ResponseModel<MovieDetailsModel>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<MovieDetailsModel> movieDetails) {
            if (movieDetails != null) {
                if (movieDetails.getCode() == SUCCESS_CODE) {
                    thereIsMovieDetails.setValue(movieDetails.getResponse());
                } else if (movieDetails.getCode() == SESSION_EXPIRED_CODE) {
                    activityTellerIsSessionExpired.setValue(true);
                }
            } else {
                isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR);
            }
        }
    };

    private Observer<Integer> receiverPageSizeServiceObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer pageSize) {
            FilmDataSourceFactory itemDataSourceFactory =
                    new FilmDataSourceFactory(pageSize,
                            String.valueOf(SingletonFilmID.INSTANCE.getID()), FILTER_SIMILARITY);
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

    private Observer<ResponseModel<FilmsResults>> filmsResultsObserver = new Observer<ResponseModel<FilmsResults>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<FilmsResults> responseModel) {
            if (responseModel != null) {
                if (responseModel.getCode() == SUCCESS_CODE) {
                    receiverPageSizeService.setValue(responseModel.getResponse().getTotal_results());
                    activityTellerThereIsFilmResults.setValue(responseModel.getResponse());
                    if (responseModel.getResponse().getTotal_results() == 0) {
                        similarMoviesListEmpty.setValue(true);
                    } else {
                        similarMoviesListEmpty.setValue(false);
                    }
                } else if (responseModel.getCode() == ERROR_UNEXPECTED_CODE) {
                    isErrorMessageForToast.setValue(MESSAGE_ERROR_RECURRENT);
                }
            } else {
                isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR);
            }
        }
    };

    private void setupObserversForever() {
        filmRepository.getViewModelTellerIsSessionExpiredPagination().observeForever(isSessionExpiredPaginationObserver);
        filmRepository.getThereIsPaginationError().observeForever(thereIsPaginationErrorObserve);
        receiverPageSizeService.observeForever(receiverPageSizeServiceObserver);
    }

    public void executeServiceGetFilmResults(String page, Integer filmID) {
        isLoading.setValue(true);
        setupObserversForever();
        filmsResults = filmRepository.getFilmsResults(page, String.valueOf(filmID), FILTER_SIMILARITY);
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
                activityTellerIsSessionExpired.setValue(true);
            }
        }
    };

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (filmsResults != null && filmRepository.getThereIsPaginationError() != null
                && receiverPageSizeService != null
                && filmRepository.getViewModelTellerIsSessionExpiredPagination() != null
                && getAddFavoriteFilm() != null
                && getRemoveFavoriteFilm() != null) {
            filmsResults.removeObserver(filmsResultsObserver);
            filmRepository.getThereIsPaginationError().removeObserver(thereIsPaginationErrorObserve);
            receiverPageSizeService.removeObserver(receiverPageSizeServiceObserver);
            filmRepository.getViewModelTellerIsSessionExpiredPagination().removeObserver(isSessionExpiredPaginationObserver);
            getAddFavoriteFilm().removeObserver(addFavoriteFilmObserver);
            getRemoveFavoriteFilm().removeObserver(removeFavoriteFilmObserver);
        }
    }
}
