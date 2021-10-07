package com.rocketinsights.android.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TheGreatestRecyclerViewViewModel : ViewModel() {

    private var id = 0

    private val _list = MutableLiveData<List<ItemModel>>()
    val list: LiveData<List<ItemModel>> = _list

    private val randomStrings = listOf("some", "random", "strings", "to", "be", "unique")

    init {
        _list.value = listOf(getRandomItem(), getRandomItem(), getRandomItem())
    }

    private fun getRandomItem(): ItemModel {
        val shuffledList = ArrayList(randomStrings)
        shuffledList.shuffle()
        val item = ItemModel(
            text = shuffledList.joinToString(" "),
            id = id
        )
        id++
        return item
    }

    fun addItem() {
        _list.value?.let {
            val item = getRandomItem()
            val list = ArrayList(it)
            list.add((0..list.size).random(), item)
            _list.value = list
        }
    }

    fun subtractItem() {
        _list.value?.let {
            val list = ArrayList(it)
            list.remove(list.random())
            _list.value = list
        }
    }

    fun removeItem(position: Int) {
        _list.value?.let {
            val list = ArrayList(it)
            list.removeAt(position)
            _list.value = list
        }
    }

    fun shuffleItems() {
        _list.value?.let {
            val list = ArrayList(it)
            list.shuffle()
            _list.value = list
        }
    }

    data class ItemModel(
        val id: Int,
        val text: String
    )
}
