package com.mustafa.prizprojem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView


class AnaSayfaFragment : Fragment() {

    private lateinit var myToolbar: Toolbar
    private lateinit var myRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(/* hasMenu = */ true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ana_sayfa, container, false)
        myToolbar = view.findViewById(R.id.toolbar)

        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(myToolbar)


        return view
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
                else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}