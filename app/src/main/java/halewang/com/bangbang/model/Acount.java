package halewang.com.bangbang.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by halewang on 2016/12/29.
 */

public class Acount extends BmobObject {

    private String phone;
    private int earning; //收入
    private int pay; //支出

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getEarning() {
        return earning;
    }

    public void setEarning(int earning) {
        this.earning = earning;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    @Override
    public String toString() {
        return "Acount{" +
                "phone='" + phone + '\'' +
                ", earning=" + earning +
                ", pay=" + pay +
                '}';
    }
}
