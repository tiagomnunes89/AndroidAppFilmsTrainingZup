package br.com.estagio.oletrainning.zup.otmovies.ui.loginActivity;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.components.ErrorEditText;
import br.com.estagio.oletrainning.zup.otmovies.R;

public class LoginViewHolder {

    ImageView imageViewBackArrow;
    TextView textViewEmailEntered;
    ErrorEditText errorEditTextPassword;
    TextView textViewForgetPassword;
    LinearLayout linearLayout;
    Button buttonSignIn;
    ConstraintLayout constraintLayout;
    ProgressBar progressBar;
    TextView textViewRedToast;
    FrameLayout frameLayout;


    LoginViewHolder(View view) {
        imageViewBackArrow = view.findViewById(R.id.imageView_backArrow);
        textViewEmailEntered = view.findViewById(R.id.textView_emailEntered);
        textViewForgetPassword = view.findViewById(R.id.textView_password);
        errorEditTextPassword = view.findViewById(R.id.errorEditText_login);
        constraintLayout = view.findViewById(R.id.layout_login);
        linearLayout = view.findViewById(R.id.linearLayout_red_toast);
        buttonSignIn = view.findViewById(R.id.button_sign_in);
        progressBar = view.findViewById(R.id.progress_bar);
        textViewRedToast = view.findViewById(R.id.textView_red_toast);
        frameLayout = view.findViewById(R.id.loading_layout);
    }
}