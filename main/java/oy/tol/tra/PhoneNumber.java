package oy.tol.tra;

public class PhoneNumber {
    private String countryCode;
    private String areaCode;
    private String phoneNumber;

    public PhoneNumber(String countryCode, String areaCode, String phoneNumber) {
        this.countryCode = countryCode;
        this.areaCode = areaCode;
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return countryCode + "-" + areaCode + "-" + phoneNumber;
    }

}
