package org.hdm.app.timetracker.datastorage;

/**
 * Created by Hannes on 24.10.2016.
 */
public class ActivityObject {

    // General Parameter
    private String _id = null;
    private String title = null;
    private String imageName = null;
    private String externalWork = "";
    private String group_activity = "";
    private String item = null;




    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }


    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }


    public String getImageName() {
        return imageName;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }


    public String getExternalWork() {
        return externalWork;
    }
    public void setExternalWork(String externalWork) {
        this.externalWork = externalWork;
    }


    public String getGroup_activity() {
        return group_activity;
    }
    public void setGroup_activity(String group_activity) {
        this.group_activity = group_activity;
    }


    public String getItem() {return item;}
    public void setItem(String item) {
        this.item = item;
    }

}
