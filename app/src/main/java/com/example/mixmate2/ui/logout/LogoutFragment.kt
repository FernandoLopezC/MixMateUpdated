package com.example.mixmate2.ui.logout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.mixmate2.R
import com.example.mixmate2.ui.settings.SettingsFragment

class LogoutFragment : Fragment() {

    companion object {
        fun newInstance() = LogoutFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        val root: View = inflater.inflate(R.layout.fragment_logout, container, false)


        activity?.supportFragmentManager?.setFragmentResult("premium", bundleOf("bundleKey" to false))
        activity?.supportFragmentManager?.setFragmentResult("login", bundleOf("bundleKey2" to false))

        return root
    }



}