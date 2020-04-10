package mx.edu.itt.ladm_u3_practica1_danielmora

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE ACTIVIDADES(ID_ACTIVIDAD INTEGER PRIMARY KEY AUTOINCREMENT ,DESCRIPCION VARCHAR(200),FECHACAPTURA VARCHAR(200),FECHAENTREGA VARCHAR(200) )")
        db?.execSQL("CREATE TABLE EVIDENCIAS(ID_EVIDENCIA INTEGER PRIMARY KEY AUTOINCREMENT ,ID_ACTIVIDAD INTEGER,FOTO BLOB ,FOREIGN KEY (ID_ACTIVIDAD) REFERENCES ACTIVIDADES (ID_ACTIVIDAD) )")

    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}