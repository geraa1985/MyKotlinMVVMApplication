package com.example.mykotlinmvvmapplication.presentation.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.mykotlinmvvmapplication.R
import com.firebase.ui.auth.AuthUI

import kotlinx.android.synthetic.main.dialog_logout.*

class LogoutDialogFragment : DialogFragment(), View.OnClickListener {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.setTitle(R.string.logout_title)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_logout, container)
    }

    override fun onStart() {
        super.onStart()
        logout_button_yes.setOnClickListener(this)
        logout_button_no.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id){
            logout_button_no.id -> dismiss()
            logout_button_yes.id -> logout()
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