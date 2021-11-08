package com.rocketinsights.android.ui

import android.Manifest
import android.database.Cursor
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rocketinsights.android.R
import com.rocketinsights.android.databinding.FragmentContactsBinding
import com.rocketinsights.android.extensions.hide
import com.rocketinsights.android.extensions.observeEvent
import com.rocketinsights.android.extensions.setupActionBar
import com.rocketinsights.android.extensions.showToast
import com.rocketinsights.android.extensions.viewBinding
import com.rocketinsights.android.models.Contact
import com.rocketinsights.android.ui.ContactDetailBottomSheetFragment.Companion.CONTACT_DETAIL_BOTTOM_SHEET_FRAGMENT_TAG
import com.rocketinsights.android.ui.adapters.ContactsAdapter
import com.rocketinsights.android.ui.common.BaseFragment
import com.rocketinsights.android.viewmodels.ContactsViewModel
import com.rocketinsights.android.viewmodels.PermissionsResult
import com.rocketinsights.android.viewmodels.PermissionsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContactsFragment : BaseFragment(R.layout.fragment_contacts), LoaderManager.LoaderCallbacks<Cursor> {

    private val binding by viewBinding(FragmentContactsBinding::bind)
    private val viewModel by viewModels<ContactsViewModel>()
    private val permissionsViewModel: PermissionsViewModel by viewModel()
    private val contactsAdapter = ContactsAdapter(::onContactClicked)

    override fun doOnViewCreated(view: View, savedInstanceState: Bundle?) {
        setupActionBar(binding.toolbar)
        requestContactsPermission()
        initContactList()
        initObservers()
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            requireContext(),
            viewModel.getContentUri(),
            viewModel.getProjection(),
            viewModel.getSelection(),
            null,
            viewModel.getSortOrder()
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        viewModel.onLoadFinished(loader, data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        viewModel.onLoaderReset()
    }

    private fun requestContactsPermission() {
        permissionsViewModel.requestPermissions(this, Manifest.permission.READ_CONTACTS)
    }

    private fun initContactList() {
        binding.recyclerViewContacts.apply {
            adapter = contactsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(requireContext(), (layoutManager as LinearLayoutManager).orientation))
        }
    }

    private fun initObservers() {
        viewModel.contactsLiveData.observe(viewLifecycleOwner) { contacts ->
            binding.progressBarContacts.hide()
            contactsAdapter.submitList(contacts)
        }

        permissionsViewModel.permissionsResult.observeEvent(viewLifecycleOwner) { permissionResult ->
            when (permissionResult) {
                is PermissionsResult.PermissionsError -> onReadContactsPermissionDenied()
                PermissionsResult.PermissionsGranted -> onReadContactsPermissionGranted()
            }
        }
    }

    private fun onContactClicked(contact: Contact) {
        ContactDetailBottomSheetFragment
            .newInstance(contact)
            .show(parentFragmentManager, CONTACT_DETAIL_BOTTOM_SHEET_FRAGMENT_TAG)
    }

    private fun onReadContactsPermissionGranted() {
        LoaderManager.getInstance(this).initLoader(viewModel.getContactsQueryId(), null, this)
    }

    private fun onReadContactsPermissionDenied() {
        requireContext().showToast(getString(R.string.contacts_permission_not_granted))
    }
}
