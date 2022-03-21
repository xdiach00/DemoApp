package com.xdiach.demoapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.xdiach.demoapp.adapter.distance
import com.xdiach.demoapp.data.ObjectModel
import com.xdiach.demoapp.retrofit.RetroInstance
import com.xdiach.demoapp.retrofit.RetroServiceInterface
import retrofit2.*


class ObjectFragment : Fragment() {

    var uuid: String = ""
    var name: String = ""
    var tvName: TextView? = null
    var tvDistance: TextView? = null
    var tvCity: TextView? = null
    var tvStreet: TextView? = null
    var tvDescription: TextView? = null
    var imageURL: String = ""
    var tbImage: ImageView? = null
    var latitudeTarget: Double = 0.0
    var longitudeTarget: Double = 0.0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_object, container, false)

        uuid = arguments?.getString("uuid", "123").toString()
        Log.d(uuid, "Fragment")

        loadDescription(uuid)

        activity?.window?.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvName = view.findViewById(R.id.tvName)
        tvCity = view.findViewById(R.id.tvCity)
        tvStreet = view.findViewById(R.id.tvStreet)
        tvDescription = view.findViewById(R.id.tvDescription)
        tbImage = view.findViewById(R.id.imageObject)
        tvDistance = view.findViewById(R.id.tvDistance)
        val btnNavigate = view.findViewById<Button>(R.id.btnNav)
        val imageNav = view.findViewById<ImageView>(R.id.imageNavigate)
        val imageBack = view.findViewById<ImageView>(R.id.imageBack)

        imageBack.setOnClickListener(View.OnClickListener {
            goBack()
        })

        imageNav.setOnClickListener(View.OnClickListener {
            openGoogle()
        })

        btnNavigate.setOnClickListener(View.OnClickListener {
            openGoogle()
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    goBack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    private fun loadDescription(uuid: String) {

        val retroService =
            RetroInstance.getRetroInstance().create(RetroServiceInterface::class.java)
        val call = retroService.getDataObject(uuid)

        call.enqueue(object : retrofit2.Callback<ObjectModel> {
            override fun onResponse(call: Call<ObjectModel>, response: Response<ObjectModel>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    Log.d("Target", "Success")
                    data?.let {
                        tvName!!.setText(data.name.toString())
                        imageURL = data.image.toString()
                        tvCity!!.setText(data.address.city)
                        tvStreet!!.setText(data.address.zip + " " + data.address.street)
                        tvDescription!!.setText(data.description)
                        latitudeTarget = data.location.latitude
                        longitudeTarget = data.location.longitude
                        Picasso.get().load(imageURL).into(tbImage)

                        val startPoint = Location("locationA")
                        startPoint.setLatitude(latitude)
                        startPoint.setLongitude(longitude)

                        val endPoint = Location("locationA")
                        endPoint.setLatitude(data.location.latitude)
                        endPoint.setLongitude(data.location.longitude)

                        distance = startPoint.distanceTo(endPoint)
                        distance = distance / 1000

                        tvDistance!!.setText(distance.toInt().toString() + " km")
                    }
                } else {
                    Log.d("Target", "Failed")
                }
            }

            override fun onFailure(call: Call<ObjectModel>, t: Throwable) {
                Log.d("Target", "Failed response")
            }
        })
    }

    fun goBack() {
        activity?.window?.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            statusBarColor = resources.getColor(R.color.yellow)
        }

        getFragmentManager()?.popBackStack()
    }

    fun openGoogle() {
        // Create a Uri from an intent string. Use the result to create an Intent.
        val gmmIntentUri = Uri.parse("geo:" + latitudeTarget + "," + longitudeTarget)

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps")

        // Attempt to start an activity that can handle the Intent
        startActivity(mapIntent)
    }
}