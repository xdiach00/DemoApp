package com.xdiach.demoapp.adapter

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.xdiach.demoapp.ObjectFragment
import com.xdiach.demoapp.R
import com.xdiach.demoapp.data.DataModel
import com.xdiach.demoapp.latitude
import com.xdiach.demoapp.longitude

var distance: Float = 0.0f

class DataListAdapter: RecyclerView.Adapter<DataListAdapter.ViewHolder>() {

    private var dataList: List<DataModel>? = null

    fun setDataList(dataList: List<DataModel>?) {
        this.dataList = dataList

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataListAdapter.ViewHolder, position: Int) {
        holder.bind(dataList?.get(position)!!)

        val uuid = dataList?.get(position)!!.uuid
        holder.itemView.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {

                val activity = v!!.context as AppCompatActivity
                val objectFragment = ObjectFragment()
                val args = Bundle()
                args.putString("uuid", uuid)
                objectFragment.arguments = args

                activity.supportFragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.slide_out,
                    R.anim.fade_in,
                    R.anim.fade_out
                ).replace(R.id.mainLayout, objectFragment).addToBackStack(null).commit()
            }
        })
    }

    override fun getItemCount(): Int {
        if (dataList == null) return 0
        else return dataList?.size!!
    }

    class ViewHolder(view : View): RecyclerView.ViewHolder(view) {

        val thumbImage = view.findViewById<ImageView>(R.id.thumbImage)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvBody = view.findViewById<TextView>(R.id.tvBody)

        fun bind(data: DataModel) {

            val startPoint = Location("locationA")
            startPoint.setLatitude(latitude)
            startPoint.setLongitude(longitude)

            val endPoint = Location("locationA")
            endPoint.setLatitude(data.location.latitude)
            endPoint.setLongitude(data.location.longitude)

            distance = startPoint.distanceTo(endPoint)

            tvTitle.text = data.name
            tvBody.text = (distance / 1000).toInt().toString() + " km"

            Picasso.get().load(data.thumbnail_image).into(thumbImage)
        }
    }
}