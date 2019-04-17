package br.com.estagio.oletrainning.zup.otmovies.server.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import br.com.estagio.oletrainning.zup.otmovies.model.ErrorMessage;
import br.com.estagio.oletrainning.zup.otmovies.model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.server.remote.UserServices;
import br.com.estagio.oletrainning.zup.otmovies.server.remote.RetrofitServiceBuilder;
import br.com.estagio.oletrainning.zup.otmovies.model.UserData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository  extends CommonRepository{

    private UserServices userServices;

    public UserRepository(){
        userServices= RetrofitServiceBuilder.buildService(UserServices.class);
    }

    public LiveData<ResponseModel<UserData>> getUserData(String email) {
        final MutableLiveData<ResponseModel<UserData>> data = new MutableLiveData<>();
        userServices.getUsersDate(email)
                .enqueue(new Callback<UserData>() {
                    @Override
                    public void onResponse(Call<UserData> call, Response<UserData> response) {
                        ResponseModel<UserData> responseModel = new ResponseModel<>();
                        if(response.code() == SUCCESS_CODE){
                            responseModel.setResponse(response.body());
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
                    public void onFailure(Call<UserData> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    public LiveData<ResponseModel<UserData>> postUserRegister (UserData userData) {
        final MutableLiveData<ResponseModel<UserData>> data = new MutableLiveData<>();
        userServices.userRegister(userData)
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
}
