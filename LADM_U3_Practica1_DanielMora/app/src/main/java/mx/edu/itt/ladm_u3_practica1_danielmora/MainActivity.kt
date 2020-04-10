package mx.edu.itt.ladm_u3_practica1_danielmora

import android.content.Intent
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.Layout
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.agregar_layout.*
import kotlinx.android.synthetic.main.agregar_layout.view.*
import kotlinx.android.synthetic.main.agregar_layout.view.fechaCap

class MainActivity : AppCompatActivity() {
    var listaId=ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnAgregar.setOnClickListener {
            AbrirDialogoInsertar()
        }
        btnBuscar.setOnClickListener {
            AbrirDialogoBuscar()
        }
        cargarLista()




}

    private fun AbrirDialogoInsertar() {
     val  NewAct = LayoutInflater.from(this).inflate(R.layout.agregar_layout,null)
        val mBuild =AlertDialog.Builder(this).setView(NewAct).setTitle("Agregar Actividad")
        val mAlertDialog =mBuild.show()
        NewAct.Insert.setOnClickListener {
            mAlertDialog.dismiss()
            //obtener datos

            var descDialog =NewAct.desc.text.toString()
            var FCap=NewAct.fechaCap.text.toString()
            var FEnt=NewAct.fechaEnt.text.toString()

            if (descDialog.isEmpty()) {
                mensaje("Error no escribiste una descripción")
                return@setOnClickListener
            }
            if (FCap.isEmpty()) {
                mensaje("Error no escribiste \n una Fecha de captura")
                return@setOnClickListener
            }
            if (FEnt.isEmpty()) {
                mensaje("Error no escribiste \n una Fecha de Entrega")
               return@setOnClickListener
            }
            InsertarActividad(descDialog,FCap,FEnt)

        }
        NewAct.cancel.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    fun InsertarActividad(d: String, fc: String, fe: String) {
        var Act=ACTIVIDADES(d.toString(),fc.toString(),fe.toString())
        Act.asignarPuntero(this)
        var r=Act.insertar()
        if(r==true) {
            mensaje("Se capturó Actividad")
            cargarLista()
        }
        else{
            when(Act.error){
                  1->{dialogo("Error en tabla,no se creó \n o no se conectó   ")}
                  2->{dialogo("Error en tabla,no se pudo insertar  ")}
            }
        }
        cargarLista()
    }

    private fun cargarLista() {
        try {
            var con =ACTIVIDADES("","","")
            con.asignarPuntero(this)
            var data=con.mostrarTodos()
            if(data.size==0){
                if(con.error==3){
                    dialogo("no se pudo realizar consulta/Tabla vacia")
                }
                return
            }
            var total=data.size-1
            var vect =Array<String>(data.size,{""})
            listaId=ArrayList<String>()

            (0..total).forEach {
                var actividad=data[it]
                var item =actividad.id.toString()+" Descripcion:"+actividad.Descripcion+"\n"+"Fecha Captura:"+actividad.FechaCap
                vect[it]=item
                listaId.add(actividad.id.toString())
            }

            ListaItems.adapter= ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,vect)
            ListaItems.setOnItemClickListener { parent, view, position, id ->
                var con =ACTIVIDADES("","","")
                con.asignarPuntero(this)
                var ActEnc=con.buscar(listaId[position])

                if(con.error==4){
                    dialogo("Error no se encontro id")
                    return@setOnItemClickListener
                }
                AlertDialog.Builder(this).setTitle("¿Que deseas hacer?").setMessage("Nombre ${ActEnc.Descripcion} \n ${ActEnc.FechaEntr}")
                    .setPositiveButton("Agregar evidencia"){d,r-> cargarOtroActivity(ActEnc)}
                    .setNeutralButton("Cancelar"){d,r->}
                    .show()
            }
        }catch (e:Exception){
            dialogo(e.message.toString())
        }
    }

    private fun cargarOtroActivity(ActEnc: ACTIVIDADES) {
        var intento = Intent(this,Main2Activity::class.java)

        intento.putExtra("id",ActEnc.id)
        intento.putExtra("Desc",ActEnc.Descripcion)
        intento.putExtra("FC",ActEnc.FechaCap)
        intento.putExtra("FE",ActEnc.FechaEntr)
        startActivityForResult(intento,0)

    }


    fun AbrirDialogoBuscar() {
        var campo  = EditText (this)
        campo.setHint("Numero entero")
        campo.inputType= InputType.TYPE_CLASS_NUMBER

        AlertDialog.Builder(this)
            .setTitle("ATENCION")
            .setMessage("Escriba el ID a buscar")
            .setPositiveButton("buscar "){d, r->
                if(campo.text.toString().isEmpty()){
                    mensaje("Error no escribiste un id")
                    return@setPositiveButton
                }
                cargarListaBusqueda(campo.text.toString())
            }
            .setNeutralButton("Cancelar "){d,r->}
            .setView(campo)
            .show()
    }


    private fun cargarListaBusqueda(id :String) {
        try {
            var con =ACTIVIDADES("","","")
            con.asignarPuntero(this)
            var data=con.buscar(id)



            var vect =Array<String>(1,{""})
            listaId=ArrayList<String>()
                if(con.error!=4){
                //if(!data.Descripcion.equals("-1")) {
                    var actividad = data
                    var item =
                        actividad.id.toString() + " Descripcion:" + actividad.Descripcion + "\n" + "Fecha Captura:" + actividad.FechaCap
                    vect[0] = item
                    listaId.add(actividad.id.toString())
                }else{
                    dialogo("No existe el id")
                    cargarLista()
                }

            ListaItems.adapter= ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,vect)
            ListaItems.setOnItemClickListener { parent, view, position, id ->
                var con =ACTIVIDADES("","","")
                con.asignarPuntero(this)
                var ActEnc=con.buscar(listaId[position])

                if(con.error==4){
                    dialogo("Error no se encontro id")
                    return@setOnItemClickListener
                }
                AlertDialog.Builder(this).setTitle("¿Que deseas hacer?").setMessage("Nombre ${ActEnc.Descripcion} \n ${ActEnc.FechaEntr}")
                    .setPositiveButton("Agregar evidencia"){d,r-> cargarOtroActivity(ActEnc)}
                    .setNeutralButton("Cancelar"){d,r->}
                    .show()
            }
        }catch (e:Exception){
            dialogo(e.message.toString())
        }
    }

    private fun dialogo(s: String) {
        AlertDialog.Builder(this).setTitle("ATENCIÓN").setMessage(s).show()
    }

    private fun mensaje(s: String) {
        Toast.makeText(this,s,Toast.LENGTH_LONG)
    }


}
