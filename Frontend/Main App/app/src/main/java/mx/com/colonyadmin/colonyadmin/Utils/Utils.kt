package mx.com.colonyadmin.colonyadmin.Utils

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import mx.com.colonyadmin.colonyadmin.Services.LoginService
import retrofit2.Retrofit
import java.util.regex.Pattern

class Utils {

    val GuestList = "GuestList"
    val ProfileFragment = "Profile"
    val MapFragment = "Map"
    companion object {
        val URL = "http://akarokhome.ddns.net:3000/v1/"
    }

    enum class types {
        STRING, INTEGER, FLOAT, BOOLEAN
    }



    fun addFragmentToActivity(activity: Activity, fragmentManager: FragmentManager,
                              fragment: Fragment, frameId: Int, tag: String) {
        //We create the transaction and establish the frameId (place that contains the fragment)
        // and the fragment that will be shown
        try {
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(frameId, fragment, tag)
            //Only if is not my main fragment i will add it to tha back stack

            if (tag != "GuestList")
                transaction.addToBackStack(tag)
            transaction.commit()
        } catch (ex: Exception) {
            activity.finish()
        }

    }


    /*
     * activity = The actual activity
     * map = The map that contains the value-key that will be saved in sharedPreferences
     * */
    fun editSharedPreferences(activity: AppCompatActivity, map: Map<String, String>): Boolean {
        //Instance of sharedPreferences with the url where will be generated and the accessibility mode
        val sharedPreferences = activity.getSharedPreferences("mx.com.epcon.pdh", MODE_PRIVATE)

        try {
            //Iterate the keys in map and if it matches with the value "STRING", "INTEGER", etc,
            // is the type that will be saved, and when it finish returns true, if something crashes return false
            for (key in map.keys) {
                if (key.contains("STRING"))
                    sharedPreferences.edit().putString(key.replace("STRING", ""), map[key]).apply()
                else if (key.contains("INTEGER"))
                    sharedPreferences.edit().putInt(key.replace("INTEGER", ""), Integer.parseInt(map[key])).apply()
                else if (key.contains("FLOAT"))
                    sharedPreferences.edit().putFloat(key.replace("FLOAT", ""), java.lang.Float.parseFloat(map[key])).apply()
                else if (key.contains("BOOLEAN"))
                    sharedPreferences.edit().putBoolean(key.replace("BOOLEAN", ""), java.lang.Boolean.parseBoolean(map[key])).apply()
            }
            return true
        } catch (e: Exception) {
            return false
        }

    }

    fun getSharedPreference(activity: AppCompatActivity, key: String, type: types): Any? {
        //Instance of sharedPreferences with the url where will be generated and the accessibility mode
        val sharedPreferences = activity.getSharedPreferences("mx.com.epcon.pdh", MODE_PRIVATE)
        when (type) {
            Utils.types.STRING -> return sharedPreferences.getString(key, "")
            Utils.types.INTEGER -> return sharedPreferences.getInt(key, 0)
            Utils.types.FLOAT -> return sharedPreferences.getFloat(key, 0f)
            Utils.types.BOOLEAN -> return sharedPreferences.getBoolean(key, false)
            else -> return null
        }
    }

    fun isEmailValid(email: String): Boolean {
        return Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }
}