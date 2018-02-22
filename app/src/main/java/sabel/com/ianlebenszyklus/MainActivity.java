package sabel.com.ianlebenszyklus;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "######################" + MainActivity.class.getSimpleName();
    private static final int RQ_CONTACTS = 4711;
    private ArrayList<String> personenliste;
    private ArrayAdapter arrayAdapter;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTitle("Kontakte");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        log("onCreate");

        listView = findViewById(R.id.lvListe);
        personenliste = new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(MainActivity.this, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return super.onWindowStartingActionMode(callback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        log("onStart");

        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // Berechtigung nicht vorhanden
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                // zusätzliche Infos, warum App Berechtigung benötigt
                Toast.makeText(this, "Bitte Berechtigung gewähren.....", Toast.LENGTH_LONG).show();
                requestPermission();
            } else {
                // Berechtigung muss angefordert werden
                requestPermission();
            }

        } else {
            // Berechtigung vorhanden
            showContacts();
        }
    }

    private void requestPermission() {
        String[] permissions = new String[]{Manifest.permission.READ_CONTACTS};
        requestPermissions(permissions, RQ_CONTACTS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RQ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Berechtigung wurde gewährt
                showContacts();
            } else {
                Toast.makeText(this, "Schade", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showContacts() {
        Log.d(TAG, "showContacts");

        ContentResolver contentResolver = getContentResolver();

        String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME};
        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = ?";
        String[] selectionArgs = new String[]{"1"};
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, projection, selection, selectionArgs, ContactsContract.Contacts.DISPLAY_NAME + " ASC");

        while (cursor.moveToNext()) {
            personenliste.add(cursor.getString(0) + " " + cursor.getString(1));
        }
        cursor.close();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, personenliste);
        listView.setAdapter(arrayAdapter);

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        log("onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        log("onResume");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        log("onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        log("onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        log("onPause");
    }

    public void log(String text) {
        Log.i(TAG, text);
    }


}
