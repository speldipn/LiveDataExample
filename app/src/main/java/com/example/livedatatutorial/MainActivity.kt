package com.example.livedatatutorial

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.livedatatutorial.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private lateinit var sourceMerger: MediatorLiveData<Boolean>
    private val id = MutableLiveData("")
    private val password = MutableLiveData("")
    var buttonEnabled = MutableLiveData(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
        setObserver()
    }

    private fun setup() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this
        binding.idEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                id.postValue(s?.toString() ?: "")
            }
        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                password.postValue(s?.toString() ?: "")
            }
        })

        binding.lifecycleOwner = this
    }

    private fun setObserver() {
        sourceMerger = MediatorLiveData<Boolean>().apply {
            addSource(id)       { value = isEnableButton() }
            addSource(password) { value = isEnableButton() }
        }
        sourceMerger.observe(this, Observer {
            buttonEnabled.postValue(isEnableButton())
        })
    }

    private fun isEnableButton() = !TextUtils.isEmpty(id.value) && !TextUtils.isEmpty(password.value)

    companion object {
        const val TAG = "speldipn"
    }
}