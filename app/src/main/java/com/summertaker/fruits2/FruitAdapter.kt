package com.summertaker.fruits2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.item_container_location.view.*

class FruitAdapter(private val list: ArrayList<Fruit>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        val view = inflater.inflate(R.layout.item_container_location, container, false)

        //Glide.with(container.context).load(list[position].image).placeholder(R.drawable.placeholder).into(view.ivFruit);

        view.textLocation.text = list[position].name
        //view.tvFruitFurigana.text = list[position].furigana
        //view.tvFruitGroupTeam.text = list[position].group + " " + list[position].team

        /*
        val source = list[position].birthday
        val result = source.split("-").toTypedArray()
        val birthday = result[0] + "年 " + result[1] + "月 " + result[2] + "日"
        val age = " (" + list[position].age.toString() + "歳)"
        view.tvFruitBirthday.text = birthday + age

        if (list[position].twitter.isEmpty()) {
            view.ivTwitter.visibility = View.GONE
        } else {
            view.ivTwitter.visibility = View.VISIBLE
            view.ivTwitter.setOnClickListener {
                container.context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(list[position].twitter))
                )
            }
        }
        if (list[position].instagram.isEmpty()) {
            view.ivInstagram.visibility = View.GONE
        } else {
            view.ivInstagram.visibility = View.VISIBLE
            view.ivInstagram.setOnClickListener {
                container.context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(list[position].instagram))
                )
            }
        }
        if (list[position].wiki.isEmpty()) {
            view.ivWiki.visibility = View.GONE
        } else {
            view.ivWiki.visibility = View.VISIBLE
            view.ivWiki.setOnClickListener {
                container.context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(list[position].wiki))
                )
            }
        }
        */

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View?)
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return list.size
    }
}