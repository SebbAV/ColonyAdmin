package com.example.cbqa_0043.ca_guestapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.Toast
import com.example.cbqa_0043.ca_guestapp.LoginActivity.LoginActivity
import com.example.cbqa_0043.ca_guestapp.Services.LoginService
import com.example.cbqa_0043.ca_guestapp.Services.UserResponse
import com.example.cbqa_0043.ca_guestapp.Services.newUser
import com.example.cbqa_0043.ca_guestapp.Utils.Utils
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException

class Signup : AppCompatActivity() {
    lateinit var util: Utils
    lateinit var snackbar: Snackbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        util = Utils()
        btnCreateUser.setOnClickListener{
            //Validar los datos
            if(validateData()) {
                val nuevoUsuario = newUser(

                    edtEmailR.text.toString(),
                    edtPwd1.text.toString(),
                    edtName.text.toString(),
                    edtLast.text.toString(),
                    edtPhone.text.toString(),
                    "2",
                    edtVehicle.text.toString()
                )
                snackbar = Snackbar.make(
                    getWindow().getDecorView().getRootView(), // Parent view
                    "Creando usuario...", // Message to show
                    Snackbar.LENGTH_INDEFINITE // How long to display the message.
                )
                snackbar.show()

                //Enviar los datos
                sendData(nuevoUsuario)



            }
            else
                Toast.makeText(baseContext, "Verifica los valores ingresados" , Toast.LENGTH_SHORT).show()
        }
    }
    fun validateData(): Boolean{

        var valid: Boolean = true
        if (edtName.text.toString().isEmpty()){
            edtName.error = "Ingresa un nombre"
            valid = false
        }

        if (edtLast.text.toString().isEmpty()){
            edtLast.error = "Ingresa un apellido"
            valid = false
        }
        if (edtPhone.text.toString().isEmpty()){
            edtPhone.error = "Ingresa un telefono"
            valid = false
        }
        if (edtPhone.text.toString().length !=10) {
            edtPhone.error = "Error con el telefono ingresado"
            valid = false
        }

        if (edtEmailR.text.toString().isEmpty())
        {
            edtEmailR.error = "Ingresa un correo"
            valid = false
        }

        if(!util.isEmailValid(edtEmailR.text.toString())){
            //Toast.makeText(baseContext, "El correo que se ingreso es incorrecto" , Toast.LENGTH_SHORT).show()
            edtEmailR.error = "El correo que se ingreso es incorrecto"
            valid = false
        }

        if(edtPwd1.text.toString() != edtPassword2.text.toString()){
            edtPwd1.error = "Los valores no coinciden"
            valid = false
        }

        if(edtPwd1.text.toString().isEmpty()){
            edtPwd1.error = "Ingresa una contraseña"
            edtPassword2.error = "Los valores no coinciden"
            valid = false
        }

        if (edtVehicle.text.toString().isEmpty()){
            edtVehicle.error = "Ingresa tus placas"
            valid = false
        }

        return  valid
    }
    fun sendData(user: newUser){

        val retrofit = Retrofit.Builder().baseUrl(Utils.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        //Variable declaration
        var mAPIService: LoginService? = retrofit.create(LoginService::class.java)

        //Some Button click
        var activity = this

        mAPIService!!.createUser(user).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {

                if (response.isSuccessful()) {
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(activity, "Usuario creado", Toast.LENGTH_LONG).show()
                    //We save our  user object

                    snackbar.dismiss()

                }
                else{
                    if(response.message()== "Not found")
                        Toast.makeText(activity, "Error: no se encontro el usuario" , Toast.LENGTH_SHORT).show()
                    if(response.code() == 409){
                        Toast.makeText(activity, "Error: El usuario ya existe con el correo"+edtEmailR.text.toString() , Toast.LENGTH_SHORT).show()
                        val intent = Intent(activity.baseContext, LoginActivity::class.java)
                        startActivity(intent)
                    }

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

            }
        })
    }
}
