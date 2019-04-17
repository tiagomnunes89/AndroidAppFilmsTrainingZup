package br.com.estagio.oletrainning.zup.otmovies.server.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;

import br.com.estagio.oletrainning.zup.otmovies.model.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.model.MovieDetailsModel;
import br.com.estagio.oletrainning.zup.otmovies.model.ResponseModel;

import br.com.estagio.oletrainning.zup.otmovies.server.remote.FilmService;
import br.com.estagio.oletrainning.zup.otmovies.server.remote.RetrofitServiceBuilder;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmGenres;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmResponse;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonAccessToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmRepository extends CommonRepository{

    private FilmService filmService;
    private int SUCCESS_CODE = 200;
    private String UNEXPECTED_ERROR_KEY = "erro.inesperado";
    private String UNEXPECTED_ERROR_MESSAGE = "Erro inesperado, tente novamente mais tarde!";
    private int SESSION_EXPIRED_CODE = 401;
    private static final int FIRST_PAGE = 1;

    private MutableLiveData<ErrorMessage> thereIsPaginationError;
    private MutableLiveData<Boolean> viewModelTellerIsSessionExpiredPagination;

    public MutableLiveData<Boolean> getViewModelTellerIsSessionExpiredPagination() {
        return viewModelTellerIsSessionExpiredPagination;
    }

    public MutableLiveData<ErrorMessage> getThereIsPaginationError() {
        return thereIsPaginationError;
    }

    public FilmRepository(){
        filmService = RetrofitServiceBuilder.buildService(FilmService.class);
        thereIsPaginationError = new MutableLiveData<>();
        viewModelTellerIsSessionExpiredPagination = new MutableLiveData<>();
    }

    public LiveData<ResponseModel<FilmGenres>> getGenreList() {
        final MutableLiveData<ResponseModel<FilmGenres>> data = new MutableLiveData<>();
        filmService.getGenres()
                .enqueue(new Callback<FilmGenres>() {
                    @Override
                    public void onResponse(Call<FilmGenres> call, Response<FilmGenres> response) {
                        SingletonAccessToken.setAccessTokenReceived(response.headers().get("x-access-token"));
                        ResponseModel<FilmGenres> responseModel = new ResponseModel<>();
                        if(response.code() == SUCCESS_CODE && response.body() != null){
                            responseModel.setCode(SUCCESS_CODE);
                            responseModel.setResponse(response.body());
                        } else if (response.code() == SESSION_EXPIRED_CODE){
                            responseModel.setCode(SESSION_EXPIRED_CODE);
                            viewModelTellerIsSessionExpiredPagination.postValue(true);
                        } else {
                            if(response.errorBody() != null){
                                responseModel.setErrorMessage(serializeErrorBody(response.errorBody()));
                            } else {
                                ErrorMessage errorMessage = new ErrorMessage();
                                errorMessage.setKey(UNEXPECTED_ERROR_KEY);
                                errorMessage.setMessage(UNEXPECTED_ERROR_MESSAGE);
                                responseModel.setErrorMessage(errorMessage);
                            }
                        }
                        data.setValue(responseModel);
                    }

                    @Override
                    public void onFailure(Call<FilmGenres> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    public LiveData<ResponseModel<MovieDetailsModel>> getMovieDetails(int id) {
        final MutableLiveData<ResponseModel<MovieDetailsModel>> data = new MutableLiveData<>();
        filmService.getMovieDetails(id)
                .enqueue(new Callback<MovieDetailsModel>() {
                    @Override
                    public void onResponse(Call<MovieDetailsModel> call, Response<MovieDetailsModel> response) {
                        SingletonAccessToken.setAccessTokenReceived(response.headers().get("x-access-token"));
                        ResponseModel<MovieDetailsModel> responseModel = new ResponseModel<>();
                        if(response.code() == SUCCESS_CODE && response.body() != null){
                            responseModel.setCode(SUCCESS_CODE);
                            responseModel.setResponse(response.body());
                        } else if (response.code() == SESSION_EXPIRED_CODE){
                            responseModel.setCode(SESSION_EXPIRED_CODE);
                            viewModelTellerIsSessionExpiredPagination.postValue(true);
                        } else {
                            if(response.errorBody() != null){
                                responseModel.setErrorMessage(serializeErrorBody(response.errorBody()));
                            } else {
                                ErrorMessage errorMessage = new ErrorMessage();
                                errorMessage.setKey(UNEXPECTED_ERROR_KEY);
                                errorMessage.setMessage(UNEXPECTED_ERROR_MESSAGE);
                                responseModel.setErrorMessage(errorMessage);
                            }
                        }
                        data.setValue(responseModel);
                    }

                    @Override
                    public void onFailure(Call<MovieDetailsModel> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    public LiveData<ResponseModel<FilmsResults>> getFilmsResults(String page, String filterID, String filter) {
        final MutableLiveData<ResponseModel<FilmsResults>> data = new MutableLiveData<>();
        filmService.getMovieGenre(filter,filterID,"20",page)
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {
                        SingletonAccessToken.setAccessTokenReceived(response.headers().get("x-access-token"));
                        ResponseModel<FilmsResults> responseModel = new ResponseModel<>();
                        if(response.code() == SUCCESS_CODE && response.body() != null){
                            responseModel.setCode(SUCCESS_CODE);
                            responseModel.setResponse(response.body());
                        } else if (response.code() == SESSION_EXPIRED_CODE){
                                viewModelTellerIsSessionExpiredPagination.postValue(true);
                        } else {
                            if(response.errorBody() != null){
                                responseModel.setErrorMessage(serializeErrorBody(response.errorBody()));
                            } else {
                                ErrorMessage errorMessage = new ErrorMessage();
                                errorMessage.setKey(UNEXPECTED_ERROR_KEY);
                                errorMessage.setMessage(UNEXPECTED_ERROR_MESSAGE);
                                responseModel.setErrorMessage(errorMessage);
                            }
                        }
                        data.setValue(responseModel);
                    }

                    @Override
                    public void onFailure(Call<FilmsResults> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    public void getFilmsResultsLoadInitial (
            final PageKeyedDataSource.LoadInitialCallback<Integer, FilmResponse> callback,
            String firstPage, String genreID,String filter) {
        filmService.getMovieGenre(filter,genreID,"20",firstPage)
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {
                        SingletonAccessToken.setAccessTokenReceived(response.headers().get("x-access-token"));
                        if(response.code() == SUCCESS_CODE && response.body() != null){
                            callback.onResult(response.body().getResults(), null, FIRST_PAGE + 1);
                        } else if (response.code() == SESSION_EXPIRED_CODE){
                                viewModelTellerIsSessionExpiredPagination.postValue(true);
                        } else {
                            if(response.errorBody() != null){
                                ErrorMessage errorMessage = serializeErrorBody(response.errorBody());
                                thereIsPaginationError.setValue(errorMessage);
                            } else {
                                ErrorMessage errorMessage = new ErrorMessage();
                                errorMessage.setKey(UNEXPECTED_ERROR_KEY);
                                errorMessage.setMessage(UNEXPECTED_ERROR_MESSAGE);
                                thereIsPaginationError.setValue(errorMessage);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FilmsResults> call, Throwable t) {
                        ErrorMessage errorMessage = new ErrorMessage();
                        errorMessage.setMessage(t.getMessage());
                        thereIsPaginationError.setValue(errorMessage);
                    }
                });
    }

    public void getFilmsResultsLoadBefore (
            final PageKeyedDataSource.LoadParams<Integer> params, final PageKeyedDataSource.LoadCallback<Integer,
            FilmResponse> callback, String genreID,String filter) {
        filmService.getMovieGenre(filter,genreID,"20",String.valueOf(params.key))
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {
                        SingletonAccessToken.setAccessTokenReceived(response.headers().get("x-access-token"));
                        if(response.code() == SUCCESS_CODE && response.body() != null){
                            Integer key = (params.key > 1) ? params.key - 1 : null;
                            callback.onResult(response.body().getResults(),key);
                        } else if (response.code() == SESSION_EXPIRED_CODE){
                                viewModelTellerIsSessionExpiredPagination.postValue(true);
                        } else {
                            if(response.errorBody() != null){
                                ErrorMessage errorMessage = serializeErrorBody(response.errorBody());
                                thereIsPaginationError.setValue(errorMessage);
                            } else {
                                ErrorMessage errorMessage = new ErrorMessage();
                                errorMessage.setKey(UNEXPECTED_ERROR_KEY);
                                errorMessage.setMessage(UNEXPECTED_ERROR_MESSAGE);
                                thereIsPaginationError.setValue(errorMessage);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FilmsResults> call, Throwable t) {
                        ErrorMessage errorMessage = new ErrorMessage();
                        errorMessage.setMessage(t.getMessage());
                        thereIsPaginationError.setValue(errorMessage);
                    }
                });
    }

    public void getFilmsResultsloadAfter (
            final Integer PAGE_SIZE, final PageKeyedDataSource.LoadParams<Integer> params,
            final PageKeyedDataSource.LoadCallback<Integer, FilmResponse> callback, String genreID, String filter) {
        filmService.getMovieGenre(filter,genreID,"20",String.valueOf(params.key))
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {
                        SingletonAccessToken.setAccessTokenReceived(response.headers().get("x-access-token"));
                        if(response.code() == SUCCESS_CODE && response.body() != null){
                            Integer key = (params.key < PAGE_SIZE)? params.key + 1 : null;
                            callback.onResult(response.body().getResults(), key);
                        } else if (response.code() == SESSION_EXPIRED_CODE){
                                viewModelTellerIsSessionExpiredPagination.postValue(true);
                        } else {
                            if(response.errorBody() != null){
                                ErrorMessage errorMessage = serializeErrorBody(response.errorBody());
                                thereIsPaginationError.setValue(errorMessage);
                            } else {
                                ErrorMessage errorMessage = new ErrorMessage();
                                errorMessage.setKey(UNEXPECTED_ERROR_KEY);
                                errorMessage.setMessage(UNEXPECTED_ERROR_MESSAGE);
                                thereIsPaginationError.setValue(errorMessage);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FilmsResults> call, Throwable t) {
                        ErrorMessage errorMessage = new ErrorMessage();
                        errorMessage.setMessage(t.getMessage());
                        thereIsPaginationError.setValue(errorMessage);
                    }
                });
    }
}
