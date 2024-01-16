package com.mustafa.prizprojem

import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.mustafa.prizprojem.models.RuleInfoFloat
import com.mustafa.prizprojem.models.RuleInfoJson
import com.mustafa.prizprojem.models.RuleResponseFloat
import com.mustafa.prizprojem.models.RuleResponseJson
import com.mustafa.prizprojem.services.RetrofitObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.Calendar


class KuralEklemeFragment : Fragment() {
    lateinit var sharedPref : SharedPreferences

    private lateinit var kural1: EditText
    private lateinit var kural2: EditText
    private lateinit var kuralBaslangic: TextView
    private lateinit var kuralBitis: TextView
    private lateinit var myButton: Button
    private val myAPIService = RetrofitObject
    private var gonderInt = -1000
    private var gonderFloat :Float = -1000F
    private lateinit var gonderMap :Map<String,String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_kural_ekleme, container, false)
        // arguments kullanarak değeri al
        kural1 = view.findViewById(R.id.kural1Text)
        kural2 = view.findViewById(R.id.kural2Text)
        kuralBaslangic = view.findViewById(R.id.kural3BaslangicText)
        kuralBitis = view.findViewById(R.id.kural3BitisText)
        myButton = view.findViewById(R.id.kaydetButon)



        var temp = kuralEkraniOlustur()

        myButton.setOnClickListener {

            when(temp){
                1-> {
                    if(kural1.text.toString().isBlank()==false){
                    //işlem yap
                    var myStr = kural1.text.toString()
                   if(kuralKontrol(myStr)){
                       CoroutineScope(Dispatchers.Main).launch {
                           kuralEkleFloat(temp,myStr.toFloat()) // apimi cagirdim
                           delay(100)
                           Toast.makeText(requireContext(),"Kuraliniz basariyla kaydedildi",Toast.LENGTH_SHORT).show()
                           val action = KuralEklemeFragmentDirections.actionKuralEklemeFragmentToAnaSayfaFragment()
                           Navigation.findNavController(requireView()).navigate(action)
                       }

                   }
                }
                else{
                    Toast.makeText(requireContext(),"Lutfen min derecegi giriniz",Toast.LENGTH_SHORT).show()
                }

                }
                2-> {
                    if(kural2.text.toString().isBlank()==false){
                        //işlem yap
                        var myStr = kural2.text.toString()
                        if(kuralKontrol(myStr)){
                            CoroutineScope(Dispatchers.Main).launch {
                                kuralEkleFloat(temp,myStr.toFloat()) // apimi cagirdim
                                delay(100)
                                Toast.makeText(requireContext(),"Kuraliniz basariyla kaydedildi",Toast.LENGTH_SHORT).show()
                                val action = KuralEklemeFragmentDirections.actionKuralEklemeFragmentToAnaSayfaFragment()
                                Navigation.findNavController(requireView()).navigate(action)
                            }
                            // kuralim dogrulandi, action islemini yapabilirim

                        }
                    }
                    else{
                        Toast.makeText(requireContext(),"Lutfen min derecegi giriniz",Toast.LENGTH_SHORT).show()
                    }

                }
                3-> {
                    if (kuralBaslangic.text.isBlank() == true) {
                        //println("ustu bos biraktin")
                        Toast.makeText(requireContext(),"Lutfen baslangic saatini seciniz", Toast.LENGTH_SHORT).show()
                    } else if (kuralBitis.text.isBlank() == true) {
                        // println("alti bos biraktin")
                        Toast.makeText(requireContext(), "Lutfen bitis saatini seciniz", Toast.LENGTH_SHORT)
                            .show()

                    } else {
                        // kaydetme islemini yapacaksin
                        val key1 = "s_date"
                        val value1 = kuralBaslangic.text.toString()

                        val key2 = "e_date"
                        val value2 = kuralBitis.text.toString()

                        val mapWithValues = mapOf(key1 to value1, key2 to value2)
                        CoroutineScope(Dispatchers.Main).launch {
                            kuralEkleJson(temp, mapWithValues)// apimi cagirdim
                            delay(100)
                            val action = KuralEklemeFragmentDirections.actionKuralEklemeFragmentToAnaSayfaFragment()
                            Navigation.findNavController(requireView()).navigate(action)
                        }
                        println("bos biraktigin yok ")
                    }
                }// 3 NUMARA BİTİS
            }// WHEN BİTİS
        }


        kuralBaslangic.setOnClickListener {
            showTimePickerDialog(1)

        }

        kuralBitis.setOnClickListener {
            kuralBitis.text = showTimePickerDialog(2)

        }
        return view
    }


    private fun showTimePickerDialog(myInt: Int): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        var selectedTime: String = ""
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { view, selectedHour, selectedMinute ->
                selectedTime = "$selectedHour:$selectedMinute"
                selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)

                //println("sectiğim saat ${selectedTime}")
                //println("type : ${selectedTime::class}")
                when (myInt) {
                    1 -> {
                        kuralBaslangic.text = selectedTime
                    }

                    2 -> {
                        kuralBitis.text = selectedTime
                    }
                }

                // Seçilen saati kullanmak için yapılacak işlemler
                // Örneğin, bir metin görüntüleme alanına atama yapabilirsiniz.
                // textView.text = selectedTime
            },
            hour,
            minute,
            true // 24-saat formatında gösterilsin mi?
        )
        timePickerDialog.show()
        return selectedTime
    }


    fun kuralEkraniOlustur(): Int {
        val receivedValue = arguments?.getInt("kural_id")
        if (receivedValue == 1) {
            //println("kural 1 geldi")
            kuralBaslangic.visibility = View.GONE
            kuralBitis.visibility = View.GONE
            kural2.visibility = View.GONE
            return receivedValue
        } else if (receivedValue == 2) {
            //println("kural 2 geldi")
            kuralBitis.visibility = View.GONE
            kural1.visibility = View.GONE
            kuralBaslangic.visibility = View.GONE
            return receivedValue
        } else if (receivedValue == 3) {
            //println("kural 3 geldi")
            kural1.visibility = View.GONE
            kural2.visibility = View.GONE
            return receivedValue
        }
        return -1
    }

    fun kuralKontrol(deger: String): Boolean {

                if (deger.toInt() < -20 || deger.toInt() > 30) {
                    Toast.makeText(
                        requireContext(),
                        "Sıcaklık -20 ile 30 derece arasinda olmalidir. Yeni bir değer girin.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
        return true
    }


    fun kuralEkleFloat(rule : Int, value: Float){
        val token =  sharedPref.getString("token", "")

        var myKural : RuleInfoFloat = RuleInfoFloat(rule, value)
        //var myToken = "BEARER eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOjgsInVzZXJuYW1lIjoiYWhtZXQiLCJjaXR5IjoiIiwicHJvdmluY2UiOiIiLCJpYXQiOjE3MDUyMzM3OTgsImV4cCI6MTcwNTQwNjU5OH0.aAay2IjaDkcdTJN28soB_mKqIYsMufsvgOU9lh2fff0"
        token?.let {tokenn->
            val call: Call<RuleResponseFloat> = myAPIService.apiService.postRuleFloat("BEARER ${tokenn}",myKural)
            try {
                call.enqueue(object : Callback<RuleResponseFloat>{
                    override fun onResponse(
                        call: Call<RuleResponseFloat>,
                        response: Response<RuleResponseFloat>
                    ) {
                        println("CEvap geldii")
                        println("response bodyi ${response.body()==null} ")
                        response.body()?.let {

                            /*
                             println("cevabim ${it.msg}")
                             println("cevabim ${it.rule}")
                             println("cevabim ${it.value}")
                             */
                            gonderInt = it.rule
                            gonderFloat = it.value
                        }

                    }

                    override fun onFailure(call: Call<RuleResponseFloat>, t: Throwable) {
                        println("Cevap gelmedi")
                    }

                })
            }catch (e:Exception){
                println("TRY CATCH'E YAKALANDIN KOCUM")
            }
        }

    }


    fun kuralEkleJson(rule: Int, myMap: Map<String,String>){
        var myKural: RuleInfoJson = RuleInfoJson(rule,myMap)
        val token =  sharedPref.getString("token", "")


        //var myToken = "BEARER eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOjgsInVzZXJuYW1lIjoiYWhtZXQiLCJjaXR5IjoiIiwicHJvdmluY2UiOiIiLCJpYXQiOjE3MDUyMzM3OTgsImV4cCI6MTcwNTQwNjU5OH0.aAay2IjaDkcdTJN28soB_mKqIYsMufsvgOU9lh2fff0"

        token?.let {tokenn->
            val call : Call<RuleResponseJson> = myAPIService.apiService.postRuleJson("BEARER ${tokenn}",myKural)
            try {
                call.enqueue(object : Callback<RuleResponseJson>{
                    override fun onResponse(
                        call: Call<RuleResponseJson>,
                        response: Response<RuleResponseJson>
                    ) {
                        println("Cevap geldi")
                        println("Response: ${response.body() == null}")
                        response.body()?.let {

                            println("Cevabim: ${it.msg}")
                            println("Cevabim: ${it.rule}")
                            println("Cevabim: ${it.value}")

                            gonderInt = it.rule
                            gonderMap = it.value
                        }
                    }

                    override fun onFailure(call: Call<RuleResponseJson>, t: Throwable) {
                        println("Cevap gelmedi")
                    }

                })
            }catch (e: Exception){
                println("Cath'e yakalandın KOCUM")
            }
        }

    }










}// fragment sonu



