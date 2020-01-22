function qryzServer(){
  return 'https://qeryz.com';
}

function qryzVersion(){
  return 'qeryz_v3.2.php';
}

function qryzInit(url) {
    var url = url + "&qryz_vtd_s="+qryzGetVtdS()+"&qryz_ref='" + document.referrer + "'";
    var ajx;
    if (window.XMLHttpRequest) {
        ajx = new XMLHttpRequest();
    }
    else {
        ajx = new ActiveXObject("Microsoft.XMLHTTP");
    }
    ajx.onreadystatechange = function () {
      if (ajx.readyState == 4 && ajx.status == 200) {
        var s = document.getElementById("qryz_plks");
        s.innerHTML = ajx.responseText;
        var arr = s.getElementsByTagName('script');
        for (var i = 0; i < arr.length; i++) {
            eval(arr[i].innerHTML);
        }
      }
    }
    ajx.open("POST", url, true);
    ajx.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    ajx.send();
}

function qryzInit2(uid){
  var url, p1, p2, apiExists = false, reload = true;
  for (var z = 0; z < qRz.length; z++){
    if (qRz[z].indexOf('showSurvey') > -1 && (qryzReadCookie('qryz_Vtd'+qRz[z][1]) == null || document.cookie.indexOf(qryzReadCookie('qryz_Vtd'+qRz[z][1])) < 0)){
      p1 = 'showSurvey';
      p2 = qRz[z][1];
      //if cookie qryz_Etapi exists, means api3 is active
      if (qryzReadCookie('qryz_Etapi') != null)
          reload = false;
      if (qRz[z][2] == true) {
        qryzCreateCookie('qryz_Etapi', 'active:'+p2, 1);
      }
      qryzCreateCookie('qryz_Api', p1+':'+p2, 1);
      apiExists = true;
      break;
    }
    else if (qRz[z].indexOf('tag') > -1 && (qryzReadCookie('qryz_Vtd'+qRz[z][1]) == null || document.cookie.indexOf(qryzReadCookie('qryz_Vtd'+qRz[z][1])) < 0)){
      p1 = 'tag';
      p2 = qRz[z][1];
      qryzCreateCookie('qryz_Api', p1+':'+p2, 1);
      apiExists = true;
      break;
    }
  }
  /*this line of code will stop a survey from reloading
  when event was already triggered and survey is already shown*/
  if (reload === false)
      return;
  if (!apiExists){
      qryzCreateCookie('qryz_Api', 'x:noapi', 1);
      qryzEraseCookie('qryz_Etapi');
  }
  if (qryzReadCookie('qryz_Api') && qryzReadCookie('qryz_Api') != 'x:noapi'){
      url = qryzServer()+"/survey/"+qryzVersion()+"?qryz_uid="+uid+"&qryz_url="+encodeURIComponent(document.URL)+"&qryz_vtd_s="+qryzGetVtdS()+"&qryz_ref='"+document.referrer+"'"+"&qryz_api="+p1+':'+p2;
  }
  else{
      url = qryzServer()+"/survey/"+qryzVersion()+"?qryz_uid="+uid+"&qryz_url="+encodeURIComponent(document.URL)+"&qryz_vtd_s="+qryzGetVtdS()+"&qryz_ref='"+document.referrer+"'";
  }
  var ajx;
  if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
      ajx = new XMLHttpRequest();
  }
  else {// code for IE6, IE5
      ajx = new ActiveXObject("Microsoft.XMLHTTP");
  }
  ajx.onreadystatechange=function() {
      if (ajx.readyState == 4 && ajx.status == 200) {
          var s = document.getElementById("qryz_plks");
          s.innerHTML = ajx.responseText;
          var arr = s.getElementsByTagName('script');
          for (var i = 0; i < arr.length; i++) {
              eval(arr[i].innerHTML);
          }
      }
  }
  ajx.open("POST", url, true);
  ajx.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
  ajx.send();
}

function qryzRecordViews(sid, ref, uid, qid) {
    var url = qryzServer()+"/survey/save_v3.php?qryz_sid=" + sid + "&qryz_ref='" + ref + "'&qryz_uid=" + uid + "&qryz_qstn=" + qid;
    var ajx;
    if (window.XMLHttpRequest) {
        ajx = new XMLHttpRequest();
    }
    else {
        ajx = new ActiveXObject("Microsoft.XMLHTTP");
    }
    ajx.open("POST", url, true);
    ajx.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    ajx.send();
}

