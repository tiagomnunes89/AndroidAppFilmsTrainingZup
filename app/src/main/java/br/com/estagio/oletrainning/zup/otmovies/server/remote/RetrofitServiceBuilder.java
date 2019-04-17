package br.com.estagio.oletrainning.zup.otmovies.server.remote;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonAccessToken;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceBuilder {

    private static RetrofitServiceBuilder mInstance;

    private static final String URL = "https://ole.dev.gateway.zup.me/client-training/v1/";

    private static final String KEY_SERVICE_VALIDATION = "593c3280aedd01364c73000d3ac06d76";

    private static HttpLoggingInterceptor logger =
            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);


    private static OkHttpClient.Builder okHttp =
            new OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            HttpUrl url = request.url().newBuilder().addQueryParameter("gw-app-key",KEY_SERVICE_VALIDATION).build();
                            request = request.newBuilder().url(url).build();
                            return chain.proceed(request);
                        }
                    })
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            SingletonAccessToken accessToken = SingletonAccessToken.INSTANCE;
                            if (accessToken.getLastestAuth() != null){
                                String token = "Bearer " + accessToken.getLastestAuth();
                                request = request.newBuilder()
                                        .addHeader("Authorization", token).build();
                                Log.d("tokenReceived","Bearer " + accessToken.getLastestAuth());
                            }
                            return chain.proceed(request);
                        }
                    })
                    .connectTimeout(100,TimeUnit.SECONDS)
                    .readTimeout(300,TimeUnit.SECONDS)
                    .writeTimeout(100,TimeUnit.SECONDS)
                    .callTimeout(100,TimeUnit.SECONDS);

    private static Retrofit getRetroInstance() {
        return new Retrofit.Builder().baseUrl(URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp.build()).build();
    }

    private static Retrofit retrofit = getRetroInstance();

    public static <S> S buildService(Class<S> serviceType){
        return retrofit.create(serviceType);
    }

    public static synchronized RetrofitServiceBuilder getInstance(){
        if(mInstance == null){
            mInstance = new RetrofitServiceBuilder();
        }
        return mInstance;
    }

    public FilmService getApi(){
        return retrofit.create(FilmService.class);
    }
}
