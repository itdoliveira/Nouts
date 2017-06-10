package com.example.logonrm.nouts.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

public class NotesDAO {

    private SQLiteDatabase db;
    private DBOpenHelper banco;

    public NotesDAO(Context context) {
        banco = new DBOpenHelper(context);
    }

    private static final String TABELA_NOTES = "notes";
    private static final String COLUNA_DESCRICAO = "descricao";
    private static final String COLUNA_CATEGORIA_ID = "categoria_id";

    public String add(Notes notes) {
        long resultado;
        SQLiteDatabase db = banco.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUNA_DESCRICAO, notes.getDescricao());
        values.put(COLUNA_CATEGORIA_ID, notes.getCategoria().getId());
        resultado = db.insert(TABELA_NOTES, null, values);
        db.close();
        if (resultado == -1) {
            return "Erro ao inserir registro";
        } else {
            return "Registro inserido com sucesso";
        }
    }

    public List<Notes> getAll() {
        List<Notes> notes = new LinkedList<>();
        String rawQuery = "SELECT t.*, c.nome FROM notes t INNER JOIN categoria c ON t.categoria_id = c.id";
        SQLiteDatabase db = banco.getReadableDatabase();
        Cursor cursor = db.rawQuery(rawQuery, null);
        Notes note = null;
        if (cursor.moveToFirst()) {
            do {
                note = new Notes();
                note.setId(cursor.getInt(0));
                note.setDescricao(cursor.getString(1));
                note.setCategoria(new Categoria(cursor.getInt(2),
                        cursor.getString(3)));
                notes.add(note);
            } while (cursor.moveToNext());
        }
        db.close();
        return notes;
    }


    public void deleteByID(int id) {
        SQLiteDatabase db = banco.getReadableDatabase();
        db.delete(NotesDAO.TABELA_NOTES, "id = " + id, null);
        db.close();
    }

    public String editByID(Notes notes){
        long resultado;
        SQLiteDatabase db = banco.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("descricao", notes.getDescricao());
        values.put("categoria_id", notes.getCategoria().getId());
        resultado = db.update("notes", values, " id = "+ notes.getId(), null);
        db.close();
        if (resultado == -1) {
            return "Erro ao editar registro";
        } else {
            return "Registro editado com sucesso";
        }
    }

    public Notes getByID(int id){
        SQLiteDatabase db = banco.getReadableDatabase();
        String rawQuery = "SELECT t.*, c.nome FROM " +
                "notes t INNER JOIN " +
                "categoria c ON t.categoria_id = c." +
                CategoriaDAO.COLUNA_ID + " where t.id = " + id;
        Cursor cursor = db.rawQuery(rawQuery, null);
        Notes notes = null;
        if (cursor.moveToFirst()) {
            do {
                notes = new Notes();
                notes.setId(cursor.getInt(0));
                notes.setDescricao(cursor.getString(1));
                notes.setCategoria(new Categoria(cursor.getInt(2),
                        cursor.getString(3)));
            } while (cursor.moveToNext());
        }
        db.close();
        return notes;
    }

}
