package com.mustafa.prizprojem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class KuralGosterFragment : Fragment() {
    private lateinit var myTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_kural_goster, container, false)
        myTextView = view.findViewById(R.id.kuralGosterText)

        val receivedValue = arguments?.getInt("kuralPosition")
        myTextView.text = receivedValue.toString()

        // Inflate the layout for this fragment
        return view
    }

}