function qryzSerialize(qryz_form) {
    if (!qryz_form || qryz_form.nodeName !== "FORM") {
        return;
    }
    var qryz_i, qryz_j, qryz_q = [];
    var elementName;
    for (qryz_i = 0; qryz_i < qryz_form.elements.length; qryz_i++) {
        elementName = qryz_form.elements[qryz_i].name;
        if (elementName === "") {
            continue;
        }
        switch (qryz_form.elements[qryz_i].nodeName) {
        case 'INPUT':
            switch (qryz_form.elements[qryz_i].type) {
            case 'text':
                qryz_q.push(elementName + "=" + encodeURIComponent(qryz_form.elements[qryz_i].value));
                break;
            case 'hidden':
                qryz_q.push(elementName + "=" + encodeURIComponent(qryz_form.elements[qryz_i].value));
            case 'password':
            case 'button':
            case 'reset':
            case 'submit':
                qryz_q.push(elementName + "=" + encodeURIComponent(qryz_form.elements[qryz_i].value));
                break;
            case 'checkbox':
            case 'radio':
                if (qryz_form.elements[qryz_i].checked) {
                    qryz_q.push(elementName + "=" + encodeURIComponent(qryz_form.elements[qryz_i].value));
                }
                break;
            }
            break;
        case 'TEXTAREA':
            qryz_q.push(elementName + "=" + encodeURIComponent(qryz_form.elements[qryz_i].value));
            break;
        case 'SELECT':
            switch (qryz_form.elements[qryz_i].type) {
            case 'select-one':
                qryz_q.push(elementName + "=" + encodeURIComponent(qryz_form.elements[qryz_i].value));
                break;
            case 'select-multiple':
                for (qryz_j = qryz_form.elements[qryz_i].options.length - 1; qryz_j >= 0; qryz_j = qryz_j - 1) {
                    if (qryz_form.elements[qryz_i].options[qryz_j].selected) {
                        qryz_q.push(elementName + "=" + encodeURIComponent(qryz_form.elements[qryz_i].options[qryz_j].value));
                    }
                }
                break;
            }
            break;
        case 'BUTTON':
            switch (qryz_form.elements[qryz_i].type) {
            case 'reset':
            case 'submit':
            case 'button':
                qryz_q.push(elementName + "=" + encodeURIComponent(qryz_form.elements[qryz_i].value));
            }
            break;
        }
    }
    return qryz_q.join("&");
}

function qryzSubmit(qryz_uid, qryz_qtype, qryz_qdisplay, qryz_sid, qryz_shw_aftr, qryz_srvr_name, qryz_qq, qryz_v_info, qryz_step_no) {
    if (qryzVoted(qryz_qtype)) {
        if (qryz_qdisplay == 'until_response')
            qryzCreateCookie("qryz_Rps" + qryz_sid, "y", qryz_shw_aftr);
    }

    var qryzMainUrl = qryzServer()+"/survey/"+qryzVersion();
    var qryzUrl, qryzDataString, qryzNonFormDataString;
    var qryzIdentity = qryzReadCookie('qryz_Guest');

    if (qryzReadCookie('qryz_Api') && qryzReadCookie('qryz_Api') != 'x:noapi'){
        qryzNonFormDataString = '&qryz_uid=' + qryz_uid + '&qryz_url=' + encodeURIComponent(document.URL) + '&qryz_v_info=' + qryzIdentity + '&qryz_step=' + qryz_step_no + '&qryz_api=' + qryzReadCookie('qryz_Api');
    }
    else{
        qryzNonFormDataString = '&qryz_uid=' + qryz_uid + '&qryz_url=' + encodeURIComponent(document.URL) + '&qryz_v_info=' + qryzIdentity + '&qryz_step=' + qryz_step_no;
    }

    if (qryz_qq == '') {
        if (!qryzVoted(qryz_qtype))
            return false;
        qryzFormDataString = qryzSerialize(document.getElementById('qryz_frm'));
    } else {
        qryzFormDataString = 'qryz_qq=' + qryz_qq;
    }
    qryzUrl = qryzMainUrl + '?' + qryzFormDataString;
    qryzInit(qryzUrl + qryzNonFormDataString);
}

function qryzCreateCookie(qryz_name, qryz_value, qryz_days) {
    var qryz_expires = "";
    var qryz_path = "";
    if (qryz_days == 0) qryz_days++;
    if (qryz_days) {
        var date = new Date();
        date.setTime(date.getTime() + (qryz_days * 24 * 60 * 60 * 1000));
        qryz_expires = "; expires=" + date.toGMTString();
        var qryz_stype = qryzReadCookie('qryz_Stv');
        if (qryz_stype != null || qryz_stype != "")
            qryz_path = "/";
        else
            qryz_path = document.URL;
    }
    document.cookie = qryz_name + "=" + qryz_value + qryz_expires + "; path=" + qryz_path;
}

function qryzCreateCookieNopath(qryz_name, qryz_value, qryz_days) {
    var qryz_expires = "";
    var qryz_path = "";
    if (qryz_days == 0) qryz_days++;
    if (qryz_days) {
        var date = new Date();
        date.setTime(date.getTime() + (qryz_days * 24 * 60 * 60 * 1000));
        qryz_expires = "; expires=" + date.toGMTString();
    }
    document.cookie = qryz_name + "=" + qryz_value + qryz_expires + "; path=/";
}

function qryzReadCookie(qryz_name) {
    var nameEQ = qryz_name + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
}

function qryzGetVtdS() {
    var  vtdS = "";
    var j = 0;
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') c = c.substring(1, c.length);
        if (c.indexOf("qryz_Vtd") == 0){
          j = c.indexOf("=");
          vtdS += c.substring(j+1, c.length) + ".";
        }
    }
    return vtdS;
}

function qryzEraseCookie(qryz_name) {
    qryzCreateCookie(qryz_name, "", -1);
}

