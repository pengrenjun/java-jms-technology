package com.activemq;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @Description: 消息信息
 * @Author：pengrj
 * @Date : 2019/5/6 0006 21:56
 * @version:1.0
 */
public class QueueMessageBo implements Serializable {

    private String id;

    private String content;

    private Date createTime;

    public QueueMessageBo(String id, String content, Date createTime) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "QueueMessageBo{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
