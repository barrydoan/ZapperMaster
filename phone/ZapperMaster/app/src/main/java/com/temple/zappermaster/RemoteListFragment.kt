package com.temple.zappermaster

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.temple.zappermaster.database.AppDatabase

class RemoteListFragment : Fragment() {
    private lateinit var remoteViewModel: RemoteViewModel
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // init view model
        remoteViewModel = ViewModelProvider(requireActivity())[RemoteViewModel::class.java]
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
                remoteList = RemoteList(true)
                remoteViewModel.setRemoteList(remoteList)
            }
            Log.d("AAA","RemoteListFrag-$remoteList")
            // click event
            val clickEven= {remote:RemoteObj -> Unit
                remoteViewModel.setSelectedRemote(remote)
                Log.d("AAA","set selected remote- $remote")
                (requireActivity() as SelectionFragmentInterface).remoteSelected()
                // notify the main that a book is selected
            }

            val shareEven= {remote:RemoteObj -> Unit
                (requireActivity() as DbInterface).shareRemote(remote)
                // update status of remote in database
                remote.shared = true
                (requireActivity() as DbInterface).updateRemote(remote)
            }
            val deleteEven = { remote :RemoteObj-> Unit
                Log.d("AAA","delete clicked")
                if (remoteList.getLocalFlag()){
                    (requireActivity() as DbInterface).deleteRemote(remote)

                }
            }
            var downloadEvent = { remote:RemoteObj-> Unit
                (requireActivity() as DbInterface).saveRemote(remote)
            }
            var checkExistedInLocalDB = { modelNumber: String -> Boolean
                (requireActivity() as DbInterface).checkExistedOnLocal(modelNumber)
            }
            layoutManager = LinearLayoutManager(requireContext())
            adapter = RemoteAdapter(remoteList, clickEven, shareEven, deleteEven, downloadEvent, checkExistedInLocalDB)
            // add divider line
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            remoteViewModel.getRemoteList().observe(requireActivity()) {
                // notify the dataset is changed
                Log.d("AAA", "Remote list change")
                this.adapter?.notifyDataSetChanged()
            }
        }
    }

    class RemoteAdapter(_remoteList: RemoteList, _clickEvent: (remote:RemoteObj)->Unit, _shareEvent: (remote:RemoteObj)->Unit, _delEvent:(remote:RemoteObj) -> Unit, _downloadEvent: (remote: RemoteObj) -> Unit, _checkExistedInLocalDb: (modelNumber: String)->Boolean): RecyclerView.Adapter<RemoteAdapter.RemoteViewHolder>() {
        val remoteList = _remoteList
        val clickEven = _clickEvent
        val shareEven = _shareEvent
        val delEven = _delEvent
        var downloadEvent = _downloadEvent
        var checkExistedInLocalDb = _checkExistedInLocalDb

        inner class RemoteViewHolder(_view: View) : RecyclerView.ViewHolder(_view) {
            val titleTxt: TextView = _view.findViewById(R.id.textView)
            val typeTxt: TextView = _view.findViewById(R.id.type)
            val brandTxt: TextView = _view.findViewById(R.id.brand)
            val btnDelete: ImageButton = _view.findViewById(R.id.btnDelete)
            var btnShare: ImageButton = _view.findViewById(R.id.btnShare)
            var btnDownload: ImageButton = _view.findViewById(R.id.btnDownload)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemoteViewHolder {
            return RemoteViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.remote_list_layout, parent, false)
            )
        }

        override fun onBindViewHolder(holder: RemoteViewHolder, position: Int) {
            val remote = remoteList[position]
            holder.titleTxt.text = remote.model_number
            holder.typeTxt.text = remote.type
            holder.brandTxt.text = remote.manufacture

            holder.btnDelete.visibility = View.INVISIBLE
            holder.btnShare.visibility = View.INVISIBLE
            holder.btnDownload.visibility = View.INVISIBLE
            if (remoteList.getLocalFlag()) {
                holder.btnDelete.visibility = View.VISIBLE
                if (!remote.shared) {
                    holder.btnShare.visibility = View.VISIBLE
                }

            }
            else {
                if (!checkExistedInLocalDb(remote.model_number)) {
                    holder.btnDownload.visibility = View.VISIBLE
                }
            }

            holder.itemView.setOnClickListener{clickEven(remote)}
            holder.btnShare.setOnClickListener {shareEven(remote)}
            holder.btnDelete.setOnClickListener{delEven(remote)}
            holder.btnDownload.setOnClickListener{downloadEvent(remote)}
        }

        override fun getItemCount(): Int {
            return remoteList.size()
        }
    }

    interface SelectionFragmentInterface {
        fun remoteSelected()
    }

}