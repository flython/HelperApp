package com.benmu.framework.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.SystemClock;
import android.widget.ImageView;
import android.widget.Toast;

import com.benmu.framework.R;
import com.benmu.framework.http.Api;
import com.benmu.framework.constant.Constant;
import com.benmu.framework.manager.ManagerFactory;
import com.benmu.framework.manager.impl.AxiosManager;
import com.benmu.framework.manager.impl.FileManager;
import com.benmu.framework.manager.impl.ModalManager;
import com.benmu.framework.manager.impl.PermissionManager;
import com.benmu.framework.model.UploadImageBean;
import com.benmu.framework.utils.ImageUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.loader.ImageLoader;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by Carry on 2017/8/21.
 */

public class DefaultImageAdapter {
    private static DefaultImageAdapter mInstance = new DefaultImageAdapter();

    private static ImagePicker imagePicker = ImagePicker.getInstance();

    private DefaultImageAdapter() {
    }

    public static DefaultImageAdapter getInstance() {
        return mInstance;
    }

    public void pickPhoto(final Context context, UploadImageBean bean) {
        if (!checkPermission(context)) return;

        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //
        imagePicker.setCrop(false);//允许裁剪（单选才有效）
        imagePicker.setMultiMode(true);//是否是多张
        imagePicker.setSelectLimit(bean.maxCount);    //选中数量限制
        Intent intent = new Intent(context, ImageGridActivity.class);
        intent.putExtra(Constant.ImageConstants.UPLOADIMAGERBEAN_WITH, bean.imageWidth);
        ((Activity) context).startActivityForResult(intent, Constant.ImageConstants.IMAGE_PICKER);

    }

    public void pickAvatar(final Context context, UploadImageBean bean) {
        if (!checkPermission(context)) return;

        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setMultiMode(false);//是否是多张
        imagePicker.setCrop(true);//允许裁剪
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(Constant.ImageConstants.BIGGESTWIDTH);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(Constant.ImageConstants.BIGGESTWIDTH);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        Intent intent = new Intent(context, ImageGridActivity.class);
        intent.putExtra(Constant.ImageConstants.UPLOADIMAGERBEAN_WITH, bean.imageWidth);
        ((Activity) context).startActivityForResult(intent, Constant.ImageConstants.IMAGE_PICKER);
    }


    public void UpMultipleImageData(Context context, ArrayList<ImageItem> items, int newWidth) {
        ModalManager.BmLoading.showLoading(context, null, false);
        ArrayList imagesFilrUrl = new ArrayList();
        if (items != null && items.size() > 0) {
            for (ImageItem item : items) {
                Bitmap bitmap = ImageUtil.getBitmap(item.path, context);
                //TODO 图片改为全路径
                String path = new File(FileManager.getTempFilePath(context), String.valueOf
                        (SystemClock.currentThreadTimeMillis())).getAbsolutePath();
                String imageFileUrl = ImageUtil.zoomImage(context, bitmap, newWidth, Constant
                        .ImageConstants.BIGGESTWIDTH, path);
                imagesFilrUrl.add(imageFileUrl);
                bitmap.recycle();

            }
        }
        AxiosManager axiosManager = ManagerFactory.getManagerService(AxiosManager.class);
        axiosManager.upload(Api.UPLOAD_URL, imagesFilrUrl, null, null);
    }


    /**
     * 判断Sd卡是否挂载，是否有Sd卡权限
     */
    private boolean checkPermission(Context context) {
        PermissionManager permissionManager = ManagerFactory.getManagerService(PermissionManager
                .class);
        boolean hasPermisson = permissionManager.hasPermissions(context, Manifest.permission
                .READ_EXTERNAL_STORAGE);
        if (!hasPermisson) {
            ModalManager.BmToast.toast(context, "读取sd卡存储权限未授予，请到应用设置页面开启权限!", Toast.LENGTH_SHORT);
        }
        return hasPermisson;
    }

    public class GlideImageLoader implements ImageLoader {

        @Override
        public void displayImage(Activity activity, String path, ImageView imageView, int width,
                                 int height) {

            Glide.with(activity)                             //配置上下文
                    .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                    .error(R.mipmap.default_image)           //设置错误图片
                    .placeholder(R.mipmap.default_image)     //设置占位图片
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                    .into(imageView);
        }

        @Override
        public void clearMemoryCache() {

        }
    }

}

