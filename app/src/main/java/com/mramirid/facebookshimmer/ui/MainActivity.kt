package com.mramirid.facebookshimmer.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mramirid.facebookshimmer.R
import com.mramirid.facebookshimmer.adapters.RecipeListAdapter
import com.mramirid.facebookshimmer.models.Recipe
import com.mramirid.facebookshimmer.utils.ShimmerApplication
import com.mramirid.facebookshimmer.utils.ShimmerDividerItemDecoration
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray


class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val URL_API = "https://api.androidhive.info/json/shimmer/menu.php"
    }

    private var cartList = mutableListOf<Recipe>()
    private var recipeListAdapter = RecipeListAdapter(cartList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.addItemDecoration(
            ShimmerDividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL,
                16
            )
        )
        recycler_view.adapter = recipeListAdapter

        // Making http call and fetching menu json
        fetchRecipes()
    }

    private fun fetchRecipes() {
        val request = JsonArrayRequest(URL_API,
            Response.Listener<JSONArray> { response ->
                if (response.length() == 0) {
                    Toast.makeText(
                        this@MainActivity,
                        "Couldn't fetch the menu! Please try again.",
                        Toast.LENGTH_LONG
                    ).show()
                    return@Listener
                }

                val recipes: List<Recipe> = Gson().fromJson(
                    response.toString(),
                    object : TypeToken<List<Recipe>>() {}.type
                )

                // Adding recipes to cart list
                cartList.clear()
                cartList.addAll(recipes)

                // Refreshing recycler view
                recipeListAdapter.notifyDataSetChanged()

                // Stop animating Shimmer and hide the layout
                shimmer_view_container.stopShimmer()
                shimmer_view_container.visibility = View.GONE
            }, Response.ErrorListener { error ->
                // error in getting json
                Log.e(TAG, "Error: ${error.message}")
                Toast.makeText(this@MainActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        ShimmerApplication.getInstance().addToRequestQueue(request)
    }

    override fun onResume() {
        super.onResume()
        shimmer_view_container.startShimmer()  // Mulai animasi Shimmer
    }

    override fun onPause() {
        shimmer_view_container.stopShimmer()  // Stop animasi Shimmer
        super.onPause()
    }
}
