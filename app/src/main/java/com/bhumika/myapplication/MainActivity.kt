package com.bhumika.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bhumika.myapplication.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvAdapter: RvAdapter
    private lateinit var data:ArrayList<Modal>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        supportActionBar?.hide()
        data=ArrayList()
        apiData
        rvAdapter=RvAdapter(this,data)
        binding.rv.layoutManager=LinearLayoutManager(this)
        binding.rv.adapter=rvAdapter
        binding.search.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                val filteredData=ArrayList<Modal>()
                for (item in data){
                    if (item.name.lowercase(Locale.getDefault()).contains(p0.toString().lowercase(Locale.getDefault()))){
                        filteredData.add(item)
                    }

                }
                if (filteredData.isEmpty()){
                    Toast.makeText(this@MainActivity, "no data available", Toast.LENGTH_SHORT).show()
                }
                else{
                    rvAdapter.changeData(filteredData)
                }
            }

        })

    }
    val apiData:Unit
        get() {
            val url="https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"

            val queue=Volley.newRequestQueue(this)
            val jsonObjectRequest:JsonObjectRequest =

                object:JsonObjectRequest(Method.GET,url,null,Response.Listener {
                    response ->
                    binding.progressBar.isVisible = false
                    try {
                        val dataArray = response.getJSONArray("data")
                        for (i in 0 until dataArray.length()){
                            val dataObject = dataArray.getJSONObject(i)
                            val symbol = dataObject.getString("symbol")
                            val name = dataObject.getString("name")
                            val quote = dataObject.getJSONObject("quote")
                            val USD = quote.getJSONObject("USD")
                            val price = String.format("$"+"%.2f",USD.getDouble("price"))

                            data.add(Modal(name,price,symbol))
                        }
                    }catch (e:Exception){
                        Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show()
                    }
                },Response.ErrorListener {
                    Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show()
                })
                {
                    override fun getHeaders(): Map<String, String> {
                        val headers = HashMap<String,String>()
                        headers["X-CMC_PRO_API_KEY"]="2cb23bbf-ad97-4d95-99eb-1137c93c3be3"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)
        }
}
