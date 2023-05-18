package com.example.mixmate2.ui.settings

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mixmate2.R


class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val spinner: Spinner? = getView()?.findViewById(R.id.sizes_spinner)


        val root: View = inflater.inflate(R.layout.fragment_settings, container, false)

        val linkTextView = root.findViewById<TextView>(R.id.textView4);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());

        activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        return root
    }



}