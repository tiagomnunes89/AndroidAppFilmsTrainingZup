package br.com.estagio.oletrainning.zup.otmovies.ui.tokenValidationActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;

import com.sdsmdg.tastytoast.TastyToast;

import br.com.estagio.oletrainning.zup.otmovies.ui.BaseActivity;
import br.com.estagio.oletrainning.zup.otmovies.ui.loginActivity.LoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.ui.preLoginActivity.PreLoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonEmail;

public class TokenValidationActivity extends BaseActivity {

    private TokenValidationViewHolder tokenValidationViewHolder;
    private TokenValidationViewModel tokenValidationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_token_validation, null);
        this.tokenValidationViewHolder = new TokenValidationViewHolder(view);
        setContentView(view);

        tokenValidationViewModel = ViewModelProviders.of(this).get(TokenValidationViewModel.class);

        if(SingletonEmail.INSTANCE.getEmail() == null){
            Intent intent = new Intent(this, PreLoginActivity.class);
            startActivity(intent);
        }

        tokenValidationViewHolder.textViewEmail.setText(SingletonEmail.INSTANCE.getEmail());

        setupObservers();

        hideKeyword(getWindow());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        colorStatusBar(this.getWindow(),R.color.colorBackground,true);
        setupListeners();
    }

    private void setupListeners() {
        tokenValidationViewHolder.button.setOnClickListener(buttonOnClickListener);
        tokenValidationViewHolder.imageView.setOnClickListener(imageViewBackArrowOnClickListener);
        tokenValidationViewHolder.errorEditText.getEditText().addTextChangedListener(errorEditTextTextWatcher);
        tokenValidationViewHolder.textViewReSendToken.setOnClickListener(textViewOnClickListener);
    }

    private void setupObservers() {
        tokenValidationViewModel.getTokenContainsErrorStatus().observe(this, tokenErrorStatusObserver);
        tokenValidationViewModel.getIsLoading().observe(this, progressBarObserver);
        tokenValidationViewModel.getIsErrorMessageForToast().observe(this,isErrorMessageForToastObserver);
        tokenValidationViewModel.getIsValidatedToken().observe(this,isValidatedTokenObserver);
        tokenValidationViewModel.getMessageErrorChanged().observe(this,messageErrorChangedObserver);
        tokenValidationViewModel.getIsMessageSuccessForToast().observe(this,isMessageSuccessForToastObserver);
    }

    private Observer<String> messageErrorChangedObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String message) {
            tokenValidationViewHolder.errorEditText.setMessageError(message);
            tokenValidationViewHolder.errorEditText.setErrorVisibility(true);
        }
    };

    private Observer<String> isValidatedTokenObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS)
                    .setGravity(Gravity.CENTER,0,500);
            Intent intent = new Intent(TokenValidationActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    };

    private Observer<String> isErrorMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    .setGravity(Gravity.CENTER,0,500);
        }
    };

    private Observer<String> isMessageSuccessForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(getApplicationContext(),message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS)
                    .setGravity(Gravity.CENTER,0,700);
        }
    };

    private Observer<Boolean> tokenErrorStatusObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean containsErrorStatus) {
            if (containsErrorStatus != null) {
                tokenValidationViewHolder.errorEditText.setErrorVisibility(containsErrorStatus);
            }
        }

    };

    private Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isLoading) {
            loadingExecutor(isLoading,
                    tokenValidationViewHolder.progressBar,
                    tokenValidationViewHolder.frameLayout,
                    tokenValidationViewHolder.button);
        }
    };

    View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboardFrom(TokenValidationActivity.this,
                    tokenValidationViewHolder.errorEditText);
            String code = tokenValidationViewHolder.errorEditText.getEditText().getText().toString().trim();
            tokenValidationViewModel.tokenEntered(code);
        }
    };

    View.OnClickListener textViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tokenValidationViewModel.tokenForwardingRequested();
        }
    };

    TextWatcher errorEditTextTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tokenValidationViewModel.tokenTextChanged();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    View.OnClickListener imageViewBackArrowOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.imageView_backArrow) {
                Intent intent = new Intent(TokenValidationActivity.this, PreLoginActivity.class);
                startActivity(intent);
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), PreLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tokenValidationViewModel.removeObserver();
    }
}