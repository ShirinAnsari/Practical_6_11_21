package com.example.shirinansaripracticalapp.view.fragment

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.shirinansaripracticalapp.R
import com.example.shirinansaripracticalapp.databinding.FragmentDetailBinding
import com.example.shirinansaripracticalapp.model.UserResponse
import com.shirinansaripractical.view.fragment.base.BaseFragment

class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    private var userInfo: UserResponse.UserItem? = null
    
    private fun getBundle() {
        val bundle = arguments
        userInfo = bundle!!.getParcelable(getString(R.string.user_detail))
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_detail
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getBundle()
        setDetailUI()
    }

    private fun setDetailUI() {
        if (userInfo == null) return

        //Picture
        if (userInfo!!.picture != null && userInfo!!.picture!!.large != null) {
            Glide.with(requireContext())
                .load(userInfo!!.picture!!.large)
                .centerCrop()
                .into(dataBinding!!.ivUser)
        }
        //Name
        dataBinding!!.tvUserName.text =
            getString(
                R.string.str_user_name_detail,
                userInfo!!.name!!.title,
                userInfo!!.name!!.first,
                userInfo!!.name!!.last
            )
        //Email
        dataBinding!!.tvEmail.text =
            getString(
                R.string.str_email,
                userInfo!!.email
            )
        //Phone
        dataBinding!!.tvPhone.text =
            getString(
                R.string.str_phone,
                userInfo!!.phone
            )
        //Gender
        dataBinding!!.tvGender.text =
            getString(
                R.string.str_gender,
                userInfo!!.gender
            )
        //City
        dataBinding!!.tvCity.text =
            getString(
                R.string.str_city,
                userInfo!!.location!!.city
            )
        //State
        dataBinding!!.tvState.text =
            getString(
                R.string.str_state,
                userInfo!!.location!!.state
            )
        //Country
        dataBinding!!.tvCountry.text =
            getString(
                R.string.str_country,
                userInfo!!.location!!.country
            )
        //PostCode
        dataBinding!!.tvPostCode.text =
            getString(
                R.string.str_postcode,
                userInfo!!.location!!.postcode
            )
    }
}