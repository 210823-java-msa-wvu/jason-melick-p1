function jsonDeCookifier(name){

    let cookies = document.cookie;
    //console.log(name)
    if(cookies.length != 0){

        let cookieArray = cookies.split("=");
        //console.log(cookieArray);
        for(var i = 0; i < cookieArray.length - 1; i += 2){

        let cookieName = cookieArray[i];
        //console.log(i);
        //console.log(cookieName);

            if(cookieName == name){

                let uglyCookie = cookieArray[i + 1];
                let lessUglyCookie = uglyCookie.replace(/\\/g,'');
                let prettyCookie = lessUglyCookie.slice(1, lessUglyCookie.length - 1);

                let yoJson = JSON.parse(prettyCookie);

                return yoJson;
            }
        }
    }else return null;
 }

let empInfo = (jsonDeCookifier)('empInfo');
let empId = empInfo.empId;
//console.log(empId);

if(empInfo.supervisor || empInfo.deptHead){
    document.getElementById("sup-nav").innerHTML = `<a class="nav-link" href="management.html">Management Menu</a>`;
    }
if(empInfo.department == 'benco'){
        document.getElementById("benco-nav").innerHTML = `<a class="nav-link" href="benco.html">BenCo Menu</a>`;
    }

document.getElementById('submitButton').addEventListener('click', addRequest);

async function addRequest() {

    let request = {

        reqBy: empId,
        startsOn: document.getElementById('startDate').value,
        startTime: document.getElementById('startTime').value,
        institution: document.getElementById('institution').value,
        instCity: document.getElementById('instCity').value,
        instState: document.getElementById('instState').value,
        eventDesc: document.getElementById('eventDesc').value,
        eventCost: document.getElementById('eventCost').value,
        timeMissed: document.getElementById('timeMissed').value,
        justification: document.getElementById('justification').value,
        eventType: document.getElementById('eventType').value,
        gradeFormat: document.getElementById('gradeFormat').value,
        minPass: document.getElementById('minPass').value,
        supPreApp: document.getElementById('supPreApp').value,
        spaAttString: document.getElementById('spaAtt').value,
        dhPreApp: document.getElementById('dhPreApp').value,
        dhpaAttString: document.getElementById('dhpaAtt').value,
        eventAttString: document.getElementById('eventAttString').value

    }

    console.log(JSON.stringify(request));

    let url ='submit';

    let res = await fetch(url, {
        method: 'POST',
        body: JSON.stringify(request),
        headers: {
            'Content-Type': 'application/json'
        }
    });
    
    let resJson = await res.text()
    .then((res) => {
        console.log(res);
    }).catch((error) => {
        console.log(error);
    });

    window.location.replace("http://localhost:8080/project1/view-my-requests.html");
}