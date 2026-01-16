package ProiectColectiv.Domain.DTOs;

public class AddressDTO {
    private String fullName;
    private String phone;
    private String address;
    private String city;
    private String postalCode;

    public AddressDTO() {
    }

    @Override
    public String toString() {
        return fullName + ", " + address + ", " + city + ", " + postalCode + ", Tel: " + phone;
    }

    public String getFullName() { return fullName; }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}