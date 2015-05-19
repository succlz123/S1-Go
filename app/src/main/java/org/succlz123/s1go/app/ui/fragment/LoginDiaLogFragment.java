package org.succlz123.s1go.app.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.S1GoApplication;
import org.succlz123.s1go.app.dao.interaction.LoginAsyncTask;

/**
 * Created by fashi on 2015/4/12.
 */
public class LoginDiaLogFragment extends DialogFragment {
    private String mConfirm;
    private String mPlease_input_un_pw;
    private String mCancel;

    private View mView;
    private EditText mUsername;
    private EditText mPassword;
    private LoginAsyncTask mLoginAsyncTask;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initViews();
        initString();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mView)
                .setPositiveButton(mConfirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String un = mUsername.getText().toString();
                        String pw = mPassword.getText().toString();
                        if (!un.isEmpty() && !pw.isEmpty()) {
                            mLoginAsyncTask = new LoginAsyncTask(un, pw);
                            mLoginAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else if (un.isEmpty() || pw.isEmpty()) {
                            Toast.makeText(getActivity(), mPlease_input_un_pw, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(mCancel, null);
        return builder.create();
    }

    private void initViews() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.logindialogfragment, null);
        mUsername = (EditText) mView.findViewById(R.id.login_username);
        mPassword = (EditText) mView.findViewById(R.id.login_password);
    }

    private void initString() {
        mConfirm = S1GoApplication.getInstance().getApplicationContext()
                .getResources().getString(R.string.confirm);
        mPlease_input_un_pw = S1GoApplication.getInstance().getApplicationContext()
                .getResources().getString(R.string.please_input_un_pw);
        mCancel = S1GoApplication.getInstance().getApplicationContext()
                .getResources().getString(R.string.cancel);
    }
}
