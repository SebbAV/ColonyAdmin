package mx.com.colonyadmin.colonyadmin.NewGuestFragment

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.dialog_invitation_guest.view.*
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

public class CustomAdapterGuests (context: Context, resource: Int, list:MutableList<DataXXXXXXXX>, activity: MainActivity?,userId:String)
        : ArrayAdapter<DataXXXXXXXX>(context, resource, list) , View.OnClickListener {

        var resource: Int
        var list: MutableList<DataXXXXXXXX>
        var vi: LayoutInflater
        var act: MainActivity?
        var userid : String
        init {
            this.resource = resource
            this.list = list
            this.vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            this.act = activity
            userid = userId
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

            holder.txtName!!.text = list[position].firstName
            holder.txtName!!.setTag(position)
            holder.txtEntrance!!.text = "Numero: "+ list[position].phone
            holder.txtPlates!!.text = "Email: "+list[position].email
            holder.linearLayout!!.setOnClickListener(this)

            return retView
        }


        override fun onClick(v: View) {
            val vh = v.tag as ViewHolder

            val position = vh.txtName!!.getTag() as Int
            val `object` = getItem(position)
            val dataModel = `object` as DataXXXXXXXX
            showInvitationDialog(dataModel)

        }

        private fun showInvitationDialog(dataModel : DataXXXXXXXX){
            //button click to show dialog

            //Inflate the dialog with custom view
            val mDialogView = LayoutInflater.from(act).inflate(R.layout.dialog_invitation_guest, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(act)
                    .setView(mDialogView)
            //show dialog
            val  mAlertDialog = mBuilder.show()
            //login button click of custom layout
            mDialogView.btnToolbarCloseInvitation.setOnClickListener {
                //dismiss dialog
                mAlertDialog.dismiss()

            }
            //send dialog
            mDialogView.invitation_sendInvitation.setOnClickListener {
                act!!.showLoading()
                var objInvitation : InvitationGuestRequest = InvitationGuestRequest(dataModel.id,userid)
                sendInvitationGuest(objInvitation)
                mAlertDialog.dismiss()
            }

        }

        private fun sendInvitationGuest(guest: InvitationGuestRequest){

            val retrofit = Retrofit.Builder().baseUrl(Utils.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()


            //Variable declaration
            var mAPIService: LoginService? = retrofit.create(LoginService::class.java)

            //Some Button click
            var activity = act

            mAPIService!!.sendGuestInvitation(guest).enqueue(object : Callback<InvitationGuestResponse> {
                override fun onResponse(call: Call<InvitationGuestResponse>, response: Response<InvitationGuestResponse>) {

                    if (response.isSuccessful()) {
                        Toast.makeText(activity, "Se ha enviado la invitación", Toast.LENGTH_SHORT).show()
                        activity!!.hideLoading()
                    }
                    else{
                        if (response.code() == 500)
                            Toast.makeText(activity, response.message(), Toast.LENGTH_SHORT).show()
                        activity!!.hideLoading()
                    }
                }

                override fun onFailure(call: Call<InvitationGuestResponse>, t: Throwable) {

                    if(t is SocketTimeoutException){

                        Toast.makeText(activity, "La invitación no fue mandada por error con el servidor", Toast.LENGTH_LONG).show()

                    }
                    else{
                        Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()

                    }
                    activity!!.hideLoading()
                }
            })
        }


        internal class ViewHolder {

            var txtName : TextView? = null
            var txtPlates : TextView? = null
            var txtEntrance : TextView? = null
            var linearLayout : LinearLayout? = null
        }


}