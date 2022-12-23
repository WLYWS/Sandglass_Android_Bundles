package com.wyze.sandglasslibrary.moudle.SLFResponse;

/**
 * 返回json基础对象
 */
public class SLFBaseResponseMoudle {

    /**
     * code : 1
     * current : 1
     * data : {}
     * hash : 1
     * instance_id : 2b08204a924fcd082c94293bf9e2782d
     * message : Success
     * total : 1
     * version : 1
     */

    protected int code;
    protected int current;
    protected String hash;
    protected String instance_id;
    protected String message;
    protected int total;
    protected int version;



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getInstance_id() {
        return instance_id;
    }

    public void setInstance_id(String instance_id) {
        this.instance_id = instance_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "BaseData{" +
                "code=" + code +
                ", current=" + current +
                ", hash='" + hash + '\'' +
                ", instance_id='" + instance_id + '\'' +
                ", message='" + message + '\'' +
                ", total=" + total +
                ", version=" + version +
                '}';
    }
}
