package mx.com.colonyadmin.colonyadmin.MainActivity

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import mx.com.colonyadmin.colonyadmin.GuestList.GuestListFragment
import mx.com.colonyadmin.colonyadmin.MapFragment.MapFragment
import mx.com.colonyadmin.colonyadmin.ProfileFragment.ProfileFragment
import mx.com.colonyadmin.colonyadmin.R
import mx.com.colonyadmin.colonyadmin.Utils.Utils
import java.net.URISyntaxException

class MainActivity : AppCompatActivity(), ProfileFragment.OnFragmentInteractionListener, GuestListFragment.OnFragmentInteractionListener, MapFragment.OnFragmentInteractionListener {

    public lateinit var utils  : Utils

    private var mSocket: Socket? = null
    //Inicialization of socket io
        fun intializeSocket(): Socket?{
            try {
                mSocket = IO.socket("http://akarokhome.ddns.net:3000")
                return mSocket
            } catch (e: URISyntaxException) {
                return null
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
