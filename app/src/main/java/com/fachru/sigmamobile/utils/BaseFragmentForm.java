package com.fachru.sigmamobile.utils;

import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by fachru on 25/11/15.
 */
public abstract class BaseFragmentForm extends Fragment {

    private static final int BUTTON_STATE_ENABLE = 0;
    private static final int BUTTON_STATE_DISABLE = 1;
    private static final int EDIT_TEXT_STATE_ENABLE = 2;
    private static final int EDIT_TEXT_STATE_DISABLE = 3;
    private static final int EDIT_TEXT_CLEAR = 4;

    protected abstract void actionAdd();

    protected abstract void actionEdit();

    protected abstract void actionUpdate();

    protected abstract void actionDelete();

    protected abstract void initListener();

    protected void enableButton(ViewGroup group) {
        myLoop(group, BUTTON_STATE_ENABLE);
    }

    protected void disableButton(ViewGroup group) {
        myLoop(group, BUTTON_STATE_DISABLE);
    }

    protected void clearForm(ViewGroup group) {
        myLoop(group, EDIT_TEXT_CLEAR);
    }

    protected void disableForm(ViewGroup group) {
        myLoop(group, EDIT_TEXT_STATE_DISABLE);
    }

    protected void enableForm(ViewGroup group) {
        myLoop(group, EDIT_TEXT_STATE_ENABLE);
    }

    private void myLoop(ViewGroup group, int state) {
        for (int i = 0, count = group.getChildCount(); i < count; i++) {
            View view = group.getChildAt(i);
            checkInstant(view, state);
        }
    }

    private void checkInstant(View view, int state) {
        switch (state) {
            case BUTTON_STATE_DISABLE:
                instanceButton(view, false);
                break;
            case BUTTON_STATE_ENABLE:
                instanceButton(view, true);
                break;
            case EDIT_TEXT_STATE_DISABLE:
                instanceEditText(view, false);
                break;
            case EDIT_TEXT_STATE_ENABLE:
                instanceEditText(view, true);
                break;
            case EDIT_TEXT_CLEAR:
                instanceEditText(view);
                break;
            default:
                break;
        }
    }

    private void instanceButton(View view, boolean b) {
        if (view instanceof Button) view.setEnabled(b);

        if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0)) {
            if (b) {
                enableButton((ViewGroup) view);
            } else {
                disableButton((ViewGroup) view);
            }
        }
    }

    private void instanceEditText(View view) {
        if (view instanceof EditText) {
            ((EditText) view).getText().clear();
            ((EditText) view).setGravity(Gravity.LEFT);
        }

        if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
            clearForm((ViewGroup) view);
    }

    private void instanceEditText(View view, boolean b) {
        if (view instanceof EditText) view.setEnabled(b);

        if (view instanceof Spinner) {
            try {
                ((Spinner) view).getSelectedView().setEnabled(b);
                view.setEnabled(b);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }

        if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0)) {
            if (b) {
                enableForm((ViewGroup) view);
            } else {
                disableForm((ViewGroup) view);
            }
        }
    }
}
