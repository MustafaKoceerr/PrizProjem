package com.mustafa.prizprojem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.mustafa.prizprojem.models.UserRegisterInfo
import com.mustafa.prizprojem.models.UserRegisterResponse
import com.mustafa.prizprojem.services.RetrofitObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterEkraniFragment : Fragment() {

    // Şehir ve İl listeleri
    //val cityList = getTurkishCities()
    //private val myViewModel = RegisterModelView()
    private val myAPIService = RetrofitObject

    private lateinit var myButton: Button
    private lateinit var textUsername: EditText
    private lateinit var textPassword: EditText
    var sehirIlceGlobal: ArrayList<List<String>> = ArrayList()


    private lateinit var textProvince: AutoCompleteTextView
    private lateinit var textCity: AutoCompleteTextView

    var myIlList: ArrayList<String> = ArrayList()
    var myIlceList: ArrayList<List<String>> = ArrayList()
    var flag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register_ekrani, container, false)
        // Inflate the layout for this fragment
        // adapter tanımlıyorum


        myButton = view.findViewById(R.id.buttonRegister)
        textUsername = view.findViewById(R.id.usernameText)
        textPassword = view.findViewById(R.id.passwordText)
        textCity = view.findViewById(R.id.AutoCompleteTextViewCity)
        textProvince = view.findViewById(R.id.AutoCompleteTextViewProvince)


        // statik bir yerden veriyi çekeceği için yani veriler zaten öntanımlı ve oradan çekecek,
        // bu yüzden oncreate'de bir kere çekmesi yeterli


        CoroutineScope(Dispatchers.Main).launch {
            try {
                ilIlceGetir()
                //println(sehirIlceGlobal.isEmpty())
                delay(1000)
                //println(sehirIlceGlobal.isEmpty())
                sehirIlceGlobal.forEach {
                    //println("listemde ${it.get(0)}")
                    myIlList.add(it.get(0))
                    //println(it.drop(1))
                    myIlceList.add(it.drop(1))
                }
                val adapter: ArrayAdapter<String> = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    myIlList
                )
                textCity.setAdapter(adapter)
                textCity.threshold = 0
                textCity.setOnClickListener {
                    textCity.showDropDown()
                }

                textCity.setOnItemClickListener { parent, view, position, id ->
                    val selectedText: String? = parent.getItemAtPosition(position).toString()
                    //println("secilen text : ${selectedText}")
                    //println("secilen text id ${position}")
                    if (selectedText != null) {
                        ilceSectir(position)
                    }
                }
            } catch (e: Exception) {
                println("Coroutine'de hata var")
            }
        }
        // Adapter'a yeni veri kümesini ayarla
        // Adapter'ın güncellendiğini belirtmek için notifyDataSetChanged yöntemini çağır


        myButton.setOnClickListener {
            //println("register butona tiklandi")

            var tx1 = textUsername.text.toString()
            var tx2 = textPassword.text.toString()
            var tx3 = textCity.text.toString()
            var tx4 = textProvince.text.toString()
            //println("${tx1} ${tx2}  ${tx3}  ${tx4} ")
            if (tx1.isBlank() == false && tx2.isBlank() == false && tx3.isBlank() == false && tx4.isBlank() == false) {
                try {
                    CoroutineScope(Dispatchers.Main).launch {
                        registerPostFun(tx1, tx2, tx3, tx4)
                        delay(250)
                        Toast.makeText(
                            requireContext(),
                            "Basariyla kayit oldunuz, giris ekranina yonlendiriliyorsunuz",
                            Toast.LENGTH_SHORT
                        ).show()


                        Navigation.findNavController(requireView()).navigate(
                            R.id.action_registerEkraniFragment_to_loginEkraniFragment,
                            null,
                            NavOptions.Builder().setPopUpTo(R.id.registerEkraniFragment, true).build()
                            // Geri yığından fragment'ı kaldır
                        )

                    }// try
                } catch (e: Exception) {
                    println("Buton kisminda cath'e yakalandi, kayit basarisiz")
                }// catch

            }// if
                else {
                    Toast.makeText(
                        requireContext(),
                        "Kayit olmak için tüm boşlukları doldurmalısınız.",
                        Toast.LENGTH_SHORT
                    ).show()

                }// else

        } // buton set on click listener

        return view
    } // view fonk

    private fun ilceSectir(myIndex: Int) {

        textProvince.text = null // eger il'i degistirirse sıfırlamayi sagliyor

        textProvince.visibility = View.VISIBLE
        val secilenIlinIlceArrayi = myIlceList.get(myIndex)
        val adapter2: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            secilenIlinIlceArrayi
        )

        textProvince.setAdapter(adapter2)
        textProvince.threshold = 0
        textProvince.setOnClickListener {
            textProvince.showDropDown()
        }

        textProvince.setOnItemClickListener { parent, view, position, id ->
            val selectedText = parent.getItemAtPosition(position).toString()
            println("secilen ilce ${selectedText}")
        }

    }


    fun registerPostFun(username: String, password: String, city: String, province: String) {
        var myRegisterInfo: UserRegisterInfo = UserRegisterInfo(username, password, city, province)
        //println("Buraya kadar geldim")
        var call: Call<UserRegisterResponse> = myAPIService.apiService.postRegister(myRegisterInfo)
        // burada api'ye istek atıyor

        try {
            call.enqueue(object : Callback<UserRegisterResponse> {
                override fun onResponse(
                    call: Call<UserRegisterResponse>,
                    response: Response<UserRegisterResponse>
                ) {
                    //println("Kayit olma cevap geldi")
                    //println("response body= ${response.body()}")
                    response.body()?.let {
                        //println("Donen cevap msg ${it.msg}")
                        //println("Donen cevap username ${it.username}")
                        //println("Donen cevap password ${it.password}")

                    }
                }
                override fun onFailure(call: Call<UserRegisterResponse>, t: Throwable) {
                    println("Apı'den cevap gelmedi")
                }
            })
        } catch (e: Exception) {
            println("registerPostFun'da catch'e yakalandın, api'ye istek gitmedi")
        }
    } // registerPostFun


     fun ilIlceGetir() {

        var tempIlIlce: ArrayList<String> = ArrayList()
        var tempAll: ArrayList<List<String>> = ArrayList()

        try {
            var call: Call<List<Map<String, Any>>> = myAPIService.apiServiceGitHub.getIlceListesi()
            // bunun ile isteğimi gönderdim myAPIService.apiServiceGitHub.getIlceListesi()
            //println("ilIlceGetir call cagirildi")
            call.enqueue(object : Callback<List<Map<String, Any>>> {
                override fun onResponse(
                    call: Call<List<Map<String, Any>>>,
                    response: Response<List<Map<String, Any>>>
                ) {
                    //println("ilIlceGetir cevap geldi")
                    //println(" gelen : ${response.body()}")
                    response.body()?.let {
                        it.forEach {
                            //println(it)
                            // illerin adini veriyor tek tek
                            //println(it["il_adi"])
                            tempIlIlce.add(it["il_adi"].toString())
                            // illeri array listime ekliyorum
                            //println(it["ilceler"])
                            if (it["ilceler"] != null) {
                                val listData = it["ilceler"] as List<Map<String, *>>
                                //println("listdatam: ${listData}")
                                listData.forEach {
                                    //println(it)
                                    // ilcelerin adini veriyor tek tek
                                    //println("ilce adim: ${it["ilce_adi"]}")
                                    tempIlIlce.add(it["ilce_adi"].toString())
                                    // ilçeleri de illeri eklediğim array listime ekliyorum
                                }
                                val musti = tempIlIlce.toList()
                                tempAll.add(musti)
                                tempIlIlce.clear()
                                // arraylist<arraylist<string>>'e ekliyorum
                            } // end of the if
                            // il elimizde ve ilceler
                        }// end of the foreach
                        sehirIlceGlobal = tempAll
                        flag = true
                        //println("ilceleri ve illeri aldim")
                    }
                }// on response bitis

                override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
                    println("ilIlceGetir Cevap gelmedi")
                } // on failure bitis
            })
        }
        catch (e: Exception){
            println("ilIlceGetir'de catch'e yakalandın, api'ye istek gitmedi")
        }
    }// ilİlce getir bitis


}// main bitis
