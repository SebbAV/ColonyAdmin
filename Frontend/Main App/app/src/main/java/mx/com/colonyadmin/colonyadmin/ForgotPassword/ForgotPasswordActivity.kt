package mx.com.colonyadmin.colonyadmin.ForgotPassword

import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.ProgressBar
import mx.com.colonyadmin.colonyadmin.R

class ForgotPasswordActivity : AppCompatActivity(), EmailForgotFragment.OnFragmentInteractionListener, EmailCodeFragment.OnFragmentInteractionListener, ChangePasswordFragment.OnFragmentInteractionListener {
    internal lateinit var progressDialog: ProgressDialog

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        addFragmentToActivity(EmailForgotFragment(),R.id.changePasswordFrame)

    }

    fun addFragmentToActivity(fragment: Fragment, frameId: Int) {

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(frameId, fragment)
        transaction.commit()

    }

    fun showLoading() {
        progressDialog = ProgressDialog.show(this, null, null)
        progressDialog.setContentView(ProgressBar(this))
        progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun hideLoading() {
        try {
            progressDialog.cancel()
        } catch (ex: Exception) {

        }
    }

}
