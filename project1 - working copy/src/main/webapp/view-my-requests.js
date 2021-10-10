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
//console.log(empInfo);

if(empInfo.supervisor || empInfo.deptHead){
    document.getElementById("sup-nav").innerHTML = `<a class="nav-link" href="management.html">Management Menu</a>`;
    }
if(empInfo.department == 'benco'){
        document.getElementById("benco-nav").innerHTML = `<a class="nav-link" href="benco.html">BenCo Menu</a>`;
    }

function pullRequests() {
    
    //console.log('Hello from inside the pullRequests function');
    let empId = empInfo.empId;

    //let userInput = document.getElementById('dataInput').value;
    //console.log('User has input: ' + userInput);
    let baseUrl = 'view-my-requests/'
    let url = baseUrl + empId;
    console.log(url);

    // 4 Steps to building an AJAX call

    // STEP 1: Create our XMLHttpRequest Object
    let xhttp = new XMLHttpRequest();

    // STEP 2: Set a callback function for the readystatechange event
    xhttp.onreadystatechange = receiveData;

    // STEP 3: Open the request
    xhttp.open('GET', url, true); // true is the default argument - meaning that this is async

    // STEP 4: Send the request
    xhttp.send(); // for get requests, the send function does not have any arguments

    function receiveData() {

        let dataSection = document.getElementById('myRequests');
        dataSection.innerHTML = '';

        // Check if the readyState is 'DONE' aka '4' and if the HTTP Status is 'ok' 200
        if (xhttp.readyState == 4 && xhttp.status == 200) {
            //console.log('Hello from inside the receiveData if Statement')
            let resp = xhttp.responseText;
            

            resp = JSON.parse(resp);
            //console.log(r);
            populateData(resp);
        }

        if(xhttp.readyState == 4 && xhttp.status == 204){

            //console.log(dataSection.value + `JS line 83`);
            let noRecords = document.createElement("p");
            noRecords.innerHTML = `<h4>You have no requests to view<h4>`;
            dataSection.appendChild(noRecords);
        }
    }
}

