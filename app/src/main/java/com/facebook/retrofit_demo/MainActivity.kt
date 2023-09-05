package com.facebook.retrofit_demo

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private  lateinit var textView: TextView
    private  lateinit var buttonget:Button
    private  lateinit var buttonpost:Button
    private  lateinit var buttonput:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView=findViewById(R.id.text_view_result)
        buttonget=findViewById(R.id.get)
        buttonpost=findViewById(R.id.post)
        buttonput=findViewById(R.id.put)
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB cache size (adjust as needed)
        val cacheDirectory = File(applicationContext.cacheDir, "http-cache")
        val cache = Cache(cacheDirectory, cacheSize)


        val OkHttpClient=OkHttpClient.Builder()
            .cache(cache)
            .readTimeout(30,TimeUnit.SECONDS)
            .writeTimeout(30,TimeUnit.SECONDS)
            .build();


        buttonget.setOnClickListener{

            val retrofit=Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .client(OkHttpClient)
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
                    textView.text=" "
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
                .client(OkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

            val jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi::class.java)

            val newPost = Post(userId = 1, id = 0, title = "New Post Title", text = "New Post Text")
           /* val userIdPart = MultipartBody.Part.createFormData("userId", "1")
            val idPart = MultipartBody.Part.createFormData("id", "0")
            val titlePart = MultipartBody.Part.createFormData("title", "New Post Title")
            val textPart = MultipartBody.Part.createFormData("text", "New Post Text")
            val partsList = listOf(userIdPart, idPart, titlePart, textPart)
*/
            val createcall: Call<Post> = jsonPlaceHolderApi.postdata(newPost)
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
                    // Handle the failure here
                    if (t != null) {
                        textView.text = "Error: ${t.message}"
                    } else {
                        textView.text = "Unknown error occurred"
                    }
                }
            })
        }

        buttonput.setOnClickListener(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(OkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)

        // Create a new Post object with updated data
        val updatedPost = Post(userId = 1, id = 1, title = "Updated Post Title", text = "Updated Post Text")

        val putCall: Call<Post> = jsonPlaceHolderApi.putPost(1, updatedPost) // Replace '1' with the ID of the post you want to update
        putCall.enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>?, response: Response<Post>?) {
                if (response != null) {
                    if (response.isSuccessful) {
                        val updatedPost = response.body()
                        textView.text = """
                        Updated Post ID: ${updatedPost?.id}
                        User ID: ${updatedPost?.userId}
                        Title: ${updatedPost?.title}
                        Text: ${updatedPost?.text}
                    """.trimIndent()
                    }
                }
            }

            override fun onFailure(call: Call<Post>?, t: Throwable?) {
                // Handle the failure here
                if (t != null) {
                    textView.text = "Error: ${t.message}"
                } else {
                    textView.text = "Unknown error occurred"
                }
            }
        })
    }
    }
}