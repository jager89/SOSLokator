package edu.feri.jager.SOSLokator;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class SendSMSActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstance) {
		// other stuff

		// the destination number
		String number = "6508570720";
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
	}

}