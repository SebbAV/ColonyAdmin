package mx.com.colonyadmin.colonyadmin.GuestList

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.dialog_invitation_guest.view.*
import mx.com.colonyadmin.colonyadmin.LoginActivity.LoginActivity
import mx.com.colonyadmin.colonyadmin.MainActivity.MainActivity
import mx.com.colonyadmin.colonyadmin.R
import mx.com.colonyadmin.colonyadmin.Services.*
import mx.com.colonyadmin.colonyadmin.Utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException

public class CustomAdapter(context: Context, resource: Int, list:MutableList<DataXXXXXX>, activity: MainActivity?)
       : ArrayAdapter<DataXXXXXX>(context, resource, list)  {

    var resource: Int
    var list: MutableList<DataXXXXXX>
    var vi: LayoutInflater
    var act: MainActivity?
    init {
        this.resource = resource
        this.list = list
        this.vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.act = activity
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var holder: ViewHolder
        var retView: View

        if(convertView == null){
            retView = vi.inflate(R.layout.info_guest, null)
            holder = ViewHolder()

            holder.txtName = retView.findViewById(R.id.txtInfoGuestName) as TextView?
            holder.txtEntrance = retView.findViewById(R.id.txtInfoGuestEntranceDate) as TextView?
            holder.txtPlates = retView.findViewById(R.id.txtInfoGuestPlates) as TextView?
            holder.linearLayout = retView.findViewById(R.id.infoWindowGuest) as LinearLayout?


            retView.tag = holder

        } else {
            holder = convertView.tag as ViewHolder
            retView = convertView
        }

        holder.txtName!!.text = list[position].name
        holder.txtName!!.setTag(position)
        holder.txtEntrance!!.text = "Hora de entrada: "+ list[position].entranceDate
        holder.txtPlates!!.text = "Placas: "+list[position].vehicle


        return retView
    }



    internal class ViewHolder {

        var txtName : TextView? = null
        var txtPlates : TextView? = null
        var txtEntrance : TextView? = null
        var linearLayout : LinearLayout? = null
    }

}