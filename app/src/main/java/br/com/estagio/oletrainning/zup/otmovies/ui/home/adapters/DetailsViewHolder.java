package br.com.estagio.oletrainning.zup.otmovies.ui.home.adapters;

import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.model.MovieDetailsModel;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmResponse;

public class DetailsViewHolder extends RecyclerView.ViewHolder{

    TextView textViewTitle;
    TextView textViewKeywords;
    TextView textViewYear;
    TextView textViewCountries;
    TextView textViewRuntime;
    TextView textViewPoints;
    TextView textViewDirector;
    TextView textViewWriter;
    TextView textViewOverview;
    RecyclerView recyclerViewDetails;
    ProgressBar progressBar;
    ImageView imageViewBanner;
    ImageView imageViewPoster;
    FrameLayout frameLayout;
    CardView cardViewPoster;
    CardView cardViewBanner;
    CheckBox checkBox;

    public DetailsViewHolder(View view, final FilmAdapterDetailsList.OnCheckBoxClickListener onCheckBoxClickListener,
    final PagedList<FilmResponse> currentList) {
        super(view);
        frameLayout = view.findViewById(R.id.loading_layout);
        textViewTitle = view.findViewById(R.id.text_title_details);
        textViewKeywords = view.findViewById(R.id.textView_keywords_details);
        textViewYear = view.findViewById(R.id.textView_year_details);
        textViewCountries = view.findViewById(R.id.textView_countries_details);
        textViewRuntime = view.findViewById(R.id.textView_runtime_details);
        textViewPoints = view.findViewById(R.id.textView_points_details);
        textViewDirector = view.findViewById(R.id.textView_diretor_details);
        textViewWriter = view.findViewById(R.id.textView_writer_details);
        textViewOverview = view.findViewById(R.id.textView_overview_details);
        recyclerViewDetails = view.findViewById(R.id.recycler_films);
        progressBar = view.findViewById(R.id.progress_bar);
        cardViewBanner = view.findViewById(R.id.cardview_banner_details);
        cardViewPoster = view.findViewById(R.id.cardview_poster_details);
        imageViewBanner = view.findViewById(R.id.imageView_banner_details);
        imageViewPoster = view.findViewById(R.id.imageView_poster_details);
        checkBox = view.findViewById(R.id.checkbox_favorite_details);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCheckBoxClickListener != null) {
                    int position = getAdapterPosition();
                    if(checkBox.isChecked()){
                        onCheckBoxClickListener.OnCheckBoxClick(position,currentList,true);
                    } else {
                        onCheckBoxClickListener.OnCheckBoxClick(position, currentList,false);
                    }
                }
            }
        });
    }

    private String sentenceBuilder(@NonNull List<String> listString) {
        StringBuilder keywordList = new StringBuilder();
        for (int i = 0; i < listString.size(); i++) {
            keywordList.append(listString.get(i));
            if (i < listString.size() - 1) {
                keywordList.append(", ");
            }
        }
        Log.d("KEYWORDS", keywordList.toString());
        return keywordList.toString();
    }

    public void setMovieDetailsInformation(MovieDetailsModel movieDetailsModel){
        this.textViewOverview.setText(movieDetailsModel.getOverview());
        this.textViewWriter.setText(sentenceBuilder(movieDetailsModel.getWriters()));
        this.textViewDirector.setText(sentenceBuilder(movieDetailsModel.getDirectors()));
        this.textViewKeywords.setText(sentenceBuilder(movieDetailsModel.getGenreNames()));
        this.textViewPoints.setText(String.valueOf(movieDetailsModel.getVoteAverage()));
        this.textViewRuntime.setText(movieDetailsModel.getRuntime());
        this.textViewCountries.setText(sentenceBuilder(movieDetailsModel.getCountries()));
        this.textViewYear.setText(String.valueOf(movieDetailsModel.getYear()));
        this.textViewTitle.setText(movieDetailsModel.getTitle());
        if(movieDetailsModel.isFavorit()){
            this.checkBox.isChecked();
        }
        if(movieDetailsModel.getPosterId() == null || movieDetailsModel.getPosterId().isEmpty()){
            this.cardViewPoster.setVisibility(View.INVISIBLE);
        } else {
            this.cardViewPoster.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load("https://ole.dev.gateway.zup.me/client-training/v1/movies/"+movieDetailsModel.getPosterId()
                            +"/image/w342?gw-app-key=593c3280aedd01364c73000d3ac06d76")
                    .into(this.imageViewPoster);
        }
        if(movieDetailsModel.getBannerId() == null || movieDetailsModel.getBannerId().isEmpty()){
            this.cardViewBanner.setVisibility(View.INVISIBLE);
        } else {
            this.cardViewBanner.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load("https://ole.dev.gateway.zup.me/client-training/v1/movies/"+movieDetailsModel.getBannerId()
                            +"/image/w1280?gw-app-key=593c3280aedd01364c73000d3ac06d76")
                    .into(this.imageViewBanner);
        }
    }
}
