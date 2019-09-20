package esi.tdm.mypharmacy.fragments


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ProgressBar
import androidx.navigation.findNavController
import esi.tdm.mypharmacy.R
import esi.tdm.mypharmacy.adapter.OrderAdapter
import esi.tdm.mypharmacy.entity.Order
import esi.tdm.mypharmacy.retrofit.RetrofitService
import esi.tdm.mypharmacy.roomDatabase.RoomService
import org.jetbrains.anko.bundleOf
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrderListFragment : Fragment() {
    var listOrdersResponse: List<Order>? = null;
    private val PREFS_NAME = "LOGIN_CREDENTIALS"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_order_list, container, false)
        var ordersList = view.findViewById(R.id.ordersList) as ListView
        val sharedPref: SharedPreferences = context!!.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        var progressBar = view.findViewById(R.id.progressBar1) as ProgressBar


        val userIdExist = sharedPref.contains("id");

        if (userIdExist ) {
            Log.e("userIdExist", userIdExist.toString())

            val userId = sharedPref.getInt("id",-1);

            var call = RetrofitService.OrderEndpoint.getOrderByUserId(userId.toString());
            call.enqueue(object : Callback<List<Order>> {
                override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                    Log.e("GetOrdersByUserID", "GetOrdersByUserID Not Working")
                    progressBar.visibility = View.INVISIBLE

                }

                override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                    if (response.isSuccessful) {
                        progressBar.visibility = View.INVISIBLE

                        listOrdersResponse = response.body()
                        Log.i("listOrdersFromAPI ", listOrdersResponse.toString())
                        ordersList.adapter = OrderAdapter(this@OrderListFragment.context!!, listOrdersResponse!!)
//                        ordersList.setOnItemClickListener { adapterView, view, i, l ->
////                            val order = (adapterView.getItemAtPosition(i) as Order)
////                            val bundle = Bundle()
////                            bundle.putString("id", order.id.toString())
////                                view.findNavController()
////                                    .navigate(R.id.action_ordersListFragment_to_orderDetailsFragment, bundle)
//                        }
                    }
                }
            })
        }else{
            view.findNavController().navigate(R.id.action_orderListFragment_to_loginFragment)
        }



        return view
    }


}
