package com.mustafa.prizprojem.adapter

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.mustafa.prizprojem.AnaSayfaFragmentDirections
import com.mustafa.prizprojem.R
import com.mustafa.prizprojem.models.DeleteInfo
import com.mustafa.prizprojem.services.RetrofitObject
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Response


// ArrayList<Map<String,Any>>
class RecyclerAdapter(val kurallarListesi: ArrayList<Map<String, Any>>,
                      sharedPref : SharedPreferences) :
    RecyclerView.Adapter<RecyclerAdapter.KurallarViewHolder>() {
        var mySharedPreferences = sharedPref

    private val myAPIService = RetrofitObject

    class KurallarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // gorunumumuz bizim oluşturduğumuz row oluyor onunla birlikte türetilecek görünüm
        // HER BİR RECYCLER ROW'A İTEM VİEW DİYORUZ

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KurallarViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.reycler_row, parent, false)


        return KurallarViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return kurallarListesi.size
        // bu size kadar veriyi gosterecek bize
    }

    private lateinit var birinciText: TextView
    private lateinit var ikinciText: TextView
    private lateinit var kuralTipiText: TextView
    private lateinit var silButton: Button


    override fun onBindViewHolder(holder: KurallarViewHolder, position: Int) {
        kuralTipiText = holder.itemView.findViewById(R.id.kuralText)
        birinciText = holder.itemView.findViewById(R.id.dereceVeyaText)
        ikinciText = holder.itemView.findViewById(R.id.saatText)
        silButton = holder.itemView.findViewById(R.id.silButton)

        val myMap = kurallarListesi.get(position)
        // HER BİR RECYCLER ROW'A İTEM VİEW DİYORUZ
        kuralTipiText.text = "Kural Tipi : ${myMap["rule"].toString().toFloat().toInt()}"

        if (myMap["rule"].toString().equals("1.0")) {
            birinciText.text = "Min sıcaklık: ${myMap["value"]}"
            ikinciText.visibility = View.GONE

        } else if (myMap["rule"].toString().equals("2.0")) {
            birinciText.text = "Max sıcaklık: ${myMap["value"]}"
            ikinciText.visibility = View.GONE

        } else if (myMap["rule"].toString().equals("3.0")) {
            //birinciText.text = "ucuncu : ${myMap["value"]}"
            //println(myMap["value"])
            val value = myMap["value"].toString()
            //println("valuem ${value}")
            val json = JSONObject(value)
            val sDateDegeri = json.getString("s_date")
            val eDateDegeri = json.getString("e_date")

            //println("mymap : ${myMap["id"]}")
            birinciText.text = "Baslangic: ${sDateDegeri}"
            ikinciText.text = "Bitis: ${eDateDegeri}"
        }

        val tx1 = kuralTipiText.text.toString()
        val tx2 = birinciText.text.toString()
        val tx3 = ikinciText.text.toString()

        // FRAGMENT'TA KURALLARI ÇEKİP BURAYA ARRAY LİST İLE AT VE KURALA GÖRE GÖSTERİLECEK ŞEYLERİ AÇ

        holder.itemView.setOnClickListener {
            val action = AnaSayfaFragmentDirections.actionAnaSayfaFragmentToKuralGosterFragment(
                kuralTip = tx1,
                kuralText1 = tx2,
                kuralText2 = tx3
            )
            Navigation.findNavController(it).navigate(action)
        }

        silButton.setOnClickListener {
            println("kural'ımın id'si ${myMap["id"].toString().toFloat().toInt()}")
            println("sınıfım ${myMap["id"].toString().toFloat().toInt()::class}")
            deleteRule(myMap["id"].toString().toFloat().toInt())

            val adapterPosition = holder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {

                   kurallarListesi.removeAt(adapterPosition)
                   notifyItemRemoved(adapterPosition)
                   println("Pozisyon $adapterPosition")
                   println("Eleman sayısı ${kurallarListesi.size}")


                // belki bir popup verilebilir
                true
            } else {
                false
            }
        }
    }// fonskiyon bitis

    fun deleteRule(ruleId: Int){
        val token = mySharedPreferences.getString("token", "")
        //var myToken = "BEARER eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOjgsInVzZXJuYW1lIjoiYWhtZXQiLCJjaXR5IjoiIiwicHJvdmluY2UiOiIiLCJpYXQiOjE3MDUyMzM3OTgsImV4cCI6MTcwNTQwNjU5OH0.aAay2IjaDkcdTJN28soB_mKqIYsMufsvgOU9lh2fff0"
        var temp = DeleteInfo(ruleId)

        token?.let { tokenn->
            val call: retrofit2.Call<Map<String,Any>> = myAPIService.apiService.postDeleteRule("BEARER ${tokenn}" ,temp)
            try {
                call.enqueue(object : Callback<Map<String,Any>>{
                    override fun onResponse(
                        call: retrofit2.Call<Map<String,Any>>,
                        response: Response<Map<String,Any>>
                    ) {
                        if (response.isSuccessful) {
                            // İşlem başarılı, belki bir başarı mesajını alabilirsiniz.
                            println("Başarıyla silindi")
                            println(response.body())
                        } else {
                            // İşlem başarısız, hata mesajını alabilirsiniz.
                            try {
                                val errorResponse = JSONObject(response.errorBody()?.string())
                                val errorMessage = errorResponse.getString("errorMessage")
                                println("Hata mesajı: $errorMessage")
                            } catch (e: Exception) {
                                // JSON parse hatası
                                println("Hata mesajı okunamadı.")
                                e.printStackTrace()
                            }
                        }

                    }

                    override fun onFailure(call: retrofit2.Call<Map<String,Any>>, t: Throwable) {
                        println("cevap gelmedi")

                    }

                })
            }catch (e:Exception){
                println("DELETE, TRY CATCH'E YAKALANDIN ${e.printStackTrace()}")
            }
        }


    }

}