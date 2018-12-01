package mx.com.colonyadmin.colonyadmin.ForgotPassword

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import mx.com.colonyadmin.colonyadmin.LoginActivity.LoginActivity
import mx.com.colonyadmin.colonyadmin.R
import mx.com.colonyadmin.colonyadmin.Services.LoginService
import mx.com.colonyadmin.colonyadmin.Services.PasswordChangedResponse
import mx.com.colonyadmin.colonyadmin.Services.UserRequest
import mx.com.colonyadmin.colonyadmin.Utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ChangePasswordFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ChangePasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ChangePasswordFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    lateinit var btnConfirm: Button
    lateinit var edtPassword : EditText
    lateinit var edtPasswordConfirm : EditText
    lateinit var email :String
    lateinit var txtPrincipalPage: TextView
    lateinit var activity: ForgotPasswordActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_change_password, container, false)
        activity = this!!.getActivity() as ForgotPasswordActivity
        val bundle = this.arguments

        if (bundle != null) {
            // handle your code here.
            email = bundle.getString("email")

        }


        btnConfirm   = view.findViewById(R.id.changepassword_button) as Button
        edtPassword = view.findViewById(R.id.changepassword_edtPassword) as EditText
        edtPasswordConfirm = view.findViewById(R.id.changepassword_edtConfirmPassword) as EditText
        txtPrincipalPage = view.findViewById(R.id.changepassword_txtRegresar) as TextView

        btnConfirm.setOnClickListener{
            if (email!=""){
                if(validatePassword()){
                    var objUser : UserRequest = UserRequest(email, edtPassword.text.toString())
                    changePassword(objUser)
                    activity.showLoading()
                }
                else{
                    Toast.makeText(this.context, "Error con la contraseña", Toast.LENGTH_LONG).show()
                }
            }
            else{
                (getActivity() as ForgotPasswordActivity).addFragmentToActivity(EmailForgotFragment(), R.id.changePasswordFrame)
            }
        }

        txtPrincipalPage.setOnClickListener{
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    private fun validatePassword(): Boolean {
        if (edtPassword.text.toString()!=edtPasswordConfirm.text.toString()){

            edtPassword.error = "Las contraseñas no coinciden"
            return false
        }
        if (edtPassword.text.isEmpty())
            edtPassword.error = "Ingresa una contraseña"
        if (edtPasswordConfirm.text.isEmpty())
            edtPassword.error = "Confirma tú contraseña"

        if (edtPassword.text.toString().length<6){
            return false
        }

        return true
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChangePasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ChangePasswordFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    private fun changePassword(user:UserRequest){

        val retrofit = Retrofit.Builder().baseUrl(Utils.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()


        //Variable declaration
        var mAPIService: LoginService? = retrofit.create(LoginService::class.java)

        //Some Button click
        var activity = this.activity

        mAPIService!!.changePassword(user).enqueue(object : Callback<PasswordChangedResponse> {
            override fun onResponse(call: Call<PasswordChangedResponse>, response: Response<PasswordChangedResponse>) {

                if (response.isSuccessful()) {
                    if (response.body()!!.data.nModified==1) {


                        val intent = Intent(activity, LoginActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(activity, "Contraseña cambiada", Toast.LENGTH_LONG).show()
                    }
                    else{
                        val intent = Intent(activity, LoginActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(activity, "La contraseña era igual a la anterior, no fue cambiada", Toast.LENGTH_LONG).show()
                    }
                    activity.hideLoading()
                }
                else{

                        Toast.makeText(activity, response.message(), Toast.LENGTH_SHORT).show()
                    activity.hideLoading()
                }
            }

            override fun onFailure(call: Call<PasswordChangedResponse>, t: Throwable) {

                if(t is SocketTimeoutException){
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(activity, "La contraseña se ha cambiado", Toast.LENGTH_LONG).show()

                }
                else{
                    Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()

                }
                activity.hideLoading()
            }
        })
    }
}
