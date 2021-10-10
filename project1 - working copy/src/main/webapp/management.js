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
    document.getElementById("sup-nav").innerHTML = `<a class="nav-link" href="management.html">Management Menu<span class="sr-only">(current)</span></a>`;
    }
if(empInfo.department == 'benco'){
        document.getElementById("benco-nav").innerHTML = `<a class="nav-link" href="benco.html">BenCo Menu</a>`;
    }

function pullRequests() {
    
    console.log('Hello from inside the pullRequests function');
    let empId = empInfo.empId;

    //let userInput = document.getElementById('dataInput').value;
    //console.log('User has input: ' + userInput);
    let baseUrl = 'management/'
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

        let dataSection = document.getElementById('Requests');
        //console.log(dataSection.value);
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
            noRecords.innerHTML = `<h4>You have no requests in your department requiring approval<h4>`;
            dataSection.appendChild(noRecords);
        }
    }
}

function populateData(resData) {

    console.log(resData);
    let dataSection = document.getElementById('Requests');

    // create a table to hold the returned reimbursement requests
    let table = document.createElement("table");
    let tr = table.insertRow(-1);

    // set up column names as an array
    let colNames = ['Request Number', 'Request Date', 'Start Date', 'Event Description', 'Status', 'Event Cost', 'Reimbursement Amount', 'Is Urgent?']

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
                    if(thisJson.bencoApp && !thisJson.gradePassed && !thisJson.presGrade && (thisJson.gradeAtt != 0 && thisJson.gradeAtt != 1)){
                        tcell.innerHTML = 'Pending Benco approval of grade.';
                    }
                    if(thisJson.bencoApp && !thisJson.gradePassed && thisJson.presGrade && (thisJson.gradeAtt == 0 || thisJson.gradeAtt == 1)){
                        tcell.innerHTML = 'Pending employee submission of presentation.';
                    }
                    if(thisJson.bencoApp && !thisJson.gradePassed && thisJson.presGrade && (thisJson.gradeAtt != 0 && thisJson.gradeAtt != 1)){
                        tcell.innerHTML = 'Presentation submitted pending managment review.';
                    }
                    if(thisJson.bencoUpgrade || thisJson.bencoDowngrade){
                        tcell.innerHTML = 'Amount adjusted. Awaiting Employee approval.';
                    }
                    if(thisJson.denied){
                        tcell.innerHTML = `Request denied. Reason given: ${thisJson.denReason}`;
                    }
                    if(thisJson.empReject){
                        tcell.innerHTML = `Cancelled by employee`;
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
                case 7:{
                    tcell.innerHTML = `${thisJson.urgent}`
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

    let decision = document.createElement("form");
    decision.innerHTML = `<p><hr>
                <br><b>
                You can select a request, by entering the Request Number below.  Use the dropdown menu to select an action, and click 'Process Request' to proceed.
                <br>
                If you reject the request, please provide a reason for the rejection.</b>
                <br><br><hr></p>
                    <div class="form-group mb-3">
                    <label for="reqNumber">Request Number:</label>
                    <input type="number" id="reqNumField" name="reqNumField" class="form-control" placeholder="Enter the Number of the request you wish to process" required>
                    </div>
                    <br>
                    <div class="input-group">
                    <label for="decision">Approve or Deny Request</label>
                    <select id="decision" required>
                    <option value="noAction">No Action at this time.</option>
                    <option value="true">Approve</option>
                    <option value="false">Reject (DENIES Request)</option>
                    <option value="info">Request Additional Information</option>
                    </select>
                    </div>
                    <br>
                    <div class="input-group">
                    <label for="presGrade">Grade Employee's Presentation</label>
                    <select id="grade-pres" required>
                    <option value="noAction">NO ACTION - use options below only if status shows waiting on presentation or grade</option>
                    <option value="true">Confirm Pass</option>
                    <option value="false">Fail (DENIES Request)</option>
                    </select>
                    </div>
                    <br>
                    <div class="form-group mb-3">
                    <label for="reqNumber">If you are denying the request, please provide a reason:</label>
                    <input type="text" id="denReason" name="denReason" class="form-control" placeholder="Reason for rejecting the request.">
                    <br>
                    <button type="button" id="decisionButton" class="btnstyle1">Process Request</button>
                    </div>`

    dataSection.appendChild(decision);

    document.getElementById('decisionButton').addEventListener('click', decide);

            async function decide(){
                console.log('Hello from inside the management decide() function')
                let reqNumber = document.getElementById('reqNumField').value;
                thisRequest = resData[reqNumber - 1];
                //console.log('resData: ' + resData)
                //console.log('this request supInfReq: ' + thisRequest.supInfReq);
                let gradeApproval = document.getElementById('grade-pres').value;
                if (gradeApproval == 'true'){

                    thisRequest.gradePassed = true;
                    thisRequest.presBy = empInfo.empId;

                    if(thisRequest.bencoApp){
                        thisRequest.reimburse = true;
                    }
                }
                if (gradeApproval == 'false'){

                    thisRequest.denied = true;
                    thisRequest.denBy = empInfo.empId;
                    thisRequest.denReason = "Presentation did not meet Management standards"

                }

                let decide = document.getElementById('decision').value;
                console.log(decide);


                if (decide == 'true' && !empInfo.deptHead){
                    
                    thisRequest.supApp = true;
                    
                }
                if(decide == 'true' && empInfo.deptHead){

                    thisRequest.supApp = true;
                    thisRequest.dhApp = true;

                }
                if (decide == 'false'){
                    //console.log('hello in there');
                    console.log(decide);
                    thisRequest.denied = true;
                    thisRequest.denReason = document.getElementById('denReason').value;
                    thisRequest.denBy = empInfo.empId;
                    console.log(thisRequest);

                }
                //console.log('is sup & is dept head: ' + empInfo.supervisor + ' ' + empInfo.deptHead);
                //console.log('what is empinfo.supervisor: ' + typeof empInfo.supervisor);
                //console.log(typeof decide);

                if(decide == 'info' && empInfo.supervisor){
                    console.log('hello from management supinfo if statement')
                    thisRequest.supInfReq = true;


                }
                if(decide == 'info' && empInfo.deptHead){
                    console.log('hello from management supinfo if statement')
                    thisRequest.dhInfReq = true;

                }
                console.log('Hello from just above management sendDecision() function')
                async function sendDecision(){

                    //console.log(`addSupInfo function called successfully`);
                    //console.log(document.getElementById('supInfField').value);
                    console.log('request in management sendDecision function');
                    console.log(thisRequest);
                    
                    let url ='http://localhost:8080/project1/management/decision';
                    console.log(url);
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
    
                }
                console.log('Hello from the bottom of the management page')
                sendDecision();
                window.location.reload(true);
            }
}