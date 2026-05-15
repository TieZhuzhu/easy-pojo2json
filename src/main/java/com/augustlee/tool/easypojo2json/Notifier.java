package com.augustlee.tool.easypojo2json;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

/**
 * IDEA 通知工具。
 * <p>
 * 统一封装插件中的通知展示逻辑，避免业务代码直接依赖通知组细节。
 */
public class Notifier {

    /**
     * 发送错误级别通知。
     *
     * @param content 通知内容
     * @param project 当前项目
     */
    public static void notifyError(String content, Project project) {
        notify(content, NotificationType.ERROR, project);
    }

    /**
     * 发送警告级别通知。
     *
     * @param content 通知内容
     * @param project 当前项目
     */
    public static void notifyWarn(String content, Project project) {
        notify(content, NotificationType.WARNING, project);
    }

    /**
     * 发送信息级别通知。
     *
     * @param content 通知内容
     * @param project 当前项目
     */
    public static void notifyInfo(String content, Project project) {
        notify(content, NotificationType.INFORMATION, project);
    }

    /**
     * 发送指定类型的通知。
     *
     * @param content 通知内容
     * @param type    通知类型
     * @param project 当前项目
     */
    public static void notify(String content, NotificationType type, Project project) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup("com.augustlee.tool.easypojo2json.NotificationGroup")
                .createNotification(content, type)
                .notify(project);
    }
}
