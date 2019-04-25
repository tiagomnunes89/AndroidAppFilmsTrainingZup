package br.com.estagio.oletrainning.zup.otmovies.server.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import br.com.estagio.oletrainning.zup.otmovies.model.BodyChangePassword;
import br.com.estagio.oletrainning.zup.otmovies.model.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.server.remote.RetrofitServiceBuilder;
import br.com.estagio.oletrainning.zup.otmovies.server.remote.ValidationServices;
import br.com.estagio.oletrainning.zup.otmovies.model.UserData;

import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonAccessToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ValidationRepository  extends BaseRepository {

    private ValidationServices validationServices;

    public ValidationRepository(){
        validationServices = RetrofitServiceBuilder.buildService(ValidationServices.class);
    }

    public LiveData<ResponseModel<UserData>> resendToken (String email) {
        final MutableLiveData<ResponseModel<UserData>> data = new MutableLiveData<>();
        validationServices.resendToken(email)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        ResponseModel<UserData> responseModel = new ResponseModel<>();
                        if((response.code() == SUCCESS_CODE)) {
                            responseModel.setCode(response.code());
                        } else{
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
                    public void onFailure(Call<Void> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    public LiveData<ResponseModel<UserData>> confirmToken (String email, String code) {
        final MutableLiveData<ResponseModel<UserData>> data = new MutableLiveData<>();
        validationServices.confirmToken(email, code)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        ResponseModel<UserData> responseModel = new ResponseModel<>();
                        if((response.code() == SUCCESS_CODE)) {
                            responseModel.setCode(response.code());
                        } else{
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
                    public void onFailure(Call<Void> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    public LiveData<ResponseModel<UserData>> validateTokenAndChangePass (BodyChangePassword bodyChangePassword) {
        final MutableLiveData<ResponseModel<UserData>> data = new MutableLiveData<>();
        validationServices.validateTokenAndChangePass(bodyChangePassword)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        ResponseModel<UserData> responseModel = new ResponseModel<>();
                        if((response.code() == SUCCESS_CODE)) {
                            responseModel.setCode(response.code());
                        } else{
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
                    public void onFailure(Call<Void> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    public LiveData<ResponseModel<UserData>> passwordValidate (UserData userData) {
        final MutableLiveData<ResponseModel<UserData>> data = new MutableLiveData<>();
        validationServices.passwordValidate(userData)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        SingletonAccessToken.setAccessTokenReceived(response.headers().get("x-access-token"));
                        ResponseModel<UserData> responseModel = new ResponseModel<>();
                        if((response.code() == SUCCESS_CODE)) {
                            responseModel.setCode(response.code());
                        } else{
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
                    public void onFailure(Call<Void> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }
}
