package mx.com.colonyadmin.colonyadmin.GuestList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import mx.com.colonyadmin.colonyadmin.MainActivity.MainActivity
import mx.com.colonyadmin.colonyadmin.R

public class CustomAdapter(context: Context, resource: Int) {
       /*:{ ArrayAdapter<Marker>(context, resource, list) {

    var resource: Int
    var list: ArrayList<Marker>
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
            retView = vi.inflate(R.layout.info_marker, null)
            holder = ViewHolder()

            holder.image = retView.findViewById(R.id.imgInfoMarker) as ImageView?
            holder.txtDescription = retView.findViewById(R.id.txtInfoMarkerDescription) as TextView?
            holder.txtLatitude = retView.findViewById(R.id.txtInfoMarkerLat) as TextView?
            holder.txtLongitude = retView.findViewById(R.id.txtInfoMarkerLng) as TextView?
            holder.linearLayout = retView.findViewById(R.id.infoWindowMarker) as LinearLayout?


            retView.tag = holder

        } else {
            holder = convertView.tag as ViewHolder
            retView = convertView
        }

        holder.txtLatitude!!.text = list[position].latitude.toString()
        holder.txtLongitude!!.text = list[position].longitude.toString()
        holder.txtDescription!!.text = list[position].description
        holder.linearLayout!!.setOnClickListener(View.OnClickListener {

            if (context == MainActivity()) {
                (context as MainActivity).navigateToActivity(list[position].latitude.toString(), list[position].longitude.toString(), list[position].description!!)
            }
//                MainActivity().navigateToActivity(list[position].latitude.toString(), list[position].longitude.toString(), list[position].description!!)

        })

        return retView
    }

    internal class ViewHolder {
        var image: ImageView? = null
        var txtDescription : TextView? = null
        var txtLatitude : TextView? = null
        var txtLongitude : TextView? = null
        var linearLayout : LinearLayout? = null

    }
*/
}