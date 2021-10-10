let emp = (jsonDeCookifier)('empInfo');

function jsonDeCookifier(name){

    //console.log('hello decookifier')
    let cookies = document.cookie;
    console.log(cookies);
    //let name = 'empInfo'

    if(cookies.length != 0){

        let cookieArray = cookies.split("=");

        for(var i = 0; i < cookieArray.length - 1; i += 2){
        console.log('hello from inside decookifier for loop')
        let cookieName = cookieArray[i];
        console.log(cookieName);
            if(cookieName == name){
                let uglyCookie = cookieArray[i + 1];
                let lessUglyCookie = uglyCookie.replace(/\\/g,'');
                let prettyCookie = lessUglyCookie.slice(1, lessUglyCookie.length - 1);

                let yoJson = JSON.parse(prettyCookie);
                //console.log('prettyCookie: ' + prettyCookie)
                //console.log(yoJson);
                return yoJson;
             }
        }
    }
}
console.log(emp);

if(emp.supervisor || emp.deptHead){
     document.getElementById("sup-nav").innerHTML = `<a class="nav-link" href="management.html">Management Menu</a>`;
     }
if(emp.department == 'benco'){
         document.getElementById("benco-nav").innerHTML = `<a class="nav-link" href="benco.html">BenCo Menu</a>`;
    }

document.getElementById('welcome').innerHTML = `Welcome Back ${emp.firstName} ${emp.lastName}!`;

function getAmounts(){
    console.log('hello from inside getAmounts function.')
    let empId = emp.empId;

    let baseUrl = 'empmenu/';
    let url = baseUrl + empId;
    console.log(url);

    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = receiveData;
    xhttp.open('GET', url, true);
    xhttp.send();

    function receiveData(){

        let dataSection = document.getElementById('message');
        dataSection.innerHTML = '';

        if(xhttp.readyState == 4 && xhttp.status == 200){
            let resp = xhttp.responseText;

            resp = JSON.parse(resp);

            populateData(resp);
        }
    }
}

function populateData(resData){

    let dataSection = document.getElementById('message');
    dataSection.innerHTML = `You have $${emp.pending} worth of pending requests.<br>You have $${emp.eligible} remaining reimbursement eligibility.`

}

