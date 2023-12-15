package com.example.cryptoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.cryptoapp.repo.Repository
import com.example.cryptoapp.utils.NetworkUtils
import com.example.cryptoapp.viewmodel.MainViewModel
import com.example.cryptoapp.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    /** ViewModel to fetch the API result. */
    private lateinit var viewModel: MainViewModel
    private var cryptoList: MutableList<String> = ArrayList()

    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: CryptoListAdapter
    private lateinit var progressBarLayout: RelativeLayout
    private lateinit var searchView: SearchView

    /** The SwipeRefreshLayout which will be used to fetch the latest data from API. */
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val queryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            mAdapter.filter.filter(newText)
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!NetworkUtils.isInternetConnected(this)) {
            generateToast("Check your internet connection")
            finish()
        }
        setupViews()
        makeProgressBarVisible()
        setupViewModel()
        setupClickListeners()
    }

    private fun setupViews() {
        recyclerView = findViewById(R.id.recyclerView)
        progressBarLayout = findViewById(R.id.progress_layout)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        searchView = findViewById(R.id.searchView)
    }

    /** This method will set up the [viewModel] and adds an observer for planet and vehicle data. */
    private fun setupViewModel() {
        val repo = Repository()
        val viewModelFactory = MainViewModelFactory(repo)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        viewModel.cryptoLiveData.observe(this) { crypto ->
            if (crypto != null) {
                crypto.forEach { data ->
                    cryptoList.add(data.name)
                }
                mAdapter= CryptoListAdapter(cryptoList) { item, position ->
                    showDiaglogBox(item, crypto[position].symbol, crypto[position].type)
                }
                recyclerView.adapter = mAdapter
                makeProgressBarGone()
                swipeRefreshLayout.isRefreshing = false
                searchView.isFocusable = true
                searchView.isFocusableInTouchMode = true
                searchView.setQuery("", false)
                searchView.setOnQueryTextListener(queryTextListener)
            } else {
                generateToast("Error in fetching data, refetch data!!")
                makeProgressBarGone()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun setupClickListeners() {
        swipeRefreshLayout.setOnRefreshListener {
            if (!NetworkUtils.isInternetConnected(this)) {
                generateToast("Check your internet connection")
                swipeRefreshLayout.isRefreshing = false
            } else {
                cryptoList.clear()
                searchView.setQuery("", false)
                searchView.isIconified = true
                searchView.clearFocus()
                searchView.isFocusable = false
                searchView.isFocusableInTouchMode = false
                searchView.setOnQueryTextListener(null)
                viewModel.refetchCryptos()
            }
        }
    }

    private fun showDiaglogBox(name: String, symbol: String, type: String) {
        val alert = AlertDialog.Builder(this@MainActivity)
        val mView = layoutInflater.inflate(R.layout.crypto_details_layout, null)
        mView.findViewById<TextView>(R.id.name).text = "Crypto name: " + name
        mView.findViewById<TextView>(R.id.symbol).text = "Crypto Symbol: " + symbol
        mView.findViewById<TextView>(R.id.type).text = "Crypto type: " + type
        alert.setView(mView)

        val alertDialog = alert.create()
        alertDialog.setCanceledOnTouchOutside(true)
        alertDialog.show()
    }

    private fun makeProgressBarVisible() {
        progressBarLayout.visibility = View.VISIBLE
    }

    private fun makeProgressBarGone() {
        progressBarLayout.visibility = View.GONE
    }

    /** This method will show the toast. */
    private fun generateToast(msg: String) {
        Toast.makeText(
            this,
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }
}