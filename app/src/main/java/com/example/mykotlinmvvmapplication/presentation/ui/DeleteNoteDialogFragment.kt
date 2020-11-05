package com.example.mykotlinmvvmapplication.presentation.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mykotlinmvvmapplication.R
import com.example.mykotlinmvvmapplication.presentation.viewmodels.DeleteNoteViewModel
import kotlinx.android.synthetic.main.dialog_delete.*
import org.koin.android.viewmodel.ext.android.viewModel

class DeleteNoteDialogFragment : DialogFragment(), View.OnClickListener {

    private val confirmToDeleteLiveData = MutableLiveData<Boolean>()

    private val viewModel: DeleteNoteViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_delete, container)
    }

    override fun onStart() {
        super.onStart()
        delete_button_yes.setOnClickListener(this)
        delete_button_no.setOnClickListener(this)

        viewModel.apply {
            getClickOnYesLiveData().observe(this@DeleteNoteDialogFragment) {
                confirmToDeleteLiveData.value = true
            }
            getClickOnNoLiveData().observe(this@DeleteNoteDialogFragment) {
                dismiss()
            }
        }
    }

    fun getConfirmToDeleteLiveData(): LiveData<Boolean> = confirmToDeleteLiveData

    override fun onClick(v: View?) {
        when (v?.id) {
            delete_button_no.id -> viewModel.clickOnNo()
            delete_button_yes.id -> viewModel.clickOnYes()
        }
    }

}