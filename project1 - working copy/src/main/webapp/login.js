function deleteAllCookies() {
    var cookies = document.cookie.split(";");

    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        var eqPos = cookie.indexOf("=");
        var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    }
}
(deleteAllCookies)();

// function login(){
//     // collect the inputs from the login screen
//     let un = document.getElementsByName(username);
//     let pw = document.getElementsByName(password);

//     let loginData = {
//         username: un,
//         password: pw
//     }

//     console.log(loginData)

//    let xhttp = new XMLHttpRequest();

//    // xhttp.onreadystatechange = function(){

//     //    if(this.readyState == 4 && this.status == 200){
//      //   let r = this.responseText;

//       //  r = JSON.parse(r);
            
//         //console.log(r);
//        // }
//    // }

//     xhttp.open('POST', 'http://localhost:8080/LoginServlet', true);

//     xhttp.setRequestHeader('Content-Type', 'application/json');

//     xhttp.send(JSON.stringify())
//}