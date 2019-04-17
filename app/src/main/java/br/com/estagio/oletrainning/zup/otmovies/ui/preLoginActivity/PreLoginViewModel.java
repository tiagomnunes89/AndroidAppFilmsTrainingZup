package br.com.estagio.oletrainning.zup.otmovies.ui.preLoginActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.ui.BaseViewModel;
import br.com.estagio.oletrainning.zup.otmovies.model.usefulClasses.Email;
import br.com.estagio.oletrainning.zup.otmovies.model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.model.UserData;
import br.com.estagio.oletrainning.zup.otmovies.server.repositories.UserRepository;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonEmail;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonName;

public class PreLoginViewModel extends BaseViewModel {

    private Email email;
    private String REGISTERED = "REGISTERED";
    private String PENDING = "PENDING";
    private String INEXISTENT = "INEXISTENT";
    private String ERROR_INVALID_EMAIL = "error.invalid.email";
    private String ERROR_SERVICE_OR_CONNECTION_EMAIL = "Falha ao validar seu email. Verifique a conexão e tente novamente.";


    private UserRepository repository = new UserRepository();

    private LiveData<ResponseModel<UserData>> userData;

    private MutableLiveData<Boolean> emailContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<String> registrationStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> isInvalidEmail = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsInvalidEmail() {
        return isInvalidEmail;
    }

    public MutableLiveData<String> getRegistrationStatus() {
        return registrationStatus;
    }

    public MutableLiveData<Boolean> getEmailContainsErrorStatus() {
        return emailContainsErrorStatus;
    }

    public void textChanged() {
        emailContainsErrorStatus.postValue(false);
    }

    public void emailEntered(String emailEntered) {
        email = new Email(emailEntered);
        emailContainsErrorStatus.postValue(!email.validateEmail());
        if (email.isValidEmail()) {
            SingletonEmail.setEmailEntered(emailEntered);
            executeServiceCallGetUserData(emailEntered);
        }
    }

    private Observer<ResponseModel<UserData>> getUserResponseObserver = new Observer<ResponseModel<UserData>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<UserData> responseModel) {
            if (responseModel != null && responseModel.getResponse() != null) {
                if (responseModel.getResponse().getRegistrationStatus() != null) {
                    if (responseModel.getResponse().getRegistrationStatus().equals(REGISTERED)) {
                        getRegistrationStatus().setValue(REGISTERED);
                        SingletonName.setCompleteName(responseModel.getResponse().getCompleteName());
                    } else if (responseModel.getResponse().getRegistrationStatus().equals(PENDING)) {
                        getRegistrationStatus().setValue(PENDING);
                        SingletonName.setCompleteName(responseModel.getResponse().getCompleteName());
                    } else if (responseModel.getResponse().getRegistrationStatus().equals(INEXISTENT)) {
                        getRegistrationStatus().setValue(INEXISTENT);
                    }
                } else {
                    isLoading.postValue(false);
                    if (responseModel.getErrorMessage().getKey().equals(ERROR_INVALID_EMAIL)) {
                        getIsInvalidEmail().setValue(true);
                    } else {
                        getIsErrorMessageForToast().setValue(responseModel.getErrorMessage().getMessage());
                    }
                }
            } else {
                isLoading.setValue(false);
                getIsErrorMessageForToast().setValue(ERROR_SERVICE_OR_CONNECTION_EMAIL);
            }
        }

    };

    private void executeServiceCallGetUserData(String email) {
        isLoading.postValue(true);
        userData = repository.getUserData(email);
        userData.observeForever(getUserResponseObserver);
    }

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (userData != null) {
            userData.removeObserver(getUserResponseObserver);
        }
    }
}