package mx.com.colonyadmin.colonyadmin.MapFragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import mx.com.colonyadmin.colonyadmin.R
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import mx.com.colonyadmin.colonyadmin.MainActivity.MainActivity
import mx.com.colonyadmin.colonyadmin.Services.DataXXXXX
import mx.com.colonyadmin.colonyadmin.Services.DataXXXXXX
import org.json.JSONArray
import org.json.JSONObject
import java.net.URISyntaxException


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MapFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MapFragment : Fragment(), OnMapReadyCallback {

    // TODO: Rename and change types of parameters

    private var listener: OnFragmentInteractionListener? = null
    private var mSocket: Socket? = intializeSocket()
    private var gMap :GoogleMap? = null

    //Inicialization of socket io
    fun intializeSocket(): Socket? {
        try {
            return IO.socket("http://akarokhome.ddns.net:3000")
        } catch (e: URISyntaxException) {
            return null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        if(mSocket !=null){
            mSocket!!.connect()
        }
        var mapFragment : SupportMapFragment?=null
        mapFragment = fragmentManager!!.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        val mapFragment: SupportMapFragment? = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment?.getMapAsync(this)
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
         * @return A new instance of fragment MapFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                MapFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }

    fun recieveGuest() {
        val rootObject = JSONObject()
        mSocket!!.on("visitors_location", Emitter.Listener { args ->
            val obj = args[0] as JSONArray
            println(obj.toString())
            activity!!.runOnUiThread(Runnable { createSydneyMarker(obj) })
        })
    }
    override fun onMapReady(googleMap: GoogleMap) {

        gMap = googleMap
        recieveGuest()
    }
    fun createSydneyMarker(obj : JSONArray){
        for (i in 0 .. (obj.length() -1)){
            val data = obj[i] as JSONObject
            var ltng:LatLng =  LatLng(data["lat"] as Double, data["lng"] as Double)

            gMap!!.addMarker(MarkerOptions().position(ltng)
                    .title("Guest"))
        }



    }
}
