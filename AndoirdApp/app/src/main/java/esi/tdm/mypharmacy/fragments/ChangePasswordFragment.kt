package esi.tdm.mypharmacy.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import esi.tdm.mypharmacy.R
import esi.tdm.mypharmacy.entity.User
import esi.tdm.mypharmacy.models.LoginResponse
import esi.tdm.mypharmacy.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_change_password, container, false)


        val newPasswordTextField = view.findViewById<EditText>(R.id.newPassword);
        val newPasswordConfirmationField = view.findViewById<EditText>(R.id.newPasswordConfirmation);
        val changePasswordButton = view.findViewById<Button>(R.id.changePasswordButton);

        if (getArguments() != null) {
            val phone = getArguments()?.getString("phone");
            val old_password = getArguments()?.getString("password");
            changePasswordButton.setOnClickListener {

                val newPassword = newPasswordTextField.getText().toString()
                val newPasswordConfirmation = newPasswordConfirmationField.getText().toString()

                if (newPassword.isEmpty()) {
                    newPasswordTextField.error = "New Password required !"
                    newPasswordTextField.requestFocus()
                    return@setOnClickListener
                }

                if (newPasswordConfirmation.isEmpty() || newPassword != newPasswordConfirmation) {
                    newPasswordConfirmationField.error = "Confirm your password required !"
                    newPasswordConfirmationField.requestFocus()
                    return@setOnClickListener
                }


                var call = RetrofitService.UserEndpoint.changePassword(
                    phone,
                    old_password.toString(),
                    newPassword
                )
                call.enqueue(object : Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.e("changePassword", "failed to change password")
                    }

                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@ChangePasswordFragment.context, "Password Changed Successfully", Toast.LENGTH_LONG).show()
                        } else {
                            
                        }
                    }
                })
            }

        }



        return view
    }


}
