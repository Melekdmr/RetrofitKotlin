package com.melekdmr.retrofittkotlin.service

import io.reactivex.Observable
import com.melekdmr.retrofittkotlin.model.CryptoModel
import retrofit2.Call
import retrofit2.http.GET

interface CryptoAPI {
    // kullannılacak metot (GET(veri çekmek) ,POST(yazmak,değiştirmek),DELETE);

    //https://raw.githubusercontent.com/
    // atilsamancioglu/K21-JSONDataSet/master/crypto.json
    @GET(" atilsamancioglu/K21-JSONDataSet/master/crypto.json")
    //observabla:gözlemlenebilir obje, veriler geldiğinde alan ve bu verileri yayın yapan
    fun getData():Observable<List<CryptoModel>>
    //getData() fonksiyonunun hiçbir parametre almadan ve Call<List<CryptoModel>> t
    //türünde bir değer döndüreceğini belirtir.bu değeri çağıran kod Call türünden bir nesne alır
    //fun getData(): Call<List<CryptoModel>>



}