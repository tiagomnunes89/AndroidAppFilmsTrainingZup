package br.com.estagio.oletrainning.zup.otmovies.ui.recoverPasswordActivity;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.components.ErrorEditText;
import br.com.estagio.oletrainning.zup.otmovies.R;

public class RecoverPasswordViewHolder {

    ImageView imageView;
    TextView textViewEmail;
    ErrorEditText errorEditTextToken;
    TextView textViewReSendToken;
    ErrorEditText errorEditTextPassword;
    ErrorEditText errorEditTextConfirmPassword;
    Button button;
    ProgressBar progressBar;
    FrameLayout frameLayout;

    RecoverPasswordViewHolder(View view){
        imageView = view.findViewById(R.id.imageView_backArrow);
        textViewEmail = view.findViewById(R.id.textView_emailEntered);
        errorEditTextToken = view.findViewById(R.id.errorEditText_token);
        textViewReSendToken = view.findViewById(R.id.textView_ReSendToken);
        errorEditTextPassword = view.findViewById(R.id.errorEditText_password);
        errorEditTextConfirmPassword = view.findViewById(R.id.errorEditText_confirmPassword);
        button = view.findViewById(R.id.button_changePassword);
        progressBar = view.findViewById(R.id.progress_bar);
        frameLayout = view.findViewById(R.id.loading_layout);
    }
}