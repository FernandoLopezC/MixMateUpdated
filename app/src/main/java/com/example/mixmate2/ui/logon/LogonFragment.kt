package com.example.mixmate2.ui.logon

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.example.mixmate2.R
import com.google.android.material.navigation.NavigationView
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class LoginFragment : Fragment() {

    private val url = "http://192.168.202.1:5000/"

    private val POST = "POST"
    private val GET = "GET"
    private val client = OkHttpClient()
    private var contextOfApplication: Context? = null

    fun getContextOfApplication(): Context? {
        return contextOfApplication
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_login, container, false)

        activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // Inflate the layout for this fragment
        val registerScreen = rootView.findViewById<View>(R.id.link_to_register) as TextView


        val user = rootView.findViewById<TextView>(R.id.user_email)
        val pw = rootView.findViewById<TextView>(R.id.user_pw)
        val loginButton = rootView.findViewById<Button>(R.id.btnLogin)

        registerScreen.setOnClickListener { // Switching to Register screen
            val navController = rootView.findNavController()
            navController.navigate(R.id.nav_register)

        }

        loginButton.setOnClickListener {
            val email: String = user.getText().toString()
            val password: String = pw.getText().toString()
            var content: JSONObject
            if(!(email.isEmpty() || password.isEmpty())){

                val request_url = url+"login_user/"+email+"/"+password
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
                        loginUser(null, null)
                        return
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (!response.isSuccessful){
                            return
                        }

                        try {
                            content = JSONObject(response.body?.string() ?: "")
                            loginUser(content, rootView)

                            }
                        catch (e: JSONException) {
                            // Error parsing JSON object
                        }
                    }

                })

            }
        }

        return rootView
    }


    fun loginUser(content: JSONObject?, rootView: View?): Boolean{

        if (content?.get("login")  == true) {
            activity?.runOnUiThread {
                Toast.makeText(activity?.applicationContext, "Login Successful", Toast.LENGTH_SHORT).show()


                val navController = rootView?.findNavController()
                navController?.navigate(R.id.nav_home)

                activity?.supportFragmentManager?.setFragmentResult("premium", bundleOf("bundleKey" to content["premium"]))
                activity?.supportFragmentManager?.setFragmentResult("login", bundleOf("bundleKey2" to content["login"]))

            }
        }
        else{
            getActivity()?.runOnUiThread {
                Toast.makeText(activity?.applicationContext, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
        return false

    }
    override fun onAttach(activity: Activity) {

        super.onAttach(activity)

    }

    override fun onDetach() {
        super.onDetach()
    }

}