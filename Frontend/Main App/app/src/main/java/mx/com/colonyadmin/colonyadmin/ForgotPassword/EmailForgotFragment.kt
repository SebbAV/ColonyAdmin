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
import android.widget.Toast
import mx.com.colonyadmin.colonyadmin.R
import mx.com.colonyadmin.colonyadmin.Services.*
import mx.com.colonyadmin.colonyadmin.Utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [EmailForgotFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [EmailForgotFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class EmailForgotFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    lateinit var btnConfirm: Button
    lateinit var edtEmail : EditText
    lateinit var utils: Utils

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

        var view :View = inflater.inflate(R.layout.fragment_email_forgot, container, false)
        btnConfirm   = view.findViewById(R.id.forgotPassword_confirm) as Button
        edtEmail = view.findViewById(R.id.forgotPassword_edtEmail) as EditText
        utils = Utils()

        btnConfirm.setOnClickListener{
            //Desmadre del correo

            if (validateEmail()) {
                var email: EmailRequestObject = EmailRequestObject(edtEmail.text.toString())
                sendEmail(email)
            }

        }

        return view
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
         * @return A new instance of fragment EmailForgotFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                EmailForgotFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    fun validateEmail():Boolean{
        if(!utils.isEmailValid(edtEmail.text.toString())|| edtEmail.text.toString().isEmpty()){
            Toast.makeText(this.context, "El correo que se ingreso es incorrecto" , Toast.LENGTH_SHORT).show()
            return false
        }
        else
            return true

    }

    fun sendEmail(user: EmailRequestObject){

        val retrofit = Retrofit.Builder().baseUrl(Utils.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()


        //Variable declaration
        var mAPIService: LoginService? = retrofit.create(LoginService::class.java)

        //Some Button click
        var activity = this.activity
        Toast.makeText(activity, "Correo enviado", Toast.LENGTH_LONG).show()
        //We save our  user object of the fragment to pass the email

        val ecf : EmailCodeFragment = EmailCodeFragment()
        val bundle = Bundle()
        bundle.putString("email", user.email)
        ecf.arguments = bundle

        (getActivity() as ForgotPasswordActivity).addFragmentToActivity(ecf, R.id.changePasswordFrame)

        mAPIService!!.sendEmailForgotPassword(user).enqueue(object : Callback<EmailResposeObject> {
            override fun onResponse(call: Call<EmailResposeObject>, response: Response<EmailResposeObject>) {

                if (response.isSuccessful()) {

                }
                /*else{
                   if(response.message()== "Not found")
                        Toast.makeText(activity, "Error: no se encontro el usuario" , Toast.LENGTH_SHORT).show()
                    if(response.code() == 409)
                        Toast.makeText(activity, "Error: El usuario ya existe con el correo"+edtEmail.text.toString() , Toast.LENGTH_SHORT).show()


                }*/
            }

            override fun onFailure(call: Call<EmailResposeObject>, t: Throwable) {
                //Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()

            }
        })
    }
}
