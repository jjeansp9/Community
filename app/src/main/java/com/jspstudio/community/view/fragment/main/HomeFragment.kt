package com.jspstudio.community.view.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.jspstudio.community.databinding.FragmentHomeBinding
import com.google.firebase.firestore.CollectionReference
import com.jspstudio.community.base.BaseFragment
import com.jspstudio.community.viewmodel.MainViewModel

class HomeFragment: BaseFragment<FragmentHomeBinding>("HomeFragment") {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.vmMain = ViewModelProvider(mContext)[MainViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val hom : CollectionReference
        binding.vmMain?.setTitle("home")
    }

//    private fun setRecyclerView() {
//        binding.bookRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        binding.bookRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
//        val adapter = BookAdapter(requireContext(), resources.getStringArray(R.array.book_list))
//
//        // BookAdapter 인터페이스로 전달받은 아이템의 BookTitle을 번들로 저장하여 BookDetailFragment로 이동
//        adapter.setOnItemClickListener(object : BookAdapter.OnItemClickListener {
//            override fun onItemClick(book: String) {
//                val bundle = bundleOf("bookTitle" to book)
//                findNavController().navigate(R.id.action_first_to_detail, bundle)
//            }
//        })
//        binding.bookRecyclerView.adapter = adapter
//
//    }
}