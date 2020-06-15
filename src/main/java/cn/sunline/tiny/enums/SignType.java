package cn.sunline.tiny.enums;

public enum SignType {
    /**
     * MD5 加密
     */
    MD5("MD5");

    SignType(String type) {
        this.type = type;
    }

    private final String type;

    public String getType() {
        return type;
    }
}
