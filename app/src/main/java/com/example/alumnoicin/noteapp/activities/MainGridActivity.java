package com.example.alumnoicin.noteapp.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.alumnoicin.noteapp.R;
import com.example.alumnoicin.noteapp.adapters.MyAdapter;
import com.example.alumnoicin.noteapp.models.Nota;


import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainGridActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Nota>>,View.OnClickListener {

    private MyAdapter adapter;
    private GridView gridView;
    final Context context = this;
    String notas, notas2;
    int posicion;
    private Button btnql;


    private Realm realm;
    private RealmResults<Nota> nota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_grid);
        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();
        gridView = (GridView) findViewById(R.id.gridView);

        nota = getAllNota();
        nota.addChangeListener(this);

        adapter = new MyAdapter(nota, R.layout.grid_item, this);

        gridView.setAdapter(adapter);
        registerForContextMenu(gridView);


    }

    @Override
    public void onChange(RealmResults<Nota> element) {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        realm.removeAllChangeListeners();
        realm.close();
        super.onDestroy();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        //menu.setHeaderTitle(this.nota.get(info.position).getid() + "");
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete_item:
                realm.beginTransaction();
                nota.deleteFromRealm(info.position); // App crash
                realm.commitTransaction();
                return true;
            case R.id.change_item: {
                // get prompts.xml view
                posicion = info.position;
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.popup, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and set it to result
                                        // edit text
                                        notas2 = String.valueOf(userInput.getText());

                                        realm.beginTransaction();
                                        nota.get(posicion).setNota(notas2);
                                        realm.commitTransaction();

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();



                return true;
            }case R.id.change_color:
                posicion = info.position;
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.popup_color, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);



                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView).setNegativeButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });;
               // findViewById(R.id.button_amarillo).setOnClickListener(this);
                //findViewById(R.id.button_blanco).setOnClickListener(this);
                //findViewById(R.id.button_celeste).setOnClickListener(this);
                //findViewById(R.id.button_verde).setOnClickListener(this);
                //findViewById(R.id.button_rojo).setOnClickListener(this);







                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                //realm.beginTransaction();
                //nota.get(info.position).setColor(1);
                //realm.commitTransaction();

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_add:
                addNota();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private RealmResults<Nota> getAllNota() {
        return realm.where(Nota.class).findAll();
    }


    private void addNota() {
        // get prompts.xml view

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.popup, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                notas = String.valueOf(userInput.getText());

                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {


                                        Nota n1 = new Nota(notas,0);


                                        realm.copyToRealmOrUpdate(n1);


                                        nota = getAllNota();
                                    }
                                });
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }


   /* public void onClick(View v) {
        int color;

        //View contenedor = v.getRootView();

        switch (v.getId()) {
            case R.id.button_amarillo:
                //color = Color.parseColor("#FFFFBB33"); // Verde azulado
                realm.beginTransaction();
                nota.get(1).setColor(1);
                realm.commitTransaction();

                break;
            case R.id.button_blanco:
                color = Color.parseColor("FFFFFFFF"); // Verde
                break;
            case R.id.button_celeste:
                color = Color.parseColor("FF33B5E5"); // Verde claro
                break;
            case R.id.button_verde:
                color = Color.parseColor("FF99CC00"); // Lima
                break;
            case R.id.button_rojo:
                color = Color.parseColor("FFCC0000"); // Lima
                break;
            default:
                color = Color.WHITE; // Blano
        }

       /// contenedor.setBackgroundColor(color);
    }*/



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_amarillo:
                //Toast.makeText(MainGridActivity.this, "Amarillo", Toast.LENGTH_SHORT).show();
                realm.beginTransaction();
                nota.get(posicion).setColor(1);
                realm.commitTransaction();
                break;
            case R.id.button_blanco:
                Toast.makeText(MainGridActivity.this, "Blanco", Toast.LENGTH_SHORT).show();
                realm.beginTransaction();
                nota.get(posicion).setColor(0);
                realm.commitTransaction();
                break;
            case R.id.button_celeste:
                Toast.makeText(MainGridActivity.this, "Celeste", Toast.LENGTH_SHORT).show();
                realm.beginTransaction();
                nota.get(posicion).setColor(2);
                realm.commitTransaction();
                break;
            case R.id.button_rojo:
                Toast.makeText(MainGridActivity.this, "Rojo", Toast.LENGTH_SHORT).show();
                realm.beginTransaction();
                nota.get(posicion).setColor(3);
                realm.commitTransaction();
                break;
            case R.id.button_verde:
                Toast.makeText(MainGridActivity.this, "Verde", Toast.LENGTH_SHORT).show();
                realm.beginTransaction();
                nota.get(posicion).setColor(4);
                realm.commitTransaction();
                break;

        }
    }
}