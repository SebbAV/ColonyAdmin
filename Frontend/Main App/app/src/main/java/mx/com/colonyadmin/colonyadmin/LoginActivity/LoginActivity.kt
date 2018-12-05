package mx.com.colonyadmin.colonyadmin.LoginActivity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import mx.com.colonyadmin.colonyadmin.ForgotPassword.ForgotPasswordActivity
import mx.com.colonyadmin.colonyadmin.MainActivity.MainActivity
import mx.com.colonyadmin.colonyadmin.R
import mx.com.colonyadmin.colonyadmin.Services.LoginService
import mx.com.colonyadmin.colonyadmin.Services.UserRequest
import mx.com.colonyadmin.colonyadmin.Services.UserResponse
import mx.com.colonyadmin.colonyadmin.SignUpActivity.SignupActivity
import mx.com.colonyadmin.colonyadmin.Utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.HashMap
import android.preference.PreferenceManager
import android.content.SharedPreferences



class LoginActivity : AppCompatActivity() {
    lateinit var btnLogin: Button
    lateinit var edtEmail : EditText
    lateinit var edtPassword : EditText
    lateinit var txtForgotPassword : TextView
    lateinit var txtRegister : TextView
    var retrofit: Retrofit? = null
    var utils:Utils? = null
    val BaseURL = "http://akarokhome.ddns.net:3000/v1/"
    lateinit var snackbar: Snackbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin   = findViewById(R.id.login_btnLogin) as Button
        edtEmail = findViewById(R.id.login_email) as EditText
        txtForgotPassword = findViewById(R.id.login_forgotPassword) as TextView
        edtPassword = findViewById(R.id.login_password) as EditText
        txtRegister = findViewById(R.id.login_createAccount) as TextView


        /*val prefs = this.getSharedPreferences("mx.com.colonyadmin.android", MODE_PRIVATE)

        val email =  prefs.getString("EmailSTRING", "")
        val password = prefs.getString("PasswordSTRING","")
        if(email!="" && password!=""){
            var objLoginApiSolicitud2 : UserRequest = UserRequest(email,password)
            login(objLoginApiSolicitud2, this.baseContext)
            snackbar = Snackbar.make(
                    rootLoginLayout, // Parent view
                    "Autenticando...", // Message to show
                    Snackbar.LENGTH_INDEFINITE // How long to display the message.
            )
            snackbar.show()
        }*/
        txtForgotPassword.setOnClickListener{
            val intent = Intent(this.baseContext, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        txtRegister.setOnClickListener{
            val intent = Intent(this.baseContext, SignupActivity::class.java)
            startActivity(intent)
        }
        btnLogin.setOnClickListener{
            //Validar entrada
            val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = this.getCurrentFocus()
            if (view == null) {
                view = (this) as View
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
            var objLoginApiSolicitud : UserRequest? = UserRequest(edtEmail.text.toString(),edtPassword.text.toString())
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

        //check shared preferences



    }

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
                    intent.putExtra("_id", response.body()!!.data.id)
                    intent.putExtra("_address", response.body()!!.data.address)
                    intent.putExtra("address_number", response.body()!!.data.addressNumber)
                    intent.putExtra("email", response.body()!!.data.email)
                    startActivity(intent)
                    Toast.makeText(context, "Bienvenido " + response.body()!!.data.firstName.toUpperCase(), Toast.LENGTH_SHORT).show()

                    //We save our  user object

                    val sharedPreferences = activity.getSharedPreferences("mx.com.colonyadmin.android", Context.MODE_PRIVATE)
                    sharedPreferences.edit().putString("EmailSTRING", response.body()!!.data.email).apply()

                    sharedPreferences.edit().putString("PasswordSTRING", user.password).apply()
                    snackbar.dismiss()
                    finish()

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
