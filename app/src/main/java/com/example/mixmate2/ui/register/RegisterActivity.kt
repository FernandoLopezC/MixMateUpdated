package com.example.mixmate2.ui.register

import com.example.mixmate2.R
import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.mixmate2.ui.settings.SettingsFragment
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class RegisterFragment : androidx.fragment.app.Fragment() {

    private val url = "http://192.168.202.1:5000/"

    private val POST = "POST"
    private val GET = "GET"
    private val client = OkHttpClient()

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val root: View = inflater.inflate(R.layout.fragment_register, container, false)
        activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val loginScreen = root.findViewById<View>(R.id.link_to_login) as TextView

        val user = root.findViewById<TextView>(R.id.reg_fullname)
        val email = root.findViewById<TextView>(R.id.reg_email)
        val pw = root.findViewById<TextView>(R.id.reg_password)
        val registerButton = root.findViewById<Button>(R.id.btnRegister)

        loginScreen.setOnClickListener {
            getParentFragmentManager().popBackStack();
        }


        registerButton.setOnClickListener {
            val curEmail: String = email.getText().toString()
            val password: String = pw.getText().toString()
            val userName: String = user.getText().toString()
            var content: JSONObject

            if(!(curEmail.isEmpty() || password.isEmpty() || userName.isEmpty())){

                val request_url = url+"register_user/"+userName+"/"+curEmail+"/"+password
                println(request_url)
                val request = Request.Builder()
                    .url(request_url)
                    .build()
//                client.newCall(request).execute().use { response ->
//                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
//                    println(response.toString())
//                }
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        return
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (!response.isSuccessful){
                            return
                        }

                        try {
                            content = JSONObject(response.body?.string() ?: "")
                            activity?.runOnUiThread {
                                Toast.makeText(activity?.applicationContext, "Login Successful", Toast.LENGTH_SHORT)
                                    .show()
                                val navController = root.findNavController()
                                navController.navigate(R.id.nav_home)
                            }

                        }
                        catch (e: JSONException) {
                            // Error parsing JSON object
                        }
                    }

                })

            }


        }
        return root
    }


    }
