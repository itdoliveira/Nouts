package com.example.logonrm.nouts.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

public class CategoriaDAO {
    private DBOpenHelper banco;

    public CategoriaDAO(Context context) {
        banco = new DBOpenHelper(context);
    }

    public static final String TABELA_CATEGORIA = "categoria";
    public static final String COLUNA_ID = "id";
    public static final String COLUNA_NOME = "nome";

    public List<Categoria> getAll() {
        List<Categoria> categorias = new LinkedList<>();
        String query = "SELECT * FROM " + TABELA_CATEGORIA;
        SQLiteDatabase db = banco.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Categoria categoria = null;
        if (cursor.moveToFirst()) {
            do {
                categoria = new Categoria();
                categoria.setId(cursor.getInt(cursor.getColumnIndex(COLUNA_ID)));

                categoria.setNome(cursor.getString(cursor.getColumnIndex(COLUNA_NOME)));
                categorias.add(categoria);
            } while (cursor.moveToNext());
        }
        return categorias;
    }

    public Categoria getBy(int id) {
        SQLiteDatabase db = banco.getReadableDatabase();
        String colunas[] = {COLUNA_ID, COLUNA_NOME};
        String where = "id = " + id;
        Cursor cursor = db.query(true, TABELA_CATEGORIA, colunas, where, null, null,
                null, null, null);
        Categoria categoria = null;
        if (cursor != null) {
            cursor.moveToFirst();
            categoria = new Categoria();
            categoria.setNome(cursor.getString(cursor.getColumnIndex(COLUNA_NOME)));
            categoria.setId(cursor.getInt(cursor.getColumnIndex(COLUNA_ID)));
        }
        return categoria;
    }
}
