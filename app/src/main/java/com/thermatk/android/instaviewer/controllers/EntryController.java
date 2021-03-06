package com.thermatk.android.instaviewer.controllers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.ControllerChangeHandler;
import com.bluelinelabs.conductor.ControllerChangeType;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.mikepenz.materialdrawer.util.KeyboardUtil;
import com.thermatk.android.instaviewer.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.Unbinder;

import static com.thermatk.android.instaviewer.utils.BuildBundle.createBundleWithString;

public class EntryController extends Controller{
    private Unbinder unbinder;
    @BindView(R.id.username) TextInputEditText mUsernameView;

    private final static String BUNDLE_KEY = "code";
    private String code;

    public EntryController(@Nullable Bundle args) {
        super(args);
    }

    public EntryController(String code) {
        this(createBundleWithString(BUNDLE_KEY,code));
    }

    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_entry, container, false);
        unbinder = ButterKnife.bind(this, view);

        code = getArgs().getString(BUNDLE_KEY);

        return view;
    }



    @Override
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
        unbinder.unbind();
        unbinder = null;
    }

    @OnEditorAction(R.id.username)
    protected boolean editorAction(int actionId) {
        if (actionId == R.id.searchuser || actionId == EditorInfo.IME_NULL) {
            goToUser();
        }
        return false;
    }

    @OnClick(R.id.go_button)
    protected void goToUser() {

        // Reset errors.
        mUsernameView.setError(null);

        // Store values at the time of the login attempt.
        final String username = mUsernameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError("Empty???");
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError("Wrong");
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            KeyboardUtil.hideKeyboard(getActivity());
            getRouter().pushController(
                    RouterTransaction.with(new PhotosListController(username))
                            .pushChangeHandler(new FadeChangeHandler())
                            .popChangeHandler(new FadeChangeHandler()));
        }
    }

    private boolean isUsernameValid(String username) {
        //TODO: Do the logic
        return true;
    }

    @Override
    protected void onChangeStarted(@NonNull ControllerChangeHandler changeHandler, @NonNull ControllerChangeType changeType) {
        setOptionsMenuHidden(!changeType.isEnter);

        if (changeType.isEnter) {
            Log.d("katyagram", "first opening");
        }
    }
}
