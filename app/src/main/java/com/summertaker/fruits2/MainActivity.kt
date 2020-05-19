package com.summertaker.fruits2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.math.abs


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationViewPager.clipToPadding = false
        locationViewPager.clipChildren = false
        locationViewPager.offscreenPageLimit = 3
        locationViewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.95f + r * 0.05f
        }
        locationViewPager.setPageTransformer(compositePageTransformer)

        val url = "http://summertaker.cafe24.com/reader/akb48_json.php"

        // Post parameters: Form fields and values
        val params = HashMap<String, String>()
        params["foo1"] = "bar1"
        params["foo2"] = "bar2"
        val jsonObject = JSONObject(params as Map<*, *>)

        // Volley post request with parameters
        val request =
            JsonObjectRequest(Request.Method.POST, url, jsonObject, Response.Listener { response ->
                //println("응답: $response");
                val fruits = ArrayList<Fruit>()
                try {
                    val jsonArray = response.getJSONArray("members")
                    val shuffleArray = shuffleJsonArray(jsonArray);
                    if (shuffleArray != null) {
                        for (i in 0 until shuffleArray.length()) {
                            val r = shuffleArray.getJSONObject(i)
                            fruits.add(
                                Fruit(
                                    r.getString("groups"),
                                    r.getString("team"),
                                    r.getString("name"),
                                    r.getString("furigana"),
                                    r.getString("birthday"),
                                    r.getInt("age"),
                                    r.getString("image"),
                                    r.getString("twitter"),
                                    r.getString("instagram"),
                                    r.getString("wiki")
                                )
                            )
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                val adapter = PagerRecyclerAdapter(fruits)
                locationViewPager.adapter = adapter
            }, Response.ErrorListener {
                println("에러: $it")
            })

        // Volley request policy, only one time request to avoid duplicate transaction
        request.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            // 0 means no retry
            0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
            1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        // Add the volley post request to the request queue
        VolleySingleton.getInstance(this).addToRequestQueue(request)
    }

    /*
     * https://stackoverflow.com/questions/5531130/an-efficient-way-to-shuffle-a-json-array-in-java
     */
    @Throws(JSONException::class)
    fun shuffleJsonArray(array: JSONArray): JSONArray? {
        // Implementing Fisher–Yates shuffle
        val rnd = Random()
        for (i in array.length() - 1 downTo 0) {
            val j: Int = rnd.nextInt(i + 1)
            // Simple swap
            val `object` = array[j]
            array.put(j, array[i])
            array.put(i, `object`)
        }
        return array
    }
}
