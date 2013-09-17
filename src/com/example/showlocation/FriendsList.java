package com.example.showlocation;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class FriendsList extends ListActivity {

	Databasehandler db = new Databasehandler(this);

	List<Contact> contactList;
	String friends[];
	String status = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		contactList = db.getAllContacts();
		friends = new String[contactList.size()];

		for (int i = 0; i < contactList.size(); i++) {
			friends[i] = contactList.get(i).getName() + " \n"
					+ contactList.get(i).getPhoneNumber();
		}

		setListAdapter(new ArrayAdapter<String>(FriendsList.this,
				R.layout.list_item, friends));
		try {
			Bundle gotbasket = getIntent().getExtras();
			status = gotbasket.getString("stat");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, final int position,
			long id) {

		super.onListItemClick(l, v, position, id);

		if (status.equals((String) "Delete")) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Delete friend contact");
			alert.setMessage("You really want to Delete the contact ?");

			alert.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String chkNum=contactList.get(position).getPhoneNumber();
							db.deleteContact(contactList.get(position));
							
							if(!db.checkContact(chkNum))
							{
								Toast.makeText(getBaseContext(), "Friend contact is Deleted",Toast.LENGTH_SHORT).show();
							}else
							{
								Toast.makeText(getBaseContext(), "Error : can not delete",Toast.LENGTH_SHORT).show();
							}
							
						}
					});

			alert.setNegativeButton("No",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();

						}
					});

			AlertDialog al = alert.create();
			al.show();
		}
	}

}
