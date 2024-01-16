package com.mustafa.prizprojem

import android.animation.ObjectAnimator
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
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


class LoginEkraniFragment : Fragment() {
    lateinit var dataStorePreferences: DataStore<Preferences>
    lateinit var sharedPref : SharedPreferences

    private lateinit var myRegisterButton : Button
    private lateinit var usernameTextView: TextView
    private lateinit var passwordTextView: TextView
    private lateinit var myButton: Button
    private lateinit var myProgressBar: ProgressBar
    private var rotation : ObjectAnimator? = null

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
        myProgressBar = view.findViewById(R.id.progressBar)
        usernameTextView = view.findViewById(R.id.usernameText)
        passwordTextView = view.findViewById(R.id.passwordText)
        myButton = view.findViewById(R.id.loginButton)
        myRegisterButton = view.findViewById(R.id.registerButton)

        rotation = ObjectAnimator.ofFloat(myProgressBar, "rotation", 0f, 360f)


        myRegisterButton.setOnClickListener {
            val action = LoginEkraniFragmentDirections.actionLoginEkraniFragmentToRegisterEkraniFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }

        myButton.setOnClickListener {
            val username = usernameTextView.text.toString().trim()
            val password = passwordTextView.text.toString().trim()

            if (username.isBlank() == false || password.isBlank()==false) {
                // Kullanıcı adı ve şifre dolu, bu durumu işle
                CoroutineScope(Dispatchers.Main).launch {
                    loginPostFun(username,password) // apimi cagirdim
                    progressBarGoster()
                    delay(1000)
                    progressBarGizle()

                    val token : String? = veriyiCek()

                    if (token != ""){
                        // geri donulmeyecek şekilde geçiş yap
                        Navigation.findNavController(requireView()).navigate(
                            R.id.action_loginEkraniFragment_to_anaSayfaFragment,
                            null,
                            NavOptions.Builder().setPopUpTo(R.id.loginEkraniFragment, true).build()
                            // Geri yığından fragment'ı kaldır
                        )
                    }
                    else{
                        //println("benim token ${token}")
                        Toast.makeText(requireContext(),"Lutfen kullanıcı adınızı veya şifrenizi kontrol ediniz!",Toast.LENGTH_SHORT).show()
                    }


                }
            } else {
                Toast.makeText(requireContext(), "Kullanıcı adı veya Şifre boş olamaz", Toast.LENGTH_SHORT).show()
            }
        }




        CoroutineScope(Dispatchers.Main).launch {
            var kosul = veriyiCek()!=""

            progressBarGoster()
            delay(180)

            if (kosul) {
                // Daha önce giriş yapılmışsa, AnasayfaFragment'a git
                Navigation.findNavController(requireView()).navigate(
                    R.id.action_loginEkraniFragment_to_anaSayfaFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.loginEkraniFragment, true).build()
                    // Geri yığından fragment'ı kaldır
                )
            }

            progressBarGizle()
        }




        return view
    }// end of the view


    fun closeFragment() {
        // FragmentManager'ı al
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager

        // İlgili fragment'ı bul
        val fragment: Fragment? = fragmentManager.findFragmentByTag("LoginEkraniFragment")

        // Eğer fragment bulunduysa, transaction başlat ve fragment'ı kaldır
        fragment?.let {
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.remove(it)
            transaction.commit()
        }
    }

    fun loginPostFun(username: String, password:String){
        var myUserInfo : UserInfo = UserInfo(username,password)
        val call: Call<UserResponse> = myAPIService.apiService.postLogin(myUserInfo)
        //  myViewModel.apiService.postLogin(myUserInfo) bu kısımda zaten isteğimi gönderiyorum
        try {
            call.enqueue(object : Callback<UserResponse>{
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    println("Cevap geldi")
                    //println("cavapt ${response.body()}")
                    if (response.isSuccessful) {
                        // İşlem başarılı, belki bir başarı mesajını alabilirsiniz.
                        println("Başarıyla girdi")
                        //println(response.body())

                        response.body()?.let {
                            println("tokenim ${it.accessToken}")
                            //myToken = it.accessToken
                            //veriyiKaydet(it.accessToken)
                            veriyiKaydet(it.accessToken)

                        } // end of the let
                    } // end of the if
                    else {
                        println("islemBasarisiz")
                        // İşlem başarısız, hata mesajını alabilirsiniz.
                        veriyiKaydet("")
                    }
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

fun veriyiKaydet(myStr : String){
    with(sharedPref.edit()) {
        // "token" adlı değişkeni string olarak kaydet
        putString("token", myStr)
        // Değişiklikleri kaydet
        apply()
    }
}

    fun veriyiCek(): String?{
        return sharedPref.getString("token", "")
    }



    fun progressBarGoster(){
        myProgressBar.visibility = View.VISIBLE
        if (rotation != null){
            rotation!!.duration = 1000 // Animasyon süresi (milisaniye cinsinden), isteğe bağlı
            rotation!!.repeatCount = ObjectAnimator.INFINITE // Sonsuz tekrar
            rotation!!.start()
        }
        usernameTextView.visibility = View.INVISIBLE
        passwordTextView.visibility = View.INVISIBLE
        myButton.visibility = View.GONE
        myRegisterButton.visibility = View.GONE

    }

    fun progressBarGizle() {
        myProgressBar.visibility = View.GONE
        if (rotation != null){
            rotation!!.end()
        }
        usernameTextView.visibility = View.VISIBLE
        passwordTextView.visibility = View.VISIBLE
        myButton.visibility = View.VISIBLE
        myRegisterButton.visibility = View.VISIBLE


    }

    }

