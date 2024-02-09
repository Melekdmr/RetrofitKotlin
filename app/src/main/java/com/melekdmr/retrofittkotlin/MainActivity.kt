package com.melekdmr.retrofittkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.melekdmr.retrofittkotlin.adapter.RecyclerViewAdapter
import com.melekdmr.retrofittkotlin.databinding.ActivityMainBinding
import com.melekdmr.retrofittkotlin.model.CryptoModel
import com.melekdmr.retrofittkotlin.service.CryptoAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MainActivity : AppCompatActivity(),RecyclerViewAdapter.Listener {

    private lateinit var binding:ActivityMainBinding
    private val BASE_URL="https://raw.githubusercontent.com/"
    private var cryptoModels:ArrayList<CryptoModel> ?= null
    private var recyclerViewAdapter:RecyclerViewAdapter?=null
    //Disposable RxJava
    private var compositeDisposable:CompositeDisposable?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     binding= ActivityMainBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        //https://raw.githubusercontent.com/atilsamancioglu/K21-JSONDataSet/master/crypto.json
        compositeDisposable= CompositeDisposable()

        //RecyclerView
        val layoutManager:RecyclerView.LayoutManager=LinearLayoutManager(this)
        binding.recyclerView.layoutManager=layoutManager

         loadData()
    }
    private fun loadData(){

        val retrofit=Retrofit.Builder()
            .baseUrl(BASE_URL)
            //addConverterFactory dönen yanıt tipini neye çevireceğini söyleyen bir yapı.
            // json’ı java objelerine çevireceğiz
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(CryptoAPI::class.java)

        compositeDisposable?.add(retrofit.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResponse))


      /*  önceden yapılandırılmış bir Retrofit nesnesi (retrofit) kullanılarak bir
         CryptoAPI arayüzüne dayalı bir servis oluşturulur.
        val service=retrofit.create(CryptoAPI::class.java)
        oluşturulan servis (service) aracılığıyla getData() işlevi çağrılır.
         Bu, HTTP GET isteğini belirli bir URL'ye yapar ve sunucudan veri almayı başlatır.
          val call=service.getData()
        Retrofit, HTTP isteklerinin yanıtlarını işlemek için Java'nın Callback arayüzünü kullanır.

      call.enqueue(object:Callback<List<CryptoModel>>{
            override fun onResponse(
                call: Call<List<CryptoModel>>,
                response: Response<List<CryptoModel>>
            ) {
                if(response.isSuccessful){
                response.body()?.let{//boş gelmediyse kod bloğunun içindekini yap
                    cryptoModels=ArrayList(it)
                    cryptoModels?.let {
                        recyclerViewAdapter=RecyclerViewAdapter(it,this@MainActivity)
                        binding.recyclerView.adapter=recyclerViewAdapter
                    }

                   for(cryptoModel:CryptoModel in cryptoModels !!) {
                        println(cryptoModel.currency)
                        println(cryptoModel.price)

                    }

                }
                }

            }

            override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
                // istek sırasında bir hata oluştuğunda burası çalışır

            }

        })*/


    }
    private fun handleResponse(cryptoList: List<CryptoModel>) {
        cryptoModels = ArrayList(cryptoList)
        cryptoModels?.let {
            recyclerViewAdapter = RecyclerViewAdapter(it, this@MainActivity)
            binding.recyclerView.adapter = recyclerViewAdapter
        }
    }
    override fun onItemClick(cryptoModel: CryptoModel) {
        Toast.makeText(this,"Clicked: ${cryptoModel.currency}",Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }

}