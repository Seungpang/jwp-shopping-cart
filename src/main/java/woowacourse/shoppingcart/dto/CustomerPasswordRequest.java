package woowacourse.shoppingcart.dto;

public class CustomerPasswordRequest {

    private String password;

    public CustomerPasswordRequest() {}

    public CustomerPasswordRequest(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
