package br.com.estagio.oletrainning.zup.otmovies.server.mapper;

import java.util.ArrayList;
import java.util.List;

import br.com.estagio.oletrainning.zup.otmovies.model.Film;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmResponse;

public class FilmMapper {

    public static List<Film> reponseForDomain(List<FilmResponse> listFilmResponse){
        List<Film> filmList = new ArrayList<>();

        for(FilmResponse filmsResponse : listFilmResponse){
            final Film film = new Film(filmsResponse.getId(),filmsResponse.getPosterId(),filmsResponse.getBannerId(),
                    filmsResponse.getVoteAverage(),filmsResponse.getVoteCount(),filmsResponse.getTitle(),
                    filmsResponse.getYear(),filmsResponse.getGenreNames(),filmsResponse.getRuntime(),filmsResponse.getOverview(),
                    filmsResponse.isFavorit(),filmsResponse.getPrice(),filmsResponse.isAcquired());
            filmList.add(film);
        }
        return filmList;
    }
}