function qryzUnloadSurvey(qryz_survey_id, qryz_optns_display, qryz_question_id, qryz_sa, qryz_xpops) {
    try{
        var qryzVtdCkiStr =  qryz_survey_id;
        if (qryzReadCookie('qryz_Api')){
            var api = qryzReadCookie('qryz_Api');
            var arr = api.split(':');
            if (arr[0] == 'tag')
                qryzVtdCkiStr = arr[1];
        }
        var qryz_voted_cki = qryzReadCookie("qryz_Vtd" + qryzVtdCkiStr);
        if (qryz_voted_cki != null)
            return;// finish this function if survey voted cookie exists
        var qryz_prev_page = qryzReadCookie("qryz_Ppg" + qryz_survey_id);
        var qryz_next_page = document.URL;
        var arrPop = new Array();
        arrPop = qryz_xpops.split("-");
        var qryz_xpop = qryzReadCookie("qryz_XPop" + qryz_survey_id);
        var qryz_fqs = null;
        qryz_fqs = qryzReadCookie("qryz_Fqs" + qryz_survey_id);
        if (qryz_optns_display == "one_shot") {
            if (qryz_fqs != null && qryz_voted_cki == null) {
                if (qryzIsPageReloaded(qryz_prev_page) && qryzIsFirstQuestionShown(qryz_fqs, qryz_question_id)) {
                    if (qryz_sa == 0)
                        qryzCreateCookie("qryz_Vtd" + qryz_survey_id, qryz_survey_id, 500000);
                    else
                        qryzCreateCookie("qryz_Vtd" + qryz_survey_id, qryz_survey_id, qryz_sa);
                }
            }
            if (qryz_xpop != null && arrPop[0] == '0') {
                qryzEraseCookie("qryz_XPop" + qryz_survey_id);
            }
        }
        else if (qryz_optns_display == "until_response") {
            var qryz_response = qryzReadCookie("qryz_Rps" + qryz_survey_id);
            if (qryz_fqs != null && qryz_voted_cki == null) {
                if (qryzIsQuestionAnswered(qryz_response) && qryzIsPageReloaded(qryz_prev_page) && qryzIsFirstQuestionShown(qryz_fqs, qryz_question_id)) {
                    if (qryz_sa == 0)
                        qryzCreateCookie("qryz_Vtd" + qryz_survey_id, qryz_survey_id, 500000);
                    else
                        qryzCreateCookie("qryz_Vtd" + qryz_survey_id, qryz_survey_id, qryz_sa);
                }
            }
            if (qryz_xpop != null && arrPop[0] == '0') {
                qryzEraseCookie("qryz_XPop" + qryz_survey_id);
            }
        }
        else if (qryz_optns_display == "persistent") {
            if (qryz_xpop != null && arrPop[0] == '0') {
                qryzEraseCookie("qryz_XPop" + qryz_survey_id);
            }
        }
    }
    catch(err){
        console.log('Qeryz error: '+err.message);
    }
}

function qryzIsFirstQuestionShown(first_question, current_question) {
    if (first_question == current_question) {
        return true;
    } else {
        return false;
    }
}

function qryzIsQuestionAnswered(response_code) {
    if (response_code == 'y') {
        return true;
    } else {
        return false;
    }
}

function qryzIsPageReloaded(prev_page) {
    if (prev_page != null) {
        return true;
    } else {
        return false;
    }
}

function qryzCreateCookieSlide(qryz_name, qryz_value, qryz_days) {
    var qryz_expires = "";
    var qryz_path = "";
    if (qryz_days == 0) qryz_days++;
    if (qryz_days) {
        var date = new Date();
        date.setTime(date.getTime() + (qryz_days * 24 * 60 * 60 * 1000));
        qryz_expires = "; expires=" + date.toGMTString();
        var qryz_stype = qryzReadCookie('qryz_Stv');
        if (qryz_stype != null || qryz_stype != "")
            qryz_path = "/";
        else
            qryz_path = document.URL;
    }
    document.cookie = qryz_name + "=" + qryz_value + qryz_expires + "; path=" + qryz_path;
}

function qryzSlide(qryz_slider_cookie, qryz_show_after) {
    var qryz_sToggle = qryzReadCookie(qryz_slider_cookie);
    if (qryz_sToggle == 'hide') {
        qryzCreateCookieSlide(qryz_slider_cookie, "show", qryz_show_after);
        document.getElementById("qryz_cdst").style.display = "block";
        document.getElementById("qryz_biun").innerHTML = '-';
        document.getElementById("qryz_minmsg").style.display = 'none';
    } else {
        qryzCreateCookieSlide(qryz_slider_cookie, "hide", qryz_show_after);
        document.getElementById("qryz_cdst").style.display = "none";
        document.getElementById("qryz_biun").innerHTML = '+';
        document.getElementById("qryz_minmsg").style.display = 'block';
        qryzRemoveOverlay('qryz_overlay');
    }
}

function qryzShowRdoComment() {
    var qryz_x = document.getElementById("qryz_frm");
    for (var qryz_i = 0; qryz_i < qryz_x.length; qryz_i++) {
        if (qryz_x.elements[qryz_i].type == 'radio') {
            var qryz_pollRadio = qryz_x.elements[qryz_i].value;
            var qryz_pollRadioComment = qryz_pollRadio.split(",", 2).pop(1);
            if (qryz_x.elements[qryz_i].checked) {
                if (qryz_pollRadioComment == "small") {
                    qryz_x.elements[qryz_i + 1].style.display = "inline";
                    qryz_x.elements[qryz_i + 1].focus();
                } else if (qryz_pollRadioComment == "large") {
                    qryz_x.elements[qryz_i + 2].style.display = "inline";
                    qryz_x.elements[qryz_i + 2].focus();
                }
            } else {
                qryz_x.elements[qryz_i + 1].style.display = "none";
                qryz_x.elements[qryz_i + 2].style.display = "none";
            }
            qryz_x.elements[qryz_i + 1].style.font = "12px arial, sans-serif";
            qryz_x.elements[qryz_i + 1].style.padding = "2px 4px 2px 4px";
            qryz_x.elements[qryz_i + 1].style.color = "#000000";
            qryz_x.elements[qryz_i + 1].style.border = "2px solid #000000";
            qryz_x.elements[qryz_i + 2].style.font = "12px arial, sans-serif";
            qryz_x.elements[qryz_i + 2].style.padding = "2px 4px 2px 4px";
            qryz_x.elements[qryz_i + 2].style.color = "#000000";
            qryz_x.elements[qryz_i + 2].style.border = "2px solid #000000";
        }
    }
}

