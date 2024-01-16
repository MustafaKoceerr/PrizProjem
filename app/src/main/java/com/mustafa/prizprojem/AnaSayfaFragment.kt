package com.mustafa.prizprojem

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mustafa.prizprojem.adapter.RecyclerAdapter
import com.mustafa.prizprojem.services.RetrofitObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AnaSayfaFragment : Fragment() {
    lateinit var sharedPref : SharedPreferences

    private lateinit var myToolbar: Toolbar
    private lateinit var myRecyclerView: RecyclerView
    private val myAPIService = RetrofitObject
    private lateinit var bosButton: Button
    private lateinit var popUpWindow : PopupWindow
    private lateinit var myYardimTextView : TextView

    private var kurallarListem : ArrayList<Map<String,Any>> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return

        setHasOptionsMenu(/* hasMenu = */ true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ana_sayfa, container, false)
        myYardimTextView = view.findViewById(R.id.yardimTextView)
        myYardimTextView.visibility = View.GONE

        myToolbar = view.findViewById(R.id.toolbar)
        myRecyclerView = view.findViewById(R.id.recyclerView)
        popUpWindow = PopupWindow(requireContext())
        val myView = layoutInflater.inflate(R.layout.popup_window,null)
        popUpWindow.contentView = myView

        bosButton = view.findViewById(R.id.bosButton)

        bosButton.visibility = View.GONE

        bosButton.setOnClickListener {
            getRulesArray()
            val token = sharedPref.getString("token", "")
            println("benim anasayfa tokenim: ${token}")

        }

        var myTempArray : ArrayList<Map<String,Any>>
        CoroutineScope(Dispatchers.Main).launch {
            val layoutManager = LinearLayoutManager(requireContext())
            myRecyclerView.layoutManager = layoutManager
            getRulesArray() // apimi cagirdim
            delay(800)
            myTempArray = kurallarListem.clone() as ArrayList<Map<String, Any>>
            yardimGoster(myTempArray)
            val adapter = RecyclerAdapter(myTempArray,sharedPref)
            myRecyclerView.adapter = adapter
            delay(100)
            kurallarListem.clear()

        }


        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(myToolbar)

        // ADAPTER'A VERİ GONDERME

        val existingList = listOf("Eleman 1", "Eleman 2", "Eleman 3","Eleman 4","Eleman 5")

        val tempArrayList = ArrayList<String>()
        tempArrayList.addAll(existingList)
        // YAPILACAKLAR: KURALLARI İNTERNETTEN ÇEKİP ARRAY LİSTE ATIP, GEREKLİ İŞLEMLERİ ADAPTER KISMINDA DÜZENLE


        return view
    }

    private fun yardimGoster(tanimliKurallar :ArrayList<Map<String, Any>>) {
        if (tanimliKurallar.isEmpty()){
            myYardimTextView.visibility = View.VISIBLE
            myRecyclerView.visibility = View.GONE
        }
        else{
            myYardimTextView.visibility = View.GONE
            myRecyclerView.visibility = View.VISIBLE
        }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.my_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.kural1 -> {
                // Menü öğesine tıklandığında buraya gelecek kodu ekleyin
                println("Menü 1öğesine tıklandı")
                val action = AnaSayfaFragmentDirections.actionAnaSayfaFragmentToKuralEklemeFragment(1)
                Navigation.findNavController(requireView()).navigate(action)

            }

            R.id.kural2 -> {
                // Menü öğesine tıklandığında buraya gelecek kodu ekleyin
                println("Menü 2öğesine tıklandı")
                val action = AnaSayfaFragmentDirections.actionAnaSayfaFragmentToKuralEklemeFragment(2)
                Navigation.findNavController(requireView()).navigate(action)
            }

            R.id.kural3 -> {
                // Menü öğesine tıklandığında buraya gelecek kodu ekleyin
                println("Menü 3öğesine tıklandı")
                val action = AnaSayfaFragmentDirections.actionAnaSayfaFragmentToKuralEklemeFragment(3)
                Navigation.findNavController(requireView()).navigate(action)
                // Diğer menü öğelerini burada kontrol edebilirsiniz
            }
            R.id.yardimMenu -> {
                // Menü öğesine tıklandığında buraya gelecek kodu ekleyin
                println("yardim menu öğesine tıklandı")
                showPopupWindow()
                myRecyclerView.visibility = View.INVISIBLE
                myYardimTextView.visibility = View.INVISIBLE

                // Diğer menü öğelerini burada kontrol edebilirsiniz
            }

                else -> return super.onOptionsItemSelected(item)
        }
        return true
    }


    fun getRulesArray(){
        val token = sharedPref.getString("token", "")

        //var myToken = "BEARER eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOjgsInVzZXJuYW1lIjoiYWhtZXQiLCJjaXR5IjoiIiwicHJvdmluY2UiOiIiLCJpYXQiOjE3MDUyMzM3OTgsImV4cCI6MTcwNTQwNjU5OH0.aAay2IjaDkcdTJN28soB_mKqIYsMufsvgOU9lh2fff0"
        token?.let {tokenn->

            try {
                var call : Call<List<Map<String,Any>>> = myAPIService.apiService.getRuleArray("BEARER ${tokenn}")

                call.enqueue(object : Callback<List<Map<String,Any>>>{
                    override fun onResponse(
                        call: Call<List<Map<String, Any>>>,
                        response: Response<List<Map<String, Any>>>
                    ) {
                        println("Cevap geldi")
                        println("response body'im = ${response.body()}")
                        response.body()?.let {
                                listem ->
                            println("Cevap icerden")

                            listem.forEach {
                                //println("deneme yapıyorum ${it["rule"]!!::class}")
                                //println("deneme yapıyorum ${it["value"]} ve idm ${it["id"]}")
                                kurallarListem.add(it)
                                //println("size ${kurallarListem.size}")

                            }
                        }
                    }

                    override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
                        println("Cevap gelmedi")
                    }

                })
            }catch (e: Exception){
                println("Catch'e yakalandın koçum")
            }
        }
    }

    private fun showPopupWindow() {
        val popupStr= "Merhaba! Kural tanımlama ekranına hoş geldin.\n\nBurada kurallarını tanımlayabilir ve tanımladığın kuralları görebilirsin.\n\n" +
                "Kural tanımlamak için sağ üstte bulunan üç noktalı simgeye tıklayabilirsin. Her kuralın bir işlevi var.\n\n" +
                "1- Bu kuralda şehrinin sıcaklığı, tanımladığın sıcaklığın altına düşerse cihaz aktifleşir.\n\n" +
                "2- Bu kuralda şehrinin sıcaklığı, tanımladığın sıcaklığın üstüne çıkarsa cihaz aktifleşir.\n\n" +
                "3- Bu kuralda seçtiğin iki saat arası cihaz aktifleşir."
        val popupView = layoutInflater.inflate(R.layout.popup_window, null)
        val popupTextView = popupView.findViewById<TextView>(R.id.popupText)
        popupTextView.text = popupStr
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,

            true
        )

        popupWindow.setOnDismissListener {
            // PopupWindow kapatıldığında RecyclerView'ı tekrar görünür yap
            myRecyclerView.visibility = View.VISIBLE
            myYardimTextView.visibility = View.VISIBLE
        }




        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true


        popupWindow.showAtLocation(requireView(), Gravity.CENTER, 0, 0)

    }


}// fragment sonu