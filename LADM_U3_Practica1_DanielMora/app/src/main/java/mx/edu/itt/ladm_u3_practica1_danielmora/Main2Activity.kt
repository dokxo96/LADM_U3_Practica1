package mx.edu.itt.ladm_u3_practica1_danielmora

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*


class Main2Activity : AppCompatActivity() {
    var listaId=ArrayList<String>()

    private val CARPETA_RAIZ = "misImagenesPrueba/"
    private val RUTA_IMAGEN = CARPETA_RAIZ + "misFotos"

    val COD_SELECCIONA = 10
    val COD_FOTO = 20
    var path: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        var extr = intent.extras
        var id =extr?.getInt("id").toString()
        textView.setText(id)
        txtDescrip.setText("Descripción: ${extr?.getString("Desc")}")
        txtFechaCa.setText("Fecha Captura: ${extr?.getString("FC")}")
        txtFechaEn.setText("Fecha Entrega: ${extr?.getString("FE")}")

        btnAgregarEvi.setOnClickListener {
            cargarOtroActivity(id.toInt())

            //cargarLista()
        }

        btnBuscarEvi.setOnClickListener {

        }
        //cargarLista()

    }


    ///////////////////////////////////////////////////////////
    private fun cargarOtroActivity(id:Int) {
        var intento = Intent(this,Main3Activity::class.java)

        intento.putExtra("id",id)
        startActivityForResult(intento,0)

    }

    private fun dialogo(s: String) {
        androidx.appcompat.app.AlertDialog.Builder(this).setTitle("ATENCIÓN").setMessage(s).show()
    }

    private fun mensaje(s: String) {
        Toast.makeText(this,s, Toast.LENGTH_LONG)
    }

}
