package com.mustafa.prizprojem

import android.app.TimePickerDialog
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
import java.util.Calendar


class KuralEklemeFragment : Fragment() {
    private lateinit var kural1: EditText
    private lateinit var kural2: EditText
    private lateinit var kuralBaslangic: TextView
    private lateinit var kuralBitis: TextView
    private lateinit var myButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                       // todo: burada action'a gitmeyi yap
                       Toast.makeText(requireContext(),"Kuraliniz basariyla kaydedildi",Toast.LENGTH_SHORT).show()

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
                            // kuralim dogrulandi, action islemini yapabilirim
                            // todo: burada action'a gitmeyi yap
                            Toast.makeText(requireContext(),"Kuraliniz basariyla kaydedildi",Toast.LENGTH_SHORT).show()

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
                        // TODO:
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
}