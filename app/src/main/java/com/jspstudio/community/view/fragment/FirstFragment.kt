package com.jspstudio.community.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jspstudio.community.R
import com.jspstudio.community.databinding.FragmentFirstBinding

class FirstFragment: Fragment() {

    private lateinit var binding: FragmentFirstBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_first, container, false)

        // data binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()

    }

    private fun setRecyclerView() {
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

    }
}