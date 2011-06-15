package edu.feri.jager.SOSLokator;

import java.util.Vector;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ContactsListActivity extends ListActivity implements OnItemClickListener{
	private static final int CONTACTS_REQ = 123;
	MainApplication mainApp;
	Menu mMenu;
	View lastSelectedView = null;
	int lastSelectedItem;
	Drawable selectedBackgrund;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mainApp = (MainApplication) getApplication();

		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getList()));

		lastSelectedItem = -1;

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		mMenu = menu;
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.contacts_list_menu, mMenu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.delete:
			System.out.println("DELETE BUTTON");
			if(lastSelectedItem != -1 && mainApp.getVecContactsID().size() > 0) {

				new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Brisasnje kontakta")
				.setMessage("Ali ste preprièani, da želite odstraniti kontakt?")
				.setPositiveButton("Da", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						mainApp.remove(mainApp.getVecContactsID().get(lastSelectedItem).getId());
						mainApp.getVecContactsID().remove(lastSelectedItem);
						lastSelectedItem = -1;
						ContactsListActivity.this.setListAdapter(new ArrayAdapter<String>(ContactsListActivity.this, android.R.layout.simple_list_item_1, getList()));  
					}

				})
				.setNegativeButton("Ne", null)
				.show();


			}

			return true;
		case R.id.add:

			System.out.println("ADD BUTTON");
			Intent intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
			startActivityForResult(intent, CONTACTS_REQ);

			return true;
		}
		return false;
	}

	private String[] getList() {
		Vector<MyContacts> vec = mainApp.getVecContactsID();

		if(vec.size() == 0)
			return new String[]{"Seznam prejemnikov je prazen, dodajte prejemnika!"};

		String[] str = new String[vec.size()];
		for(int i = 0; i < vec.size(); i++) {
			Cursor phones = getContentResolver().query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + " = " + vec.get(i).getContactID(), null, null); 
			if(phones.moveToFirst()) {
				str[i] = phones.getString(phones.getColumnIndex(Phone.DISPLAY_NAME));    
			}
			phones.close();
		}

		return str;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		System.out.println("CLICK");
		if(mainApp.getVecContactsID().size() == 0) {
			Intent intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
			startActivityForResult(intent, CONTACTS_REQ);
		}
		else {	
			if (lastSelectedView != null) {
				lastSelectedView.setBackgroundColor(Color.BLACK);
			}
			lastSelectedItem = position;
			lastSelectedView = view;
			lastSelectedView.requestFocus();

			lastSelectedView.setBackgroundColor(Color.BLUE);
		}
		//				lastSelectedView.setBackgroundResource(android.R.drawable.list_selector_background);
	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);
		if(reqCode == CONTACTS_REQ) {
			if (resultCode == Activity.RESULT_OK) {
				addContact(data.getData());
				setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getList()));
			}
		}
	}

	private void addContact(Uri uri){
		Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		if(cursor.moveToFirst()) {
			boolean exists = false;
			String contactId = cursor.getString(cursor.getColumnIndex(Contacts._ID)); 
			for(int i = 0; i < mainApp.getVecContactsID().size(); i++) 
				if(mainApp.getVecContactsID().get(i).getContactID().equalsIgnoreCase(contactId)) {
					exists = true;
					break;
				}

			if(!exists) {
				mainApp.getVecContactsID().add(new MyContacts(contactId));
				mainApp.addDBRezultat(mainApp.getVecContactsID().lastElement());
			}

		}
	}
}