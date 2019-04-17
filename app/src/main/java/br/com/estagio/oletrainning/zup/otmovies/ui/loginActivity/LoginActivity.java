package br.com.estagio.oletrainning.zup.otmovies.ui.loginActivity;

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
import br.com.estagio.oletrainning.zup.otmovies.ui.home.homeActivity.HomeActivity;
import br.com.estagio.oletrainning.zup.otmovies.ui.recoverPasswordActivity.RecoverPasswordActivity;
import br.com.estagio.oletrainning.zup.otmovies.ui.preLoginActivity.PreLoginActivity;
import br.com.estagio.oletrainning.zup.otmovies.R;
import br.com.estagio.oletrainning.zup.otmovies.ui.singleton.SingletonEmail;


public class LoginActivity extends BaseActivity {

    private LoginViewHolder loginViewHolder;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_login, null);
        this.loginViewHolder = new LoginViewHolder(view);
        setContentView(view);

        if(SingletonEmail.INSTANCE.getEmail() == null){
            Intent intent = new Intent(LoginActivity.this, PreLoginActivity.class);
            startActivity(intent);
        }

        loginViewHolder.textViewEmailEntered.setText(SingletonEmail.INSTANCE.getEmail());

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        setupObservers();

        hideKeyword(getWindow());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        colorStatusBarBackground();
        setupListeners();
    }

    private void colorStatusBarBackground(){
        colorStatusBar(this.getWindow(),R.color.colorBackground,true);
    }

    private void colorStatusBarRed(){
        colorStatusBar(this.getWindow(),R.color.colorRed,false);
    }

    private void setupObservers() {
        loginViewModel.getPasswordContainsErrorStatus().observe(this, passwordContainsErrorObserver);
        loginViewModel.getIsLoading().observe(this, progressBarObserver);
        loginViewModel.getIsValidatedPassword().observe(this,isValidatedPasswordObserver);
        loginViewModel.getMessageErrorChanged().observe(this,messageErrorChangedObserver);
        loginViewModel.getIsErrorMessageForToast().observe(this,isErrorMessageForToastObserver);
        loginViewModel.getForwardedToken().observe(this,forwardedTokenObserver);
    }

    private void setupListeners() {
        loginViewHolder.buttonSignIn.setOnClickListener(buttonSignInOnClickListener);
        loginViewHolder.imageViewBackArrow.setOnClickListener(backArrowOnClickListener);
        loginViewHolder.buttonSignIn.setOnClickListener(buttonSignInOnClickListener);
        loginViewHolder.errorEditTextPassword.getEditText().addTextChangedListener(editTextPasswordTextChangedListener);
        loginViewHolder.textViewForgetPassword.setOnClickListener(textViewForgetPasswordOnClickListener);
    }



    private Observer<String> forwardedTokenObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String message) {
            TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.INFO)
                    .setGravity(Gravity.CENTER,0,500);
            Intent intent = new Intent(LoginActivity.this, RecoverPasswordActivity.class);
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

    private Observer<String> messageErrorChangedObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            loginViewHolder.textViewRedToast.setText(message);
            loginViewModel.setPasswordContainsErrorStatus(true);
        }
    };

    private Observer<String> isValidatedPasswordObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            loginViewModel.setPasswordContainsErrorStatus(false);
            TastyToast.makeText(getApplicationContext(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS)
                    .setGravity(Gravity.CENTER,0,600);
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    };

    private Observer<Boolean> passwordContainsErrorObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean containsErrorStatus) {
            if (containsErrorStatus != null) {
                loginViewHolder.errorEditTextPassword.setErrorVisibility(containsErrorStatus);
                loginViewHolder.errorEditTextPassword.setMessageError("");
                if (loginViewModel.getPasswordContainsErrorStatus().getValue() != null) {
                    if (loginViewModel.getPasswordContainsErrorStatus().getValue()) {
                        loginViewHolder.linearLayout.setVisibility(View.VISIBLE);
                        colorStatusBarRed();
                    } else {
                        loginViewHolder.linearLayout.setVisibility(View.GONE);
                        colorStatusBarBackground();
                    }
                }
            }
        }
    };

    private Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isLoading) {
            loadingExecutor(isLoading,
                    loginViewHolder.progressBar,
                    loginViewHolder.frameLayout,
                    loginViewHolder.buttonSignIn);
        }
    };


    private View.OnClickListener buttonSignInOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboardFrom(LoginActivity.this,
                    loginViewHolder.errorEditTextPassword);
            String password = loginViewHolder.errorEditTextPassword.getText().toString().trim();
            loginViewModel.passwordEntered(password);
        }
    };

    private View.OnClickListener backArrowOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.imageView_backArrow) {
                Intent intent = new Intent(LoginActivity.this, PreLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    };

    private View.OnClickListener textViewForgetPasswordOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loginViewModel.tokenForwardingRequested();
        }
    };

    private TextWatcher editTextPasswordTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            loginViewModel.passwordTextChanged();
        }

        @Override
        public void afterTextChanged(Editable s) {

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
        loginViewModel.removeObserver();
    }
}