package com.mitchelldevries.mywishlist;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Mitchell de Vries.
 */
public class WishAdapter extends RecyclerView.Adapter<WishAdapter.WishViewHolder> {

    private List<Wish> wishes;
    private Context context;

    public WishAdapter(Context context) {
        this.context = context;
    }

    public void setWishes(List<Wish> wishes) {
        this.wishes = wishes;
        notifyDataSetChanged();
    }

    @Override
    public WishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_wish, parent, false);
        return new WishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WishViewHolder holder, int position) {
        holder.bindWish(wishes.get(position), position);
    }

    @Override
    public int getItemCount() {
        return wishes.size();
    }

    public class WishViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.relative_layout_list_item)
        RelativeLayout layout;
        @BindView(R.id.list_item_image)
        ImageView wishImage;
        @BindView(R.id.list_item_title)
        TextView wishTitle;
        @BindView(R.id.list_item_target)
        TextView wishTarget;
        @BindView(R.id.list_item_current)
        TextView wishCurrent;
        @BindView(R.id.progress)
        ProgressBar progressBar;

        private Wish wish;

        public WishViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindWish(Wish wish, int position) {
            this.wish = wish;

            int[] colors = context.getResources().getIntArray(R.array.colors);
            int color = position % colors.length;
            int backgroundColor = colors[color];
            layout.setBackgroundColor(backgroundColor);

            wishTitle.setText(wish.getTitle());
            wishImage.setImageResource(wish.getImage());
            wishTarget.setText(String.format("€%s", String.valueOf(wish.getTarget())));
            wishCurrent.setText(String.format("€%s", String.valueOf(wish.getCurrent())));
            double progress = (wish.getCurrent() / wish.getTarget()) * 100;
            Log.i("TAG", "bindWish: " + progress);
            progressBar.setProgress((int) progress);
        }

        @OnClick(R.id.list_item_deposit)
        void deposit() {
            DialogFragment newFragment = DepositDialog.newInstance(wish.getId());
            newFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "deposit");
        }

        @OnClick(R.id.list_item_withdraw)
        void withdraw() {
            DialogFragment newFragment = WithdrawDialog.newInstance(wish.getId());
            newFragment.setTargetFragment(WishListFragment.newInstance(), 1);
            newFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "withdraw");
        }
    }
}
