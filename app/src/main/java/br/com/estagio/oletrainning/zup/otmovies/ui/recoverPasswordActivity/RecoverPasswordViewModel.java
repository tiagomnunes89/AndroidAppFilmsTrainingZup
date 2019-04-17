package br.com.estagio.oletrainning.zup.otmovies.ui.recoverPasswordActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.ui.BaseViewModel;
import br.com.estagio.oletrainning.zup.otmovies.model.usefulClasses.Password;
import br.com.estagio.oletrainning.zup.otmovies.model.usefulClasses.Token;
import br.com.estagio.oletrainning.zup.otmovies.model.BodyChangePassword;
import br.com.estagio.oletrainning.zup.otmovies.model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.model.UserData;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonEmail;


public class RecoverPasswordViewModel extends BaseViewModel {

    private Password password;
    private Token token;
    private String INVALID_PASSWORD_MISMATCH_KEY = "error.invalid.password.mismatch";
    private String INVALID_PASSWORD_KEY = "error.invalid.password";
    private String UNAUTHORIZED_TOKEN_KEY = "error.unauthorized.token";
    private String ERROR_RESOURCE_TOKEN_KEY = "error.resource.token";
    private String SUCCESS_MESSAGE_CHANGE_PASS = "Senha alterada com sucesso!";
    private String SERVICE_OR_CONNECTION_ERROR_CHANGE_PASSWORD = "Falha ao validar alterar a senha. Verifique a conexão e tente novamente.";

    private MutableLiveData<Boolean> tokenContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> passwordContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<Boolean> confirmPasswordContainsErrorStatus = new MutableLiveData<>();

    private MutableLiveData<String> passwordChanged = new MutableLiveData<>();

    private MutableLiveData<String> isErrorMessageToPasswordInput = new MutableLiveData<>();

    private MutableLiveData<String> isErrorMessageToTokenInput = new MutableLiveData<>();

    private MutableLiveData<Boolean> showPasswordConfirmationInput = new MutableLiveData<>();

    private LiveData<ResponseModel<UserData>> validateTokenAndChangePass;

    public MutableLiveData<Boolean> getShowPasswordConfirmationInput() {
        return showPasswordConfirmationInput;
    }

    public MutableLiveData<String> getIsErrorMessageToTokenInput() {
        return isErrorMessageToTokenInput;
    }

    public MutableLiveData<String> getIsErrorMessageToPasswordInput() {
        return isErrorMessageToPasswordInput;
    }

    public MutableLiveData<String> getPasswordChanged() {
        return passwordChanged;
    }

    public MutableLiveData<Boolean> getTokenContainsErrorStatus() {
        return tokenContainsErrorStatus;
    }

    public MutableLiveData<Boolean> getPasswordContainsErrorStatus() {
        return passwordContainsErrorStatus;
    }

    public MutableLiveData<Boolean> getConfirmPasswordContainsErrorStatus() {
        return confirmPasswordContainsErrorStatus;
    }


    public void showPasswordConfirmationInput (String passwordEntered){
        password = new Password(passwordEntered);
        if(password.isValidPassword()){
            getShowPasswordConfirmationInput().setValue(true);
        } else {
            getShowPasswordConfirmationInput().setValue(false);
        }
    }

    public void completedForm(String code, String passwordEntered, String confirmPasswordEntered){
        token = new Token(code);
        password = new Password(passwordEntered);
        tokenContainsErrorStatus.postValue(!token.validateTokenSize());
        passwordContainsErrorStatus.postValue(!password.validatePassword());
        confirmPasswordContainsErrorStatus.postValue(!password.validatePassword(confirmPasswordEntered));
        if(token.isValidToken()&& password.isValidPassword() && password.isValidConfirmPassword(confirmPasswordEntered)){
            BodyChangePassword bodyChangePassword = new BodyChangePassword();
            bodyChangePassword.setEmail(SingletonEmail.INSTANCE.getEmail());
            bodyChangePassword.setConfirmationToken(code);
            bodyChangePassword.setNewPassword(passwordEntered);
            bodyChangePassword.setNewPasswordConfirmation(confirmPasswordEntered);
            executeServiceValidateTokenAndChangePass(bodyChangePassword);
        }
    }

    public void confirmPasswordTextChanged(){
        confirmPasswordContainsErrorStatus.postValue(false);
    }

    public void tokenTextChanged(){
        tokenContainsErrorStatus.postValue(false);
    }

    public void passwordTextChanged(){
        passwordContainsErrorStatus.postValue(false);
    }

    private boolean isErrorMessageKeyToPasswordInput(String key){
        return (key.equals(INVALID_PASSWORD_MISMATCH_KEY)
                || key.equals(INVALID_PASSWORD_KEY));
    }

    private boolean isErrorMessageKeyToTokenInput(String key){
        return (key.equals(UNAUTHORIZED_TOKEN_KEY)
                || key.equals(ERROR_RESOURCE_TOKEN_KEY));
    }

    private Observer<ResponseModel<UserData>> serviceValidateTokenAndChangePassObserver = new Observer<ResponseModel<UserData>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<UserData> responseModel) {
            if (responseModel != null) {
                if (responseModel.getCode() == SUCCESS_CODE) {
                    getPasswordChanged().setValue(SUCCESS_MESSAGE_CHANGE_PASS);
                } else {
                    isLoading.setValue(false);
                    String key = responseModel.getErrorMessage().getKey();
                    String message = responseModel.getErrorMessage().getMessage();
                    if (isErrorMessageKeyToPasswordInput(key)) {
                        getIsErrorMessageToPasswordInput().setValue(message);
                    } else if (isErrorMessageKeyToTokenInput(key)) {
                        getIsErrorMessageToTokenInput().setValue(message);
                    } else {
                        getIsErrorMessageForToast().setValue(message);
                    }
                }
            } else {
                isLoading.setValue(false);
                getIsErrorMessageForToast().setValue(SERVICE_OR_CONNECTION_ERROR_CHANGE_PASSWORD);
            }
        }

    };

    private void executeServiceValidateTokenAndChangePass(BodyChangePassword bodyChangePassword) {
        isLoading.setValue(true);
        validateTokenAndChangePass = validationRepository.validateTokenAndChangePass(bodyChangePassword);
        validateTokenAndChangePass.observeForever(serviceValidateTokenAndChangePassObserver);
    }

    @Override
    public void removeObserver() {
        if (validateTokenAndChangePass != null && tokenResend != null) {
            validateTokenAndChangePass.removeObserver(serviceValidateTokenAndChangePassObserver);
            tokenResend.removeObserver(tokenResendObserver);
        }
    }
}