package br.com.estagio.oletrainning.zup.otmovies.ui.home.fragments.movieList;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.sdsmdg.tastytoast.TastyToast;

import br.com.estagio.oletrainning.zup.otmovies.ui.BaseFragment;
import br.com.estagio.oletrainning.zup.otmovies.ui.home.adapters.FilmAdapter;
import br.com.estagio.oletrainning.zup.otmovies.ui.home.movieDetailsActivity.MovieDetailsActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmResponse;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmsResults;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonAlertDialogSession;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonEmail;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonFilmID;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonGenreID;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonTotalResults;

public class MovieListFragment extends BaseFragment {

    private MovieListViewModel movieListViewModel;
    private MovieListViewHolder movieListViewHolder;
    private FilmAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private String GENRES_FILTER = "genres";
    private String FIRST_PAGE = "1";


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        this.movieListViewHolder = new MovieListViewHolder(view);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        if (adapter == null) {
            adapter = new FilmAdapter(getActivity());
        }

        movieListViewModel = ViewModelProviders.of(MovieListFragment.this).get(MovieListViewModel.class);
        movieListViewModel.getFragmentTellerIsSessionExpired().observe(this, sessionObserver);

        if (SingletonGenreID.INSTANCE.getGenreID() != null) {
            movieListViewModel.executeServiceGetFilmResults(FIRST_PAGE, SingletonGenreID.INSTANCE.getGenreID(), GENRES_FILTER);
        }
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        movieListViewModel.getIsLoading().setValue(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupObserversAndListeners();
        setupLayoutManager();
    }

    private void setupLayoutManager() {
        movieListViewHolder.recyclerView.setLayoutManager(linearLayoutManager);
        movieListViewHolder.recyclerView.setHasFixedSize(true);
    }

    private void setupObserversAndListeners() {
        movieListViewModel.getIsMessageSuccessForToast().observe(this, isSuccessMessageForToastObserver);
        movieListViewModel.getIsLoading().observe(this, progressBarObserver);
        movieListViewModel.getFragmentTellerThereIsFilmResults().observe(this, homeTellerThereIsFilmResultsObserver);
        movieListViewModel.getIsErrorMessageForToast().observe(this, isErrorMessageForToastObserver);

    }

    private Observer<String> isSuccessMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(getActivity(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS)
                    .setGravity(Gravity.CENTER, 0, 700);
        }
    };

    private Observer<String> isErrorMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(getActivity(), message, TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    .setGravity(Gravity.CENTER, 0, 700);
        }
    };

    private Observer<Boolean> sessionObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isSessionExpired) {
            if (SingletonAlertDialogSession.INSTANCE.getAlertDialogBuilder() == null) {
                SingletonAlertDialogSession.createAlertDialogBuilder(getActivity());
                SingletonAlertDialogSession.INSTANCE.getAlertDialogBuilder().create().setCanceledOnTouchOutside(false);
                SingletonAlertDialogSession.INSTANCE.getAlertDialogBuilder().show();
            }
        }
    };

    private final Observer<PagedList<FilmResponse>> pagedListObserver = new Observer<PagedList<FilmResponse>>() {
        @Override
        public void onChanged(@Nullable PagedList<FilmResponse> filmResponses) {
            adapter.submitList(filmResponses);
            movieListViewModel.getIsLoading().setValue(false);
        }
    };

    private Observer<FilmsResults> homeTellerThereIsFilmResultsObserver = new Observer<FilmsResults>() {
        @Override
        public void onChanged(final FilmsResults filmsResults) {
            movieListViewModel.getItemPagedList().observe(MovieListFragment.this, pagedListObserver);
            movieListViewHolder.recyclerView.setAdapter(adapter);
            SingletonTotalResults.setTotalResultsEntered(filmsResults.getTotal_results());
            adapter.setOnCheckBoxClickListener(new FilmAdapter.OnCheckBoxClickListener() {
                @Override
                public void OnCheckBoxClick(int position, PagedList<FilmResponse> currentList, Boolean isChecked) {
                    SingletonFilmID.setIDEntered(currentList.get(position).getId());
                    if (isChecked) {
                        movieListViewModel.executeAddFavoriteFilm(SingletonEmail.INSTANCE.getEmail(),
                                String.valueOf(SingletonFilmID.INSTANCE.getID()));
                    } else {
                        movieListViewModel.executeRemoveFavoriteFilm(SingletonEmail.INSTANCE.getEmail(),
                                String.valueOf(SingletonFilmID.INSTANCE.getID()));
                    }
                }
            });
            adapter.setOnItemClickListener(new FilmAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, PagedList<FilmResponse> currentList) {
                    movieListViewModel.getIsLoading().setValue(true);
                    SingletonFilmID.setIDEntered(currentList.get(position).getId());
                    if (SingletonFilmID.INSTANCE.getID() != null) {
                        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    };

    private Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isLoading) {
            loadingExecutor(isLoading,
                    movieListViewHolder.progressBar,
                    movieListViewHolder.frameLayout);
        }
    };

    public static MovieListFragment newInstance() {
        return new MovieListFragment();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        movieListViewModel.removeObserver();
        SingletonAlertDialogSession.INSTANCE.destroyAlertDialogBuilder();
        SingletonFilmID.setIDEntered(null);
    }
}