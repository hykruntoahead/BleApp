package com.example.heyukun.bleapp;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clj.fastble.data.ScanResult;

import java.util.List;

/**
 * Created by heyukun on 2017/8/28.
 */

public class ScanAdapter extends BaseAdapter {
    private List<ScanResult> mScanResults;
    private Context mCtx;
    private ViewHolder viewHolder;

    public ScanAdapter(Context mCtx, List<ScanResult> mScanResults){
        this.mCtx = mCtx;
        this.mScanResults = mScanResults;
    }

    @Override
    public int getCount() {
        return mScanResults==null ? 0 : mScanResults.size();
    }

    @Override
    public Object getItem(int position) {
        return mScanResults == null ? null : mScanResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.item_scan,parent,false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.mac = (TextView) convertView.findViewById(R.id.tv_mac);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(TextUtils.isEmpty(mScanResults.get(position).getDevice().getName()) ? "未知设备"
                : mScanResults.get(position).getDevice().getName());
        viewHolder.mac.setText(mScanResults.get(position).getDevice().getAddress());
        return convertView;
    }

    class ViewHolder{
        TextView name,mac;
    }

}
