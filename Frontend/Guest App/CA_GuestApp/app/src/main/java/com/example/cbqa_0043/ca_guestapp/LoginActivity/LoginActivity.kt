package com.example.cbqa_0043.ca_guestapp.LoginActivity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.Toast
import com.example.cbqa_0043.ca_guestapp.MainActivity.MainActivity
import com.example.cbqa_0043.ca_guestapp.R
import com.example.cbqa_0043.ca_guestapp.Services.LoginService
import com.example.cbqa_0043.ca_guestapp.Services.UserRequest
import com.example.cbqa_0043.ca_guestapp.Services.UserResponse
import com.example.cbqa_0043.ca_guestapp.SignUpActivity.Signup
import com.example.cbqa_0043.ca_guestapp.Utils.Utils
import kotlinx.android.synthetic.main.activity_login.*
import java.net.SocketTimeoutException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnRegister.setOnClickListener{
            val intent = Intent(this, Signup::class.java)
            startActivity(intent);
        }
        btnLogin.setOnClickListener{
            //Validar entrada


            var objLoginApiSolicitud : UserRequest? = UserRequest(etEmail.text.toString(),etPassword.text.toString())
            snackbar = Snackbar.make(
                rootLoginLayout, // Parent view
                "Autenticando...", // Message to show
                Snackbar.LENGTH_INDEFINITE // How long to display the message.
            )
            snackbar.show()
            //validaciones de string
            if (objLoginApiSolicitud != null) {
                login(objLoginApiSolicitud, this.baseContext)
            }

        }
        utils = Utils()
    }
    var retrofit: Retrofit? = null
    var utils:Utils? = null
    val BaseURL = "http://akarokhome.ddns.net:3000/v1/"
    lateinit var snackbar: Snackbar
    fun login(user: UserRequest, context: Context){

        val retrofit = Retrofit.Builder().baseUrl(Utils.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()



        //Variable declaration
        var mAPIService: LoginService? = retrofit.create(LoginService::class.java)

        //After oncreate

        //Some Button click
        var activity = this

        mAPIService!!.doLogin(user).enqueue(object : Callback<UserResponse> {

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {


                if (response.isSuccessful()) {
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(context, "Bienvenido " + response.body()!!.data.firstName.toUpperCase(), Toast.LENGTH_SHORT).show()

                    //We save our  user object

                    val sharedPreferences = activity.getSharedPreferences("mx.com.colonyadmin.android", Context.MODE_PRIVATE)
                    sharedPreferences.edit().putString("EmailSTRING", response.body()!!.data.email).apply()

                    sharedPreferences.edit().putString("PasswordSTRING", user.password).apply()
                    snackbar.dismiss()

                }
                else{
                    if(response.code()==404)
                        Toast.makeText(context, "Error: el correo o contraseña no son los correctos" , Toast.LENGTH_SHORT).show()
                    snackbar.dismiss()

                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                if(t is SocketTimeoutException){
                    Toast.makeText(activity, "Intentalo de nuevo, error de conexión con el servidor", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
                }
                snackbar.dismiss()
            }
        })
    }
}


