package com.mustafa.prizprojem

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.Navigation
import com.mustafa.prizprojem.modelView.LoginModelView
import com.mustafa.prizprojem.models.UserInfo
import com.mustafa.prizprojem.models.UserResponse
import com.mustafa.prizprojem.services.RetrofitObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.Exception


class LoginEkraniFragment : Fragment() {
    lateinit var dataStorePreferences: DataStore<Preferences>
    lateinit var sharedPref : SharedPreferences

    private lateinit var usernameTextView: TextView
    private lateinit var passwordTextView: TextView
    private lateinit var myButton: Button
   // private val myViewModel = LoginModelView() // tanımlanmış
   private val myAPIService = RetrofitObject
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login_ekrani, container, false)
        usernameTextView = view.findViewById(R.id.usernameText)
        passwordTextView = view.findViewById(R.id.passwordText)
        myButton = view.findViewById(R.id.loginButton)






        myButton.setOnClickListener {
            val username = usernameTextView.text.toString()
            val password = passwordTextView.text.toString()

            if (username.isBlank() == false || password.isBlank()==false) {
                // Kullanıcı adı ve şifre dolu, bu durumu işle
                CoroutineScope(Dispatchers.Main).launch {
                    loginPostFun2(username,password) // apimi cagirdim
                    delay(200)
                    val token = sharedPref.getString("token", "")

                    if (token!=null){
                        val action = LoginEkraniFragmentDirections.actionLoginEkraniFragmentToAnaSayfaFragment()
                        Navigation.findNavController(requireView()).navigate(action)
                    }
                    else{
                        println("benim token ${token}")
                        Toast.makeText(requireContext(),"Lutfen kullanıcı adınızı veya şifrenizi kontrol ediniz!",Toast.LENGTH_SHORT).show()
                    }

                    try {
                        veriyiCek()
                    }catch (e : Exception){
                        println("veriyi cekerken cath'e yakalandin")
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Kullanıcı adı veya Şifre boş olamaz", Toast.LENGTH_SHORT).show()
            }
        }


        return view
    }


/*
    fun loginPostFun(username: String, password: String){
        var myUserInfo : UserInfo = UserInfo(username,password)
        //         Call<UserResponse> call = retrofitLoginService.login(userInfo);
        var call : Call<UserResponse> = myViewModel.apiService.postLogin(myUserInfo)

        call.enqueue(object : retrofit2.Callback<UserResponse>{

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {

                // cevap geldi demektir
                println("nullsa cevap bos geldi  ${response.body()}")
                if(response.body()!= null){
                    val userResponse: UserResponse = response.body()!!
                    // tokeni sql'e gömeceksin, ve aktiviteyi değiştireceksin
                    println("tokenim geldi : ${userResponse.accessToken}\n bu da tokenimdi")
                    val action = LoginEkraniFragmentDirections.actionLoginEkraniFragmentToAnaSayfaFragment()
                    Navigation.findNavController(requireView()).navigate(action)
                }
                else{
                    Toast.makeText(requireContext(), "Kullanici bilgileri hatali",Toast.LENGTH_SHORT).show()
                }


            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {

                Toast.makeText(requireContext(), "Server'a ulasmadi vermiyor",Toast.LENGTH_SHORT).show()

            }

        })
    }

 */

    fun loginPostFun2(username: String, password:String){
        var myUserInfo : UserInfo = UserInfo(username,password)
        val call: Call<UserResponse> = myAPIService.apiService.postLogin(myUserInfo)
        //  myViewModel.apiService.postLogin(myUserInfo) bu kısımda zaten isteğimi gönderiyorum
        try {
            call.enqueue(object : Callback<UserResponse>{
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    println("Cevap geldi")
                    //println("cavapt ${response.body()}")
                    response.body()?.let {
                        println("tokenim ${it.accessToken}")
                        //myToken = it.accessToken
                        //veriyiKaydet(it.accessToken)
                        with(sharedPref.edit()) {
                            // "token" adlı değişkeni string olarak kaydet
                            putString("token", it.accessToken)

                            // Değişiklikleri kaydet
                            apply()
                        }

                    }
                    // burada gelen cevabı sql'e gömücem ve fragment değiştiricem 
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    println("Cevap gelmedi")
                }

            })
        }
        catch (e: Exception){
            println("Hata cath blogunda ")
        }
    }

fun veriyiKaydet(token : String){


    if (token == ""){
        Toast.makeText(requireContext(),"Kullanıcı tokeniniz kaydedilemedi!", Toast.LENGTH_SHORT).show()
    }
    else{
        //sharedPreferences.edit().putString("token", token).apply()
    }
}

    fun veriyiCek(){

    }

}

