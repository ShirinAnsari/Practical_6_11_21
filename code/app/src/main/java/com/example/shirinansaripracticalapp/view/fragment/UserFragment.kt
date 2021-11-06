package com.example.shirinansaripracticalapp.view.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shirinansaripracticalapp.R
import com.example.shirinansaripracticalapp.database.AppDatabase
import com.example.shirinansaripracticalapp.database.dao.UserDao
import com.example.shirinansaripracticalapp.database.dao.UserLocationDao
import com.example.shirinansaripracticalapp.database.dao.UserNameDao
import com.example.shirinansaripracticalapp.database.dao.UserPictureDao
import com.example.shirinansaripracticalapp.database.entity.UserEntity
import com.example.shirinansaripracticalapp.database.entity.UserLocationEntity
import com.example.shirinansaripracticalapp.database.entity.UserNameEntity
import com.example.shirinansaripracticalapp.database.entity.UserPictureEntity
import com.example.shirinansaripracticalapp.databinding.FragmentUserBinding
import com.example.shirinansaripracticalapp.model.UserResponse
import com.example.shirinansaripracticalapp.util.AndroidLog
import com.example.shirinansaripracticalapp.util.CommonUtil
import com.example.shirinansaripracticalapp.util.NetworkUtil
import com.example.shirinansaripracticalapp.view.adapter.UserAdapter
import com.example.shirinansaripracticalapp.viewmodel.UserViewModel
import com.shirinansaripractical.view.fragment.base.BaseFragment

class UserFragment : BaseFragment<FragmentUserBinding>(), UserAdapter.OnViewClickListener {

    private var userList = ArrayList<UserResponse.UserItem?>()
    private var userAdapter: UserAdapter? = null

    private var db: AppDatabase? = null
    private var userDao: UserDao? = null
    private var userNameDao: UserNameDao? = null
    private var userLocationDao: UserLocationDao? = null
    private var userPictureDao: UserPictureDao? = null

    override fun getLayoutResId(): Int {
        return R.layout.fragment_user
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getInstance(requireActivity())
        userDao = db?.userDao
        userNameDao = db?.userNameDao
        userLocationDao = db?.userLocationDao
        userPictureDao = db?.userPictureDao

        dataBinding!!.userViewModel = UserViewModel()

        getDataFromDB()
        if (NetworkUtil.isNetworkConnected(requireActivity())) {
            if (userList.size == 0) {
                userList.add(null)
                setAdapter()
            }
            dataBinding!!.userViewModel!!.getUsers(requireActivity())
        }

        dataBinding!!.userViewModel!!.showMsg.observe(
            viewLifecycleOwner,
            { msg: String? ->
                if (msg != null) {
                    CommonUtil.showSnackBar(dataBinding!!.clUser, msg)
                }
            })
        dataBinding!!.userViewModel!!.userResponse.observe(viewLifecycleOwner, {
            AndroidLog.e("UserResponse", "" + it)

            userList.clear()
            userList.addAll(it.results)
            setAdapter()
            addDataInDB()
        })
    }

    private fun getDataFromDB() {//Getting data from database
        val userEntityList = userDao!!.getAll()
        val userNameEntityList = userNameDao!!.getAll()
        val userLocationEntityList = userLocationDao!!.getAll()
        val userPictureEntityList = userPictureDao!!.getAll()
        userEntityList.forEach {
            val uuid = it.id

            //name
            val tempUserName = userNameEntityList.find { it.userId == uuid }
            val userName = UserResponse.UserName(
                title = tempUserName!!.title,
                first = tempUserName.first,
                last = tempUserName.last
            )

            //location
            val tempLocationName = userLocationEntityList.find { it.userId == uuid }
            val userCoordinates = UserResponse.UserCoordinates(
                latitude = tempLocationName!!.latitude,
                longitude = tempLocationName.longitude
            )
            val userLocation = UserResponse.UserLocation(
                city = tempLocationName.city,
                state = tempLocationName.state,
                country = tempLocationName.country,
                postcode = tempLocationName.postcode,
                coordinates = userCoordinates
            )

            //picture
            val tempPictureName = userPictureEntityList.find { it.userId == uuid }
            val userPicture = UserResponse.UserPicture(
                large = tempPictureName!!.large,
                medium = tempPictureName.medium,
                thumbnail = tempPictureName.thumbnail
            )

            //Login
            val userLogin = UserResponse.UserLogin(uuid = uuid)

            val userItem =
                UserResponse.UserItem(
                    gender = it.gender,
                    name = userName,
                    location = userLocation,
                    email = it.email,
                    phone = it.phone,
                    login = userLogin,
                    picture = userPicture
                )
            userList.add(userItem)
        }

        setAdapter()
    }

    private fun addDataInDB() {//Saving data into database

        userDao!!.deleteAll()
        userNameDao!!.deleteAll()
        userLocationDao!!.deleteAll()
        userPictureDao!!.deleteAll()

        userList.forEachIndexed { _, userItem ->
            if (userItem != null) {
                val userEntity = UserEntity(
                    id = userItem.login!!.uuid!!,
                    gender = userItem.gender,
                    email = userItem.email,
                    phone = userItem.phone
                )
                userDao!!.insertAll(userEntity)

                //name
                if (userItem.name != null) {
                    val userNameEntity = UserNameEntity(
                        userItem.login!!.uuid!!,
                        userId = userItem.login!!.uuid!!,
                        title = userItem.name!!.title,
                        first = userItem.name!!.first,
                        last = userItem.name!!.last
                    )
                    userNameDao!!.insertAll(userNameEntity)
                }

                //location
                if (userItem.location != null) {
                    val userLocationEntity = UserLocationEntity(
                        userItem.login!!.uuid!!,
                        userId = userItem.login!!.uuid!!,
                        city = userItem.location!!.city,
                        state = userItem.location!!.state,
                        country = userItem.location!!.country,
                        latitude = userItem.location!!.coordinates!!.latitude,
                        longitude = userItem.location!!.coordinates!!.longitude,
                        postcode = userItem.location!!.postcode
                    )
                    userLocationDao!!.insertAll(userLocationEntity)
                }

                //picture
                if (userItem.picture != null) {
                    val userPictureEntity = UserPictureEntity(
                        userItem.login!!.uuid!!,
                        userId = userItem.login!!.uuid!!,
                        large = userItem.picture!!.large,
                        medium = userItem.picture!!.medium,
                        thumbnail = userItem.picture!!.thumbnail
                    )
                    userPictureDao!!.insertAll(userPictureEntity)
                }
            }
        }
    }

    private fun setAdapter() {
        userAdapter = UserAdapter(userList)
        dataBinding!!.rvUser.layoutManager = LinearLayoutManager(requireActivity())
        userAdapter!!.setOnViewClickListener(this)
        dataBinding!!.rvUser.adapter = userAdapter
    }

    override fun onViewClick(position: Int) {
        val bundle = Bundle()
        bundle.putParcelable(getString(R.string.user_detail), userList[position])
        Navigation.findNavController(dataBinding!!.clUser).navigate(
            R.id.action_userFragment_to_detailFragment, bundle
        )
    }
}