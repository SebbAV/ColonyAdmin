package mx.com.colonyadmin.colonyadmin.MainActivity

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.widget.ProgressBar
import android.widget.Toast
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import mx.com.colonyadmin.colonyadmin.GuestList.GuestListFragment
import mx.com.colonyadmin.colonyadmin.LoginActivity.LoginActivity
import mx.com.colonyadmin.colonyadmin.MapFragment.MapFragment
import mx.com.colonyadmin.colonyadmin.NewGuestFragment.NewGuestFragment
import mx.com.colonyadmin.colonyadmin.ProfileFragment.ProfileFragment
import mx.com.colonyadmin.colonyadmin.R
import mx.com.colonyadmin.colonyadmin.Services.*
import mx.com.colonyadmin.colonyadmin.Utils.Utils
import org.json.JSONArray
import org.json.JSONObject
import java.net.URISyntaxException





class MainActivity : AppCompatActivity(), ProfileFragment.OnFragmentInteractionListener, GuestListFragment.OnFragmentInteractionListener, MapFragment.OnFragmentInteractionListener, NewGuestFragment.OnFragmentInteractionListener {

    lateinit var utils: Utils
    private lateinit var progressDialog: ProgressDialog
    lateinit var _idUser: String
    lateinit var _addressUser: String
    lateinit var _addressNumber: String
    lateinit var _email: String
    private var mSocket: Socket? = intializeSocket()

    private var lstGuests: MutableList<DataXXXXXX>? = null
    var lstAddress: MutableList<DataXXXXX>? = null
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
        setContentView(R.layout.activity_main)
        if (mSocket == null) {
            val builder = AlertDialog.Builder(this@MainActivity)

            // Set the alert dialog title
            builder.setTitle("Error al conectar el socket")

            // Display a message on alert dialog
            builder.setMessage("Hubo un error con la co7nexión del socket, si persisten las molestias dirijase a la pagina www.pornhub.com, gracias :)")

            // Set a positive button and its click listener on alert dialog
            builder.setPositiveButton("Aceptar") { dialog, which ->
                // Do something when user press the positive button

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent);
            }
        } else {
            mSocket!!.connect()
        }
        val b = intent.extras
        // or other values
        if (b != null) {
            _idUser = b.getString("_id")
            _addressUser = b.getString("_address")
            _addressNumber = b.getString("address_number")
            _email = b.getString("email")
        } else {
            val intent = Intent(this.baseContext, LoginActivity::class.java)
            startActivity(intent)
        }
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
        //Inicializamos el dialog para cargar a los invitados
        showLoading()



        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //Initialziation of the first fragment
        val fragment = GuestListFragment.newInstance()
        utils = Utils()
        utils.addFragmentToActivity(this, this.supportFragmentManager, fragment, R.id.mainFrameLayout, utils.GuestList)


    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_guest_list -> {
                val fragment = GuestListFragment.newInstance()
                utils.addFragmentToActivity(this, this.supportFragmentManager, fragment, R.id.mainFrameLayout, utils.GuestList)
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_profile -> {
                val fragment = ProfileFragment.newInstance()
                utils.addFragmentToActivity(this, this.supportFragmentManager, fragment, R.id.mainFrameLayout, utils.ProfileFragment)
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_map -> {
                val fragment = MapFragment.newInstance()
                utils.addFragmentToActivity(this, this.supportFragmentManager, fragment, R.id.mainFrameLayout, utils.MapFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    public override fun onDestroy() {
        super.onDestroy()

        mSocket!!.disconnect()
    }

    fun showLoading() {
        progressDialog = ProgressDialog.show(this, null, null)
        progressDialog.setContentView(ProgressBar(this))
        progressDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun hideLoading() {
        try {
            progressDialog.cancel()
        } catch (ex: Exception) {

        }

    }

    fun setListGuests(lst: MutableList<DataXXXXXX>) {
        lstGuests = lst
    }

    fun getListGuests(): MutableList<DataXXXXXX>? {
        return lstGuests
    }

    fun setListAddress(lst: MutableList<DataXXXXX>) {
        lstAddress = lst
    }

    fun getListAddress(): MutableList<DataXXXXX>? {
        return lstAddress
    }

    //Socket IO functionality
    fun getHelp(nameAddress: String) {

        val rootObject = JSONObject()
        rootObject.put("address", nameAddress)
        rootObject.put("address_number", _addressNumber)
        rootObject.put("email", _email)

        mSocket!!.emit("sos", rootObject)
    }



    fun addFragmentToActivity(fragment: Fragment, frameId: Int) {

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.setCustomAnimations(R.anim.exit_to_left, R.anim.enter_from_right)
        transaction.replace(frameId, fragment)
        transaction.commit()

    }
}
