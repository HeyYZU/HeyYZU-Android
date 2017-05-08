package tw.bingluen.heyyzu.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tw.bingluen.heyyzu.R;
import tw.bingluen.heyyzu.model.layout.NavigationMenuItem;

public class NavigationMenuItemAdapter extends RecyclerView.Adapter<NavigationMenuItemAdapter.ViewHolder> {

    private NavigationMenuCallback mCallback;
    private List<NavigationMenuItem> menuItemList;

    public NavigationMenuItemAdapter(List<NavigationMenuItem> menuItems, NavigationMenuCallback callback) {
        this.menuItemList = menuItems;
        this.mCallback = callback;
    }

    @Override
    public NavigationMenuItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View item = LayoutInflater.from(context).inflate(R.layout.item_navigation_menu, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(NavigationMenuItemAdapter.ViewHolder holder, int position) {
        final int pos = position;
        final NavigationMenuItem menuItem = menuItemList.get(pos);

        try {
            holder.text.setText(menuItem.getTitleResId());
        } catch (Resources.NotFoundException e) {
            holder.text.setText(menuItem.getTitle());
        } catch (NullPointerException e) {
            holder.text.setText("");
        }

        holder.leftArrow.setVisibility(menuItem.isEnableLeftArrow() ? View.VISIBLE : View.INVISIBLE);
        holder.rightArrow.setVisibility(menuItem.isEnableRightArrow() ? View.VISIBLE : View.INVISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onItemClick(v, pos, menuItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public ImageView leftArrow;
        public ImageView rightArrow;

        public ViewHolder(View itemView) {
            super(itemView);

            text = (TextView) itemView.findViewById(R.id.txt_navigation_menu);
            leftArrow = (ImageView) itemView.findViewById(R.id.ic_arrow_left);
            rightArrow = (ImageView) itemView.findViewById(R.id.ic_arrow_right);
        }
    }

    public interface NavigationMenuCallback{
        void onItemClick(View v, int position, NavigationMenuItem item);
    }
}
