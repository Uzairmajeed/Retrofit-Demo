package com.facebook.retrofit_demo

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class MainActivity : AppCompatActivity() {
    private  lateinit var textView: TextView
    private  lateinit var buttonget:Button
    private  lateinit var buttonpost:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView=findViewById(R.id.text_view_result)
        buttonget=findViewById(R.id.get)
        buttonpost=findViewById(R.id.post)


        buttonget.setOnClickListener{

            val retrofit=Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

            val jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi::class.java)
            val call:Call<List<Post>> =  jsonPlaceHolderApi.getPosts()
            call.enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>?, response: Response<List<Post>>?) {
                    if (!response?.isSuccessful!!){
                        textView.text="Code: ${response.code()}"
                        return
                    }
                    val posty=response.body()
                    for (post in posty){
                        var content=""
                        content +="ID: ${post.id}\n"
                        content += "User ID: ${post.userId}\n"
                        content += "Title: ${post.title}\n"
                        content += "Text: ${post.text}\n\n"
                        textView.append(content)
                    }
                }

                override fun onFailure(call: Call<List<Post>>?, t: Throwable?) {

                }
            })
        }

        buttonpost.setOnClickListener{
            val retrofit=Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

            val jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi::class.java)
            // Create a new post instance
            val newPost = Post(userId = 1, id = 0, title = "New Post Title", text = "New Post Text")
            val createcall:Call<Post> = jsonPlaceHolderApi.postdata(newPost)
            createcall.enqueue(object :Callback<Post>{
                override fun onResponse(call: Call<Post>?, response: Response<Post>?) {
                    if (response != null) {
                        if (response.isSuccessful){
                            val constant=response.body()
                            textView.text="""
                            Id: ${constant.id}
                            User ID: ${constant.userId}
                            Title: ${constant.title}
                            Text: ${constant.text}
                            
                        """.trimIndent()

                        }
                    }
                }

                override fun onFailure(call: Call<Post>?, t: Throwable?) {
                    TODO("Not yet implemented")
                }
            })
        }


    }
}