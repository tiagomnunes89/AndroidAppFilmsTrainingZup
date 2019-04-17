package br.com.estagio.oletrainning.zup.otmovies.ui.home.fragments.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import br.com.estagio.oletrainning.zup.otmovies.ui.BaseViewModel;
import br.com.estagio.oletrainning.zup.otmovies.model.ResponseModel;
import br.com.estagio.oletrainning.zup.otmovies.server.repositories.FilmRepository;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmGenres;
import br.com.estagio.oletrainning.zup.otmovies.server.response.GenresResponse;

public class HomeFragmentViewModel extends BaseViewModel {

    private String SERVICE_OR_CONNECTION_ERROR = "Falha ao receber lista de gêneros. Verifique a conexão e tente novamente.";

    private FilmRepository filmRepository = new FilmRepository();

    private LiveData<ResponseModel<FilmGenres>> getGenreList;

    private MutableLiveData<FilmGenres> thereIsAGenreList = new MutableLiveData<>();

    private MutableLiveData<Boolean> fragmentTellerIsSessionExpired = new MutableLiveData<>();

    public MutableLiveData<Boolean> getFragmentTellerIsSessionExpired() {
        return fragmentTellerIsSessionExpired;
    }

    public MutableLiveData<FilmGenres> getThereIsAGenreList() {
        return thereIsAGenreList;
    }

    public void executeServiceGetGenreList() {
        isLoading.setValue(true);
        getGenreList = filmRepository.getGenreList();
        getGenreList.observeForever(filmGenresObserver);
    }

    public FilmGenres changeOrderGenres (FilmGenres filmGenres){
        if (filmGenres.getGenres() != null && !(filmGenres.getGenres().get(0).getId() == -1)){
            GenresResponse genresResponse = new GenresResponse(-1, "Lançamentos");
            filmGenres.getGenres().add(0, genresResponse);
            filmGenres.getGenres().remove(filmGenres.getGenres().size() - 1);
        }
        return filmGenres;
    }

    private Observer<ResponseModel<FilmGenres>> filmGenresObserver = new Observer<ResponseModel<FilmGenres>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<FilmGenres> responseFilmGenres) {
            isLoading.setValue(false);
            if (responseFilmGenres != null) {
                if (responseFilmGenres.getCode() == SUCCESS_CODE) {
                    thereIsAGenreList.setValue(responseFilmGenres.getResponse());
                } else if (responseFilmGenres.getCode() == SESSION_EXPIRED_CODE) {
                    fragmentTellerIsSessionExpired.setValue(true);
                }
            } else {
                isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR);
            }
        }
    };

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (getGenreList != null && filmRepository.getThereIsPaginationError() != null)  {
            getGenreList.removeObserver(filmGenresObserver);
        }
    }
}
