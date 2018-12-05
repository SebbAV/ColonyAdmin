package mx.com.colonyadmin.colonyadmin.MapFragment

import android.content.ContentValues.TAG
import android.content.Context
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import mx.com.colonyadmin.colonyadmin.R
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.LocationRequest
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.os.Build
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Tasks.call
import mx.com.colonyadmin.colonyadmin.R.string.location
import org.json.JSONObject
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import mx.com.colonyadmin.colonyadmin.MainActivity.MainActivity
import org.json.JSONArray
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
class MapFragment : Fragment(), OnMapReadyCallback ,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{


    // TODO: Rename and change types of parameters

    private var listener: OnFragmentInteractionListener? = null
    private var mMap: GoogleMap? = null
    var mGoogleApiClient: GoogleApiClient? = null
    var mlastLocation: Location? = null
    var mLocationRequest: LocationRequest? = null

    private var mSocket: Socket? = intializeSocket()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    //Inicialization of socket io
    fun intializeSocket(): Socket? {
        try {
            return IO.socket("http://akarokhome.ddns.net:3000")
        } catch (e: URISyntaxException) {
            return null
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_map, container, false)
        var mapFragment : SupportMapFragment?
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        if(mSocket !=null){
            mSocket!!.connect()
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


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(activity!!.baseContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.activity!!.baseContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), 1234)

            } else {

                ConfiguracionGoogleApiClient()
                mMap!!.isMyLocationEnabled = true
            }
        } else {

            ConfiguracionGoogleApiClient()
            mMap!!.isMyLocationEnabled = true
        }


    }

    @Synchronized
    protected fun ConfiguracionGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this.activity!!.baseContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        mGoogleApiClient!!.connect()
    }


    override fun onConnected(p0: Bundle?) {
       //To change body of created functions use File | Settings | File Templates.
        mLocationRequest = LocationRequest()
        mLocationRequest!!.setInterval(1000)
        mLocationRequest!!.setFastestInterval(1000)
        mLocationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)


        if (ActivityCompat.checkSelfPermission(this.activity!!.baseContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.activity!!.baseContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
    }

    override fun onConnectionSuspended(p0: Int) {
       //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
       //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLocationChanged(location: Location?) {

    }

    private var onNewLatLng : Emitter.Listener = Emitter.Listener() {

    }


    fun recieveGuest() {
        val rootObject = JSONObject()
        mSocket!!.on("visitors_location", Emitter.Listener { args ->
            val obj = args[0] as JSONArray
            println(obj.toString())
            activity!!.runOnUiThread(Runnable { createSydneyMarker(obj) })
        })
    }



    fun createSydneyMarker(obj : JSONArray){
        for (i in 0 .. (obj.length() -1)){
            val data = obj[i] as JSONObject
            var ltng:LatLng =  LatLng(data["lat"] as Double, data["lng"] as Double)

            mMap!!.addMarker(MarkerOptions().position(ltng)
                    .title("Guest"))
        }

    }

}
