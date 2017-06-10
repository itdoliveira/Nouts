package com.example.logonrm.nouts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.logonrm.nouts.adapter.ListaAndroidAdaper;
import com.example.logonrm.nouts.dao.Notes;
import com.example.logonrm.nouts.dao.NotesDAO;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView rvNotes;
    private TextView id;
    private NotesDAO notesDAO;
    private SharedPreferences idTarefa;
    private FloatingActionButton fabDel;
    private FloatingActionButton fabEdit;
    private int idTemporario;
    private ListaAndroidAdaper adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        notesDAO = new NotesDAO(this.getApplicationContext());
        idTarefa = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        idTemporario = -1;
        rvNotes = (RecyclerView) findViewById(R.id.rvNotes);
        fabDel = (FloatingActionButton) findViewById(R.id.fabDel);
        fabEdit = (FloatingActionButton) findViewById(R.id.fabEdit);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                idTarefa.edit().putInt("ID", 0).apply();
                startActivityForResult(new Intent(MainActivity.this,
                                NewNoteActivity.class),
                        NewNoteActivity.CODE_NEW_NOTE);
            }
        });

        fabDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idTemporario != -1){
                    notesDAO.deleteByID(idTemporario);
                    carregaNotes();
                    fabDel.setVisibility(View.INVISIBLE);
                    fabEdit.setVisibility(View.INVISIBLE);
                }
            }
        });

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idTemporario != -1){
                    idTarefa.edit().putInt("ID", idTemporario).apply();
                    startActivityForResult(new Intent(MainActivity.this,
                                    NewNoteActivity.class),
                            NewNoteActivity.CODE_NEW_NOTE);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        carregaNotes();
        ItemClickSupport.addTo(rvNotes).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                id = (TextView) v.findViewById(R.id.id);
                int i = countItemSelected();

                if(!isItemSelected() && i == 0){

                    v.setSelected(true);
                    idTemporario = Integer.parseInt(id.getText().toString());
                    fabDel.setVisibility(View.VISIBLE);
                    fabEdit.setVisibility(View.VISIBLE);
                } else if(v.isSelected()){
                    v.setSelected(false);
                    fabDel.setVisibility(View.INVISIBLE);
                    fabEdit.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void carregaNotes() {
        NotesDAO noteDAO = new NotesDAO(this);
        List<Notes> notes = noteDAO.getAll();
        setUpNote(notes);
    }

    private void setUpNote(List<Notes> lista) {
        adapter = new ListaAndroidAdaper(this, lista);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setAdapter(adapter);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(MainActivity.this, "Cancelado",
                    Toast.LENGTH_LONG).show();
        } else if (requestCode == NewNoteActivity.CODE_NEW_NOTE) {
            fabDel.setVisibility(View.INVISIBLE);
            fabEdit.setVisibility(View.INVISIBLE);
            carregaNotes();
        } else if (requestCode == NewNoteActivity.CODE_EDITA_TAREFA) {
            fabDel.setVisibility(View.INVISIBLE);
            fabEdit.setVisibility(View.INVISIBLE);
            carregaNotes();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_sair:
                sair();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void sair(){
        SharedPreferences pref = getSharedPreferences(LoginActivity.KEY_APP_PREFERENCES,
                MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(LoginActivity.KEY_LOGIN, "");
        editor.apply();
        finish();
        System.exit(0);
    }

    private boolean isItemSelected(){
        for (int i = 0; i < rvNotes.getAdapter().getItemCount(); i++) {
            if(rvNotes.getChildAt(i).isSelected()){
                return true;
            }
        }
        return false;
    }

    private int countItemSelected(){
        int s = 0;
        for (int i = 0; i > rvNotes.getAdapter().getItemCount(); i++) {
            if(rvNotes.getChildAt(i).isSelected()){
                s =+ 1;
            }
        }
        return s;
    }
}
