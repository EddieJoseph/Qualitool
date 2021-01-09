import axios from 'axios';


//window.location.href.split(":")[0]+"://" + window.location.host.split(":")[0]+":8082/"
//window.location.port===""?window.location.protocol==="http:"?80:443:window.location.port

//const applicationBaseUrl = window.location.href.split(":")[0]+"://" + window.location.host.split(":")[0]
//const port = window.location.port === "" ? window.location.protocol === "http:"?80:443:window.location.port
/*const applicationFrontend = applicationBaseUrl + ":" + window.location.port === "" ? window.location.protocol === "http:"?80:443:window.location.port
const applicationBackend = applicationBaseUrl + ":" + 8082
const applicationSSO = applicationBaseUrl + ":" + 8083



console.log("Frontend",applicationBaseUrl + ":" + port)
console.log("Backend",applicationBaseUrl + ":" + 8082)
console.log("SSO",applicationBaseUrl + ":" + 8083)*/

let applicationBaseUrl = window.location.href.split(":")[0]+"://" + window.location.host.split(":")[0]
let port = window.location.port === "" ? window.location.protocol === "http:"?80:443:window.location.port
const _service={
  frontendUrl: applicationBaseUrl + ":" + port,
  backendUrl: applicationBaseUrl + ":" + 8082,
  ssoUrl: applicationBaseUrl + ":" + 8083,



  clientId: "qualitool_backend",
  authEndpoint: applicationBaseUrl + ":" + 8083 + "/auth/realms/qualitool/protocol/openid-connect/auth",
  redirectUri: "https://localhost:3000",
  tokenEndpoint: applicationBaseUrl + ":" + 8083 + "/auth/realms/qualitool/protocol/openid-connect/token",
  secret: "a889f55d-5f3e-42a2-90e0-5200ff58751b",
  loggedIn: false
}

console.log(_service)

var credHolder = (function(){
    var savedtoken = null

    function _checkToken(){
      if(savedtoken != null){
        return true
      }else{
        return false
      }
    }

    function _authenticated(successhandler){
        _service.loggedIn = _checkCredentials()
        let i = window.location.href.indexOf('code');
        if(!_service.loggedIn && i != -1){
            _retrieveToken(window.location.href.substring(i + 5),successhandler);
        }else if(!_service.loggedIn){
            _login()
        }

        return _service.loggedIn
    }

    function _login(){
        window.location.href = _service.authEndpoint+'?response_type=code&scope=email&client_id=' + _service.clientId + '&redirect_uri='+ _service.redirectUri;
    }

    function _getResource(resourceUrl,callback,errorhandling){
      let headers = {'Content-type': 'application/x-www-form-urlencoded; charset=utf-8',
      'Authorization': 'Bearer '+ savedtoken.access_token,
      }
  
      axios.get(_service.backendUrl + resourceUrl
      ,{headers:headers})
      .then((response)=>{
          callback(response.data)
      }).catch((error)=>{
          if(error.response.status==401){
            _refreshToken(()=>{_getResource(resourceUrl,callback,errorhandling)})
          }else{
            if(errorhandling==null){
              console.log("error retrieving data", error)
              }else{
                  errorhandling(error)
              }
          }
      })
    }

    //TODO Add get method check get method for pictures
    //TODO Add put method


    function _refreshToken(successfunction){
      let params = new URLSearchParams();   
      params.append('grant_type','refresh_token');
      params.append('client_id', _service.clientId);
      params.append('client_secret', _service.secret);
      params.append('redirect_uri', _service.redirectUri);
      params.append('refresh_token',savedtoken.refresh_token);
  
      let headers = {'Content-type': 'application/x-www-form-urlencoded; charset=utf-8'}
      console.log('refreshing token token')
      axios.post(_service.tokenEndpoint+"?"
      ,params.toString(),{headers:headers})

      .then((response)=>{
          _saveToken(response.data)
          if(successfunction!=null) {
            successfunction()
          }
      }).catch((error)=>{
          console.log('No Refresh possible.')
          window.location.href = "/"
      })
    }
    
    function _checkCredentials(){
        return savedtoken != null
    }
    
    function _retrieveToken(code,successhandler){
        let params = new URLSearchParams();   
        params.append('grant_type','authorization_code');
        params.append('client_id', _service.clientId);
        params.append('client_secret', _service.secret);
        params.append('redirect_uri', _service.redirectUri);
        params.append('code',code);
    
        let headers = {'Content-type': 'application/x-www-form-urlencoded; charset=utf-8'}
        console.log('retrieving access token')
        axios.post(_service.tokenEndpoint+"?"
        ,params.toString(),{headers:headers})

        .then((response)=>{
            _saveToken(response.data,successhandler)
        }).catch((error)=>{
            alert('Invalid Credentials')
        })
      }
    
     function _saveToken(token,successhandler){
        var expireDate = new Date().getTime() + (1000 * token.expires_in);
        savedtoken = token
        
        _service.loggedIn=true
        window.history.replaceState({},'','/')
        if(successhandler!=null){
          successhandler()
        }else{
        }

      }
    
      function _removeToken(){
        savedtoken = null
      }

      return {
        authenticated: function(successhandler) {
          return _authenticated(successhandler);
        },
        getResource: function(resourceUrl,callback,errorhandling) {
            _getResource(resourceUrl,callback,errorhandling);
        },
        checkToken: function() {
          return _checkToken();
        }
    }


})();

