package br.com.estagio.oletrainning.zup.otmovies.ui.home.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import br.com.estagio.oletrainning.zup.otmovies.R;

public class LoadingViewHolder extends RecyclerView.ViewHolder{

    FrameLayout frameLayoutLoad;

    public LoadingViewHolder(View view) {
        super(view);
        frameLayoutLoad = view.findViewById(R.id.loading_layout_list);
    }
}
