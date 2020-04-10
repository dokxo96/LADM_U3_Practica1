package mx.edu.itt.ladm_u3_practica1_danielmora

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main3.*
import kotlinx.android.synthetic.main.agregar_layout.*
import java.io.File


class Main3Activity : AppCompatActivity() {
    var path: String? = null
    private val CARPETA_RAIZ = "misImagenesPrueba/"
    private val RUTA_IMAGEN = CARPETA_RAIZ + "misFotos"
    var listaId=ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        var extr = intent.extras

        Id_ACti.setText(extr?.getInt("id").toString())

        imageButton2.setOnClickListener {
            AbrirDialogoInsertar()
        }

        CancelEv.setOnClickListener {
            finish()
        }

        InsertEvi.setOnClickListener {
            var bitmap=(imageView.drawable as BitmapDrawable).bitmap
            var id_ =Id_ACti.text.toString().toInt()

            var Evi=EVIDENCIAS(id_,DbBitmapUtility.getBytes(bitmap))
            Evi.asignarPuntero(this)
            var r=Evi.insertar()
            if(r==true) {
                dialogo("Se capturó Evidencia")
                finish()
            }
            else{
                when(Evi.error){
                    1->{dialogo("Error en tabla,no se creó \n o no se conectó   ")}
                    2->{dialogo("Error en tabla,no se pudo insertar  ")}
                }
            }

        }

    }

    private fun AbrirDialogoInsertar() {

        val opciones =
            arrayOf<CharSequence>("Tomar Foto", "Cargar Imagen", "Cancelar")
        val alertOpciones: AlertDialog.Builder = AlertDialog.Builder(this)
        alertOpciones.setTitle("Seleccione una Opción")
        alertOpciones.setItems(opciones,
            DialogInterface.OnClickListener { dialogInterface, i ->
                if (opciones[i] == "Tomar Foto") {
                    tomarFotografia()
                } else {
                    if (opciones[i] == "Cargar Imagen") {
                        val intent = Intent(
                            Intent.ACTION_GET_CONTENT,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        intent.type = "image/"
                        startActivityForResult(
                            Intent.createChooser(
                                intent,
                                "Seleccione la Aplicación"
                            ), 10
                        )
                    } else {
                        dialogInterface.dismiss()
                    }
                }
            })
        alertOpciones.show()

    }
    private fun tomarFotografia() {
        val fileImagen = File(Environment.getDataDirectory(), RUTA_IMAGEN)
        var isCreada: Boolean = fileImagen.exists()
        var nombreImagen = ""
        if (isCreada == false) {
            isCreada = fileImagen.mkdirs()
        }
        if (isCreada == true) {
            nombreImagen = (System.currentTimeMillis() / 1000).toString() + ".jpg"
        }
        path = Environment.getExternalStorageState() +
                File.separator + RUTA_IMAGEN + File.separator.toString() + nombreImagen+""
        val imagen = File(path)

        var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        ////
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val authorities = applicationContext.packageName + ".provider"
            val imageUri: Uri = FileProvider.getUriForFile(this, authorities, imagen)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen))
        }
        startActivityForResult(intent, 10)

        ////
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode== Activity.RESULT_OK) {
            val miPath = data!!.data
            imageView.setImageURI(miPath);
        }
    }
    private fun dialogo(s: String) {
        androidx.appcompat.app.AlertDialog.Builder(this).setTitle("ATENCIÓN").setMessage(s).show()
    }

    private fun mensaje(s: String) {
        Toast.makeText(this,s, Toast.LENGTH_LONG)
    }


}
