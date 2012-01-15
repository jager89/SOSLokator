package edu.feri.jager.SOSLokator.services;

import edu.feri.jager.SOSLokator.MainActivity;
import edu.feri.jager.SOSLokator.SOSSearchActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class MyAsyncTask extends AsyncTask<String, Void, String> {
	private static final String NEW_LINE = System.getProperty("line.separator");

	private Context context = null;
	private ProgressDialog progressDialog = null;

	public MyAsyncTask(Context context, ProgressDialog progressDialog){
		this.context = context;
		this.progressDialog = progressDialog;
	}

	protected void onPreExecute() {
		progressDialog = ProgressDialog.show(context, "", "OBDELAVA PODATKOV!" + NEW_LINE + "Prosimo poèakajte...", true);
	}
	protected String doInBackground(String... param) {
		try{
			if(context instanceof MainActivity) {
				((MainActivity) context).getMapView();
			}
			else if(context instanceof SOSSearchActivity) {
				((SOSSearchActivity) context).getMapView();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	protected void onPostExecute(String rezultat) {
		//		TextView textViewJ48 = (TextView)findViewById(R.id.textViewJ48);
		//		TextView textViewIBk = (TextView)findViewById(R.id.textViewIBk);
		//		TextView textViewNativeBayes = (TextView)findViewById(R.id.textViewNativeBayes);
		//		TextView textViewResult = (TextView)findViewById(R.id.textViewResult);
		//		HashMap<String, String> hashMap = WekaProjectActivity.this.hashtable;
		//		textViewJ48.setText(hashMap.get(CLASSIFIER_J48));
		//		textViewIBk.setText(hashMap.get(CLASSIFIER_IBk));
		//		textViewNativeBayes.setText(hashMap.get(CLASSIFIER_NATIVE_BAYES));
		//
		//		String clasifier = new String(CLASSIFIER_J48);
		//		double percent = getPercentage(hashMap.get(CLASSIFIER_J48));
		//		double maxPercent = getPercentage(hashMap.get(CLASSIFIER_IBk));
		//
		//		maxPercent = Math.max(percent, maxPercent);
		//		if(percent != maxPercent) {
		//			clasifier = new String(CLASSIFIER_IBk);
		//		}
		//
		//		percent = getPercentage(hashMap.get(CLASSIFIER_NATIVE_BAYES));
		//		maxPercent = Math.max(percent, maxPercent);
		//		if(maxPercent == percent) {
		//			clasifier = new String(CLASSIFIER_NATIVE_BAYES);
		//		}
		//		textViewResult.setText("Najuspešnejši klasifikator je, z " + maxPercent + "% pravilnih klasifikacij, klasifikator " + clasifier);
		//
		progressDialog.cancel();
	}
}
