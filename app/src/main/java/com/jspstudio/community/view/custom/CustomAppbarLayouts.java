package com.jspstudio.community.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.appbar.AppBarLayout;
import com.jspstudio.community.databinding.LayoutCustomAppbarBinding;

public class CustomAppbarLayouts extends AppBarLayout {

    private Context _context;
    private LayoutCustomAppbarBinding binding;

    public CustomAppbarLayouts(@NonNull Context context) {
        super(context);
        _context = context;
        initView();
    }

    public CustomAppbarLayouts(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        _context = context;
        initView();
    }

    public CustomAppbarLayouts(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _context = context;
        initView();
    }
    private void initView() {
        binding = LayoutCustomAppbarBinding.inflate(LayoutInflater.from(_context), this, true);
    }

    public void setOnBackClick(OnClickListener listener) {
        binding.imgBack.setOnClickListener(listener);
    }

    public void setTitle(String title) {

    }

}
