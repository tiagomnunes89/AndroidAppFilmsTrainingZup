package br.com.estagio.oletrainning.zup.otmovies.ui.home.fragments.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdsmdg.tastytoast.TastyToast;

import br.com.estagio.oletrainning.zup.otmovies.ui.BaseFragment;
import br.com.estagio.oletrainning.zup.otmovies.ui.home.adapters.FragmentStateAdapter;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmGenres;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonAlertDialogSession;

public class HomeFragment extends BaseFragment {

    private HomeFragmentViewHolder viewHolder;
    private HomeFragmentViewModel viewModelHome;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = this.getLayoutInflater().inflate(R.layout.fragment_home, container, false);
        this.viewHolder = new HomeFragmentViewHolder(view);

        viewModelHome = ViewModelProviders.of(this).get(HomeFragmentViewModel.class);

        viewModelHome.getFragmentTellerIsSessionExpired().observe(this,sessionObserver);

        viewModelHome.executeServiceGetGenreList();

        if(SingletonAlertDialogSession.INSTANCE.getAlertDialogBuilder() != null){
            SingletonAlertDialogSession.INSTANCE.getAlertDialogBuilder().show();
        }

        setupObservers();

        return view;
    }

    private void setupObservers() {
        viewModelHome.getThereIsAGenreList().observe(this, genresObserver);
        viewModelHome.getIsLoading().observe(this,progressBarObserver);
        viewModelHome.getIsErrorMessageForToast().observe(this,isErrorMessageForToastObserver);
    }

    private Observer<Boolean> sessionObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isSessionExpired) {
            if(SingletonAlertDialogSession.INSTANCE.getAlertDialogBuilder() == null){
                SingletonAlertDialogSession.createAlertDialogBuilder(getActivity());
                SingletonAlertDialogSession.INSTANCE.getAlertDialogBuilder().create().setCanceledOnTouchOutside(false);
                SingletonAlertDialogSession.INSTANCE.getAlertDialogBuilder().show();
            }
        }
    };

    private Observer<String> isErrorMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(getActivity(),message, TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    .setGravity(Gravity.CENTER,0,700);
        }
    };

    private Observer<FilmGenres> genresObserver = new Observer<FilmGenres>() {
        @Override
        public void onChanged(@Nullable FilmGenres filmGenres) {
            FragmentStatePagerAdapter fragmentStatePagerAdapter =
                    new FragmentStateAdapter(getFragmentManager(),
                            viewModelHome.changeOrderGenres(filmGenres));

            viewHolder.viewPager.setAdapter(fragmentStatePagerAdapter);

            viewHolder.tabLayout.setupWithViewPager(viewHolder.viewPager);
        }
    };

    private Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isLoading) {
            loadingExecutor(isLoading,
                    viewHolder.progressBar,
                    viewHolder.frameLayout);
        }
    };

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModelHome.removeObserver();
    }
}
