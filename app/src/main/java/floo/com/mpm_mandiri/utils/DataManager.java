package floo.com.mpm_mandiri.utils;

/**
 * Created by Floo on 2/26/2016.
 */
public class DataManager {

    //POST
    public static String url = "http://play.floostudio.com/lenteramandiri/api/v1/instance";
    public static String urlLogin = "http://play.floostudio.com/lenteramandiri/api/v1/users/login";
    public static String urlRegister = "http://play.floostudio.com/lenteramandiri/api/v1/users/register";
    public static String urlLoginSementara = "http://sandbox.floostudio.com/lenteramandiri/index.php/api/v1/users/login";
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
    public static String urlCovenant = "http://play.floostudio.com/lenteramandiri/api/v1/portfolio_acc/covenant/";
    public static String urlDashboard = "http://play.floostudio.com/lenteramandiri/api/v1/dashboard";
    public static String urlListperAccount = "http://sandbox.floostudio.com/lenteramandiri/api/v1/dashboard/listAccount";
    public static String urlGetperAccount = "http://play.floostudio.com/lenteramandiri/api/v1/dashboard/account/";
    public static String urlGetperAccountSementara = "http://sandbox.floostudio.com/lenteramandiri/api/v1/dashboard/account/";

    //PUT
    public static String urltaskNote = "http://play.floostudio.com/lenteramandiri/api/v1/tasks/note/";
    public static String urlChangePass = "http://play.floostudio.com/lenteramandiri/api/v1/users/change_password";
    public static String urlUpdProfile = "http://play.floostudio.com/lenteramandiri/api/v1/users/detail/";
    public static String urltaskDone = "http://play.floostudio.com/lenteramandiri/api/v1/tasks/done/";

}
