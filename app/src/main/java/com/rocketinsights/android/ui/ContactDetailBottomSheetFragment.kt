package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.rocketinsights.android.databinding.BottomSheetFragmentContactDetailBinding
import com.rocketinsights.android.models.Contact
import com.rocketinsights.android.ui.base.BaseBottomSheetFragment

class ContactDetailBottomSheetFragment : BaseBottomSheetFragment<BottomSheetFragmentContactDetailBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getContactArgument()?.let { contact -> showContactDetail(contact) }
    }

    override fun getViewBinding(): BottomSheetFragmentContactDetailBinding =
        BottomSheetFragmentContactDetailBinding.inflate(layoutInflater)

    private fun getContactArgument(): Contact? = arguments?.getParcelable(CONTACT_DETAIL_ARGUMENT)

    private fun showContactDetail(contact: Contact) = with(contact) {
        binding.textContactDetailName.text = name
        binding.textContactDetailPhone.text = phoneNumber
    }

    companion object {
        fun newInstance(contact: Contact): ContactDetailBottomSheetFragment = ContactDetailBottomSheetFragment().apply {
            arguments = bundleOf(Pair(CONTACT_DETAIL_ARGUMENT, contact))
        }

        const val CONTACT_DETAIL_BOTTOM_SHEET_FRAGMENT_TAG = "contact_detail_bottom_sheet_fragment_tag"
        private const val CONTACT_DETAIL_ARGUMENT = "contact_detail_argument"
    }
}
