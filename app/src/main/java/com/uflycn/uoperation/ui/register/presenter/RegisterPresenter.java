package  com.uflycn.uoperation.ui.register.presenter;

/**
 * Created by Ryan on 2016/7/13.
 */
public interface RegisterPresenter {

    void submit(String username, String email, String tel, String company);

    void renew();
}
