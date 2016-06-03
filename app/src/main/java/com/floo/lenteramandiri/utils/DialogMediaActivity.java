package com.floo.lenteramandiri.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.floo.lenteramandiri.data.TaskDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

import com.floo.lenteramandiri.MainActivity;
import com.floo.lenteramandiri.R;


public class DialogMediaActivity {

	private TaskDetailActivity mDialogMediaActivity;
	private Dialog mDialog;


	private EditText mDialogComment;
	private TextView mOKButton;
	private TextView mCancelButton;
	private ImageView mDialogImage;
	private TextView mName;
	private EditText mComment;
	String idTaskParsing, struserid;

	public static final String status_code = "status_code";
	public static final String message = "message";
	private static final String report = "report";
	private static final String user_id = "user_id";
	String urlDone = DataManager.urltaskDone;
	String strStatus, strMessage;

	public DialogMediaActivity(TaskDetailActivity mDialogMediaActivity, String idTaskParsing, String struserid) {
		this.mDialogMediaActivity = mDialogMediaActivity;
		this.idTaskParsing = idTaskParsing;
		this.struserid = struserid;
	}

	public void showDialog() {
		if (mDialog == null) {
			mDialog = new Dialog(mDialogMediaActivity,
					R.style.CustomDialogTheme);
		}
		mDialog.setContentView(R.layout.dialog_media);
		mDialog.show();
		
		mOKButton = (TextView) mDialog.findViewById(R.id.dialog_media_ok);
		mCancelButton = (TextView) mDialog.findViewById(R.id.dialog_media_cancel);
		mName = (TextView) mDialog.findViewById(R.id.dialog_media_ok);
		mComment = (EditText) mDialog.findViewById(R.id.dialog_media_comment);
		mDialogComment = (EditText) mDialog.findViewById(R.id.dialog_media_comment);
		//Log.d("idparsing", idTaskParsing);
		//Log.d("user", struserid);
		
		//ImageUtil.displayRoundImage(mDialogImage, "http://pengaja.com/uiapptemplate/newphotos/profileimages/0.jpg", null);

		
		initDialogButtons();
	}

	private void initDialogButtons() {

		mOKButton.setOnClickListener(new OnClickListener() {
			String isicomment = "";
			@Override
			public void onClick(View view) {
				isicomment = mDialogComment.getText().toString();
				new AlertDialog.Builder(mDialogMediaActivity)
						.setTitle("Confirmation")
						.setMessage("Do you want to finish this task?")
						.setNegativeButton(android.R.string.no, null)
						.setPositiveButton(android.R.string.yes,
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface arg0,
														int arg1) {

										//Log.d("idTaskParsing", idTaskParsing);
										//Log.d("struserid", struserid);
										//Log.d("contentisi3", isicemment);
										//Log.d("contentisi2", mComment.getText().toString());
										new DoneAsync(idTaskParsing, struserid, isicomment).execute();
										Intent back=new Intent(mDialogMediaActivity, MainActivity.class);
										back.putExtra("fragment", "fragment");
										mDialogMediaActivity.startActivity(back);
										//mDialogMediaActivity.finish();



									}
								}).create().show();

				//Toast.makeText(mDialogMediaActivity, "comment: " + mDialogComment.getText(), Toast.LENGTH_SHORT).show();
			}
		});

		mCancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				mDialog.dismiss();
			}
		});
	}

	public void dismissDialog() {
		mDialog.dismiss();
	}

	class DoneAsync extends AsyncTask<Void, Void, Void> {

		private String idTaskParsing;
		private String userid;
		private String strReport;


		public DoneAsync(String idTaskParsing, String userid, String strReport) {
			this.idTaskParsing = idTaskParsing;
			this.userid = userid;
			this.strReport = strReport;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			JSONObject objReport = new JSONObject();
			String objstrReport = "";

			try {
				objReport.put(user_id, userid);
				objReport.put(report, strReport);
				objstrReport = objReport.toString();

				JSONObject jsonObject = new JSONObject(DataManager.MyHttpPut(urlDone+idTaskParsing, objstrReport));
				strStatus = jsonObject.getString(status_code);
				strMessage = jsonObject.getString(message);


			} catch (JSONException e) {
				e.printStackTrace();
			}


			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (strStatus.trim().equals("200")){
				Toast.makeText(mDialogMediaActivity, strMessage, Toast.LENGTH_LONG).show();
				mDialogMediaActivity.finish();
				//Intent back = new Intent(TaskDetailActivity.this, MainActivity.class);
				//startActivity(back);


			}else {
				Toast.makeText(mDialogMediaActivity, strMessage, Toast.LENGTH_LONG).show();
			}

		}
	}
}
