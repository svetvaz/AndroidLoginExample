package com.svetlana.library;



import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.content.Context;


public class UserFunctions {

    private JSONParser jsonParser;

    //URL of the PHP API

    private static String api_URL = "http://goanhome.no-ip.info/svet_login_api/";
    		
    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String forpass_tag = "forpass";
    private static String chgpass_tag = "chgpass";
    private static String updatepoints_tag = "updatepoints";
    private static String points_tag = "points";
    private static String sync_db_tag= "sync_db";
    private static String updaterating_tag = "updaterating";

    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }

    /**
     * Function to Login
     **/

    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(api_URL, params);
        return json;
    }
    
    
    public JSONObject syncdb(String uid){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", sync_db_tag));
        params.add(new BasicNameValuePair("uid", uid));
        JSONObject json = jsonParser.getJSONFromUrl(api_URL, params);
        return json;
    }

    /**
     * Function to change password
     **/

    public JSONObject chgPass(String newpas, String email){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", chgpass_tag));
        params.add(new BasicNameValuePair("newpas", newpas));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.getJSONFromUrl(api_URL, params);
        return json;
    }


    /**
     * Function to update points
     **/

    public JSONObject updatePoints(String newpoints, String uid, String businessname,String email){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", updatepoints_tag));
        params.add(new BasicNameValuePair("newpoints", newpoints));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("businessname", businessname));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.getJSONFromUrl(api_URL, params);
        return json;
    }
    

    /**
     * Function to update rating
     **/

    public JSONObject updateRating(String rating, String uid, String feedback){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", updaterating_tag));
        params.add(new BasicNameValuePair("rating", rating));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("feedback", feedback));
        JSONObject json = jsonParser.getJSONFromUrl(api_URL, params);
        return json;
    }


    /**
     * Function to reset the password
     **/

    public JSONObject forPass(String forgotpassword){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", forpass_tag));
        params.add(new BasicNameValuePair("forgotpassword", forgotpassword));
        JSONObject json = jsonParser.getJSONFromUrl(api_URL, params);
        return json;
    }






     /**
      * Function to  Register
      **/
    public JSONObject registerUser(String fname, String lname, String email, String uname, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("fname", fname));
        params.add(new BasicNameValuePair("lname", lname));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("uname", uname));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(api_URL,params);
        return json;
    }

    

    public JSONObject userPoints(String uid){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", points_tag));
        params.add(new BasicNameValuePair("uid", uid));
        JSONObject json = jsonParser.getJSONFromUrl(api_URL, params);
        return json;
    }
    

    /**
     * Function to logout user
     * Resets the temporary data stored in SQLite Database
     * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }
    
    

}

