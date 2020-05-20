package com.summertaker.fruits2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
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

    private val fruits = ArrayList<Fruit>()
    private var mPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ivTwitter.setOnClickListener {
            val fruit: Fruit = fruits[mPosition]
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fruit.twitter))
            startActivity(intent)
        }

        ivInstagram.setOnClickListener {
            val fruit: Fruit = fruits[mPosition]
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fruit.instagram))
            startActivity(intent)
        }

        memberViewPager.clipToPadding = false
        memberViewPager.clipChildren = false
        memberViewPager.offscreenPageLimit = 3
        memberViewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.95f + r * 0.05f
        }

        memberViewPager.setPageTransformer(compositePageTransformer)
        memberViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mPosition = position
                val fruit: Fruit = fruits[position]
                //Log.e(">>", fruit.name)
                renderMember(fruit)
            }
        })

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
                                    r.getString("age"),
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
                memberViewPager.adapter = adapter
                renderMember(fruits[0])
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

    fun renderMember(fruit: Fruit) {
        supportActionBar?.title = fruit.group + " " + fruit.team

        tvBirthday.text = fruit.birthday
        tvAge.text = fruit.age
        tvName.text = fruit.name
        tvFurigana.text = fruit.furigana

        if (fruit.twitter.isEmpty()) {
            ivTwitter.visibility = View.GONE
        } else {
            ivTwitter.visibility = View.VISIBLE
        }
        if (fruit.instagram.isEmpty()) {
            ivInstagram.visibility = View.GONE
        } else {
            ivInstagram.visibility = View.VISIBLE
        }
    }
}
