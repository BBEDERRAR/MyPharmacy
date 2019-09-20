package esi.tdm.mypharmacy.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import esi.tdm.mypharmacy.R
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import esi.tdm.mypharmacy.entity.User
import esi.tdm.mypharmacy.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        val firstNameTextField = view.findViewById<EditText>(R.id.editTextFirstName);
        val lastNameTextField = view.findViewById<EditText>(R.id.editTextLastName);
        val phoneTextField = view.findViewById<EditText>(R.id.editTextPhone);
        val nssTextField = view.findViewById<EditText>(R.id.editTextNss);
        val sendButton = view.findViewById<Button>(R.id.signup);
        val loginButton = view.findViewById<Button>(R.id.login);

        loginButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        sendButton.setOnClickListener {

            val firstName = firstNameTextField.getText().toString()
            val lastName = lastNameTextField.getText().toString()
            val nss = nssTextField.getText().toString()
            val phone = phoneTextField.getText().toString()

            if (firstName.isEmpty()) {
                firstNameTextField.error = "First Name required !"
                firstNameTextField.requestFocus()
                return@setOnClickListener
            }

            if (lastName.isEmpty()) {
                lastNameTextField.error = "Last Name required !"
                lastNameTextField.requestFocus()
                return@setOnClickListener
            }

            if (nss.isEmpty()) {
                nssTextField.error = "NSS required !"
                nssTextField.requestFocus()
                return@setOnClickListener
            }

            if (phone.isEmpty()) {
                phoneTextField.error = "Phone required !"
                phoneTextField.requestFocus()
                return@setOnClickListener
            }

            var call = RetrofitService.UserEndpoint.signUp(
                firstName, lastName, phone, nss
            )
            call.enqueue(object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e("SignUP", "failed sign up")
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
//                        val user = response?.body()
//                        Log.i("SignUP","successfully sign up")
//                        Log.e("User",user?.first_name.toString())
                    } else {
                    }
                }
            })
        }

        return view
    }


}
