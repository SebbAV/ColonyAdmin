package com.example.cbqa_0043.ca_guestapp.MainActivity

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.widget.TextView
import com.example.cbqa_0043.ca_guestapp.ARFragment.ARFragment
import com.example.cbqa_0043.ca_guestapp.InvitationFragment.invitation
import com.example.cbqa_0043.ca_guestapp.R
import com.example.cbqa_0043.ca_guestapp.Utils.Utils
import com.google.android.gms.maps.MapFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_invitation.*

class MainActivity : AppCompatActivity(), invitation.OnFragmentInteractionListener, com.example.cbqa_0043.ca_guestapp.MapFragment.MapFragment.OnFragmentInteractionListener,ARFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    lateinit var utils  : Utils
    lateinit var _idUser : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //Initialziation of the first fragment
        val fragment = invitation.newInstance()
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
}
