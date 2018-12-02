package com.hangr.hangr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

public class ViewAllItems extends AppCompatActivity {
    /**
     * NOTE: The vast majority of this code is referenced from the following
     * Reference: https://www.androidbegin.com/tutorial/android-gridview-zoom-images-animation-tutorial/
     *
     * Creates activity that displays pictures of all items taken by the user
     * When an item is clicked, a larger "zoomed" version of the image is displayed
     */

    // Holds bitmaps of all items
    public ArrayList<Bitmap> mBitmapImages = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_all_items);
        getSupportActionBar().setTitle("Your Wardrobe");

        // Get the folder where all images have been saved to
        File folder = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File[] imageFiles = folder.listFiles();

        // Fill arraylist with bitmaps of all the images the user has taken
        for (File image : imageFiles) {
            String filePath = image.getPath();

            Bitmap imageBitmap = BitmapFactory.decodeFile(filePath);

            mBitmapImages.add(imageBitmap);
        }

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
    }

    public class ImageAdapter extends BaseAdapter {

        private Animator mCurrentAnimator;

        private int mShortAnimationDuration;

        private Context mContext;
        LayoutInflater inflater;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mBitmapImages.size();
        }

        public Object getItem(int position) {
            return mBitmapImages.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            final ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }

            // Putting the bitmaps into the imageviews
            imageView.setImageBitmap(mBitmapImages.get(position));

            // Setting tag to remember position
            imageView.setTag(position);
            imageView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    int id = (Integer) arg0.getTag(); // id is the position of the bitmap
                    zoomImageFromThumb(arg0, id);
                }
            });

            return imageView;
        }

        private void zoomImageFromThumb(final View thumbView, int imageResId) {
            // If there's an animation in progress, cancel it immediately and
            // proceed with this one.
            if (mCurrentAnimator != null) {
                mCurrentAnimator.cancel();
            }

            // Load the high-resolution "zoomed-in" image.
            final ImageView expandedImageView = (ImageView) ((Activity) mContext)
                    .findViewById(R.id.expanded_image);
            expandedImageView.setImageBitmap(mBitmapImages.get(imageResId));

            // Calculate the starting and ending bounds for the zoomed-in image.
            // This step
            // involves lots of math. Yay, math.
            final Rect startBounds = new Rect();
            final Rect finalBounds = new Rect();
            final Point globalOffset = new Point();

            // The start bounds are the global visible rectangle of the thumbnail,
            // and the
            // final bounds are the global visible rectangle of the container view.
            // Also
            // set the container view's offset as the origin for the bounds, since
            // that's
            // the origin for the positioning animation properties (X, Y).
            thumbView.getGlobalVisibleRect(startBounds);
            ((Activity) mContext).findViewById(R.id.container)
                    .getGlobalVisibleRect(finalBounds, globalOffset);
            startBounds.offset(-globalOffset.x, -globalOffset.y);
            finalBounds.offset(-globalOffset.x, -globalOffset.y);

            // Adjust the start bounds to be the same aspect ratio as the final
            // bounds using the
            // "center crop" technique. This prevents undesirable stretching during
            // the animation.
            // Also calculate the start scaling factor (the end scaling factor is
            // always 1.0).
            float startScale;
            if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds
                    .width() / startBounds.height()) {
                // Extend start bounds horizontally
                startScale = (float) startBounds.height() / finalBounds.height();
                float startWidth = startScale * finalBounds.width();
                float deltaWidth = (startWidth - startBounds.width()) / 2;
                startBounds.left -= deltaWidth;
                startBounds.right += deltaWidth;
            } else {
                // Extend start bounds vertically
                startScale = (float) startBounds.width() / finalBounds.width();
                float startHeight = startScale * finalBounds.height();
                float deltaHeight = (startHeight - startBounds.height()) / 2;
                startBounds.top -= deltaHeight;
                startBounds.bottom += deltaHeight;
            }

            // Hide the thumbnail and show the zoomed-in view. When the animation
            // begins,
            // it will position the zoomed-in view in the place of the thumbnail.
            thumbView.setAlpha(0f);
            expandedImageView.setVisibility(View.VISIBLE);

            // Set the pivot point for SCALE_X and SCALE_Y transformations to the
            // top-left corner of
            // the zoomed-in view (the default is the center of the view).
            expandedImageView.setPivotX(0f);
            expandedImageView.setPivotY(0f);

            // Construct and run the parallel animation of the four translation and
            // scale properties
            // (X, Y, SCALE_X, and SCALE_Y).
            AnimatorSet set = new AnimatorSet();
            set.play(
                    ObjectAnimator.ofFloat(expandedImageView, View.X,
                            startBounds.left, finalBounds.left))
                    .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                            startBounds.top, finalBounds.top))
                    .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                            startScale, 1f))
                    .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y,
                            startScale, 1f));
            set.setDuration(mShortAnimationDuration);
            set.setInterpolator(new DecelerateInterpolator());
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCurrentAnimator = null;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mCurrentAnimator = null;
                }
            });
            set.start();
            mCurrentAnimator = set;

            // Upon clicking the zoomed-in image, it should zoom back down to the
            // original bounds
            // and show the thumbnail instead of the expanded image.
            final float startScaleFinal = startScale;
            expandedImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCurrentAnimator != null) {
                        mCurrentAnimator.cancel();
                    }

                    // Animate the four positioning/sizing properties in parallel,
                    // back to their
                    // original values.
                    AnimatorSet set = new AnimatorSet();
                    set.play(
                            ObjectAnimator.ofFloat(expandedImageView, View.X,
                                    startBounds.left))
                            .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                                    startBounds.top))
                            .with(ObjectAnimator.ofFloat(expandedImageView,
                                    View.SCALE_X, startScaleFinal))
                            .with(ObjectAnimator.ofFloat(expandedImageView,
                                    View.SCALE_Y, startScaleFinal));
                    set.setDuration(mShortAnimationDuration);
                    set.setInterpolator(new DecelerateInterpolator());
                    set.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            thumbView.setAlpha(1f);
                            expandedImageView.setVisibility(View.GONE);
                            mCurrentAnimator = null;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            thumbView.setAlpha(1f);
                            expandedImageView.setVisibility(View.GONE);
                            mCurrentAnimator = null;
                        }
                    });
                    set.start();
                    mCurrentAnimator = set;
                }
            });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gallerys_menu, menu);
        return true;
    }

    // This method controls what is done when menu items are selected.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_back:
                goBack();
                return true;

            case R.id.view_saved_outfits:
                viewOutfits();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    // When this method is run, start up activity is opened.
    public void goBack() {
        Intent intent = new Intent(this, StartUp.class);
        startActivity(intent);
    }

    // When this method is run, all saved items will be shown.
    public void viewOutfits() {
        Intent intent = new Intent(this, ViewOutfits.class);
        startActivity(intent);
    }
}