function qryzShowChkComment() {
    var qryz_x = document.getElementById("qryz_frm");
    for (var qryz_i = 0; qryz_i < qryz_x.length; qryz_i++) {
        if (qryz_x.elements[qryz_i].type == 'checkbox') {
            var qryz_pollOption = qryz_x.elements[qryz_i].value;
            var qryz_pollOptionComment = qryz_pollOption.split(",", 2).pop(1);
            if (qryz_x.elements[qryz_i].checked) {
                if (qryz_pollOptionComment == "small") {
                    qryz_x.elements[qryz_i + 1].style.display = "inline";
                    qryz_x.elements[qryz_i + 1].focus();
                } else if (qryz_pollOptionComment == "large") {
                    qryz_x.elements[qryz_i + 2].style.display = "inline";
                    qryz_x.elements[qryz_i + 2].focus();
                }
            } else {
                qryz_x.elements[qryz_i + 1].style.display = "none";
                qryz_x.elements[qryz_i + 2].style.display = "none";
                qryz_x.elements[qryz_i + 1].blur();
                qryz_x.elements[qryz_i + 2].blur();
            }
            qryz_x.elements[qryz_i + 1].style.font = "12px arial, sans-serif";
            qryz_x.elements[qryz_i + 1].style.padding = "2px 4px 2px 4px";
            qryz_x.elements[qryz_i + 1].style.color = "#000000";
            qryz_x.elements[qryz_i + 1].style.border = "2px solid #000000";
            qryz_x.elements[qryz_i + 2].style.font = "12px arial, sans-serif";
            qryz_x.elements[qryz_i + 2].style.padding = "2px 4px 2px 4px";
            qryz_x.elements[qryz_i + 2].style.color = "#000000";
            qryz_x.elements[qryz_i + 2].style.border = "2px solid #000000";
        }
    }
}

function qryzCheckBoxTick(qryzChkID) {
    if (!(document.getElementById(qryzChkID).checked)) {
        document.getElementById(qryzChkID).checked = true;
    } else
        document.getElementById(qryzChkID).checked = false;
    qryzShowChkComment();
}

function qryzTextareaTick(qryzTxtID) {
    document.getElementById('qryz_btn_submit').disabled = false;
}

function qryzRadioTick(qryzRdoID, qryz_has_submit_btn) {
    if (!(document.getElementById(qryzRdoID).checked))
        document.getElementById(qryzRdoID).checked = true;
    else
        document.getElementById(qryzRdoID).checked = false; if (qryz_has_submit_btn) {
        qryzShowRdoComment();
        if (document.getElementById('qryz_btn_submit').style.display == 'none') {
            document.getElementById('qryz_btn_submit').style.display = 'block';
        }
    }
}

function qryzValidateYear(event) {
    var key = window.event ? event.keyCode : event.which;
    document.getElementById('qryz_btn_submit').disabled = false;
    if (event.keyCode == 8 || event.keyCode == 46 || event.keyCode == 37 || event.keyCode == 39) {
        return true;
    } else if (key < 48 || key > 57) {
        return false;
    } else return true;
}

function qryzSubmitBtnOnMouseOvr(qryz_bg_color, qryz_txt_color) {
    if (document.getElementById('qryz_btn_submit')) {
        document.getElementById('qryz_btn_submit').style.color = qryz_txt_color;
        document.getElementById('qryz_btn_submit').style.background = qryz_bg_color;
    }
}

function qryzSubmitBtnOnMouseOut(qryz_bg_color, qryz_txt_color) {
    if (document.getElementById('qryz_btn_submit')) {
        document.getElementById('qryz_btn_submit').style.color = qryz_txt_color;
        document.getElementById('qryz_btn_submit').style.background = qryz_bg_color;
    }
}

function qryzOptionOnMouseOvr(qryz_optn, qryz_bg_color, qryz_txt_color) {
    if (document.getElementById(qryz_optn.id)) {
        document.getElementById(qryz_optn.id).style.color = qryz_txt_color;
        document.getElementById(qryz_optn.id).style.background = qryz_bg_color;
    }
}

function qryzOptionOnMouseOut(qryz_optn, qryz_bg_color, qryz_txt_color) {
    if (document.getElementById('qryz_optn.id')) {
        document.getElementById('qryz_optn.id').style.color = qryz_txt_color;
        document.getElementById('qryz_optn.id').style.background = qryz_bg_color;
    }
}

function qryzAddClass(qryzElementId, qryzClassName) {
    var qryzElementClass = " " + qryzClassName;
    var qryz_d;
    qryz_d = document.getElementById(qryzElementId);
    qryz_d.className = qryz_d.className.replace(qryzElementClass, "");
    qryz_d.className = qryz_d.className + qryzElementClass;
}

function qryzRemoveClass(qryzElementId, qryzClassName) {
    var qryzElementClass = " " + qryzClassName;
    var qryz_d;
    qryz_d = document.getElementById(qryzElementId);
    var myClassName = qryz_d.className;
    if (myClassName != "" && myClassName != null && myClassName !== 'undefined')
        qryz_d.className = qryz_d.className.replace(qryzElementClass, "");
}

