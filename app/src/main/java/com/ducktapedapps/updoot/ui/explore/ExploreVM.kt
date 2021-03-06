package com.ducktapedapps.updoot.ui.explore

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ducktapedapps.updoot.model.Subreddit
import kotlinx.coroutines.launch

class ExploreVM(private val exploreRepo: ExploreRepo) : ViewModel() {

    val isLoading = exploreRepo.isLoading
    val result = exploreRepo.results
    val trendingSubs: LiveData<List<Subreddit>> = exploreRepo.trendingSubs

    init {
        viewModelScope.launch {
            exploreRepo.loadTrendingSubs()
        }
    }
    fun searchSubreddit(query: String) {
        viewModelScope.launch {
            exploreRepo.searchSubreddit(query)
            exploreRepo.loadTrendingSubs()
        }
    }

    override fun onCleared() {
        Log.i("ExploreVM", "onCleared")
        super.onCleared()
    }
}