package br.com.estagio.oletrainning.zup.otmovies.server.remote;

import br.com.estagio.oletrainning.zup.otmovies.model.MovieDetailsModel;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmGenres;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmsResults;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FilmService {


    @GET("movies")
    Call<FilmsResults> getMovieGenre(@Query("filter") String filter,
                                     @Query("filter_id") String filterID,
                                     @Query("amount") String amount,
                                     @Query("page") String page);

    @GET("genres")
    Call<FilmGenres> getGenres();

    @GET("movies/{id}/detail")
    Call<MovieDetailsModel> getMovieDetails(@Path("id") int id);

    @GET("users/{email}/favorits")
    Call<FilmsResults> getFavoriteList(@Path("email") String email,@Query("page") Integer page, @Query("amount") String amount);

    @POST("users/{email}/favorits/{movieId}")
    Call<Void> addFavotiteFilm(@Path("email") String email,@Path("movieId") String movieID);

    @DELETE("users/{email}/favorits/{movieId}")
    Call<Void> removeFavotiteFilm(@Path("email") String email,@Path("movieId") String movieID);
}