function qryzControlClasses(qryzSelectedLI, qryzClassName, qryzSelectedLinkID) {
    var qryzListArr = ['zero', '1st', '2nd', '3rd', '4th', '5th', '6th', '7th', '8th', '9th', '10th'];
    for (var qryz_i = 0; qryz_i <= 10; qryz_i++) {
        var qryzListElementID = document.getElementById('qryz_li' + qryzListArr[qryz_i]);
        qryzRemoveClass(qryzListElementID.id, qryzClassName);
    }
    var qryzSelectedListElementID = document.getElementById(qryzSelectedLI.id);
    qryzAddClass(qryzSelectedListElementID.id, qryzClassName);
    var qryz_li_text = document.getElementById(qryzSelectedLinkID).innerHTML;
    document.getElementById('qryz_hidden_rate').value = qryz_li_text;
}

function qryzDeleteLVC() {
    if (qryzReadCookie('question') != null)
        qryzEraseCookie('question');
    if (qryzReadCookie('qryz_survey_id') != null)
        qryzEraseCookie('qryz_survey_id');
    if (qryzReadCookie('qryz_cookie') != null)
        qryzEraseCookie('qryz_cookie');
    if (qryzReadCookie('qryz_slide_cookie') != null)
        qryzEraseCookie('qryz_slide_cookie');
    if (qryzReadCookie('qryz_sid') != null)
        qryzEraseCookie('qryz_sid');
    if (qryzReadCookie('qryz_qid') != null)
        qryzEraseCookie('qryz_qid');
    if (qryzReadCookie('qryz_prev_page') != null)
        qryzEraseCookie('qryz_prev_page');
    if (qryzReadCookie('qryz_stype') != null)
        qryzEraseCookie('qryz_stype');
}

function qryzNavigator() {
    var ua = navigator.userAgent,
        tem, M = ua.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || [];
    if (/trident/i.test(M[1])) {
        tem = /\brv[ :]+(\d+)/g.exec(ua) || [];
        return 'IE ' + (tem[1] || '');
    }
    if (M[1] === 'Chrome') {
        tem = ua.match(/\bOPR\/(\d+)/)
        if (tem != null) return 'Opera ' + tem[1];
    }
    M = M[2] ? [M[1], M[2]] : [navigator.appName, navigator.appVersion, '-?'];
    if ((tem = ua.match(/version\/(\d+)/i)) != null) M.splice(1, 1, tem[1]);
    return M.join(' ');
}

function getCookie(name) {
    var dc = document.cookie;
    var prefix = name + "=";
    var begin = dc.indexOf("; " + prefix);
    if (begin == -1) {
        begin = dc.indexOf(prefix);
        if (begin != 0) return null;
    } else {
        begin += 2;
        var end = document.cookie.indexOf(";", begin);
        if (end == -1) {
            end = dc.length;
        }
    }
    return unescape(dc.substring(begin + prefix.length, end));
}

function qryzVoted(qryz_qtype) {
    var voted = true;
    var qryz_element;
    var qryz_len = 0;
    if (qryz_qtype == 'text') {
        qryz_element = document.getElementById('qryz_TextArea');
        qryz_len = qryz_element.value.trim().length;
        if (qryz_len == 0) {
            voted = false;
            qryz_element.focus();
        }
    } else if (qryz_qtype == 'text-single') {
        qryz_element = document.getElementById('qryz_text-single');
        qryz_len = qryz_element.value.trim().length;
        if (qryz_len == 0) {
            voted = false;
            qryz_element.focus();
        }
    } else if (qryz_qtype == 'date') {
        qryz_element_m = document.getElementById('qryz_ddMonth');
        qryz_element_d = document.getElementById('qryz_ddDay');
        qryz_element_y = document.getElementById('qryz_inpYear');
        qryz_len_m = qryz_element_m.value.length;
        qryz_len_d = qryz_element_d.value.length;
        qryz_len_y = qryz_element_y.value.length;
        if (qryz_len_m == 0 || qryz_len_d == 0 || qryz_len_y < 4) {
            voted = false;
            (qryz_len_m == 0 ? qryz_element_m.focus() : (qryz_len_d == 0 ? qryz_element_d.focus() : qryz_element_y.focus()));
        }
    } else if (qryz_qtype == 'nps') {
        qryz_element = document.getElementById('qryz_hidden_rate');
        qryz_len = qryz_element.value.trim().length;
        if (qryz_len == 0) {
            voted = false;
        }
    } else if (qryz_qtype == 'checkbox') {
        qryz_element = document.getElementsByName('qryz_pollOption[]');
        var checked = false;
        for (var a = 0; a < qryz_element.length; a++) {
            if (qryz_element[a].checked) {
                checked = true;
                break;
            }
        }
        if (!checked) {
            voted = false;
        }
    } else if (qryz_qtype == 'radio') {
        qryz_element = document.getElementsByName('qryz_pollRadio[]');
        var checked = false;
        for (var a = 0; a < qryz_element.length; a++) {
            if (qryz_element[a].checked) {
                checked = true;
                break;
            }
        }
        if (!checked) {
            voted = false;
        }
    }
    return voted;
}


