package teqvirtual.deep.healthcare.SliderImage;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;
import teqvirtual.deep.healthcare.R;

public class MainSliderAdapter extends SliderAdapter {

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        switch (position) {
            case 0:
                viewHolder.bindImageSlide(R.drawable.banner1);
                break;
            case 1:
                viewHolder.bindImageSlide(R.drawable.banner2);
                break;
        }
    }
}
