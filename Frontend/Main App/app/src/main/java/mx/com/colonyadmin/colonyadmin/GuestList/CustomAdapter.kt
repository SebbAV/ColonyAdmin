package mx.com.colonyadmin.colonyadmin.GuestList

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import mx.com.colonyadmin.colonyadmin.MainActivity.MainActivity
import mx.com.colonyadmin.colonyadmin.R
import mx.com.colonyadmin.colonyadmin.Services.DataXXXXXX

public class CustomAdapter(context: Context, resource: Int, list:MutableList<DataXXXXXX>)
       : ArrayAdapter<DataXXXXXX>(context, resource, list) , View.OnClickListener {

    var resource: Int
    var list: MutableList<DataXXXXXX>
    var vi: LayoutInflater

    init {
        this.resource = resource
        this.list = list
        this.vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
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
        holder.linearLayout!!.setOnClickListener(View.OnClickListener {

/*            if (context == MainActivity()) {
                (context as MainActivity).navigateToActivity(list[position].latitude.toString(), list[position].longitude.toString(), list[position].description!!)
              }
               MainActivity().navigateToActivity(list[position].latitude.toString(), list[position].longitude.toString(), list[position].description!!)*/

        })

        return retView
    }


    override fun onClick(v: View) {
        val vh = v.tag as ViewHolder

        val position = vh.txtName!!.getTag() as Int
        val `object` = getItem(position)
        val dataModel = `object` as DataXXXXXX
        var zone = ""

        if (zone !== "") {
            val bundle = Bundle()
            bundle.putString("zone", zone)

           /* val fragment2 = DetaillZonesFragment()
            fragment2.setArguments(bundle)

            val jUtils = ActivityUtils()
            jUtils.addFragmentToActivity(activity, this.fragmentManager, fragment2, R.id.mainFrameLayout, jUtils.DetaillZonesFragment, 0)*/
        }
    }

    internal class ViewHolder {

        var txtName : TextView? = null
        var txtPlates : TextView? = null
        var txtEntrance : TextView? = null
        var linearLayout : LinearLayout? = null
    }

}