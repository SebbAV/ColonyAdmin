package mx.com.colonyadmin.colonyadmin.SignUpActivity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.*
import kotlinx.android.synthetic.main.activity_login.*
import mx.com.colonyadmin.colonyadmin.ForgotPassword.ChangePasswordFragment
import mx.com.colonyadmin.colonyadmin.ForgotPassword.ForgotPasswordActivity
import mx.com.colonyadmin.colonyadmin.LoginActivity.LoginActivity
import mx.com.colonyadmin.colonyadmin.MainActivity.MainActivity
import mx.com.colonyadmin.colonyadmin.R
import mx.com.colonyadmin.colonyadmin.Services.*
import mx.com.colonyadmin.colonyadmin.Utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {

    lateinit var btnConfirm: Button
    lateinit var edtEmail : EditText
    lateinit var edtPassword : EditText
    lateinit var edtConfirmPassword : EditText
    lateinit var edtName : EditText
    lateinit var edtLastName : EditText
    lateinit var edtAddress: Spinner
    lateinit var edtPhone: EditText
    lateinit var edtPlates: EditText
    lateinit var edtNumberAddres : EditText
    lateinit var txtBack: TextView
    lateinit var snackbar: Snackbar
    lateinit var util: Utils
    internal lateinit var progressDialog: ProgressDialog
    lateinit var lstAddress: MutableList<DataXXXXX>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        btnConfirm   = findViewById(R.id.signup_button) as Button
        edtName = findViewById(R.id.signup_edtName) as EditText
        edtLastName = findViewById(R.id.signup_edtLastName) as EditText
        edtEmail = findViewById(R.id.signup_edtEmail) as EditText
        edtPassword = findViewById(R.id.signup_edtPassword) as EditText
        edtConfirmPassword = findViewById(R.id.signup_edtConfirmPassword) as EditText
        edtAddress = findViewById(R.id.signup_edtAddress) as Spinner
        edtPhone = findViewById(R.id.signup_edtPhone) as EditText
        edtPlates = findViewById(R.id.signup_edtPlates) as EditText
        txtBack = findViewById(R.id.signup_txtRegresar) as TextView
        edtNumberAddres = findViewById(R.id.signup_edtAddress_number) as EditText
        util = Utils()
        txtBack.setOnClickListener{
            val intent = Intent(this.baseContext, LoginActivity::class.java)
            startActivity(intent)
        }
        showLoading()
        btnConfirm.setOnClickListener{

            //Validar los datos
            if(validateData()) {
                val nuevoUsuario = newUser(

                        edtEmail.text.toString(),
                        edtPassword.text.toString(),
                        edtName.text.toString(),
                        edtAddress.selectedItem.toString()+ " #${edtNumberAddres.text}",
                        edtLastName.text.toString(),
                        edtPhone.text.toString(),
                        "1",
                        edtPlates.text.toString()
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


        getAdresses()


    }

    private fun getAdresses() {
        val retrofit = Retrofit.Builder().baseUrl(Utils.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()


        //Variable declaration
        var mAPIService: LoginService? = retrofit.create(LoginService::class.java)



        mAPIService!!.getAddress().enqueue(object : Callback<AddressResponse> {
            override fun onResponse(call: Call<AddressResponse>, response: Response<AddressResponse>) {

                if (response.isSuccessful()) {

                    hideLoading()
                    lstAddress = response.body()!!.data
                    setSpinnerValues()
                }
                else{
                    getAdresses()
                    Toast.makeText(this@SignupActivity, "Error con el servicor", Toast.LENGTH_LONG).show()

                }
            }

            override fun onFailure(call: Call<AddressResponse>, t: Throwable) {
                if(t is SocketTimeoutException){

                    Toast.makeText(this@SignupActivity, "Intentando de nuevo, error de conexión con el servidor", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@SignupActivity, t.message, Toast.LENGTH_LONG).show()
                }
                getAdresses()

            }
        })
    }


    private fun setSpinnerValues(){
        var list_of_items :MutableList<String> = arrayListOf<String>()
        lstAddress.forEach{ data ->
            list_of_items.add(data.address.toString())

        }
        val array_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_of_items)
        array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        edtAddress.adapter = array_adapter
    }

    fun validateData(): Boolean{

        var valid: Boolean = true
        if (edtName.text.toString().isEmpty()){
            edtName.error = "Ingresa un nombre"
            valid = false
        }

        if (edtLastName.text.toString().isEmpty()){
            edtLastName.error = "Ingresa un apellido"
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
        if (edtAddress.selectedItem.toString().isEmpty()){
            //edtAddress.error = "Ingresa una dirección"
            valid = false
        }

        if (edtEmail.text.toString().isEmpty())
        {
            edtEmail.error = "Ingresa un correo"
            valid = false
        }

        if(!util.isEmailValid(edtEmail.text.toString())){
            //Toast.makeText(baseContext, "El correo que se ingreso es incorrecto" , Toast.LENGTH_SHORT).show()
            edtEmail.error = "El correo que se ingreso es incorrecto"
            valid = false
        }

        if(edtPassword.text.toString() != edtConfirmPassword.text.toString()){
            edtPassword.error = "Los valores no coinciden"
            valid = false
        }
        if (edtNumberAddres.text.toString().isEmpty()){
            edtNumberAddres.error = "Ingresa tu número"
            valid = false
        }



        if(edtPassword.text.toString().isEmpty()){
            edtPassword.error = "Ingresa una contraseña"
            edtConfirmPassword.error = "Los valores no coinciden"
            valid = false
        }

        if (edtPlates.text.toString().isEmpty()){
            edtPlates.error = "Ingresa tus placas"
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
                        Toast.makeText(activity, "Error: El usuario ya existe con el correo"+edtEmail.text.toString() , Toast.LENGTH_SHORT).show()
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

    fun showLoading() {
        progressDialog = ProgressDialog.show(this, null, null)
        progressDialog.setContentView(ProgressBar(this))
        progressDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun hideLoading() {
        try {
            progressDialog.cancel()
        } catch (ex: Exception) {

        }

    }


}