function qryzCtrlSendBtn(qryzQuestionType, qryzBgColor, qryzOptnColor, qryzTxtColor, qryzBtnColor){
    var qryzElement = document.getElementById('qryz_btn_submit');
    if (qryzVoted(qryzQuestionType)){
        qryzElement.setAttribute('style', 'color:'+qryzTxtColor+' !important;background:'+qryzBtnColor+' !important;cursor:pointer;border: 1px solid '+qryzOptnColor+ ' !important');
    }else{
        qryzElement.setAttribute('style', 'color:'+qryzOptnColor+' !important;background:'+qryzBgColor+' !important;cursor:not-allowed;border: 1px solid '+qryzOptnColor+ ' !important');
    }
}

function qryzAutosend(event, qryz_uid, qryz_qtype, qryz_qdisplay, qryz_sid, qryz_shw_aftr, qryz_srvr_name, qryz_qq, qryz_step_no) {
    if (event.keyCode == 13) {
        qryzSubmit(qryz_uid, qryz_qtype, qryz_qdisplay, qryz_sid, qryz_shw_aftr, qryz_srvr_name, qryz_qq, qryz_step_no);
    }
}

function mobilecheck() {
    var check = false;
    (function (a) {
        if (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i.test(a) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0, 4))) check = true
    })(navigator.userAgent || navigator.vendor || window.opera);
    return check;
}

function qryz_resize() {
    var qryz_client_height = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
    var qryz_cdst_id = document.getElementById('qryz_cdst');
    if (qryz_cdst_id != null) {
        qryz_cdst_id.style.maxHeight = (qryz_client_height - 50) + 'px';
    }
}

function qryz_addcss() {
    var path = qryzServer()+"/survey/css/";
    var style = document.createElement('link');
    style.rel = 'stylesheet';
    style.type = 'text/css';
    style.href = path + 'style_survey_v3.css?v=3.0';
    document.getElementsByTagName('head')[0].appendChild(style);
}

function qryzInArray(needle, haystack) {
    var arr_len = haystack.length;
    for (var i = 0; i < arr_len; i++) {
        if (haystack[i] == needle) return true;
    }
    return false;
}

function qryzGetIdentities(qrz) {
    var vIdentity;
    var qryzIdentity = new Array();
    var arrValidIdentities = new Array('name', 'username', 'email', 'firstname', 'lastname');
    var identity, identityValue;
    var qrz_api_code;
    var qrz_api_param;
    var arr = new Array();
    for (var qrzLen = 0; qrzLen < qrz.length; qrzLen++) {
        if (qrz[qrzLen].indexOf('QrzTrackLoggedIn') > -1) {
            var k;
            var signedInArr = new Array();
            signedInArr = qrz[qrzLen][1];
            var arrKeys = Object.keys(signedInArr);
            for (k in arrKeys) {
                identity = arrKeys[k];
                if (qryzInArray(identity, arrValidIdentities)) {
                    identityValue = signedInArr[identity];
                    qryzIdentity.push(identity + ': ' + identityValue);
                }
            }
            vIdentity = qryzIdentity.join(', ');
            qryzCreateCookie('qryz_Guest', vIdentity, 1);
        } else if (qrz[qrzLen].indexOf('QrzTrackSubmit') > -1) {
            var formID = qrz[qrzLen][1];
            if (document.getElementById(formID)) {
                document.getElementById(formID).onsubmit = function () {
                    var arr_len = document.getElementById(formID).elements.length;
                    for (var a = 0; a < arr_len; a++) {
                        identity = document.getElementById(formID)[a].name;
                        if (qryzInArray(identity, arrValidIdentities)) {
                            identityValue = document.getElementById(formID)[a].value;
                            qryzIdentity.push(identity + ': ' + identityValue);
                        }
                    }
                    vIdentity = qryzIdentity.join(', ');
                    qryzCreateCookie('qryz_Guest', vIdentity, 1);
                }
            }
        }
    }
}

function qryzSetPostn(qryz_postn) {
    var qryz = document.getElementById('qryz_plks');
    if (qryz_postn == 'left') {
        qryz.style.left = "20px";
    } else if (qryz_postn == 'right') {
        qryz.style.right = "20px";
    }
}

function qryzHideSurvey(qryzQType, ol) {
    if (qryzQType == 'Thank You Message') {
        document.getElementById("qryz_plks").style.display = 'none';
        qryzRemoveOverlay(ol);
    }
}

function qryzShowMessage(qryzRedirectURL) {
    if (qryzRedirectURL == '') {
        return true;
    }
    return false;
}

function qryzCondition(qryz_target_behavior, qryz_host, qryz_rfr, qryz_domain_condition, qryz_domain_refer, qryz_search_engine) {
    var any_condition = (qryz_target_behavior == 'any');
    var direct_condition = ((qryz_target_behavior == 'direct') && ((qryz_host == qryz_rfr) || (qryz_rfr == null)));
    var domain_condition_1st = (qryz_target_behavior == 'domain' && (qryz_domain_condition == 'is not') && (qryz_domain_refer != qryz_rfr));
    var domain_condition_2nd = (qryz_target_behavior == 'domain' && (qryz_domain_condition == 'is') && (qryz_domain_refer == qryz_rfr));
    var search_condition_any = (qryz_target_behavior == 'search' && (qryz_search_engine == 'any'));
    var search_condition_g = (qryz_target_behavior == 'search' && (qryz_search_engine == 'google') && (qryz_rfr.indexOf("google") >= 0));
    var search_condition_y = (qryz_target_behavior == 'search' && (qryz_search_engine == 'yahoo') && (qryz_rfr.indexOf("yahoo") >= 0));
    var search_condition_b = (qryz_target_behavior == 'search' && (qryz_search_engine == 'bing') && (qryz_rfr.indexOf("bing") >= 0));
    var search_condition_a = (qryz_target_behavior == 'search' && (qryz_search_engine == 'ask') && (qryz_rfr.indexOf("ask") >= 0));
    var search_engines = (qryz_rfr.indexOf("google") >= 0) || (qryz_rfr.indexOf("yahoo") >= 0) || (qryz_rfr.indexOf("bing") >= 0) || (qryz_rfr.indexOf("ask") >= 0);
    if (any_condition) {
        return true;
    } else if (direct_condition) {
        return true;
    } else if (domain_condition_1st || domain_condition_2nd) {
        return true;
    } else if (search_condition_g || search_condition_y || search_condition_b || search_condition_a) {
        return true;
    } else if (search_condition_any && search_engines) {  
        return true;
    } else {
        return false;
    }
}

