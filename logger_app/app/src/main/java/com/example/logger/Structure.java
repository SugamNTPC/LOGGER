package com.example.logger;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Structure extends RealmObject {

    @PrimaryKey
    private String _id;

    private String id;
    private String name;
    private String parent;
    private String level_leaf;
    private String dtype;
    private String slider_entries;
    private String low_lim;
    private String high_lim;
    private String disable_entry;
    private String hint_text;
    private String default_value;
    private String value;
    private String logger_name;
    private String logger_phone;
    private String entry_time;

    private String unit;

    public String getId() { return _id; }
    public String getName(){ return name; }
    public String getParent(){ return parent; }
    public String getLevelLeaf() { return level_leaf; }
    public String getDtype() { return dtype; }
    public String getSliderEntries() { return slider_entries; }
    public String getLowLim() { return low_lim; }
    public String getHighLim() { return high_lim; }
    public String getDisable_entry() { return disable_entry; }
    public String getHintText() { return hint_text; }
    public String getDefault_value() { return default_value; }
    public String getValue(){ return value; }
    public String getUnit(){ return unit; }
    public String getLoggerName() { return logger_name; }
    public String getLoggerPhone(){ return logger_phone; }
    public String getEntryTime(){ return entry_time; }

    public void setId(String id) { this._id = id; }
    public void setName(String name){ this.name = name; }
    public void setParent(String parent){ this.parent = parent; }
    public void setLevelLeaf(String level_leaf) { this.level_leaf = level_leaf; }
    public void setDtype(String dtype) { this.dtype = dtype; }
    public void setSliderEntries(String slider_entries) { this.slider_entries = slider_entries; }
    public void setLowLim(String low_lim) { this.low_lim = low_lim; }
    public void setHighLim(String high_lim) { this.high_lim = high_lim; }
    public void setDisable_entry(String disable_entry) { this.disable_entry = disable_entry; }
    public void setHintText(String hint_text) { this.hint_text = hint_text; }
    public void setDefault_value(String default_value) { this.default_value = default_value; }
    public void setValue(String value){ this.value = value; }
    public void setUnit(String unit){ this.unit = unit; }
    public void setLoggerName(String logger_name){ this.logger_name = logger_name; }
    public void setLoggerPhone(String logger_phone){ this.logger_phone = logger_phone; }
    public void setEntryTime(String entry_time){ this.entry_time = entry_time; }

}