function populateData(resData) {

    console.log(resData);
    let dataSection = document.getElementById('myRequests');

    // create a table to hold the returned reimbursement requests
    let table = document.createElement("table");
    let tr = table.insertRow(-1);

    // set up column names as an array
    let colNames = ['Request Number', 'Request Date', 'Start Date', 'Event Description', 'Status', 'Event Cost', 'Reimbursement Amount']

    // loop through the column names and create a new table header for each name
    for(let i = 0; i < colNames.length; i++){

        //let tr = table.insertRow(-1);
        let th = document.createElement("th");
        th.innerHTML = colNames[i];
        tr.appendChild(th);
        //console.log(tr);
    }
    //console.log(table);
    
    // loop through the JSON values and create a row with the correct data
    for(let i = 0; i < resData.length ; i ++){

        let thisJson = resData[i];
        console.log(thisJson);

        let tr = table.insertRow(-1);

        for(let j = 0; j < colNames.length ; j++){

            let tcell = tr.insertCell(-1);

            switch(j){
                case 0:{                   
                    let reqNumber = i + 1;
                    tcell.innerHTML = reqNumber;
                    //console.log('hello from inside the view-my-requests table switch/case')
                    break;
                }
                case 1:{
                    tcell.innerHTML = thisJson.reqDate;
                    break;
                }
                case 2:{
                    tcell.innerHTML = thisJson.startsOn;
                    break;
                }
                case 3:{
                    tcell.innerHTML = thisJson.eventDesc;
                    break;
                }
                case 4:{
                    if(!thisJson.supApp){
                        if(thisJson.supInfReq && (thisJson.supInfAtt < 2 || thisJson.supInfAtt == null)){
                            tcell.innerHTML = `Sup requested add'l info.`;                  
                        }else{
                        tcell.innerHTML = `Awaiting Sup approval.`;
                        }
                    }
                    if(thisJson.supApp && !thisJson.dhApp){
                        if(thisJson.dhInfReq && (thisJson.dhInfAtt < 2 || thisJson.dhInfAtt == null)){
                            tcell.innerHTML = `Dept Head requested add'l info.`;                        
                        }else{
                        tcell.innerHTML = `Awaiting Dept Head's approval.`;
                        }
                    }
                    if(thisJson.dhApp && !thisJson.bencoApp){
                        if(thisJson.bencoInfReq && (thisJson.bencoInfAtt < 2 || thisJson.bencoInfAtt == null)){
                            tcell.innerHTML = `BenCo requested add'l info.`;                        
                        }else{
                        tcell.innerHTML = `Awaiting BenCo approval.`;
                        }
                    }
                    if(thisJson.bencoApp && !thisJson.gradePassed && !thisJson.presGrade && (thisJson.gradeAtt == 0 || thisJson.gradeAtt == 1)){
                        tcell.innerHTML = 'Pending submission of passing grade.';
                    }
                    if(thisJson.bencoApp && !thisJson.reimburse && !thisJson.presGrade && (thisJson.gradeAtt != 0 && thisJson.gradeAtt != 1)){
                        tcell.innerHTML = 'Pending Benco approval of grade.';
                    }
                    if(thisJson.bencoApp && !thisJson.gradePassed && thisJson.presGrade && (thisJson.gradeAtt == 0 || thisJson.gradeAtt == 1)){
                        tcell.innerHTML = 'Pending employee submission of presentation.';
                    }
                    if(thisJson.bencoApp && !thisJson.reimburse && thisJson.presGrade && (thisJson.gradeAtt != 0 && thisJson.gradeAtt != 1)){
                        tcell.innerHTML = 'Presentation submitted pending managment review.';
                    }
                    if((thisJson.bencoUpgrade || thisJson.bencoDowngrade) && !thisJson.bencoApp){
                        tcell.innerHTML = 'Amount adjusted. Awaiting Employee approval.';
                    }
                    if(thisJson.denied){
                        tcell.innerHTML = `Request denied. Reason given: ${thisJson.denReason}`;
                    }
                    if(thisJson.empReject){
                        tcell.innerHTML = `Cancelled by employee`;
                    }
                    if(thisJson.reimburse){
                        tcell.innerHTML = `Submitted to HR for payroll reimbursement`
                    }
                    break;
                }
                case 5:{
                    tcell.innerHTML = `$ ${thisJson.eventCost}`
                    break;
                }
                case 6:{
                    tcell.innerHTML = `$ ${thisJson.finalAmt}`
                    break;
                }
                default:{
                    tcell.innerHTML = `A problem occurred while retrieving your requests. Please refresh your browser to try again.`
                }
            }
        }
    }
    //console.log(table);
    dataSection.appendChild(table);

    let enterReqId = document.createElement("form");
    enterReqId.innerHTML = `<div class="form-group">
        <label for="reqNumber">Request Number</label>
        <input type="number" id="reqNumField" name="reqNumField" class="form-control" placeholder="Enter the Number of a request to view details" required>
        </div> 
        <button type="button" id="viewReqButton" class="btnstyle1">View Details</button>`
    dataSection.appendChild(enterReqId);

    document.getElementById('viewReqButton').addEventListener('click', getThisRequest);

    function getThisRequest(){

        let reqNumber = document.getElementById('reqNumField').value;
        thisRequest = resData[reqNumber - 1];
        //console.log('resData: ' + resData)
        //console.log('this request supInfReq: ' + thisRequest.supInfReq);
        dataSection.removeChild(table);
        dataSection.removeChild(enterReqId);

        let para = dataSection.appendChild(document.createElement('p'));
        //console.log('sup inf requested? ' + thisRequest.supInfReq);
        //console.log('thisRequest.supInfAtt: ' + thisRequest.supInfAtt + ' ' + typeof thisRequest.supInfAtt);
        if (thisRequest.supInfReq && (thisRequest.supInfAtt == null || thisRequest.supInfAtt == 0)){

            para.innerHTML = `<p>Your supervisor has requested additional information before approval please input below and send</p>
                            <form>
                            <div class="input-group mb-3">
                            <span class="input-group-text">Additional Info:</span>
                            <input id="supInfField" type="text" class="form-control" required>
                            </div>
                            <button type="button" id="subSupInf" class="btnstyle1">Submit Info</button>
                            </form>`
                            
                             
            document.getElementById('subSupInf').addEventListener('click', addSupInfo);

            async function addSupInfo(){

                //console.log(`addSupInfo function called successfully`);
                //console.log(document.getElementById('supInfField').value);
                thisRequest.supInfString = document.getElementById('supInfField').value;
                
                //console.log(thisRequest)
                
                let url ='http://localhost:8080/project1/view-my-requests/add-sup-inf';

                let res = await fetch(url, {
                    method: 'POST',
                    body: JSON.stringify(thisRequest),
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
                location.reload(true);
            }

        } else if (thisRequest.dhInfReq && (thisRequest.dhInfAtt == null || thisRequest.dhInfAtt == 0)){

            para.innerHTML = `<p>Your department head has requested additional information before approval please input below and send</p>
                            <form>
                            <div class="input-group mb-3">
                            <span class="input-group-text">Additional Info:</span>
                            <input id="dhInfField" type="text" class="form-control" required>
                            </div>
                            <button type="button" id="subDhInf" class="btnstyle1">Submit Info</button>
                            </form>`
                             
            document.getElementById('subDhInf').addEventListener('click', addDhInfo);

            async function addDhInfo(){

                //console.log(`addSupInfo function called successfully`);
                //console.log(document.getElementById('supInfField').value);
                thisRequest.dhInfString = document.getElementById('dhInfField').value;
                
                console.log(thisRequest)
                
                let url ='http://localhost:8080/project1/view-my-requests/add-dh-inf';

                let res = await fetch(url, {
                    method: 'POST',
                    body: JSON.stringify(thisRequest),
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
                location.reload(true);
            }

        } else if (thisRequest.bencoInfReq && (thisRequest.bencoInfAttString == null || thisRequest.bencoInfAttString== 0)){

            para.innerHTML = `<p>Your department head has requested additional information before approval please input below and send</p>
                            <form>
                            <div class="input-group mb-3">
                            <span class="input-group-text">Additional Info:</span>
                            <input id="bencoInfField" type="text" class="form-control" required>
                            </div>
                            <button type="button" id="subBencoInf" class="btnstyle1">Submit Info</button>
                            </form>`
                             
            document.getElementById('subBencoInf').addEventListener('click', addBencoInf);

            async function addBencoInf(){

                console.log(`addBencoInf function called successfully`)
               
                thisRequest.bencoInfString = document.getElementById('bencoInfField').value;
                
                let url ='http://localhost:8080/project1/view-my-requests/add-benco-inf';

                let res = await fetch(url, {
                    method: 'POST',
                    body: JSON.stringify(thisRequest),
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
                let resJson = await res.json()
                .then((res) => {
                    console.log(res);
                }).catch((error) => {
                    console.log(error);
                });
                location.reload(true);
            }

        } else if (thisRequest.bencoApp && !thisRequest.gradeSubmitted) {

            para.innerHTML = `<p>Please submit your final grade or presentation below</p>
                            <form>
                            <div class="input-group mb-3">
                            <span class="input-group-text">Grade or Presentation:</span>
                            <input id="gradeField" type="text" class="form-control" required>
                            </div>
                            <button type="button" id="subGrade" class="btnstyle1">Submit Info</button>
                            </form>`
                             
            document.getElementById('subGrade').addEventListener('click', addGrade);

            async function addGrade(){

                console.log(`subGrade function called successfully`)

                thisRequest.gradeString = document.getElementById('gradeField').value;
                console.log(thisRequest);
                
                let url ='http://localhost:8080/project1/view-my-requests/grade-sub';

                let res = await fetch(url, {
                    method: 'POST',
                    body: JSON.stringify(thisRequest),
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
                let resText = await res.json()
                .then((res) => {
                    console.log(res);
                }).catch((error) => {
                    console.log(error);
                });
                location.reload(true);
            }

        } else if (thisRequest.bencoUpgrade || thisRequest.bencoDowngrade){

            para.innerHTML = `<p>Your Benefits Coordinator has changed the amount of the reimbursement.
                            <br>
                            You may reject the new amount. If you deny, your request will be cancelled. How do you wish to proceed?</p>
                            <div class="input-group mb-3">
                            <span class="input-group-text">Accept or Deny</span>
                            <select id="empChoice" required>
                            <option value="false">Accept New Amount</option>
                            <option value="true">Reject Adjusted Amount (Cancels Request)</option>
                            </select>
                            <button type="button" id="subEmpChoice" class="btnstyle1">Submit Info</button>
                            </div>`

            document.getElementById('subEmpChoice').addEventListener('click', empReject);

            async function empReject(){

                console.log(`empReject function called successfully`)

                let empValue = thisRequest.gradeString = document.getElementById('subEmpChoice').value;

                if (empValue == 'true'){

                    thisRequest.denied = true;
                    thisRequest.denBy = thisRequest.reqBy;
                    thisRequest.denReason = 'Adjusted Amount Rejected by Employee'

                } else {
                    
                    thisRequest.bencoApp = true;
                    thisRequest.reimburse = true;

                }
                
                let url ='http://localhost:8080/project1/view-my-requests/emp-reject';

                let res = await fetch(url, {
                    method: 'POST',
                    body: JSON.stringify(thisRequest),
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
                let resJson = await res.json()
                .then((res) => {
                    console.log(res);
                }).catch((error) => {
                    console.log(error);
                });
                location.reload(true);
            }
        } else {

            para.innerHTML = `<p>No action required on your part at this time. Use the button below to return to viewing requests.
                            <br>
                            <button type="button" id="viewRequests" onclick="pullRequests()" class="btnstyle1">All Requests</button>
                            </p>`

        }
    }
    //document.getElementById('viewReq').addEventListener("click", getThisRequest)
}




