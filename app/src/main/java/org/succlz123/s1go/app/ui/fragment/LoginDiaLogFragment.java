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
import org.succlz123.s1go.app.dao.Api.OkHttpApi;
import org.succlz123.s1go.app.dao.Helper.S1UrlHelper;

import java.util.HashMap;

/**
 * Created by fashi on 2015/4/12.
 */
public class LoginDiaLogFragment extends DialogFragment {
    private EditText username;
    private EditText password;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.logindialogfragment, null);
        username = (EditText) view.findViewById(R.id.login_username);
        password = (EditText) view.findViewById(R.id.login_password);
        builder.setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String un = username.getText().toString();
                        String pw = password.getText().toString();
                        if (!un.isEmpty() && !pw.isEmpty()) {
                            new LoginAsyncTask(un, pw).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else if (un.isEmpty() || pw.isEmpty()) {
                            Toast.makeText(getActivity(), "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("取消", null);

        return builder.create();
    }

    private class LoginAsyncTask extends AsyncTask<Void, Void, Void> {

        private HashMap<String, String> paramss = new HashMap<String, String>();
        private String url;
        private String username;
        private String password;

        public LoginAsyncTask(String username, String password) {
            url = S1UrlHelper.xx;
            this.username = username;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            paramss.put("username", username);
            paramss.put("password", password);
            paramss.put("cookietime", "2592000");
        }

        @Override
        protected Void doInBackground(Void... params) {
            OkHttpApi.getInstance().login(url, paramss);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
