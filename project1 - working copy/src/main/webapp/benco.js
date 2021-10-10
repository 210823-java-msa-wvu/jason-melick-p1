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
    document.getElementById("benco-nav").innerHTML = `<a class="nav-link" href="benco.html">BenCo Menu<span class="sr-only">(current)</span></a>`;
}

//document.getElementById('reimburseButton').addEventListener('click', getReimbursements);

function getReimbursements(){

    console.log('Hello from inside the getReimbursements function');
    let empId = empInfo.empId;
    //let baseUrl = 'benco/reimbursements'
    let reUrl = 'benco/reimbursements'
    //console.log(url);
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = receiveData;
    xhttp.open('GET', reUrl, true); // true is the default argument - meaning that this is async
    xhttp.send(); // for get requests, the send function does not have any arguments

    function receiveData() {

        let dataSection = document.getElementById('Requests');
        dataSection.innerHTML = '';

        if (xhttp.readyState == 4 && xhttp.status == 200) {
            //console.log('Hello from inside the receiveData if Statement')
            let resp = xhttp.responseText;
            resp = JSON.parse(resp);
            //console.log(r);
            populateRecords(resp);
        }

        if(xhttp.readyState == 4 && xhttp.status == 204){

            //console.log(dataSection.value + `JS line 83`);
            let noRecords = document.createElement("p");
            noRecords.innerHTML = `<h4>There are no reimbursements currently in the system.<\h4>
                                    <br>
                                    <button type="button" onclick="pullRequests()" class="btnstyle1">View Requests</button>`;
            dataSection.appendChild(noRecords);
        }
    }
}

function pullRequests() {
    
    console.log('Hello from inside the pullRequests function');
    let empId = empInfo.empId;

    //let userInput = document.getElementById('dataInput').value;
    //console.log('User has input: ' + userInput);
    let baseUrl = 'benco/'
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
            noRecords.innerHTML = `<h4>There are no requests requiring approval or grade confirmation at this time.<\h4>
                                    <br>
                                    <button type="button" id="reimburseButton" onclick="getReimbursements()" class="btnstyle1">View Reimbursements</button>`;
            dataSection.appendChild(noRecords);
        }
    }
}

