package com.jspstudio.community.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jspstudio.community.model.ImageData;
import com.jspstudio.community.util.LogMgr;
import com.jspstudio.community.util.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

public class PhotoViewPagerAdapter extends PagerAdapter {
    private String TAG = PhotoViewPagerAdapter.class.getSimpleName();

    private List<ImageData> mImageList = new ArrayList<>();
    private TextView mTvPage;
    boolean isFileData = false;
    public PhotoViewPagerAdapter(List<ImageData> mImageList) {
        this.mImageList = mImageList;
        isFileData = true;
    }

    // pager의 전체 페이지 수 설정
    @Override
    public int getCount() {
        if(isFileData) {
            if (mImageList == null) return 0;
            return mImageList.size();
        }else{
            return 0;
        }
    }

    public View instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());

        int paddingInPx = 2;
        //photoView.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx);

        Glide.with(container)
                .load(mImageList.get(position).getUri())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//                    .transition(DrawableTransitionOptions.with(new DrawableAlwaysCrossFadeFactory()))
                .into(photoView);
        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT); // 사이즈는 MATCH_PARENT 크기로 add

        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) { // ViewPager에서 페이지를 제거할 때 호출되는 메소드
        // 뷰페이저에서 삭제
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
