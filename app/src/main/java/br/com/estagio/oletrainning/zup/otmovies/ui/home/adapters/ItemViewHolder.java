package br.com.estagio.oletrainning.zup.otmovies.ui.home.adapters;

import android.arch.paging.PagedList;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.server.response.FilmResponse;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    private TextView textTitleFilm;
    private ProgressBar progressBar;
    private ImageView imageView;
    private TextView keywords;
    private TextView movieDescription;
    private LinearLayout informations;
    private TextView runtime;
    private TextView year;
    private FrameLayout filmNoteFrameLayout;
    private TextView filmNote;
    private CheckBox checkBox;
    private TextView price;
    private CardView cardViewPoster;

    public ItemViewHolder(View itemView, final FilmAdapter.OnItemClickListener onItemClickListener,
                          final FilmAdapter.OnCheckBoxClickListener onCheckBoxClickListener,
                          final PagedList<FilmResponse> currentList) {
        super(itemView);

        progressBar = itemView.findViewById(R.id.movie_progress);
        imageView = itemView.findViewById(R.id.movie_poster);
        keywords = itemView.findViewById(R.id.textView_keywords);
        movieDescription = itemView.findViewById(R.id.movie_description);
        informations = itemView.findViewById(R.id.linearLayout_runtimeAndYear);
        runtime = informations.findViewById(R.id.textView_runtime);
        year = informations.findViewById(R.id.textView_year);
        filmNoteFrameLayout = itemView.findViewById(R.id.frameLayout_filmNote);
        filmNote = filmNoteFrameLayout.findViewById(R.id.textView_filmNote);
        textTitleFilm = itemView.findViewById(R.id.text_title_film);
        checkBox = itemView.findViewById(R.id.checkbox_favorite);
        price = itemView.findViewById(R.id.textView_price);
        cardViewPoster = itemView.findViewById(R.id.cardview_poster_item);
        checkBox = itemView.findViewById(R.id.checkbox_favorite);
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
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                        onItemClickListener.onItemClick(position, currentList);
                }
            }
        });
    }

    private String sentenceBuilder(List<String> listString) {
        StringBuilder keywordList = new StringBuilder();
        if(listString != null) {
            for (int i = 0; i < listString.size(); i++) {
                keywordList.append(listString.get(i));
                if (i < listString.size() - 1) {
                    keywordList.append(", ");
                }
            }
        }
        Log.d("KEYWORDS", keywordList.toString());
        return keywordList.toString();
    }
    
    public void setFilmResponseInformation(FilmResponse film){
        this.textTitleFilm.setText(film.getTitle());
        if(film.getPosterId() == null || film.getPosterId().isEmpty()){
            this.cardViewPoster.setVisibility(View.INVISIBLE);
        } else {
            this.cardViewPoster.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load("https://ole.dev.gateway.zup.me/client-training/v1/movies/"+film.getPosterId()
                            +"/image/w342?gw-app-key=593c3280aedd01364c73000d3ac06d76")
                    .into(this.imageView);
        }
        this.keywords.setText(this.sentenceBuilder(film.getGenreNames()));
        this.movieDescription.setText(film.getOverview());
        this.runtime.setText(film.getRuntime());
        this.year.setText(String.valueOf(film.getYear()));
        this.filmNote.setText(String.valueOf(film.getVoteAverage()));
        if(film.isFavorit()){
            this.checkBox.setChecked(true);
        }
        Float filmPrice = film.getPrice();
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String priceText = "R$ "+ decimalFormat.format(filmPrice);
        this.price.setText(priceText);
    }
}