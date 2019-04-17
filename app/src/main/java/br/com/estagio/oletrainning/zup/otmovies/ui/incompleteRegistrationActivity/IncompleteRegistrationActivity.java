package br.com.estagio.oletrainning.zup.otmovies.ui.incompleteRegistrationActivity;

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


public class IncompleteRegistrationActivity extends BaseActivity {

    private IncompleteRegistrationViewHolder incompleteRegistrationViewHolder;
    private IncompleteRegistrationViewModel incompleteRegistrationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_finish_your_registration, null);
        this.incompleteRegistrationViewHolder = new IncompleteRegistrationViewHolder(view);
        setContentView(view);

        incompleteRegistrationViewModel = ViewModelProviders.of(this).get(IncompleteRegistrationViewModel.class);

        if(SingletonEmail.INSTANCE.getEmail() == null){
            Intent intent = new Intent(this, PreLoginActivity.class);
            startActivity(intent);
        }

        incompleteRegistrationViewHolder.textViewEmail.setText(SingletonEmail.INSTANCE.getEmail());

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
        incompleteRegistrationViewHolder.button.setOnClickListener(buttonOnClickListener);
        incompleteRegistrationViewHolder.imageView.setOnClickListener(backArrowOnClickListener);
        incompleteRegistrationViewHolder.errorEditText.getEditText().addTextChangedListener(errorEditTextTextWatcher);
        incompleteRegistrationViewHolder.textViewReSend.setOnClickListener(textViewOnClickListener);
    }

    private void setupObservers() {
        incompleteRegistrationViewModel.getTokenContainsErrorStatus().observe(this, tokenErrorStatusObserver);
        incompleteRegistrationViewModel.getIsLoading().observe(this, progressBarObserver);
        incompleteRegistrationViewModel.getIsErrorMessageForToast().observe(this,isErrorMessageForToastObserver);
        incompleteRegistrationViewModel.getIsValidatedToken().observe(this,isValidatedTokenObserver);
        incompleteRegistrationViewModel.getMessageErrorChanged().observe(this,messageErrorChangedObserver);
        incompleteRegistrationViewModel.getIsMessageSuccessForToast().observe(this,isMessageSuccessForToastObserver);
    }

    private Observer<String> messageErrorChangedObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String message) {
            incompleteRegistrationViewHolder.errorEditText.setMessageError(message);
            incompleteRegistrationViewHolder.errorEditText.setErrorVisibility(true);
        }
    };

    private Observer<String> isValidatedTokenObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(getApplicationContext(),message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS)
                    .setGravity(Gravity.CENTER,0,500);
            Intent intent = new Intent(IncompleteRegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    };

    private Observer<String> isErrorMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(getApplicationContext(),message, TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    .setGravity(Gravity.CENTER,0,500);
        }
    };

    private Observer<String> isMessageSuccessForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(getApplicationContext(),message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS)
                    .setGravity(Gravity.CENTER,0,500);
        }
    };

    private Observer<Boolean> tokenErrorStatusObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean containsErrorStatus) {
            if (containsErrorStatus != null) {
                incompleteRegistrationViewHolder.errorEditText.setErrorVisibility(containsErrorStatus);
            }
        }

    };

    private Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isLoading) {
            loadingExecutor(isLoading,
                    incompleteRegistrationViewHolder.progressBar,
                    incompleteRegistrationViewHolder.frameLayout,
                    incompleteRegistrationViewHolder.button);
        }
    };

    View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboardFrom(IncompleteRegistrationActivity.this,
                    incompleteRegistrationViewHolder.errorEditText);
            String code = incompleteRegistrationViewHolder.errorEditText.getEditText().getText().toString().trim();
            incompleteRegistrationViewModel.tokenEntered(code);
        }
    };

    View.OnClickListener textViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            incompleteRegistrationViewModel.tokenForwardingRequested();
        }
    };

    View.OnClickListener backArrowOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.imageView_backArrow) {
                Intent intent = new Intent(IncompleteRegistrationActivity.this, PreLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    };

    TextWatcher errorEditTextTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            incompleteRegistrationViewModel.tokenTextChanged();
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
        incompleteRegistrationViewModel.removeObserver();
    }
}