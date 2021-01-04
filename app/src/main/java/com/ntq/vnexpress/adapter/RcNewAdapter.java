package com.ntq.vnexpress.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ntq.vnexpress.R;
import com.ntq.vnexpress.callback.CallbackWebView;
import com.ntq.vnexpress.model.ExpressNew;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class RcNewAdapter extends RecyclerView.Adapter<RcNewAdapter.NewViewHolder> {

    private ArrayList<ExpressNew> expressNews;
    private Context context;
    private CallbackWebView callbackWebView;

    public RcNewAdapter(ArrayList<ExpressNew> expressNews, Context context, CallbackWebView callbackWebView) {
        this.expressNews = expressNews;
        this.context = context;
        this.callbackWebView = callbackWebView;
    }

    @NonNull
    @Override
    public NewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_new, parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewViewHolder holder, int position) {

        Document document = Jsoup.parse(expressNews.get(position).getDescription());
        String urlImage = document.getElementsByTag("img").attr("src");

        Glide.with(context).load(urlImage).centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(holder.ivNew);
        holder.bind(expressNews.get(position).getTitle(),
                document.wholeText(),
                expressNews.get(position).getPubDate());

        holder.itemView.setOnClickListener(v ->
                callbackWebView.showWeb(expressNews.get(position).getLink()));
    }

    @Override
    public int getItemCount() {
        return expressNews.size();
    }

    public class NewViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivNew;
        private TextView tvTitleNew;
        private TextView tvContentNew;
        private TextView tvPubDateNew;

        public NewViewHolder(@NonNull View itemView) {
            super(itemView);
            ivNew = itemView.findViewById(R.id.ivNew);
            tvTitleNew = itemView.findViewById(R.id.tvTitleNew);
            tvContentNew = itemView.findViewById(R.id.tvContentNew);
            tvPubDateNew = itemView.findViewById(R.id.tvPubDateNew);
        }

        public void bind(String title, String content, String date){
            tvTitleNew.setText(title);
            tvContentNew.setText(content);
            tvPubDateNew.setText(date);
        }

    }
}
