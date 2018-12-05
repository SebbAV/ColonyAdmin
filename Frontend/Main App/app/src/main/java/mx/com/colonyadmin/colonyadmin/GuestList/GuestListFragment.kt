package mx.com.colonyadmin.colonyadmin.GuestList

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import mx.com.colonyadmin.colonyadmin.R
import android.graphics.drawable.ColorDrawable
import android.view.KeyEvent.KEYCODE_BACK
import android.content.DialogInterface
import android.view.*
import android.view.Window.FEATURE_NO_TITLE
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.AlertDialog
import android.graphics.Color
import android.view.ViewAnimationUtils
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.widget.*
import kotlinx.android.synthetic.main.dialog_invitation_guest.view.*
import kotlinx.android.synthetic.main.fragment_dialog_sos.*
import kotlinx.android.synthetic.main.fragment_dialog_sos.view.*
import mx.com.colonyadmin.colonyadmin.ForgotPassword.EmailCodeFragment
import mx.com.colonyadmin.colonyadmin.MainActivity.MainActivity
import mx.com.colonyadmin.colonyadmin.NewGuestFragment.NewGuestFragment
import mx.com.colonyadmin.colonyadmin.Services.*
import mx.com.colonyadmin.colonyadmin.Utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.*
import kotlin.concurrent.schedule


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [GuestListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [GuestListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class GuestListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var toolbar :Toolbar
    lateinit var btnAddGuest: Button
    lateinit var btnfoa: FloatingActionButton

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var lstView: ListView
    private var adapter: CustomAdapter? = null

    private var cont: Int = 0
    private var lstAddress: MutableList<DataXXXXX>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_guest_list, container, false) as View
        toolbar = view.findViewById(R.id.toolbarGuestList)
        //btnNewGuest = view.findViewById(R.id.btnToolbarNuevoInvitado) as Button
        btnfoa = view.findViewById(R.id.guestlist_sosfab) as FloatingActionButton
        //btnAddGuest.setOnClickListener()
        lstView = view.findViewById(R.id.lstViewGuests)
        btnfoa.setOnClickListener{

            /*Dialog con animación*/
            showDiag()
        }
        /*btnNewGuest.setOnClickListener{
            val ecf = NewGuestFragment()
            val bundle = Bundle()
            bundle.putString("_uid", (this.activity as MainActivity)._idUser)
            ecf.arguments = bundle
            (this.activity as MainActivity).addFragmentToActivity(ecf, R.id.mainFrameLayout)
        }*/
        toolbar.setNavigationOnClickListener{
            val ecf = NewGuestFragment()
            val bundle = Bundle()
            bundle.putString("_uid", (this.activity as MainActivity)._idUser)
            ecf.arguments = bundle
            (this.activity as MainActivity).addFragmentToActivity(ecf, R.id.mainFrameLayout)
        }
        /*
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            val ecf = NewGuestFragment()
            val bundle = Bundle()
            bundle.putString("_uid", (this.activity as MainActivity)._idUser)
            ecf.arguments = bundle
            (this.activity as MainActivity).addFragmentToActivity(ecf, R.id.mainFrameLayout)
        })*/

        /*toolbar.setOnMenuItemClickListener {
            val ecf = NewGuestFragment()
            val bundle = Bundle()
            bundle.putString("_uid", (this.activity as MainActivity)._idUser)
            ecf.arguments = bundle
            (this.activity as MainActivity).addFragmentToActivity(ecf, R.id.mainFrameLayout)

        }*/


        var lstTemp = (activity as MainActivity).getListGuests()
        if (lstTemp==null) {
            //Obtenermos los invitados
            val userid = (this.activity as MainActivity)._idUser
            getAdresses()

            getGuests(userid)
        }
        else{
            populateListView(lstTemp)
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

        getAdresses()

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
         * @return A new instance of fragment GuestListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                GuestListFragment().apply {

                }
    }


    private fun showDiag() {

        val dialogView = View.inflate(this.activity, R.layout.fragment_dialog_sos, null)

        val dialog = Dialog(this.activity, R.style.MyAlertDialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(dialogView)

        val imageView = dialog.findViewById(R.id.btnToolbarClose) as Button
        imageView.setOnClickListener(View.OnClickListener { revealShow(dialogView, false, dialog) })

        dialog.setOnShowListener { revealShow(dialogView, true, null) }

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.btnToolbarClose.setOnClickListener{
            dialog.dismiss()
        }
        dialog.sos_btnHelp.setOnClickListener{
            if((activity as MainActivity).getListAddress() == null) {

                getAdresses()
                Toast.makeText(this@GuestListFragment.context, "Error de conexión con el servidor", Toast.LENGTH_LONG).show()
            }
            else{
                lstAddress = (activity as MainActivity).getListAddress()
                //comparamos las calles que existen
                var name :String =""
                lstAddress?.forEach{
                    if (it.id.equals((activity as MainActivity)._addressUser))
                        name = it.address
                }
                if (name!="") {
                    (activity as MainActivity).getHelp(name)

                    Toast.makeText(this@GuestListFragment.context, "Se ha enviado la petición de ayuda", Toast.LENGTH_LONG).show()
                    Timer("SettingUp", false).schedule(3000) {

                        dialog.dismiss()
                    }
                }
            }
        }
        dialog.show()
    }


    private fun revealShow(dialogView: View, b: Boolean, dialog: Dialog?) {

        val view = dialogView.findViewById(R.id.dialogSos) as View

        val w = view.getWidth()
        val h = view.getHeight()

        val endRadius = Math.hypot(w.toDouble(), h.toDouble()).toInt()

        val cx = (btnfoa.getX() + btnfoa.getWidth() / 2).toInt()
        val cy = btnfoa.getY().toInt() + btnfoa.getHeight() + 56


        if (b) {
            val revealAnimator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, endRadius.toFloat())

            view.setVisibility(View.VISIBLE)
            revealAnimator.duration = 700
            revealAnimator.start()

        } else {
            val a = cx.toInt()
            val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, endRadius.toFloat(), 0f)

            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    dialog!!.dismiss()
                    view.setVisibility(View.INVISIBLE)

                }
            })
            anim.duration = 700
            anim.start()
        }

    }

    private fun getGuests(idUser: String){
        val retrofit = Retrofit.Builder().baseUrl(Utils.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        //Variable declaration
        var mAPIService: LoginService? = retrofit.create(LoginService::class.java)

        //After oncreate

        //Some Button click
        var activity = this.activity as MainActivity

        mAPIService!!.getGuestsById(idUser).enqueue(object : Callback<GuestListResponse> {
            override fun onResponse(call: Call<GuestListResponse>, response: Response<GuestListResponse>) {

                if (response.isSuccessful()) {
                    if (response.body()!!.data.isEmpty()){
                        Toast.makeText(activity.baseContext, "No tienes invitados registrados", Toast.LENGTH_LONG).show()
                    }
                    else{
                        lstView.visibility = View.VISIBLE
                        populateListView(response.body()!!.data)
                    }


                }
                else{
                    getGuests(idUser)
                }
            }

            override fun onFailure(call: Call<GuestListResponse>, t: Throwable) {
                getGuests(idUser)
            }
        })
    }


    private fun populateListView(lstGuests : MutableList<DataXXXXXX>){
        /*Inicializamos el arreglo con la list view*/
        adapter = CustomAdapter(this.activity!!.baseContext,R.id.lstViewGuests, lstGuests, this.activity as MainActivity)
        lstView.setAdapter(adapter as ListAdapter)

        (this.activity as MainActivity).setListGuests(lstGuests)
        (this.activity as MainActivity).hideLoading()
    }

    private fun getAdresses() {
        val retrofit = Retrofit.Builder().baseUrl(Utils.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        //(this.activity as MainActivity).showLoading()
        //Variable declaration
        var mAPIService: LoginService? = retrofit.create(LoginService::class.java)
        try {
            val activity: MainActivity? = this.activity as MainActivity

            mAPIService!!.getAddress().enqueue(object : Callback<AddressResponse> {
                override fun onResponse(call: Call<AddressResponse>, response: Response<AddressResponse>) {

                    if (response.isSuccessful()) {
                        lstAddress = response.body()!!.data
                        (activity as MainActivity?)!!.lstAddress = lstAddress
                        //(activity as MainActivity).hideLoading()
                    } else {
                        getAdresses()
                        Toast.makeText(activity!!.baseContext, "Error con el servicor", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<AddressResponse>, t: Throwable) {
                    /*if(t is SocketTimeoutException){

                    //Toast.makeText(activity.baseContext, "Intentando de nuevo, error de conexión con el servidor", Toast.LENGTH_LONG).show()
                }
                else{
                   // Toast.makeText(activity.baseContext, t.message, Toast.LENGTH_LONG).show()
                }*/
                    getAdresses()

                }
            })
        }
        catch (e: Exception){

        }
    }








}
