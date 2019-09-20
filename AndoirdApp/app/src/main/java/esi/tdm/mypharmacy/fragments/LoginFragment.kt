package esi.tdm.mypharmacy.fragments


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import esi.tdm.mypharmacy.R
import esi.tdm.mypharmacy.entity.User
import esi.tdm.mypharmacy.models.LoginResponse
import esi.tdm.mypharmacy.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import esi.tdm.mypharmacy.config.PREFS_NAME
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
import esi.tdm.mypharmacy.activities.MainActivity


class LoginFragment : Fragment() {
    lateinit var loggedUser: User;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_login, container, false)
        val phoneTextField = view.findViewById<EditText>(R.id.editTextPhone);
        val passwordTextField = view.findViewById<EditText>(R.id.password);
        val loginButton = view.findViewById<Button>(R.id.login);
        val signUpButton = view.findViewById<Button>(R.id.signup);
        val sharedPref: SharedPreferences = context!!.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)


        signUpButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }



        loginButton.setOnClickListener {

            val password = passwordTextField.getText().toString()
            val phone = phoneTextField.getText().toString()


            if (password.isEmpty()) {
                passwordTextField.error = "Password required !"
                passwordTextField.requestFocus()
                return@setOnClickListener
            }

            if (phone.isEmpty()) {
                phoneTextField.error = "Phone required !"
                phoneTextField.requestFocus()
                return@setOnClickListener
            }

            var call = RetrofitService.UserEndpoint.login(
                phone, password
            )
            call.enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("SignUP", "failed sign up")
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.i("Login", "successfully Login")
                        Log.e("activated ===> ", result!!.user.activated.toString())

                        loggedUser = User(
                            result!!.user.id,
                            result!!.user.firstName,
                            result!!.user.lastName,
                            result!!.user.phone,
                            result!!.user.nss,
                            result!!.user.activated,
                            result!!.token
                        )


                        val editor: SharedPreferences.Editor = sharedPref.edit()

                        editor.putInt("id", result!!.user.id)
                        editor.putString("firstName", result!!.user.firstName)
                        editor.putString("lastName", result!!.user.lastName)
                        editor.putString("phone", result!!.user.phone)
                        editor.putString("nss", result!!.user.nss)
                        editor.putBoolean("activated", result!!.user.activated)
                        editor.putString("token", result!!.user.token)

                        editor.apply()

                        Toast.makeText(this@LoginFragment.context, result.message, Toast.LENGTH_LONG).show()
                        if (loggedUser.activated == true) {
                            val startIntent = Intent(context, MainActivity::class.java)
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context?.startActivity(startIntent)
                            // go to main fragment
//                            view.findNavController().navigate(R.id.action_loginFragment_to_mainFragment)

                        } else {
                            // change password
                            val bundle = Bundle()
                            bundle.putString("phone", result!!.user.phone)
                            bundle.putString("password", password)
                            // set User id Arguments
                            view.findNavController()
                                .navigate(R.id.action_loginFragment_to_changePasswordFragment, bundle)
                        }


                    } else {
                    }
                }
            })
        }



        return view;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val sharedPref: SharedPreferences = context!!.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val phoneExist = sharedPref.contains("phone");
        if (phoneExist) {
            // go to main fragment
            Log.e("phoneExist", phoneExist.toString())

            view!!.findNavController().navigate(R.id.action_loginFragment_to_mainFragment)


        }
    }


}
