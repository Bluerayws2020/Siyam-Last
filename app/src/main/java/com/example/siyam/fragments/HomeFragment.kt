package com.example.siyam.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.siyam.HomeActivity
import com.example.siyam.LoginActivity
import com.example.siyam.R
import com.example.siyam.RequestLoginActivity
import com.example.siyam.activities.ProductsActivity
import com.example.siyam.activities.RadiatorsActivity
import com.example.siyam.activities.SearchResultActivity
import com.example.siyam.activities.ViewProfile
import com.example.siyam.adapters.HomeItemsAdapter
import com.example.siyam.adapters.OnClickListener
import com.example.siyam.databinding.FragmentHomeBinding
import com.example.siyam.helpers.HelperUtils
import com.example.siyam.helpers.ProgressDialogFragment
import com.example.siyam.helpers.ViewUtils.hide
import com.example.siyam.helpers.ViewUtils.show
import com.example.siyam.model.GetCategoryList
import com.example.siyam.model.NetworkResults
import com.example.siyam.model.ViewUserProfile
import com.example.siyam.viewModel.AppViewModel
import com.google.android.material.navigation.NavigationView
import retrofit2.HttpException

class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var homeItemsAdapter : HomeItemsAdapter?= null
    private val dataList : ArrayList<String>  = arrayListOf()
    private lateinit var categoryList : ArrayList<GetCategoryList>
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val appVM by viewModels<AppViewModel>()
companion object{
    var partNumber = ""
}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        (activity as HomeActivity).showProgress()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)



        getAllCategoryApi()
        appVM.getAllCategory()



        binding.includeTab.tabTitle.text = resources.getString(R.string.home)


HelperUtils.setDefaultLanguage(requireContext(),"en")

        binding.includeTab.menuImg.setOnClickListener{
            (requireActivity() as HomeActivity).openDrawer()
        }

        binding.includeTab.personImg.setOnClickListener{
//            showMessage(HelperUtils.getUID(context).toString())
            if (HelperUtils.getUID(context) == "0"){
                startActivity(Intent(context, LoginActivity::class.java))

            }else {
                startActivity(Intent(context, ViewProfile::class.java))

            }
        }


        getProductByPartNumberApi()

            binding.searchBtn.setOnClickListener {



if (HelperUtils.getUID(context) == "0"){
    showMessage("you have to register or login if you have an account!")
    startActivity(Intent(context, LoginActivity::class.java))
}else{


    if (binding.partNumber.text.length == 0){




        showMessage("You Have to Write Part Number TO Search")



    }else {


//showProgress()


    appVM.getProductByPartNum(
        binding.partNumber.text.toString()
    )

    partNumber = binding.partNumber.text.toString()
    }
}


            }



        Log.d("User Id = ", "onViewCreated: " + HelperUtils.getUID(context) )




    }



    private fun getProductByPartNumberApi(){

        appVM.getProductByPartNumResponse().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
hideProgress()
                    if (result.data.msg.status == 200) {

                        binding.pdSearch.hide()
                        binding.searchBtn.show()
                        val intent = Intent(requireContext(), SearchResultActivity::class.java)

                        startActivity(intent)
                        Toast.makeText(requireContext(), result.data.msg.msg, Toast.LENGTH_SHORT).show()


                    } else {
                        Toast.makeText(requireContext(), result.data.msg.msg, Toast.LENGTH_SHORT).show()
                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    if (result.exception is HttpException)
                        showMessage(result.exception.message())
                    else
                        showMessage("No Internet connection")
                }
            }
        }
    }


    private fun getAllCategoryApi(){

        appVM.getAllCategoryResponse().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
//                    hideProgress()
                    (activity as HomeActivity).hideProgress()
//                    (activity as HomeActivity).showProgress()
                    if (result.data.msg.status == 200) {

                        categoryList = result.data.data

//        Home Item Adapter
                        val lm = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                        homeItemsAdapter = HomeItemsAdapter(categoryList, requireContext(), object :OnClickListener{
                            override fun onMove(position: Int) {

                                val intent = Intent(requireContext(), ProductsActivity::class.java)
                                intent.putExtra("category_id", result.data.data[position].id)
                                intent.putExtra("cat_name",result.data.data[position].name)
                                startActivity(intent)

                            }

                        })
                        binding.rvHome.layoutManager = lm
                        binding.rvHome.adapter = homeItemsAdapter




                    } else {
                        Toast.makeText(requireContext(), result.data.msg.msg, Toast.LENGTH_SHORT).show()
                    }
                }
                is NetworkResults.Error -> {
                    (activity as HomeActivity).hideProgress()
                    result.exception.printStackTrace()
                    if (result.exception is HttpException)
                        showMessage(result.exception.message())
                    else
                        showMessage("No Internet connection")
                }
            }
        }
    }



    private fun showMessage(message: String?) =
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    private fun showProgress() {
        ProgressDialogFragment().show(childFragmentManager, "progress_dialog")
    }

    private fun hideProgress() {
        childFragmentManager.findFragmentByTag("progress_dialog")?.let {
            if (it.isAdded)
                (it as ProgressDialogFragment).dismiss()
        }
    }



}