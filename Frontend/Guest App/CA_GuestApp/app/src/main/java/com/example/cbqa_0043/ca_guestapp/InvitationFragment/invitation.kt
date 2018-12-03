package com.example.cbqa_0043.ca_guestapp.InvitationFragment

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.cbqa_0043.ca_guestapp.MainActivity.MainActivity
import com.example.cbqa_0043.ca_guestapp.R
import com.example.cbqa_0043.ca_guestapp.Services.InvitationResponseObject
import com.example.cbqa_0043.ca_guestapp.Services.LoginService
import com.example.cbqa_0043.ca_guestapp.Utils.Utils
import kotlinx.android.synthetic.main.fragment_invitation.*
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
 * [invitation.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [invitation.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class invitation : Fragment() {
    // TODO: Rename and change types of parameters
    private var listener: OnFragmentInteractionListener? = null
    lateinit var txtCode: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_invitation, container, false) as View
        txtCode = view.findViewById(R.id.txtCode) as TextView
        val userid = (this.activity as MainActivity)._idUser
        getInvitation(userid)
        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    fun getInvitation(idUser: String) {
        val retrofit = Retrofit.Builder().baseUrl(Utils.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //Variable declaration
        var mAPIService: LoginService? = retrofit.create(LoginService::class.java)

        //After oncreate

        //Some Button click
        var activity = this

        mAPIService!!.getInvitation(idUser).enqueue(object : Callback<InvitationResponseObject> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<InvitationResponseObject>,
                response: Response<InvitationResponseObject>
            ) {

                if (response.isSuccessful()) {
                    txtCode.text = "Code: ${response.body()!!.data.invitationCode}"
                }
            }

            override fun onFailure(call: Call<InvitationResponseObject>, t: Throwable) {
                getInvitation(idUser)
            }
        })
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
         * @return A new instance of fragment invitation.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            invitation().apply {
            }
    }
}
