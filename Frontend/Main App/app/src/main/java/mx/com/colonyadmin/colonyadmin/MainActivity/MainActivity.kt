package mx.com.colonyadmin.colonyadmin.MainActivity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import mx.com.colonyadmin.colonyadmin.GuestList.GuestListFragment
import mx.com.colonyadmin.colonyadmin.LoginActivity.LoginActivity
import mx.com.colonyadmin.colonyadmin.MapFragment.MapFragment
import mx.com.colonyadmin.colonyadmin.ProfileFragment.ProfileFragment
import mx.com.colonyadmin.colonyadmin.R
import mx.com.colonyadmin.colonyadmin.Utils.Utils
import java.net.URISyntaxException

class MainActivity : AppCompatActivity(), ProfileFragment.OnFragmentInteractionListener, GuestListFragment.OnFragmentInteractionListener, MapFragment.OnFragmentInteractionListener {

    public lateinit var utils  : Utils

    private var mSocket: Socket? = intializeSocket()
    //Inicialization of socket io
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
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //Initialziation of the first fragment
        val fragment = GuestListFragment.newInstance()
        utils = Utils()
        utils.addFragmentToActivity(this,this.supportFragmentManager,fragment, R.id.mainFrameLayout, utils.GuestList)


    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_guest_list -> {
                val fragment = GuestListFragment.newInstance()
                utils.addFragmentToActivity(this,this.supportFragmentManager,fragment, R.id.mainFrameLayout, utils.GuestList)
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_profile -> {
                val fragment = ProfileFragment.newInstance()
                utils.addFragmentToActivity(this,this.supportFragmentManager,fragment, R.id.mainFrameLayout, utils.ProfileFragment)
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_map -> {
                val fragment = MapFragment.newInstance()
                utils.addFragmentToActivity(this,this.supportFragmentManager,fragment, R.id.mainFrameLayout, utils.MapFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }



    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
