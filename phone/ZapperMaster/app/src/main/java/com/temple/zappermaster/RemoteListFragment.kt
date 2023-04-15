package com.temple.zappermaster

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RemoteListFragment : Fragment() {
    private lateinit var remoteViewModel: RemoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // init view model
        remoteViewModel = ViewModelProvider(requireActivity()).get(RemoteViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_remotelist, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view as RecyclerView) {
            // empty list for first load
            var remoteList: RemoteList
            if (remoteViewModel.getRemoteList().value != null) {
                Log.d("AAA", "Remote list from live data")
                remoteList = remoteViewModel.getRemoteList().value!!
            }
            else {
                Log.d("AAA", "New remote list")
                remoteList = RemoteList()
                remoteViewModel.setRemoteList(remoteList)
            }
            // click event
            val clickEven: (RemoteObj) -> Unit = {remote:RemoteObj ->
                remoteViewModel.setSelectedRemote(remote)
                Log.d("AAA","set selected remote- $remote")
                (requireActivity() as SelectionFragmentInterface).remoteSelected()
                // notify the main that a book is selected
            }
            layoutManager = LinearLayoutManager(requireContext())
            adapter = RemoteAdapter(remoteList, clickEven)
            // add divider line
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            remoteViewModel.getRemoteList().observe(requireActivity()) {
                // notify the dataset is changed
                Log.d("AAA", "Remote list change")
                this.adapter?.notifyDataSetChanged()
            }

        }


    }

    class RemoteAdapter(_remoteList: RemoteList, _clickEvent: (remote:RemoteObj)->Unit): RecyclerView.Adapter<RemoteAdapter.RemoteViewHolder>() {
        val remoteList = _remoteList
        val clickEven = _clickEvent

        inner class RemoteViewHolder(_view: View) : RecyclerView.ViewHolder(_view) {
            val titleTxt: TextView = _view.findViewById(R.id.textView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemoteViewHolder {
            return RemoteViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.remote_list_layout, parent, false)
            )
        }

        override fun onBindViewHolder(holder: RemoteViewHolder, position: Int) {
            val remote = remoteList.get(position)
            // update information with each book
            holder.titleTxt.text = remote.model_number
            // create even listener for book
            holder.itemView.setOnClickListener{clickEven(remote)}
        }

        override fun getItemCount(): Int {
            return remoteList.size()
        }
    }

    interface SelectionFragmentInterface {
        fun remoteSelected()
    }

}