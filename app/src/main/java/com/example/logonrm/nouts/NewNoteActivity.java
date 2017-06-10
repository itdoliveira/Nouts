package com.example.logonrm.nouts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.logonrm.nouts.dao.Categoria;
import com.example.logonrm.nouts.dao.CategoriaDAO;
import com.example.logonrm.nouts.dao.Notes;
import com.example.logonrm.nouts.dao.NotesDAO;

import java.util.List;


public class NewNoteActivity extends AppCompatActivity {

    public final static int CODE_NEW_NOTE = 666;
    public final static int CODE_EDITA_TAREFA = 333;
    private TextInputLayout tilDescricaoNota;
    private TextView txDescricao;
    private Spinner spCategoria;
    private List<Categoria> categorias;
    private CalendarView cvData;
    private String data;
    private SharedPreferences id_tarefa;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        spCategoria = (Spinner) findViewById(R.id.spCategoria);
        tilDescricaoNota = (TextInputLayout) findViewById(R.id.tilDescricaoNota);
        txDescricao = (TextView) findViewById(R.id.txDescricao);
        id_tarefa = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        id = id_tarefa.getInt("ID",0);
        CategoriaDAO CategoriaDAO = new CategoriaDAO(this);
        categorias = CategoriaDAO.getAll();
        ArrayAdapter<Categoria> adapter =
                new ArrayAdapter<Categoria>(getApplicationContext(),
                        R.layout.categoria_spinner_item, categorias);
        adapter.setDropDownViewResource(R.layout.categoria_spinner_item);
        spCategoria.setAdapter(adapter);

        if(id != 0){
            editar();
        }
    }

    public void cadastrar(View v) {
        NotesDAO noteDAO = new NotesDAO(this);
        Notes note = new Notes();
        note.setDescricao(tilDescricaoNota.getEditText().getText().toString());
        note.setCategoria((Categoria) spCategoria.getSelectedItem());
        note.setId(id);
        if(id == 0){
            noteDAO.add(note);
            id_tarefa.edit().putInt("ID", 0).apply();
            retornaParaTelaAnteriorPosEditar();
        } else {
            noteDAO.editByID(note);
            retornaParaTelaAnterior();
        }
    }

    //retorna para tela de lista de tarefaes
    public void retornaParaTelaAnterior() {
        Intent intentMessage = new Intent();
        setResult(CODE_NEW_NOTE, intentMessage);
        finish();
    }

    public void retornaParaTelaAnteriorPosEditar() {
        Intent intentMessage = new Intent();
        setResult(CODE_EDITA_TAREFA, intentMessage);
        finish();
    }

    public void editar(){
        NotesDAO notesDAO = new NotesDAO(this);
        Notes Notes;
        Notes = notesDAO.getByID(id);
        txDescricao.setText(String.valueOf(Notes.getDescricao()));
        spCategoria.setSelection(Notes.getCategoria().getId() - 1);
        id_tarefa.edit().putInt("ID", 0).apply();
    }
}
