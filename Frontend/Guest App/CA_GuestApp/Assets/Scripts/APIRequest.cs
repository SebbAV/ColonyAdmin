using System.Collections;
using System.Collections.Generic;
using UnityEngine.Networking;
using UnityEngine;

public class APIRequest : MonoBehaviour {
	
	// Use this for initialization
	void Start () {
		string request_url = "http://akarokhome.ddns.net:3000";	
	}
	
	// Update is called once per frame
	void Update () {
		
	}
	public void postUser(){
		Debug.Log("Seh");
		StartCoroutine(doLogin("jsebastianav8@gmail.com","123"));
	}
	IEnumerator doLogin(string user, string pwd){
		Debug.Log("Seh inside the co");
		string jsonStr = "{email:'"+user+"',password:'"+pwd+"'}";
		WWWForm web_form = new WWWForm();
		web_form.AddField("email",user);
		web_form.AddField("password",pwd);
		Hashtable postHeader = new Hashtable();
    	postHeader.Add("Content-Type", "application/json");

    	// convert json string to byte
    	var formData = System.Text.Encoding.UTF8.GetBytes(jsonStr);

		using (UnityWebRequest www = UnityWebRequest.Post("http://akarokhome.ddns.net:3000/"+"user/login",formData)){
			www.SetRequestHeader("Accept", "application/json");
			yield return www.SendWebRequest();
			Debug.Log(www.isNetworkError);
			Debug.Log(www.isHttpError);
			if(www.isNetworkError|| www.isHttpError)
				Debug.Log(www.error);
			else 
				Debug.Log("Success!");
		}
	}
}
