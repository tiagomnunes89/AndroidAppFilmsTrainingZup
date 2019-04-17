package br.com.estagio.oletrainning.zup.otmovies.ui.registerUserActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.ui.BaseViewModel;
import br.com.estagio.oletrainning.zup.otmovies.model.usefulClasses.Name;
import br.com.estagio.oletrainning.zup.otmovies.model.usefulClasses.Password;
import br.com.estagio.oletrainning.zup.otmovies.model.usefulClasses.Username;
import br.com.estagio.oletrainning.zup.otmovies.model.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.server.repositories.UserRepository;
import br.com.estagio.oletrainning.zup.otmovies.model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.model.UserData;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonEmail;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonName;

public class RegisterNewUserViewModel extends BaseViewModel {

    private Name name;
    private Password password;
    private Username username;
    private String SUCCESSFULLY_REGISTERED = "Usuário cadastrado com sucesso!";
    private String SERVICE_OR_CONNECTION_ERROR_REGISTER = "Falha ao registrar seu cadastro. Verifique a conexão e tente novamente.";

    private UserRepository repository = new UserRepository();

    private LiveData<ResponseModel<UserData>> registerUser;

    private MutableLiveData<Boolean> nameContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> userNameContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> passwordContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<String> isRegistered = new MutableLiveData<>();

    private MutableLiveData<Boolean> isInvalidName = new MutableLiveData<>();

    private MutableLiveData<Boolean> isInvalidUsername = new MutableLiveData<>();

    private MutableLiveData<Boolean> isInvalidPassword = new MutableLiveData<>();

    private MutableLiveData<Boolean> isUsernameDuplicated = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsUsernameDuplicated() {
        return isUsernameDuplicated;
    }

    public MutableLiveData<Boolean> getIsInvalidPassword() {
        return isInvalidPassword;
    }

    public MutableLiveData<Boolean> getIsInvalidUsername() {
        return isInvalidUsername;
    }

    public MutableLiveData<Boolean> getIsInvalidName() {
        return isInvalidName;
    }

    public MutableLiveData<String> getIsRegistered() {
        return isRegistered;
    }

    public MutableLiveData<Boolean> getNameContainsErrorStatus() {
        return nameContainsErrorStatus;
    }

    public MutableLiveData<Boolean> getUserNameContainsErrorStatus() {
        return userNameContainsErrorStatus;
    }

    public MutableLiveData<Boolean> getPasswordContainsErrorStatus() {
        return passwordContainsErrorStatus;
    }

    public void completedForm(String nameEntered, String usernameEntered, String passwordEntered) {
        name = new Name(nameEntered);
        password = new Password(passwordEntered);
        username = new Username(usernameEntered);
        nameContainsErrorStatus.postValue(!name.validateName());
        userNameContainsErrorStatus.postValue(!username.validateUserName());
        passwordContainsErrorStatus.postValue(!password.validatePassword());
        if (name.isValidName() && username.isValidUserName() && password.isValidPassword()) {
            UserData userData = new UserData();
            userData.setEmail(SingletonEmail.INSTANCE.getEmail());
            userData.setCompleteName(nameEntered);
            SingletonName.setCompleteName(nameEntered);
            userData.setUsername(usernameEntered);
            userData.setPassword(passwordEntered);
            executeServiceRegisterUser(userData);
        }
    }

    public void nameTextChanged() {
        nameContainsErrorStatus.postValue(false);
    }

    public void userNameTextChanged() {
        userNameContainsErrorStatus.postValue(false);
    }

    public void passwordTextChanged() {
        passwordContainsErrorStatus.postValue(false);
    }

    private Observer<ResponseModel<UserData>> responseRegisterUserObserver = new Observer<ResponseModel<UserData>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<UserData> responseModel) {

            if (responseModel != null) {
                if (responseModel.getCode() == SUCCESS_CODE) {
                    getIsRegistered().setValue(SUCCESSFULLY_REGISTERED);
                } else {
                    isLoading.setValue(false);
                    ErrorMessage errorMessage = new ErrorMessage();
                    errorMessage.setKey(responseModel.getErrorMessage().getKey());
                    errorMessage.setMessage(responseModel.getErrorMessage().getMessage());
                    switch (errorMessage.getKey()) {
                        case "error.invalid.name":
                            getIsInvalidName().setValue(true);
                            break;
                        case "error.invalid.username":
                            getIsInvalidUsername().setValue(true);
                            break;
                        case "error.invalid.password":
                            getIsInvalidPassword().setValue(true);
                            break;
                        case "error.resource.username.duplicated":
                            getIsUsernameDuplicated().setValue(true);
                            break;
                        default:
                            getIsErrorMessageForToast().setValue(errorMessage.getMessage());
                            break;
                    }
                }
            } else {
                isLoading.setValue(false);
                getIsErrorMessageForToast().setValue(SERVICE_OR_CONNECTION_ERROR_REGISTER);
            }
        }

    };

    private void executeServiceRegisterUser(UserData userData) {
        isLoading.setValue(true);
        registerUser = repository.postUserRegister(userData);
        registerUser.observeForever(responseRegisterUserObserver);
    }

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (registerUser != null) {
            registerUser.removeObserver(responseRegisterUserObserver);
        }
    }
}