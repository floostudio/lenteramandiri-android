package floo.com.mpm_mandiri.utils;

/**
 * Created by Floo on 2/26/2016.
 */
public class DataManager {

    //POST
    public static String url = "http://play.floostudio.com/lenteramandiri/api/v1/instance";
    public static String urlLogin = "http://play.floostudio.com/lenteramandiri/api/v1/users/login";
    public static String urlRegister = "http://play.floostudio.com/lenteramandiri/api/v1/users/register";
    String name = "DOT";
    String password = "DOTVNDR";

    //GET
    public static String urltaskList = "http://play.floostudio.com/lenteramandiri/api/v1/tasks/user/";
    public static String urltaskDetails = "http://play.floostudio.com/lenteramandiri/api/v1/tasks/detail/";
    public static String urlprofilList = "http://play.floostudio.com/lenteramandiri/api/v1/users/detail/";
    public static String urlNewsList = "http://play.floostudio.com/lenteramandiri/api/v1/news";
    public static String urlFetchNews = "http://play.floostudio.com/lenteramandiri/api/v1/news/detail/";
    public static String urlMasterDirectorate = "http://play.floostudio.com/lenteramandiri/api/v1/master/directorate";
    public static String urlMasterDepartment = "http://play.floostudio.com/lenteramandiri/api/v1/master/department";
    public static String urlMasterGroup = "http://play.floostudio.com/lenteramandiri/api/v1/master/group";
    public static String urlMasterTitle = "http://play.floostudio.com/lenteramandiri/api/v1/master/title";
    public static String urlPortGroup = "http://play.floostudio.com/lenteramandiri/api/v1/portfolio_group";
    public static String urlPortGroupDetail = "";
    public static String urlPortAccount = "http://play.floostudio.com/lenteramandiri/api/v1/portfolio_acc";
    public static String urlPortAccountDetail = "";

    //PUT
    public static String urltaskNote = "http://play.floostudio.com/lenteramandiri/api/v1/tasks/note/";
    public static String urlChangePass = "http://play.floostudio.com/lenteramandiri/api/v1/users/change_password";

}
