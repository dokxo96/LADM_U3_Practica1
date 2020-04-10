package mx.edu.itt.ladm_u3_practica1_danielmora

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import androidx.core.graphics.createBitmap


class EVIDENCIAS(d:Int , image: ByteArray? ) {

        var Id_Acti=d
        var Foto=image


    var id =0
    var error =-1
    var errMes=""
    /*
    valores de error
        1-error en tbla,no se creo o conecto a Bd
        2-no se pudo insertar
        3-no se pudo realizar consulta o tABLA vacia
        4-no se encontro id
        5-no se actualizó registro
        6-no se pudo eliminar


    * */
    var nombreBD="Tareas"
    var puntero :Context ?= null

    fun asignarPuntero(p:Context){
        puntero=p
    }

    fun insertar():Boolean{
        error=-1
        try {
            var base=BaseDatos(puntero!!,nombreBD,null,1)
            var insertar =base.writableDatabase
            var datos =ContentValues()

            datos.put("ID_ACTIVIDAD",Id_Acti.toString())
            datos.put("FOTO",Foto)

            var resp =insertar.insert("EVIDENCIAS", "ID_EVIDENCIA",datos)
            if(resp.toInt()==-1){
                error=2

                return false
            }
        }catch (e:SQLiteException){
            error=1
            errMes=e.message.toString()
            return false
        }
        return true
    }
    fun mostrarTodos():ArrayList<EVIDENCIAS>{
            var data =ArrayList<EVIDENCIAS>()
            error=-1
        try {
            var base =BaseDatos(puntero!!,nombreBD,null,1)
            var select =base.readableDatabase
            var ev =EVIDENCIAS(-1,null)

            var columnas=arrayOf("*")
            var cursor =select.query("EVIDENCIAS",columnas,null,null,null,null,null)
            if(cursor.moveToFirst()){

                do {

                    var EvidTemp=EVIDENCIAS(
                        cursor.getInt(1)
                        ,cursor.getBlob(cursor.getColumnIndex("FOTO")))

                    EvidTemp.id =cursor.getInt(0)
                    data.add(EvidTemp)

                }while (cursor.moveToNext())

            }else{
                error=3
            }
        }catch (e:SQLiteException){
            error=1
            errMes=e.message.toString()
        }
        return data
    }
    fun buscar(id:String):EVIDENCIAS{
        var EvidenciadEncontrar=EVIDENCIAS( -1, null )
        error =-1
        try {
            var base =BaseDatos(puntero!!,nombreBD,null,1)
             var select = base.readableDatabase
            var columnas= arrayOf("*")
            var idBuscar =arrayOf(id)
            var cursor=select.query("EVIDENCIAS",columnas,"ID_EVIDENCIA =?",idBuscar,null,null,null,null)
            if(cursor.moveToFirst()){
                EvidenciadEncontrar.id=id.toInt()
                EvidenciadEncontrar.Id_Acti=cursor.getInt(1)
                EvidenciadEncontrar.Foto=cursor.getBlob(2)
            }else{
                error =4
            }

        }catch (e:SQLiteException){
            error=1

        }

        return EvidenciadEncontrar
    }

    fun actualizar():Boolean{
        error=-1
        try {
            var base=BaseDatos(puntero!!,nombreBD,null,1)
            var actualizar =base.writableDatabase
            var datos =ContentValues()
            var idActualizar =arrayOf(id.toString())
            datos.put("ID_ACTIVIDAD",Id_Acti)
            datos.put("FOTO",Foto)


            var resp =actualizar.update("EVIDENCIAS",datos,"ID_EVIDENCIA = ?",idActualizar)
            if(resp.toInt()==0){
                error=5

                return false
            }
        }catch (e:SQLiteException){
            error=1
            errMes=e.message.toString()
            return false
        }
        return true
    }
    fun eliminar():Boolean{
        error=-1
        try {
            var base=BaseDatos(puntero!!,nombreBD,null,1)
            var eliminar =base.writableDatabase
            var idEliminar =arrayOf(id.toString())

            var resp =eliminar.delete("EVIDENCIAS","ID_EVIDENCIA = ?",idEliminar)
            if(resp.toInt()==0){
                error=6

                return false
            }
        }catch (e:SQLiteException){
            error=1
            errMes=e.message.toString()
            return false
        }
        return true
    }





}
