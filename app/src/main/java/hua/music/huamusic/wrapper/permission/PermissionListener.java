package hua.music.huamusic.wrapper.permission;

/**
 * 6.0动态权限申请回调
 *
 * @author hua
 * @version  2017/7/31
 */

public interface PermissionListener {

    /**
     * 有权限通过时调用
     *
     * @param permissions 通过的回调
     */
    void onGranted(String[] permissions);

    /**
     * 有权限被拒绝时调用
     *
     * @param permissions 被拒绝的回调
     */
    void onDenied(String[] permissions);
}
