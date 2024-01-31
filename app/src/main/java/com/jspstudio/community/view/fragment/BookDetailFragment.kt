package com.jspstudio.community.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.jspstudio.community.R
import com.jspstudio.community.databinding.FragmentBookDetailBinding

class BookDetailFragment: Fragment() {

    private lateinit var binding: FragmentBookDetailBinding
    private var bookTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // bundle에서 bookTitle 값 가져오기
        bookTitle = arguments?.getString("bookTitle")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_detail, container, false)

        binding.bookTextView.text = bookTitle

        return binding.root
    }

}