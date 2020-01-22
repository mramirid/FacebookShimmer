package com.mramirid.facebookshimmer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mramirid.facebookshimmer.R
import com.mramirid.facebookshimmer.models.Recipe
import kotlinx.android.synthetic.main.recipe_list_item.view.*

class RecipeListAdapter(
    private val cartList: List<Recipe>
) : RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recipe_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = cartList.size

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bindViews(cartList[position])
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindViews(recipe: Recipe) {
            with(itemView) {
                name.text = recipe.name
                chef.text = "By ${recipe.chef}"
                description.text = recipe.description
                price.text = "Price: $${recipe.price}"
                timestamp.text = recipe.timestamp

                Glide.with(context)
                    .load(recipe.thumbnail)
                    .into(thumbnail)
            }
        }
    }
}