function qryzNotSubmitForm(event) {
    event.preventDefault();
}

function qryz_STN(qryzStep) {
    var qryz_step_no = qryzReadCookie('qryz_Step');
    return qryz_step_no;
}

function qryzLinkColor(qryz_link_color) {
    var qryz_link = document.getElementById('qryz_s');
    if (qryz_link != null) {
        var qryz_link_a = qryz_link.getElementsByTagName('a');
        for (var i = 0; i < qryz_link_a.length; i++) {
            qryz_link_a[i].style.color = qryz_link_color;
        }
    }
}

function qryzAddListener(element, action, callback) {
    if (element.addEventListener) element.addEventListener(action, callback);
    else if (element.attachEvent) element.attachEvent('on' + action, callback);
}

function qryzRemoveListener(obj, evt, fn) {
    if (obj.removeEventListener) {
        obj.removeEventListener(evt, fn, false);
    }
}

function qryzOverlay(plks, ol) {
    if (document.getElementById(plks)) {
        if (document.getElementById(ol)){
            var  y = document.getElementById(ol);
            document.body.removeChild(y);
        }

       var  z = document.createElement('div');
       z.id = ol;
       z.style.width = "100%";
       z.style.height = "100%";
       z.style.top = "0px";
       z.style.left = "0px";
       z.style.position = "fixed";
       z.style.display = "block";
       z.style.opacity = "0.7";
       z.style.backgroundColor = "#000000";
       z.style.zIndex = "99998";
       z.onclick = function(){
            document.body.removeChild(z);
       };
       document.body.appendChild(z);
    }
}

function qryzRemoveOverlay(ol) {
    if (document.getElementById(ol)) {
       var  z = document.getElementById(ol);
       document.body.removeChild(z);
    }
}

function qryzGVP(s) {
    var y = new Array();
    if (s.indexOf('-') > -1){
        y = s.split('-');
         var z = new Array(), y , w, x, v;
         w = document.URL;
         v = w.replace('http://www.', '');
         v = v.replace('https://www.', '');
         v = v.replace('http://', '');
         v = v.replace('https://', '');
         if (qryzReadCookie('qryz_'+y[0]+'Vp') && qryzReadCookie('qryz_'+y[0]+'Vp') != null){
            x = qryzReadCookie('qryz_'+y[0]+'Vp');
            z = x.split("#####");
         }
         var hasViewed = false;
         for (var a = 0; a < z.length; a++){
            if (v == z[a]){
                hasViewed = true;
                break;
            }
         }

         //add cookie data as visitor visits pages in your domain
         if (!hasViewed)
            qryzCreateCookie('qryz_'+y[0]+'Vp', (x ? x : '') + v + "#####", y[1]);
    }
}

function qryzStopAfterXPop(od, popArr, sid, qid, sa){
  if (popArr[0] == '1' && od != 'one_shot'){
    var displayLimit = parseInt(popArr[1]);
    var xpop = qryzReadCookie("qryz_XPop" + sid);
    var popCount = 1;
    var fqSID = null;
    fqSID = qryzReadCookie("qryz_Fqs" + sid);

    if (qryzIsFirstQuestionShown(fqSID, qid)) {
      if (xpop != null) {
        popCount = parseInt(xpop) + 1;
      }
      qryzCreateCookie("qryz_XPop" + sid, popCount, 500000);
    }
    if (popCount >= displayLimit) {
      if (sa == 0)
        qryzCreateCookie("qryz_Vtd" + sid, sid, 500000);
      else
        qryzCreateCookie("qryz_Vtd" + sid, sid, sa);
    }
  }
}

function qryzStopAfterXPopNUR(od, popArr, sid, qid, sa){
  if (popArr[0] == '1' && od != 'one_shot'){
    var displayLimit = parseInt(popArr[1]);
    var popCount = qryzReadCookie("qryz_XPop" + sid) == null ? 0 : parseInt(qryzReadCookie("qryz_XPop" + sid));
    var fqSID = null;
    fqSID = qryzReadCookie("qryz_Fqs" + sid);

    if (qryzIsFirstQuestionShown(fqSID, qid)) {;
      if (popCount >= displayLimit) {
        if (sa == 0)
          qryzCreateCookie("qryz_Vtd" + sid, sid, 500000);
        else
          qryzCreateCookie("qryz_Vtd" + sid, sid, sa);
      }
      popCount += 1;
      qryzCreateCookie("qryz_XPop" + sid, popCount, 500000);
    }
  }
}

