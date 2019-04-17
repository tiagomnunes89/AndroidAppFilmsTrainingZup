package br.com.estagio.oletrainning.zup.otmovies.ui.home.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.estagio.oletrainning.zup.otmovies.ui.home.fragments.movieList.MovieListFragment;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmGenres;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonGenreID;

import static java.lang.String.valueOf;

public class FragmentStateAdapter extends FragmentStatePagerAdapter {

    private FilmGenres genre;

    public FragmentStateAdapter(FragmentManager fm, FilmGenres genre) {
        super(fm);
        this.genre = genre;
    }

    @Override
    public Fragment getItem(int position) {
        SingletonGenreID.setGenreIDEntered(findGenreID(position));
        return MovieListFragment.newInstance();
    }

    @Override
    public int getCount() {
        return this.genre.getGenres().size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return genre.getGenres().get(position).getName();
    }

    private String findGenreID (int position){
        return valueOf(genre.getGenres().get(position).getId());
    }
}
