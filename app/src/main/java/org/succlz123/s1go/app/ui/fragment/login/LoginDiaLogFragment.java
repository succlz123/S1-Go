package org.succlz123.s1go.app.ui.fragment.login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.succlz123.s1go.app.R;
import org.succlz123.s1go.app.support.asynctask.LoginAsyncTask;

/**
 * Created by fashi on 2015/4/12.
 */
public class LoginDiaLogFragment extends DialogFragment {
	private View mView;
	private EditText mUsername;
	private EditText mPassword;
	private LoginAsyncTask mLoginAsyncTask;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		initViews();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(mView)
				.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String un = mUsername.getText().toString();
						String pw = mPassword.getText().toString();
						if (!un.isEmpty() && !pw.isEmpty()) {
							mLoginAsyncTask = new LoginAsyncTask(un, pw);
							mLoginAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						} else if (un.isEmpty() || pw.isEmpty()) {
							Toast.makeText(getActivity(), getString(R.string.please_input_un_pw), Toast.LENGTH_SHORT).show();
						}
					}
				})
				.setNegativeButton(getString(R.string.cancel), null);
		return builder.create();
	}

	private void initViews() {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mView = inflater.inflate(R.layout.logindialogfragment, null);
		mUsername = (EditText) mView.findViewById(R.id.login_username);
		mPassword = (EditText) mView.findViewById(R.id.login_password);
	}
}
