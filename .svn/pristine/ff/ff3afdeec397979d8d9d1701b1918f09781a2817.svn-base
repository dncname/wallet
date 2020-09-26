package com.zlw.base.ui.adapter;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by tcg on 2017/10/15.
 */
public abstract class TabPagerAdapter extends FragmentPagerAdapter {


    private String[] _titles;
    private boolean isCustomView;
//    private var mReMapValue = mutableMapOf(0 to 0)
//    private var mReMapKey   = mutableMapOf(0 to 0)

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public TabPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        _titles = titles;
    }

    public TabPagerAdapter(FragmentManager fm, String[] titles, boolean isCustomView) {
        super(fm);
        this.isCustomView = isCustomView;
        _titles = titles;
    }
    /**
     * 替换缓存名称
     */
//    fun addReplaceMap(fromId: Int, toId: Int) {
//        mReMapValue.put(fromId, toId)
//        mReMapKey.put(toId, fromId)
//        //LogUtils.i(TAG,"replace $fromId $toId" )
//    }
    /**
     * 还原缓存名称
     */
//    fun deleteReplaceMap(fromId: Int, toId: Int) {
//        mReMapValue.put(fromId, fromId)
//        mReMapKey.put(toId, toId)
//        //LogUtils.i(TAG,"replace $fromId $toId" )
//    }

    /**
     * 修改缓存
     */
//    override fun getItem(position: Int): Fragment {
//        if( position in mReMapKey.values) {
//            val pKey = mReMapKey[position]
//            pKey?.let{
//                return mFragments[it]
//            }
//        }
//        return mFragments[position]
//    }

    /**
     * 修改缓存
     */
//    override fun getItemId(position: Int): Long {
//        if( position in mReMapValue.keys) {
//            val value = mReMapValue[position]
//            return value!!.toLong()
//        }
//        return  position.toLong()
//    }


    @Override
    public long getItemId(int position) {
        return _titles[position].hashCode();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return isCustomView ? null : _titles[position];
    }

    @Override
    public int getCount() {
        return _titles.length;
    }
}

