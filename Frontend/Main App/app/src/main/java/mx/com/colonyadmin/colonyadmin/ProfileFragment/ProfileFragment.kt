package mx.com.colonyadmin.colonyadmin.ProfileFragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import mx.com.colonyadmin.colonyadmin.ForgotPassword.ChangePasswordFragment
import mx.com.colonyadmin.colonyadmin.ForgotPassword.ForgotPasswordActivity
import mx.com.colonyadmin.colonyadmin.LoginActivity.LoginActivity
import mx.com.colonyadmin.colonyadmin.MainActivity.MainActivity
import mx.com.colonyadmin.colonyadmin.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ProfileFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters

    lateinit var btnChangePassword: Button
    lateinit var btnLogOut: Button


    private var listener: OnFragmentInteractionListener? = null
    public var TAG: String  = "Profile"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_profile, container, false) as View
        btnChangePassword = view.findViewById(R.id.profile_btnChangePassword) as Button
        btnLogOut = view.findViewById(R.id.profile_btnSignOut) as Button
        val sharedPreferences = this.activity!!.getSharedPreferences("mx.com.colonyadmin.android", Context.MODE_PRIVATE)
        btnChangePassword.setOnClickListener {
            val email = sharedPreferences.getString("EmailSTRING", "")

            val intent = Intent(activity, ForgotPasswordActivity::class.java)
            intent.putExtra("email", email)
            startActivity(intent)

            this.activity!!.finish()
        }

        btnLogOut.setOnClickListener{

            sharedPreferences.edit().putString("EmailSTRING", "").apply()
            sharedPreferences.edit().putString("PasswordSTRING", "").apply()
            val intent = Intent(context, LoginActivity::class.java)

            startActivity(intent)
            this.activity!!.finish()
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
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                ProfileFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}
