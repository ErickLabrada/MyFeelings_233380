package erick.labrada.myfeelings

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import erick.labrada.myfeelings.utilities.CustomBarDrawable
import erick.labrada.myfeelings.utilities.Emociones
import erick.labrada.myfeelings.utilities.JSONFile
import erick.labrada.myfeelings.utilities.CustomCircleDrawable
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    var jsonFile: JSONFile?=null
    var veryHappy=0.0f
    var happy=0.0f
    var neutral=0.0f
    var sad=0.0f
    var verySad=0.0f
    var data: Boolean = false
    var lista = ArrayList <Emociones>()



    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val graph = findViewById<ConstraintLayout>(R.id.graph);
        val graphVeryHappy = findViewById<View>(R.id.graphVeryHappy);
        val graphHappy = findViewById<View>(R.id.graphHappy);
        val graphNeutral = findViewById<View>(R.id.graphNeutral);
        val graphSad = findViewById<View>(R.id.graphSad);
        val graphVerySad = findViewById<View>(R.id.graphVerySad);
        val icon = findViewById<ImageView>(R.id.icon);


        val guardarButton = findViewById<Button>(R.id.guardarButton);
        val veryHappyButton = findViewById<ImageButton>(R.id.verySatisfiedButton);
        val happyButton = findViewById<ImageButton>(R.id.satisfiedButton);
        val neutralButton = findViewById<ImageButton>(R.id.neutralButton);
        val sadButton = findViewById<ImageButton>(R.id.sadButton);
        val verySadButton = findViewById<ImageButton>(R.id.verySadButton);

        jsonFile =JSONFile()
        fetchingData()
        if(!data){
            var emociones = ArrayList<Emociones>()
            val fondo= CustomCircleDrawable(this, emociones)
            graph.background = fondo
            graphVeryHappy.background = CustomBarDrawable(this, Emociones("Muy feliz", 0.0f, R.color.mustard, veryHappy))
            graphHappy.background = CustomBarDrawable(this, Emociones("Feliz", 0.0f, R.color.orange, happy))
            graphNeutral.background = CustomBarDrawable(this, Emociones("Neutral", 0.0f, R.color.greenie, neutral))
            graphSad.background = CustomBarDrawable(this, Emociones("Triste", 0.0f, R.color.blue, sad))
            graphVerySad.background = CustomBarDrawable(this, Emociones("Muy triste", 0.0f, R.color.deepblue, verySad))
        } else {
            actualizarGrafica()
            iconoMayoria()
        }

        guardarButton.setOnClickListener(){
            guardar()
        }

        veryHappyButton.setOnClickListener(){
            veryHappy++
            iconoMayoria()
            actualizarGrafica()
        }

        happyButton.setOnClickListener(){
            happy++
            iconoMayoria()
            actualizarGrafica()
        }

        neutralButton.setOnClickListener(){
            neutral++
            iconoMayoria()
            actualizarGrafica()
        }

        sadButton.setOnClickListener(){
            sad++
            iconoMayoria()
            actualizarGrafica()
        }

        verySadButton.setOnClickListener(){
            verySad++
            iconoMayoria()
            actualizarGrafica()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }


    fun fetchingData(){
        try{
            var json : String = jsonFile?.getData((this))?:""
            if(json!=""){
                this.data=true
                var jsonArray: JSONArray=JSONArray(json)
                this.lista=parseJson(jsonArray)

                for (i in lista){
                    when (i.nombre){
                        "Muy feliz" -> veryHappy = i.total
                        "Feliz" -> happy = i.total
                        "Neutral" -> neutral = i.total
                        "Triste" -> sad = i.total
                        "Muy triste" -> verySad = i.total

                    }
                }
            } else{
                this.data = false
            }
        } catch (exception : JSONException){
            exception.printStackTrace()
        }
    }

    fun parseJson(jsonArray: JSONArray): ArrayList<Emociones>{

        var lista = ArrayList<Emociones>()

        for (i in 0..jsonArray.length()){
            try {
                val nombre = jsonArray.getJSONObject(i).getString("nombre")
                val porcentaje = jsonArray.getJSONObject(i).getDouble("porcentaje").toFloat()
                val color = jsonArray.getJSONObject(i).getInt("total")
                val total = jsonArray.getJSONObject(i).getDouble("total").toFloat()
                var emocion = Emociones(nombre, porcentaje, color, total)
                lista.add(emocion)
            } catch (exception: JSONException){
                exception.printStackTrace()
            }
        }
        return lista
    }

    fun actualizarGrafica(){

        val graph = findViewById<ConstraintLayout>(R.id.graph);
        val graphVeryHappy = findViewById<View>(R.id.graphVeryHappy);
        val graphHappy = findViewById<View>(R.id.graphHappy);
        val graphNeutral = findViewById<View>(R.id.graphNeutral);
        val graphSad = findViewById<View>(R.id.graphSad);
        val graphVerySad = findViewById<View>(R.id.graphVerySad);
        val icon = findViewById<ImageView>(R.id.icon);

        val total = veryHappy+happy+neutral+sad+verySad

        var pVH: Float = (veryHappy*100/total).toFloat()
        var pH: Float = (happy*100/total).toFloat()
        var pN: Float = (neutral*100/total).toFloat()
        var pS: Float = (sad*100/total).toFloat()
        var pVS: Float = (verySad*100/total).toFloat()

        Log.d("porcentajes", "very happy"+pVH)
        Log.d("porcentajes", "happy"+pH)
        Log.d("porcentajes", "neutral"+pN)
        Log.d("porcentajes", "sad"+pS)
        Log.d("porcentajes", "very sad"+pVS)

        lista.clear()
        lista.add(Emociones("Muy feliz", pVH, R.color.mustard, veryHappy))
        lista.add(Emociones("Feliz", pH, R.color.orange, happy))
        lista.add(Emociones("Neutral", pN, R.color.greenie, neutral))
        lista.add(Emociones("Triste", pS, R.color.blue, sad))
        lista.add(Emociones("Muy triste", pVS, R.color.deepblue, verySad))

        val fondo = CustomCircleDrawable(this, lista)

        graphVeryHappy.background = CustomBarDrawable(this, Emociones("Muy feliz", pVH, R.color.mustard, veryHappy))
        graphHappy.background = CustomBarDrawable(this, Emociones("Feliz", pH, R.color.orange, happy))
        graphNeutral.background = CustomBarDrawable(this, Emociones("Neutral", pN, R.color.greenie, neutral))
        graphSad.background = CustomBarDrawable(this, Emociones("Triste", pS, R.color.blue, sad))
        graphVerySad.background = CustomBarDrawable(this, Emociones("Muy triste", pVS, R.color.deepblue, verySad))

        graph.background = fondo
    }

    fun iconoMayoria(){
        val icon = findViewById<ImageView>(R.id.icon);
        if (happy>veryHappy&&happy>neutral&&happy>sad&&happy>verySad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.sentiment_satisfied_alt))
        }
        else if (veryHappy>happy&&veryHappy>neutral&&veryHappy>sad&&veryHappy>verySad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.sentiment_very_satisfied))
        }
        else if (neutral>veryHappy&&neutral>happy&&neutral>sad&&neutral>verySad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.sentiment_neutral))
        }
        else if (sad>veryHappy&&sad>happy&&sad>neutral&&sad>verySad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.sentiment_dissatisfied))
        } else{
            icon.setImageDrawable(resources.getDrawable(R.drawable.sentiment_very_dissatisfied))
        }
    }

    fun guardar(){
        var jsonArray = JSONArray()
        var o: Int =0
        for (i in lista){
            Log.d("objetos", i.toString())
            var j = JSONObject()
            j.put("nombre", i.nombre)
            j.put("porcentaje", i.porcentaje)
            j.put("color", i.color)
            j.put("total", i.total)
            jsonArray.put(o,j)
            o++
        }
        jsonFile?.saveData(this, jsonArray.toString())
        Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
    }



}