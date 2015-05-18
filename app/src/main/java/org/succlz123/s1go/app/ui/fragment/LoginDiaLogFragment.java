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
import org.succlz123.s1go.app.dao.interaction.LoginAsyncTask;

/**
 * Created by fashi on 2015/4/12.
 */
public class LoginDiaLogFragment extends DialogFragment {
    private View mView;
    private EditText username;
    private EditText password;
    private LoginAsyncTask mLoginAsyncTask;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initViews();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String un = username.getText().toString();
                        String pw = password.getText().toString();
                        if (!un.isEmpty() && !pw.isEmpty()) {
                            mLoginAsyncTask = new LoginAsyncTask(un, pw);
                            mLoginAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else if (un.isEmpty() || pw.isEmpty()) {
                            Toast.makeText(getActivity(), "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("取消", null);
        return builder.create();
    }

    private void initViews() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.logindialogfragment, null);
        username = (EditText) mView.findViewById(R.id.login_username);
        password = (EditText) mView.findViewById(R.id.login_password);
    }
}
