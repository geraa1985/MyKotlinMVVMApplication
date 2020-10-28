package com.example.mykotlinmvvmapplication.presentation.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.mykotlinmvvmapplication.R
import com.example.mykotlinmvvmapplication.presentation.viewmodels.LogoutViewModel
import com.firebase.ui.auth.AuthUI

import kotlinx.android.synthetic.main.dialog_logout.*

class LogoutDialogFragment : DialogFragment(), View.OnClickListener {

    private val viewModel by lazy { ViewModelProvider(this).get(LogoutViewModel::class.java) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_logout, container)
    }

    override fun onStart() {
        super.onStart()
        logout_button_yes.setOnClickListener(this)
        logout_button_no.setOnClickListener(this)

        viewModel.apply {
            getClickOnYesLiveData().observe(this@LogoutDialogFragment) {
                logout()
            }
            getClickOnNoLiveData().observe(this@LogoutDialogFragment){
                dismiss()
            }
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            logout_button_no.id -> viewModel.clickOnNo()
            logout_button_yes.id -> viewModel.clickOnYes()
        }
    }

    private fun logout() {
        AuthUI.getInstance()
                .signOut(requireActivity())
                .addOnCompleteListener {
                    SplashActivity.start(requireContext())
                    requireActivity().finish()
                }
    }
}