package floo.com.mpm_mandiri.utils;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import floo.com.mpm_mandiri.R;
import floo.com.mpm_mandiri.data.DetailTaskActivity;


public class DialogUniversalWarningUtils {

	private DetailTaskActivity mDialogUniversalWarningActivity;
	private Dialog mDialog;

	private TextView mDialogText;
	private TextView mDialogOKButton;
	private TextView mDialogCancelButton;

	public DialogUniversalWarningUtils(
			DetailTaskActivity mDialogUniversalWarningActivity) {
		this.mDialogUniversalWarningActivity = mDialogUniversalWarningActivity;
	}

	public void showDialog() {
		if (mDialog == null) {
			mDialog = new Dialog(mDialogUniversalWarningActivity,
					R.style.CustomDialogTheme);
		}
		mDialog.setContentView(R.layout.dialog_universal_warning);
		mDialog.setCancelable(true);
		mDialog.show();

		mDialogText = (TextView) mDialog
				.findViewById(R.id.dialog_universal_warning_text);
		mDialogOKButton = (TextView) mDialog
				.findViewById(R.id.dialog_universal_warning_ok);


		initDialogButtons();
	}

	private void initDialogButtons() {

		mDialogOKButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				mDialog.dismiss();
			}
		});


	}

	public void dismissDialog() {
		mDialog.dismiss();
	}
}
