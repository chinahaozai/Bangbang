package halewang.com.bangbang.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by halewang on 2016/12/29.
 */

public class Requirement extends BmobObject {

    private String initiatorPhone;  //发起人手机号
    private String title;           //需求标题
    private String content;         //需求内容
    private String time;            //发起时间
    private int money;              //预算
    private int watchCount;         //查看人数
    private String status;          //任务状态
    private String site;            //位置信息
    private String receiverPhone;   //接收人手机号
    private String installationId;  //推送需要的installationId
    private String latitude;
    private String longitude;

    public String getInitiatorPhone() {
        return initiatorPhone;
    }

    public void setInitiatorPhone(String initiatorPhone) {
        this.initiatorPhone = initiatorPhone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getWatchCount() {
        return watchCount;
    }

    public void setWatchCount(int watchCount) {
        this.watchCount = watchCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getInstallationId() {
        return installationId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Requirement{" +
                "initiatorPhone='" + initiatorPhone + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", money=" + money +
                ", watchCount=" + watchCount +
                ", status='" + status + '\'' +
                ", site='" + site + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", installationId='" + installationId + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