Object.freeze(credHolder)

console.log(credHolder)

const authenticated =(successhandler) =>{ 
    return credHolder.authenticated(successhandler)
}

const checkToken =() =>{
  return credHolder.checkToken()
}
/*const authenticate =() =>{ 
  return credHolder.authenticate()
}*/

const getResource=(resourceUrl,callback,errorhandling)=>{
    return credHolder.getResource(resourceUrl,callback,errorhandling)
}

/*
const cookies = new Cookies();

const authenticated = () =>{
    _service.loggedIn = checkCredentials()
    let i = window.location.href.indexOf('code');
    if(!_service.loggedIn && i != -1){
        _service.loggedIn=true
        retrieveToken(window.location.href.substring(i + 5));
    }

    if(!_service.loggedIn){
        login()
    }

    //TODO Romove as this is only for test use
    if(_service.loggedIn){
        var callback = (resp) =>{console.log(resp)}
        console.log(getResource("/block",callback))
    }



}

const login = () => {
    window.location.href = _service.authEndpoint+'?response_type=code&scope=email&client_id=' + _service.clientId + '&redirect_uri='+ _service.redirectUri;
}

const getResource = (resourceUrl,callback,errorhandling) => {
    let headers = {'Content-type': 'application/x-www-form-urlencoded; charset=utf-8',
    'Authorization': 'Bearer '+ cookies.get('access_token')
}

    axios.get("https://localhost:8082"+resourceUrl
    ,{headers:headers})
    .then((response)=>{
        callback(response.data)
    }).catch((error)=>{
        if(errorhandling==null){
        console.log("error retrieving data", error)
        }else{
            errorhandling(error)
        }
    })
  }

const checkCredentials=()=>{
    return getCookie("access_token")!==""
}
const getCookie = (cname) =>{
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
      var c = ca[i];
      while (c.charAt(0) === ' ') {
        c = c.substring(1);
      }
      if (c.indexOf(name) === 0) {
        return c.substring(name.length, c.length);
      }
    }
    return "";
  }

const retrieveToken=(code) =>{
    let params = new URLSearchParams();   
    params.append('grant_type','authorization_code');
    params.append('client_id', _service.clientId);
    params.append('client_secret', _service.secret);
    params.append('redirect_uri', _service.redirectUri);
    params.append('code',code);

    let headers = {'Content-type': 'application/x-www-form-urlencoded; charset=utf-8'}//, "Access-Control-Allow-Origin": "access-control-allow-origin"
    axios.post(_service.tokenEndpoint+"?"
    ,params.toString(),{headers:headers})
    .then((response)=>{
        //console.log("token response",response.data)
        saveToken(response.data)
    }).catch((error)=>{
        alert('Invalid Credentials')
    })
  }

 const saveToken=(token) =>{
    var expireDate = new Date().getTime() + (1000 * token.expires_in);
    console.log('Obtained Access token');
    console.log(token)
    //TODO enable use of refresh token!!
    cookies.set('access_token', token.access_token, { path: '/' });
    
    console.log('Obtained Access token');
    //window.location.href = 'https://localhost:3000';
  }

  const removeToken=() => {
    cookies.remove('access_token');
  }
*/
  export default {authenticated, getResource, checkToken, credHolder};
  //export default getResource;