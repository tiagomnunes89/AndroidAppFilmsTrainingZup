package br.com.estagio.oletrainning.zup.otmovies.ui.loginActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.ui.BaseViewModel;
import br.com.estagio.oletrainning.zup.otmovies.model.usefulClasses.Password;
import br.com.estagio.oletrainning.zup.otmovies.model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.model.UserData;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonEmail;

public class LoginViewModel extends BaseViewModel {

    private Password password;
    private String KEY_INVALID_PASSWORD = "error.invalid.password";
    private String KEY_UNAUTHORIZED_LOGIN =  "error.unauthorized.login";
    private String KEY_UNAUTHORIZED_PASSWORD = "error.unauthorized.password";
    private String SUCCESS_MESSAGE_LOGIN = "Senha confirmada, login autorizado!";
    private String SERVICE_OR_CONNECTION_ERROR_LOGIN = "Falha ao validar sua senha. Verifique a conexão e tente novamente.";

    private LiveData<ResponseModel<UserData>> passwordValidation;

    private MutableLiveData<Boolean> passwordContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<String> messageErrorChanged = new MutableLiveData<>();

    private MutableLiveData<String> isValidatedPassword = new MutableLiveData<>();

    public MutableLiveData<String> getIsValidatedPassword() {
        return isValidatedPassword;
    }

    public MutableLiveData<String> getMessageErrorChanged() {
        return messageErrorChanged;
    }

    public MutableLiveData<Boolean> getPasswordContainsErrorStatus() {
        return passwordContainsErrorStatus;
    }

    public void passwordEntered(String passwordEntered){
        password = new Password(passwordEntered);
        passwordContainsErrorStatus.postValue(!password.validatePassword());
        if (password.isValidPassword()) {
            UserData userData = new UserData();
            userData.setEmail(SingletonEmail.INSTANCE.getEmail());
            userData.setPassword(passwordEntered);
            executeServicePasswordValidation(userData);
        }
    }

    public void changeSuccessMessageResendToken(){
        SUCCESS_RESEND_TOKEN = "Foi enviado um código para seu e-mail!";
    }

    @Override
    public void tokenForwardingRequested() {
        super.tokenForwardingRequested();
        passwordContainsErrorStatus.setValue(false);
        changeSuccessMessageResendToken();
        String email = SingletonEmail.INSTANCE.getEmail();
        executeServiceTokenResend(email);
    }

    public void passwordTextChanged(){
        passwordContainsErrorStatus.postValue(false);
    }

    public void setPasswordContainsErrorStatus(boolean containsErrorPassword) {
        passwordContainsErrorStatus.postValue(containsErrorPassword);
    }

    public boolean isMessageErrorTopToast(String key){
        return (key.equals(KEY_INVALID_PASSWORD)
                || key.equals(KEY_UNAUTHORIZED_LOGIN)
                || key.equals(KEY_UNAUTHORIZED_PASSWORD));
    }

    private Observer<ResponseModel<UserData>> passwordValidationObserver = new Observer<ResponseModel<UserData>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<UserData> responseModel) {
            if (responseModel != null) {
                if (responseModel.getCode() == SUCCESS_CODE) {
                    getIsValidatedPassword().setValue(SUCCESS_MESSAGE_LOGIN);
                } else {
                    isLoading.setValue(false);
                    String key = responseModel.getErrorMessage().getKey();
                    String message = responseModel.getErrorMessage().getMessage();
                    if (isMessageErrorTopToast(key)) {
                        getMessageErrorChanged().setValue(message);
                    } else {
                        getIsErrorMessageForToast().setValue(message);
                    }
                }
            } else {
                isLoading.setValue(false);
                getIsErrorMessageForToast().setValue(SERVICE_OR_CONNECTION_ERROR_LOGIN);
            }
        }

    };

    private void executeServicePasswordValidation(UserData userData) {
        isLoading.setValue(true);
        passwordValidation = validationRepository.passwordValidate(userData);
        passwordValidation.observeForever(passwordValidationObserver);
    }

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (passwordValidation != null) {
            passwordValidation.removeObserver(passwordValidationObserver);
        }
    }
}