function qryzStopAfterXPopWResponse(od, popArr, sid, qid, sa){
  var qryzArr = new Array();
  qryzArr = popArr.split('-');
  if (qryzArr[0] == '1' && od != 'one_shot'){
    var displayLimit = parseInt(qryzArr[1]);
    var xpop = qryzReadCookie("qryz_XPop" + sid);
    var popCount = 1;
    var qryzFQ = null;
    qryzFQ = qryzReadCookie("qryz_Fqs" + sid);

    if (qryzIsFirstQuestionShown(qryzFQ, qid)) {
      if (xpop != null) {
        popCount = parseInt(xpop) + 1;
      }
      qryzCreateCookie("qryz_XPop" + sid, popCount, 500000);
    }
    if (popCount >= displayLimit) {
      qryzCreateCookie("qryz_Vtd" + sid, sid, sa == 0 ? 500000 : sa);
    }
  }
}

function qryzStopAfterXPopWoResponse(od, popArr, sid, qid, sa){
  var qryzArr = new Array();
  qryzArr = popArr.split('-');
  if (qryzArr[0] == '1' && od != 'one_shot'){
    var displayLimit = parseInt(qryzArr[1]);
    var popCount = qryzReadCookie("qryz_XPop" + sid) == null ? 0 : parseInt(qryzReadCookie("qryz_XPop" + sid));
    var qryzFQ = null;
    qryzFQ = qryzReadCookie("qryz_Fqs" + sid);
    if (qryzIsFirstQuestionShown(qryzFQ, qid)) {
      if (popCount >= displayLimit) {
        qryzCreateCookie("qryz_Vtd" + sid, sid, sa == 0 ? 500000 : sa);
      }
      popCount += 1;
      qryzCreateCookie("qryz_XPop" + sid, popCount, 500000);
    }
  }
}

function qryzDie(qryz_survey_id, qryz_optns_display, qryz_question_id, qryz_sa, qryz_xpops, qryz_cki_name, qryz_cki_value, qryz_tag) {
//*** SET WHEN SURVEY WILL BE LIVE AGAIN AFTER GETTING A QUESTION ANSWERED WITH THE SET EXPECTED ANSWER
if (qryzReadCookie("qryz_"+qryz_survey_id+"AQS")){
  var qryzSAArr = new Array();
  qryzSAArr = qryzReadCookie("qryz_"+qryz_survey_id+"AQS").split(":");
  qryz_sa = qryzSAArr[1];
}

if (qryz_tag == 'completed'){
  qryzCreateCookie(qryz_cki_name, qryz_cki_value, qryz_sa == 0 ? 500000 : qryz_sa);
}
else if (qryz_tag.indexOf('ntimes_shown') > -1){
  var qryzArr = new Array();
  qryzArr = qryz_tag.split(':');
  if (qryzArr[1] == 'wour')
    qryzStopAfterXPopWoResponse(qryz_optns_display, qryz_xpops, qryz_survey_id, qryz_question_id, qryz_sa);
  else if (qryzArr[1] == 'wur')
    qryzStopAfterXPopWResponse(qryz_optns_display, qryz_xpops, qryz_survey_id, qryz_question_id, qryz_sa);
}
else{
    var qryzVtdCkiStr =  qryz_survey_id;
    if (qryzReadCookie('qryz_Api')){
        var api = qryzReadCookie('qryz_Api');
        var arr = api.split(':');
        if (arr[0] == 'tag')
            qryzVtdCkiStr = arr[1];
    }
    var qryz_voted_cki = qryzReadCookie("qryz_Vtd" + qryzVtdCkiStr);
    if (qryz_voted_cki != null)
        return;// finish this function if survey voted cookie exists
    var qryz_prev_page = qryzReadCookie("qryz_Ppg" + qryz_survey_id);
    var qryz_next_page = document.URL;
    var arrPop = new Array();
    arrPop = qryz_xpops.split("-");
    var qryz_xpop = qryzReadCookie("qryz_XPop" + qryz_survey_id);
    var qryz_fqs = null;
    qryz_fqs = qryzReadCookie("qryz_Fqs" + qryz_survey_id);
    if (qryz_optns_display == "one_shot") {
        if (qryz_fqs != null && qryz_voted_cki == null) {
            if (qryzIsPageReloaded(qryz_prev_page) && qryzIsFirstQuestionShown(qryz_fqs, qryz_question_id)) {
              qryzCreateCookie(qryz_cki_name, qryz_cki_value, qryz_sa == 0 ? 500000 : qryz_sa);
            }
        }
        if (qryz_xpop != null && arrPop[0] == '0') {
            qryzEraseCookie("qryz_XPop" + qryz_survey_id);
        }
    }
    else if (qryz_optns_display == "until_response") {
        var qryz_response = qryzReadCookie("qryz_Rps" + qryz_survey_id);
        if (qryz_fqs != null && qryz_voted_cki == null) {
            if (qryzIsQuestionAnswered(qryz_response) && qryzIsPageReloaded(qryz_prev_page) && qryzIsFirstQuestionShown(qryz_fqs, qryz_question_id)) {
              qryzCreateCookie(qryz_cki_name, qryz_cki_value, qryz_sa == 0 ? 500000 : qryz_sa);
            }
        }
        if (qryz_xpop != null && arrPop[0] == '0') {
            qryzEraseCookie("qryz_XPop" + qryz_survey_id);
        }
    }
    else if (qryz_optns_display == "persistent") {
        if (qryz_xpop != null && arrPop[0] == '0') {
            qryzEraseCookie("qryz_XPop" + qryz_survey_id);
        }
    }
  }
}