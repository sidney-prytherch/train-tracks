package com.sid.app.traintracks.ui.puzzle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PuzzleViewModel : ViewModel() {

    fun initialize(answers: Array<Int>, currentInput: Array<Int>, topHints: Array<Int>, leftHints: Array<Int>) {
        _storedValues.value = currentInput
        _answers.value = answers
        _initialized.value = true
        _topHints.value = topHints
        _leftHints.value = leftHints
    }

    private val _topHints = MutableLiveData<Array<Int>>().apply {
        value = Array(8) {0}
    }
    val topHints: LiveData<Array<Int>> = _topHints

    private val _leftHints = MutableLiveData<Array<Int>>().apply {
        value = Array(8) {0}
    }
    val leftHints: LiveData<Array<Int>> = _leftHints

    private val _storedValues = MutableLiveData<Array<Int>>().apply {
        value = Array(100) {0}
    }
    val storedValues: LiveData<Array<Int>> = _storedValues

    private val _answers = MutableLiveData<Array<Int>>().apply {
        value = Array(100) {0}
    }
    val answers: LiveData<Array<Int>> = _answers

    private val _initialized = MutableLiveData<Boolean>().apply {
        value = false
    }
    val initialized: LiveData<Boolean> = _initialized

    private val _text = MutableLiveData<String>().apply {
        value = ""
    }
    val text: LiveData<String> = _text
}