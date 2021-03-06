package br.com.estagio.oletrainning.zup.otmovies.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.model.UserData;
import br.com.estagio.oletrainning.zup.otmovies.server.repositories.FavoriteListRepository;
import br.com.estagio.oletrainning.zup.otmovies.server.repositories.ValidationRepository;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonEmail;

public abstract class BaseViewModel extends ViewModel {

    protected ValidationRepository validationRepository = new ValidationRepository();
    protected int SUCCESS_CODE = 200;
    protected int SESSION_EXPIRED_CODE = 401;
    protected int OTHER_ERROR_CODE = 500;
    protected String SUCCESS_RESEND_TOKEN = "Código reenviado com sucesso!";
    private String SERVICE_OR_CONNECTION_ERROR_RESEND_TOKEN = "Falha ao reenviar o código. Verifique a conexão e tente novamente.";
    private String SUCCESS_MESSAGE_ADD = "Filme adicionado aos favoritos com sucesso";
    private String SUCCESS_MESSAGE_DELETE = "Filme removido dos favoritos com sucesso";
    private String SERVICE_OR_CONNECTION_ERROR_ADD = "Falha ao adicionar aos favoritos. Verifique a conexão e tente novamente.";
    private String MESSAGE_OTHER_ERROR_REMOVE = "Não foi possível remover esse filme. Verifique se já foi removido!";
    private String MESSAGE_OTHER_ERROR_ADD = "Não foi possível adicionar esse filme. Verifique se já foi adicionado!";
    private String SERVICE_OR_CONNECTION_ERROR_DELETE = "Falha ao remover dos favoritos. Verifique a conexão e tente novamente.";
    protected FavoriteListRepository favoriteListRepository = new FavoriteListRepository();

    private LiveData<ResponseModel<Void>> addFavoriteFilm;

    private LiveData<ResponseModel<Void>> removeFavoriteFilm;

    public LiveData<ResponseModel<Void>> getAddFavoriteFilm() {
        return addFavoriteFilm;
    }

    public LiveData<ResponseModel<Void>> getRemoveFavoriteFilm() {
        return removeFavoriteFilm;
    }

    protected MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    protected LiveData<ResponseModel<UserData>> tokenResend;

    protected MutableLiveData<String> isErrorMessageForToast = new MutableLiveData<>();

    protected MutableLiveData<String> forwardedToken = new MutableLiveData<>();

    protected MutableLiveData<String> isMessageSuccessForToast = new MutableLiveData<>();

    public MutableLiveData<String> getIsMessageSuccessForToast() {
        return isMessageSuccessForToast;
    }

    public MutableLiveData<String> getIsErrorMessageForToast() {
        return isErrorMessageForToast;
    }

    public LiveData<ResponseModel<UserData>> getTokenResend() {
        return tokenResend;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<String> getForwardedToken() {
        return forwardedToken;
    }

    protected Observer<ResponseModel<UserData>> tokenResendObserver = new Observer<ResponseModel<UserData>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<UserData> responseModel) {
            isLoading.setValue(false);
            if (responseModel != null) {
                if (responseModel.getCode() == SUCCESS_CODE) {
                    getIsMessageSuccessForToast().setValue(SUCCESS_RESEND_TOKEN);
                    getForwardedToken().setValue(SUCCESS_RESEND_TOKEN);
                } else {
                    getIsErrorMessageForToast().setValue(responseModel.getErrorMessage().getMessage());
                }
            } else {
                getIsErrorMessageForToast().setValue(SERVICE_OR_CONNECTION_ERROR_RESEND_TOKEN);
            }
        }
    };

    protected void executeServiceTokenResend(String email) {
        isLoading.setValue(true);
        tokenResend = validationRepository.resendToken(email);
        tokenResend.observeForever(tokenResendObserver);
    }

    public void tokenForwardingRequested() {
        String email = SingletonEmail.INSTANCE.getEmail();
        executeServiceTokenResend(email);
    }

    public void executeAddFavoriteFilm(String email, String movieID) {
        addFavoriteFilm = favoriteListRepository.addFavoriteFilm(email, movieID);
        addFavoriteFilm.observeForever(addFavoriteFilmObserver);
    }

    protected Observer<ResponseModel<Void>> addFavoriteFilmObserver = new Observer<ResponseModel<Void>>() {
        @Override
        public void onChanged(ResponseModel<Void> responseModel) {
            if (responseModel.getCode() == SUCCESS_CODE) {
                isMessageSuccessForToast.setValue(SUCCESS_MESSAGE_ADD);
            } else if (responseModel.getCode() == OTHER_ERROR_CODE){
                isErrorMessageForToast.setValue(MESSAGE_OTHER_ERROR_ADD);
            } else {
                isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR_ADD);
            }
        }
    };

    public void executeRemoveFavoriteFilm(String email, String movieID) {
        removeFavoriteFilm = favoriteListRepository.removeFavoriteFilm(email, movieID);
        removeFavoriteFilm.observeForever(removeFavoriteFilmObserver);
    }

    protected Observer<ResponseModel<Void>> removeFavoriteFilmObserver = new Observer<ResponseModel<Void>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<Void> responseModel) {
            if (responseModel.getCode() == SUCCESS_CODE) {
                isMessageSuccessForToast.setValue(SUCCESS_MESSAGE_DELETE);
            } else if (responseModel.getCode() == OTHER_ERROR_CODE){
                isErrorMessageForToast.setValue(MESSAGE_OTHER_ERROR_REMOVE);
                } else {
                    isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR_DELETE);
                }
            }
    };

    public void removeObserver() {

    }
}