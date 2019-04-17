package br.com.estagio.oletrainning.zup.otmovies.ui.recoverPasswordActivity;

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


public class RecoverPasswordActivity extends BaseActivity {

    private RecoverPasswordViewHolder recoverPasswordViewHolder;
    private RecoverPasswordViewModel recoverPasswordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_inform_token_and_new_password, null);
        this.recoverPasswordViewHolder = new RecoverPasswordViewHolder(view);
        setContentView(view);

        recoverPasswordViewModel = ViewModelProviders.of(this).get(RecoverPasswordViewModel.class);

        if(SingletonEmail.INSTANCE.getEmail() == null){
            Intent intent = new Intent(this, PreLoginActivity.class);
            startActivity(intent);
        }

        recoverPasswordViewHolder.textViewEmail.setText(SingletonEmail.INSTANCE.getEmail());

        setupObservers();

        hideKeyword(getWindow());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        colorStatusBar(this.getWindow(),R.color.colorBackground,true);
        setupListeners();
    }

    private void setupObservers() {
        recoverPasswordViewModel.getIsLoading().observe(this, progressBarObserver);
        recoverPasswordViewModel.getTokenContainsErrorStatus().observe(this, tokenErrorStatusObserver);
        recoverPasswordViewModel.getPasswordContainsErrorStatus().observe(this, passwordContainsErrorObserver);
        recoverPasswordViewModel.getConfirmPasswordContainsErrorStatus().observe(this, confirmPasswordContainsErrorObserver);
        recoverPasswordViewModel.getShowPasswordConfirmationInput().observe(this,showPasswordConfirmationInput);
        recoverPasswordViewModel.getIsErrorMessageForToast().observe(this,isErrorMessageForToastObserver);
        recoverPasswordViewModel.getIsErrorMessageToPasswordInput().observe(this,isErrorMessageToPasswordInputObserver);
        recoverPasswordViewModel.getPasswordChanged().observe(this,passwordChangedObserver);
        recoverPasswordViewModel.getIsErrorMessageToTokenInput().observe(this,isErrorMessageToTokenInputObserver);
        recoverPasswordViewModel.getIsMessageSuccessForToast().observe(this,isMessageSuccessForToastObserver);
    }

    private void setupListeners() {
        recoverPasswordViewHolder.imageView.setOnClickListener(backArrowOnClickListener);
        recoverPasswordViewHolder.errorEditTextToken.getEditText().addTextChangedListener(errorEditTextTextWatcherToken);
        recoverPasswordViewHolder.errorEditTextPassword.getEditText().addTextChangedListener(errorEditTextTextWatcherPassword);
        recoverPasswordViewHolder.errorEditTextConfirmPassword.getEditText().addTextChangedListener(errorEditTextTextWatcherConfirmPassword);
        recoverPasswordViewHolder.textViewReSendToken.setOnClickListener(textViewOnClickListener);
        recoverPasswordViewHolder.button.setOnClickListener(buttonOnClickListener);
    }

    private View.OnClickListener backArrowOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.imageView_backArrow) {
                Intent intent = new Intent(RecoverPasswordActivity.this, PreLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

        }
    };

    private View.OnClickListener textViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            recoverPasswordViewModel.tokenForwardingRequested();
        }
    };

    View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboardFrom(RecoverPasswordActivity.this,
                    recoverPasswordViewHolder.errorEditTextToken);
            String code = recoverPasswordViewHolder.errorEditTextToken.getText().toString().trim();
            String password = recoverPasswordViewHolder.errorEditTextPassword.getText().toString().trim();
            String confirmPassword = recoverPasswordViewHolder.errorEditTextConfirmPassword.getText().toString().trim();
            recoverPasswordViewModel.completedForm(code,password,confirmPassword);
        }
    };

    private Observer<String> isErrorMessageToTokenInputObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String message) {
            recoverPasswordViewHolder.errorEditTextToken.setMessageError(message);
            recoverPasswordViewHolder.errorEditTextToken.setErrorVisibility(true);
        }
    };

    private Observer<String> passwordChangedObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            TastyToast.makeText(getApplicationContext(),getString(R.string.success_message_change_pass), TastyToast.LENGTH_LONG, TastyToast.SUCCESS)
                    .setGravity(Gravity.CENTER,0,600);
            Intent intent = new Intent(RecoverPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    };

    private Observer<String> isErrorMessageToPasswordInputObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String message) {
            recoverPasswordViewHolder.errorEditTextConfirmPassword.setMessageError("");
            recoverPasswordViewHolder.errorEditTextPassword.setMessageError(message);
            recoverPasswordViewHolder.errorEditTextConfirmPassword.setErrorVisibility(true);
            recoverPasswordViewHolder.errorEditTextPassword.setErrorVisibility(true);
        }
    };

    private Observer<String> isErrorMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(getApplicationContext(),message, TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    .setGravity(Gravity.CENTER,0,600);
        }
    };

    private Observer<String> isMessageSuccessForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(getApplicationContext(),message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS)
                    .setGravity(Gravity.CENTER,0,700);
        }
    };

    private Observer<Boolean> showPasswordConfirmationInput = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean show) {
            if(show){
                recoverPasswordViewHolder.errorEditTextConfirmPassword.setErrorVisibility(false);
                recoverPasswordViewHolder.errorEditTextConfirmPassword.setVisibility(View.VISIBLE);
            } else {
                recoverPasswordViewHolder.errorEditTextConfirmPassword.setVisibility(View.INVISIBLE);
            }
        }
    };

    private Observer<Boolean> tokenErrorStatusObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean containsErrorStatus) {
            if (containsErrorStatus != null) {
                recoverPasswordViewHolder.errorEditTextToken.setErrorVisibility(containsErrorStatus);
            }
        }

    };

    private Observer<Boolean> passwordContainsErrorObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean containsErrorStatus) {
            if (containsErrorStatus != null) {
                recoverPasswordViewHolder.errorEditTextPassword.setErrorVisibility(containsErrorStatus);
            }
        }
    };

    private Observer<Boolean> confirmPasswordContainsErrorObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean containsErrorStatus) {
            if (containsErrorStatus != null) {
                recoverPasswordViewHolder.errorEditTextConfirmPassword.setErrorVisibility(containsErrorStatus);
            }
        }
    };

    private Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isLoading) {
            loadingExecutor(isLoading,
                    recoverPasswordViewHolder.progressBar,
                    recoverPasswordViewHolder.frameLayout,
                    recoverPasswordViewHolder.button);
        }
    };

    private TextWatcher errorEditTextTextWatcherToken = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            recoverPasswordViewModel.tokenTextChanged();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher errorEditTextTextWatcherPassword = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String password = recoverPasswordViewHolder.errorEditTextPassword.getText().toString().trim();
            recoverPasswordViewModel.showPasswordConfirmationInput(password);
            recoverPasswordViewModel.passwordTextChanged();
            recoverPasswordViewHolder.errorEditTextConfirmPassword.getEditText().setText("");
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher errorEditTextTextWatcherConfirmPassword = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            recoverPasswordViewModel.confirmPasswordTextChanged();
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
        recoverPasswordViewModel.removeObserver();
    }
}