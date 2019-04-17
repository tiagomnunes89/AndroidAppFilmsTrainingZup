package br.com.estagio.oletrainning.zup.otmovies.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import br.com.estagio.oletrainning.zup.otmovies.R;


public class ErrorEditText extends ConstraintLayout {

    private View view;
    private EditText editText;
    private TextView textView;
    private final int TYPE_CLASS_TEXT = 0x00000001;

    public ErrorEditText(Context context) {
        super(context);
        init(context);
    }

    public EditText getEditText() {
        return editText;
    }

    public ErrorEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.ErrorEditText);

        String hintTextEditText = typedArray.getString(
                R.styleable.ErrorEditText_setHint);
        if (hintTextEditText != null) {
            editText.setHint(hintTextEditText);
        }

        String errorMessageTextView = typedArray.getString(
                R.styleable.ErrorEditText_setMessageError);
        if (errorMessageTextView != null) {
            setMessageError(errorMessageTextView);
        }

        Boolean defaultErrorVisibility = typedArray.getBoolean(R.styleable.ErrorEditText_setInicialErrorVisibility,Boolean.FALSE);
            if(defaultErrorVisibility){
                setErrorVisibility (true);
            } else {
                setErrorVisibility (false);
            }

        Integer setInputType = typedArray.getInt(R.styleable.ErrorEditText_setInputType,TYPE_CLASS_TEXT);
        editText.setInputType(setInputType);

        typedArray.recycle();
    }


    public ErrorEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public Editable getText(){
        return editText.getText();
    }

   private void init(Context context) {
        view = inflate(context,R.layout.component_error_edit_text,this);
        editText = view.findViewById(R.id.editText_ErrorEditText);
        textView = view.findViewById(R.id.textView_ErrorEditText);
    }

    public void setErrorVisibility (boolean visible){
        if(visible){
            editText.setBackground(getResources().getDrawable(R.drawable.border_input_error,null));
            textView.setVisibility(View.VISIBLE);
        } else {
            editText.setBackground(getResources().getDrawable(R.drawable.border_input,null));
            textView.setVisibility(View.INVISIBLE);
        }
    }

    public void setMessageError(String text){
        this.textView.setText(text);
    }
}