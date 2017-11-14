package com.benmu.framework.event.tool;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.benmu.framework.model.BaseResultBean;
import com.benmu.framework.model.StorageRaesultBean;
import com.benmu.framework.utils.BaseCommonUtil;
import com.taobao.weex.bridge.JSCallback;

/**
 * Created by Carry on 2017/9/18.
 */

public class EventTool {
    public void resignKeyboard(Context context, JSCallback jscallback) {
        BaseResultBean bean = new StorageRaesultBean();
        if (BaseCommonUtil.getKeyBoardState(context)) {
            bean.resCode = 0;
        } else {
            bean.resCode = 9;
        }
        bean.msg = null;
        if (jscallback != null) {
            jscallback.invoke(bean);
        }
    }


    public void isWXInstall(Context context, JSCallback jsCallback) {
        BaseResultBean bean = new BaseResultBean();
        bean.resCode = BaseCommonUtil.isWeChatInstall(context) ? 0 : 9;
        if (jsCallback != null) {
            jsCallback.invoke(bean);
        }
    }

    public void copyString(Context context, String params, JSCallback callback) {
        BaseCommonUtil.copyString(context, params);
        BaseResultBean bean = new BaseResultBean(0, "复制成功");
        if (callback != null) {
            callback.invoke(bean);
        }
    }

}

