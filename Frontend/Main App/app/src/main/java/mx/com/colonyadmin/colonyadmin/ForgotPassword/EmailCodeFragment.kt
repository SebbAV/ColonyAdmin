package mx.com.colonyadmin.colonyadmin.ForgotPassword

import android.content.Context
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
import mx.com.colonyadmin.colonyadmin.R
import mx.com.colonyadmin.colonyadmin.Services.*
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
 * [EmailCodeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [EmailCodeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class EmailCodeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    lateinit var btnConfirm: Button
    lateinit var edtChar1 : EditText
    lateinit var edtChar2 : EditText
    lateinit var edtChar3 : EditText
    lateinit var edtChar4 : EditText
    lateinit var edtChar5 : EditText
    lateinit var txtResendEmail : TextView
    lateinit var activity: ForgotPasswordActivity
    lateinit var email: String

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
        val view : View = inflater.inflate(R.layout.fragment_email_code, container, false)
        val bundle = this.arguments
        activity = this!!.getActivity() as ForgotPasswordActivity
        if (bundle != null) {
            // handle your code here.
            email = bundle.getString("email")

        }

        btnConfirm   = view.findViewById(R.id.forgotPassword_code_confirm) as Button
        edtChar1 = view.findViewById(R.id.forgotPassword_edtChar1) as EditText
        edtChar2 = view.findViewById(R.id.forgotPassword_edtChar2) as EditText
        edtChar3 = view.findViewById(R.id.forgotPassword_edtChar3) as EditText
        edtChar4 = view.findViewById(R.id.forgotPassword_edtChar4) as EditText
        edtChar5 = view.findViewById(R.id.forgotPassword_edtChar5) as EditText
        txtResendEmail = view.findViewById(R.id.forgotPassword_resendmail) as TextView

        btnConfirm.setOnClickListener{
            if (email!=""){
                validateCode()
            }
            else{
                (getActivity() as ForgotPasswordActivity).addFragmentToActivity(EmailForgotFragment(), R.id.changePasswordFrame)
            }
        }
        txtResendEmail.setOnClickListener{
            resendEmail()
        }
        return view
    }

    private fun resendEmail() {
        val user: EmailRequestObject = EmailRequestObject(email)

        val retrofit = Retrofit.Builder().baseUrl(Utils.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()


        //Variable declaration
        var mAPIService: LoginService? = retrofit.create(LoginService::class.java)

        //Some Button click
        var activity = this.activity
        Toast.makeText(activity, "Correo enviado", Toast.LENGTH_LONG).show()
        //We save our  user object of the fragment to pass the email


        mAPIService!!.sendEmailForgotPassword(user).enqueue(object : Callback<EmailResposeObject> {
            override fun onResponse(call: Call<EmailResposeObject>, response: Response<EmailResposeObject>) {

                if (response.isSuccessful()) {

                }
                else{
                       // Toast.makeText(activity, response.errorBody().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EmailResposeObject>, t: Throwable) {
                //Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()

            }
        })
    }

    private fun validateCode() {
        var code: String = getCode()
        if (code.length!=5){
            Toast.makeText(activity, "Añade un codigo correcto : ", Toast.LENGTH_LONG).show()
            return
        }
        else{
            activity.showLoading()
            sendCode(code.toUpperCase())
        }


    }

    private fun sendCode(code:String){
        val retrofit = Retrofit.Builder().baseUrl(Utils.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()


        //Variable declaration
        var mAPIService: LoginService? = retrofit.create(LoginService::class.java)

        //Some Button click
        var activity = this.activity

        mAPIService!!.sendCodeConfirmation(code).enqueue(object : Callback<CodeResponseObject> {
            override fun onResponse(call: Call<CodeResponseObject>, response: Response<CodeResponseObject>) {

                if (response.isSuccessful()) {
                    if (response.body()!!.data.email == email) {


                        val ecf: ChangePasswordFragment = ChangePasswordFragment()
                        val bundle = Bundle()
                        bundle.putString("email", email)
                        ecf.arguments = bundle
                        activity.hideLoading()
                        (getActivity() as ForgotPasswordActivity).addFragmentToActivity(ecf, R.id.changePasswordFrame)
                    }


                }
                else{
                    if(response.message()== "Not found") {
                        Toast.makeText(activity, "Error: codigo no válido", Toast.LENGTH_SHORT).show()
                        activity.hideLoading()
                    }

                }
            }

            override fun onFailure(call: Call<CodeResponseObject>, t: Throwable) {
                if(t is SocketTimeoutException){
                    Toast.makeText(activity, "Intentalo de nuevo, error de conexión con el servidor", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
                }
                activity.hideLoading()
            }
        })
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
         * @return A new instance of fragment EmailCodeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                EmailCodeFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }


    fun getCode():String{
            return edtChar1.text.toString()+ edtChar2.text.toString()+ edtChar3.text.toString()+ edtChar4.text.toString()+edtChar5.text.toString()
    }


}
