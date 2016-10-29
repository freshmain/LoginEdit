package com.lcq.library.loginedit;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Lucas on 2016/10/18.
 */

public class LoginEditText extends RelativeLayout implements View.OnClickListener, View.OnFocusChangeListener {

    public static final int INPUT_TYPE_PHONE = EditorInfo.TYPE_CLASS_PHONE;
    public static final int INPUT_TYPE_PASSWORD_INVISIBLE = EditorInfo.TYPE_TEXT_VARIATION_PASSWORD | EditorInfo.TYPE_CLASS_TEXT;
    public static final int INPUT_TYPE_PASSWORD_VISIBLE = EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
    private static final String PHONE_PATTERN = "^1\\d{10}$";
    private Pattern mPattern = Pattern.compile(PHONE_PATTERN);

    private TextInputLayout mInputLayout;
    private EditText mEditText;
    private ImageButton mImageButton;

    private String mError;
    private int mInputType;

    public LoginEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.edit_layout, this);
        mInputLayout = (TextInputLayout) findViewById(R.id.textInputLayout);
        mEditText = (EditText) findViewById(R.id.editText);
        mImageButton = (ImageButton) findViewById(R.id.imageButton);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LoginEditText);
        String hint = array.getString(R.styleable.LoginEditText_hint);
        int buttonBgId = array.getResourceId(R.styleable.LoginEditText_button_image, -1);
        mError = array.getString(R.styleable.LoginEditText_error);
        mInputType = array.getInt(R.styleable.LoginEditText_input_type, -1);

        mInputLayout.setHint(hint);
        mEditText.setInputType(mInputType);
        mEditText.setOnFocusChangeListener(this);
        mImageButton.setImageResource(buttonBgId);
        mImageButton.setOnClickListener(this);
    }

    public String getText() {
        return mEditText.getText().toString();
    }

    public void setFilters(InputFilter[] filters) {
        mEditText.setFilters(filters);
    }

    public void setEditText(String text) {
        mEditText.setText(text);
    }

    public void setEnable(boolean enable) {
        mEditText.setEnabled(enable);
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        mEditText.addTextChangedListener(textWatcher);
    }

    public boolean isRight(boolean showError) {
        return validate(mInputType, mInputLayout, mEditText, mError, showError);
    }

    /**
     * 检查输入是否符合规则
     *
     * @param inputType
     * @param inputLayout
     * @param editText
     * @param error
     * @return
     */
    private boolean validate(int inputType, TextInputLayout inputLayout, EditText editText, String error, boolean showError) {
        boolean isRight;
        Editable text = editText.getText();
        if (TextUtils.isEmpty(text)) {
            isRight = false;
        } else if (inputType == INPUT_TYPE_PHONE) {
            Matcher matcher = mPattern.matcher(text);
            isRight = matcher.matches();
        } else {
            isRight = text.length() >= 4;
        }
        if (showError && inputLayout != null) {
            inputLayout.setError(isRight ? null : error);
        }
        return isRight;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mImageButton.getId()) {
            if (mInputType == INPUT_TYPE_PHONE) {
                mEditText.setText(null);
            } else if (mInputType == INPUT_TYPE_PASSWORD_INVISIBLE) {
                mImageButton.setImageResource(R.drawable.ic_visibility);
                mEditText.setInputType(INPUT_TYPE_PASSWORD_VISIBLE);
                mEditText.setSelection(mEditText.getText().length());
                mInputType = INPUT_TYPE_PASSWORD_VISIBLE;
            } else if (mInputType == INPUT_TYPE_PASSWORD_VISIBLE) {
                mImageButton.setImageResource(R.drawable.ic_visibility_off);
                mEditText.setInputType(INPUT_TYPE_PASSWORD_INVISIBLE);
                mEditText.setSelection(mEditText.getText().length());
                mInputType = INPUT_TYPE_PASSWORD_INVISIBLE;
            }
            mEditText.requestFocus();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == mEditText.getId()) {
            mImageButton.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
        }
    }
}
