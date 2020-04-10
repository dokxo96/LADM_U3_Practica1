package mx.edu.itt.ladm_u3_practica1_danielmora

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import java.util.*
import kotlin.collections.ArrayList

class ACTIVIDADES(d:String, fc:String, fe:String) {

        var Descripcion=d
        var FechaCap=fc
        var FechaEntr=fe

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
            datos.put("DESCRIPCION",Descripcion)
            datos.put("FECHACAPTURA",FechaCap.toString())
            datos.put("FECHAENTREGA",FechaEntr.toString())

            var resp =insertar.insert("ACTIVIDADES","ID_ACTIVIDAD",datos)
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
    fun mostrarTodos():ArrayList<ACTIVIDADES>{
            var data =ArrayList<ACTIVIDADES>()
            error=-1
        try {
            var base =BaseDatos(puntero!!,nombreBD,null,1)
            var select =base.readableDatabase

            var columnas=arrayOf("*")
            var cursor =select.query("ACTIVIDADES",columnas,null,null,null,null,null)
            if(cursor.moveToFirst()){

                do {
                    var trabTemp=ACTIVIDADES(cursor.getString(1),cursor.getString(2),cursor.getString(3))
                    trabTemp.id=cursor.getInt(0)
                    data.add(trabTemp)

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
    fun buscar(id:String):ACTIVIDADES{
        var ActividadEncontrar=ACTIVIDADES( "-1","-1","-1" )
        error =-1
        try {
            var base =BaseDatos(puntero!!,nombreBD,null,1)
             var select = base.readableDatabase
            var columnas= arrayOf("*")
            var idBuscar =arrayOf(id)
            var cursor=select.query("ACTIVIDADES",columnas,"ID_ACTIVIDAD =?",idBuscar,null,null,null,null)
            if(cursor.moveToFirst()){
                ActividadEncontrar.id=id.toInt()
                ActividadEncontrar.Descripcion=cursor.getString(1)
                ActividadEncontrar.FechaCap=cursor.getString(2)
                ActividadEncontrar.FechaEntr=cursor.getString(3)
            }else{
                error =4
            }

        }catch (e:SQLiteException){
            error=1

        }

        return ActividadEncontrar
    }

    fun actualizar():Boolean{
        error=-1
        try {
            var base=BaseDatos(puntero!!,nombreBD,null,1)
            var actualizar =base.writableDatabase
            var datos =ContentValues()
            var idActualizar =arrayOf(id.toString())
            datos.put("DESCRIPCION",Descripcion)
            datos.put("FECHACAPTURA",FechaCap.toString())
            datos.put("FECHAENTREGA",FechaEntr.toString())

            var resp =actualizar.update("ACTIVIDADES",datos,"ID_ACTIVIDAD = ?",idActualizar)
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

            var resp =eliminar.delete("ACTIVIDADES","ID_ACTIVIDAD = ?",idEliminar)
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