function populateRecords(resData) {

    console.log(resData);
    let dataSection = document.getElementById('Requests');

    // create a table to hold the returned reimbursement requests
    let reTable = document.createElement("table");
    let tr = reTable.insertRow(-1);

    // set up column names as an array
    let colNames = ['Req #', 'Employee', 'Start Date', 'Event Type', 'Event Desc', 'Institution', 'Event Cost', 'Amount', 'Amt Upgraded?', 'Reason']

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

        let tr = reTable.insertRow(-1);

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
                    tcell.innerHTML = thisJson.reqBy;
                    break;
                }
                case 2:{
                    tcell.innerHTML = thisJson.startsOn;
                    break;
                }
                case 3:{
                    tcell.innerHTML = thisJson.eventType;
                    break;
                }             
                case 4:{
                    tcell.innerHTML = thisJson.eventDesc;
                    break;
                }
                case 5:{
                    tcell.innerHTML = thisJson.institution;
                    break;
                }
                case 6:{
                    tcell.innerHTML = `$ ${thisJson.eventCost}`
                    break;
                }
                case 7:{
                    tcell.innerHTML = `$ ${thisJson.finalAmt}`
                    break;
                }
                case 8:{
                    tcell.innerHTML = `${thisJson.bencoUpgrade}`
                    break;
                }
                case 9:{
                    
                    tcell.innerHTML = `${thisJson.whyBencoUpgrade}`
                    break;
                }
                default:{
                    tcell.innerHTML = `A problem occurred while retrieving reimbursements. Please refresh your browser to try again.`
                }
            }
        }
    }
    //console.log(table);
    dataSection.appendChild(reTable);
}
function populateData(resData) {

    console.log(resData);
    let dataSection = document.getElementById('Requests');

    // create a table to hold the returned reimbursement requests
    let table = document.createElement("table");
    let tr = table.insertRow(-1);

    // set up column names as an array
    let colNames = ['Req #', 'Req Date', 'Start Date', 'Event Type', 'Description', 'Status', 'Event Cost', 'Amount', 'Urgent?', 'Grade Type', 'Min Pass', 'Grade']

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
                    tcell.innerHTML = thisJson.eventType;
                    break;
                }             
                case 4:{
                    tcell.innerHTML = thisJson.eventDesc;
                    break;
                }
                case 5:{
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
                    if((thisJson.bencoUpgrade || thisJson.bencoDowngrade) && !thisJson.bencoApp){
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
                case 6:{
                    tcell.innerHTML = `$ ${thisJson.eventCost}`
                    break;
                }
                case 7:{
                    tcell.innerHTML = `$ ${thisJson.finalAmt}`
                    break;
                }
                case 8:{
                    tcell.innerHTML = `${thisJson.urgent}`
                    break;
                }
                case 9:{
                    
                    tcell.innerHTML = `${thisJson.gradeFormat}`
                    break;
                }
                case 10:{
                    tcell.innerHTML = `${thisJson.minPass}`
                    break;
                }
                case 11:{
                    tcell.innerHTML = `${thisJson.gradeString}`
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
    console.log('benco display requests working to line 353 benco.js')
    let decision = document.createElement("form");
    decision.innerHTML = `<p>
                <br>
                <hr>
                <br>
                You can select a request, by entering the Request Number below.
                <br>
                Use the dropdown menu to select an action, and click 'Process Request' to proceed.
                <br>
                If you reject the request, please provide a reason for the rejection.
                <br><br><hr></p>
                    <div class = col-md-6>
                    <div class="form-group mb-3">
                    <label for="reqNumber">Request Number:</label>
                    <input type="number" id="reqNumField" name="reqNumField" class="form-control" placeholder="Enter Req #:" required>
                    </div>
                    <br>
                    <div class="form-group mb-3">
                    <label for="reqNumber">Adjust Amount of Reimbursement:</label>
                    <input type="number" id="finalAmtField" name="finalAmt" class="form-control"  required>
                    </div>
                    <br>
                    <div class="input-group">
                    <label for="decision">Approve or Deny Request</label>
                    <select id="decision" required>
                    <option value ="na">N/A</option>
                    <option value="true">Approve</option>
                    <option value="false">Reject (DENIES Request)</option>
                    <option value="info">Request Additional Information</option>
                    </select>
                    </div>
                    <br>
                    <div class="input-group">
                    <label for="presGrade">Confirm employee's passing grade</label>
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
                    <hr>
                    <button type="button" id="reimburseButton" onclick="getReimbursements()" class="btnstyle1">View Reimbursements</button>
                    </div>`

    dataSection.appendChild(decision);

    document.getElementById('decisionButton').addEventListener('click', decide);

            async function decide(){

                let reqNumber = document.getElementById('reqNumField').value;
                thisRequest = resData[reqNumber - 1];
                //let amt = document.getElementById('finalAmtField').value;
                //console.log(amt);
                //console.log('this request supInfReq: ' + thisRequest.supInfReq);
                let gradeApproval = document.getElementById('grade-pres').value;
                if (gradeApproval == 'true'){

                    //console.log('Test gradePassed exists' + thisRequest.gradePassed);
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

                //console.log(document.getElementById('finalAmtField'));

                let decide = document.getElementById('decision').value;
                let adjAmt = document.getElementById('finalAmtField').value;
                //console.log('Adjusted amount: ' + adjAmt);
                if(adjAmt != null && adjAmt != ''){
                    prevAmt = thisRequest.finalAmt;
                    newAmt = adjAmt;
                    if(newAmt > adjAmt){
                        thisRequest.bencoUpgrade = true;
                        thisRequest.whyBencoUpgrade = 'Exceeding available funds for reporting purposes.';
                    }else{
                        newAmt = adjAmt;
                        thisRequest.bencoDowngrade = true;
                        thisRequest.whyBencoUpgrade = 'Downgraded by BenCo.';
                    }
                }

                if (decide == 'true'){
                    
                    thisRequest.supApp = true;
                    thisRequest.dhApp = true;
                    thisRequest.bencoApp = true;   
                    thisRequest.bencoAppBy = empInfo.empId;
                }

                if (decide == 'false'){
                    //console.log('hello in there');
                    console.log(decide);
                    thisRequest.denied = true;
                    thisRequest.denReason = document.getElementById('denReason').value;

                    thisRequest.denBy = empInfo.empId;
                    console.log(thisRequest);

                }
                if(decide == 'info' && empInfo.supervisor){

                    thisRequest.supInfReq = true;

                }
                if(decide == 'info' && empInfo.deptHead){

                    thisRequest.dhInfReq = true;

                }

                async function sendDecision(){

                    console.log(`benco sendDecision function called successfully`);
                    //console.log(document.getElementById('supInfField').value);

                    //console.log(thisRequest);
                    
                    let url ='http://localhost:8080/project1/benco/decision';
                    console.log(url);
                    let res = await fetch(url, {
                        method: 'POST',
                        body: JSON.stringify(thisRequest),
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    });
                    location.reload();
                    let resJson = await res.text
                    .then((res) => {
                        console.log(res);
                    }).catch((error) => {
                        console.log(error);
                    });
                    
                }
                sendDecision();
                window.location.reload(true);
    }
}
    


