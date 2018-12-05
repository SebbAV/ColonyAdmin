package mx.com.colonyadmin.colonyadmin.NewGuestFragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.Toast
import android.support.v7.widget.Toolbar
import mx.com.colonyadmin.colonyadmin.ForgotPassword.ChangePasswordFragment
import mx.com.colonyadmin.colonyadmin.ForgotPassword.EmailForgotFragment
import mx.com.colonyadmin.colonyadmin.GuestList.CustomAdapter
import mx.com.colonyadmin.colonyadmin.GuestList.GuestListFragment
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NewGuestFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewGuestFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class NewGuestFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var uid: String
    private lateinit var lstUsers: MutableList<DataXXXXXXXX>
    private lateinit var lstView: ListView
    private var adapter: CustomAdapterGuests? = null
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        val bundle = this.arguments

        if (bundle != null) {
            // handle your code here.
            uid = bundle.getString("_uid")
            getGuests()
            (activity as MainActivity).showLoading()
        }
        else{
            Toast.makeText(this.activity, "Error del servidor", Toast.LENGTH_LONG).show()
            val intent = Intent(this.activity!!.baseContext, LoginActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view: View =inflater.inflate(R.layout.fragment_new_guest, container, false)
        lstView = view.findViewById(R.id.lstViewAllGuests) as ListView
        toolbar = view.findViewById(R.id.toolbarNewGuest) as Toolbar

        toolbar.setNavigationOnClickListener{
            val fragment = GuestListFragment.newInstance()
            var utils:Utils = Utils()
            utils.addFragmentToActivity(activity as Activity, activity!!.supportFragmentManager, fragment, R.id.mainFrameLayout, utils.GuestList)
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
         * @return A new instance of fragment NewGuestFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                NewGuestFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }


    private fun getGuests(){
        val retrofit = Retrofit.Builder().baseUrl(Utils.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        //Variable declaration
        var mAPIService: LoginService? = retrofit.create(LoginService::class.java)

        //After oncreate

        //Some Button click
        var activity:MainActivity? = this.activity as MainActivity

        mAPIService!!.getAllGuests().enqueue(object : Callback<AllUsersResponse> {
            override fun onResponse(call: Call<AllUsersResponse>, response: Response<AllUsersResponse>) {

                if (response.isSuccessful()) {
                    if (response.body()!!.data.isEmpty()){
                        Toast.makeText(activity, "No hay invitados registrados",Toast.LENGTH_LONG).show()

                    }
                    else{
                        lstUsers = response.body()!!.data
                        populateList(lstUsers)
                    }


                }
                else{
                    getGuests()
                }
            }

            override fun onFailure(call: Call<AllUsersResponse>, t: Throwable) {
                getGuests()
            }
        })
    }



    private fun populateList(lstU: MutableList<DataXXXXXXXX>){
        adapter = CustomAdapterGuests(this.activity!!.baseContext,R.id.lstViewGuests, lstU, this.activity as MainActivity,uid)
        lstView.setAdapter(adapter as ListAdapter)

        (this.activity as MainActivity).hideLoading()
    }
}
