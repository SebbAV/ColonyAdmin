package com.example.cbqa_0043.ca_guestapp.MainActivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AlertDialog
import android.widget.TextView
import android.widget.Toast
import com.example.cbqa_0043.ca_guestapp.ARFragment.ARFragment
import com.example.cbqa_0043.ca_guestapp.InvitationFragment.invitation
import com.example.cbqa_0043.ca_guestapp.LoginActivity.LoginActivity
import com.example.cbqa_0043.ca_guestapp.R
import com.example.cbqa_0043.ca_guestapp.Utils.Utils
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.android.gms.maps.MapFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_invitation.*
import org.json.JSONObject
import java.net.URISyntaxException

class MainActivity : AppCompatActivity(), invitation.OnFragmentInteractionListener, com.example.cbqa_0043.ca_guestapp.MapFragment.MapFragment.OnFragmentInteractionListener,ARFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    lateinit var utils  : Utils
    lateinit var _idUser : String
    private var mSocket: Socket? = intializeSocket()
    fun intializeSocket(): Socket?{
        try {
            return IO.socket("http://akarokhome.ddns.net:3000")
        } catch (e: URISyntaxException) {
            return null
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //Initialziation of the first fragment
        val fragment = invitation.newInstance()
        if (mSocket==null){
            val builder = AlertDialog.Builder(this@MainActivity)

            // Set the alert dialog title
            builder.setTitle("Error al conectar el socket")

            // Display a message on alert dialog
            builder.setMessage("Hubo un error con la conexión del socket, si persisten las molestias dirijase a la pagina www.pornhub.com, gracias :)")

            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("Aceptar"){dialog, which ->
                // Do something when user press the positive button

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent);
            }
        }
        else{
            mSocket!!.connect()
        }
        val b = intent.extras
        // or other values
        if (b != null) {
            _idUser = b.getString("_id")
            println(_idUser)
        }
        utils = Utils()
        utils.addFragmentToActivity(this,this.supportFragmentManager,fragment, R.id.mainFrameLayout, utils.InvitationFragment)
    }
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_invitation -> {
                val fragment = invitation.newInstance()
                utils.addFragmentToActivity(this,this.supportFragmentManager,fragment, R.id.mainFrameLayout, utils.InvitationFragment)
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_map -> {
                val fragment = com.example.cbqa_0043.ca_guestapp.MapFragment.MapFragment.newInstance()
                utils.addFragmentToActivity(this,this.supportFragmentManager,fragment, R.id.mainFrameLayout, utils.MapFragment)
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_AR -> {
                val fragment = ARFragment.newInstance()
                utils.addFragmentToActivity(this,this.supportFragmentManager,fragment, R.id.mainFrameLayout, utils.ARFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    //Socket IO functionality
    fun visitor(lat: Double, lng: Double){
        Toast.makeText(this,"Si entra",Toast.LENGTH_SHORT).show()
        val rootObject= JSONObject()
        rootObject.put("idUser",_idUser)
        rootObject.put("lat",lat)
        rootObject.put("lng",lng)
        mSocket!!.emit("visitor", rootObject)
    }
    fun visitorLocation(lat: Double, lng: Double){
        val rootObject= JSONObject()
        rootObject.put("idUser",_idUser)
        rootObject.put("lat",lat)
        rootObject.put("lng",lng)
        mSocket!!.emit("visitor_location", rootObject)
    }
    fun visitorExit()
    {
        val rootObject= JSONObject()
        rootObject.put("idUser",_idUser)
        mSocket!!.emit("visitor_exit", rootObject)
        mSocket!!.disconnect()
    }
}
