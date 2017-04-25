//displaying source code with syntax highlighting 
//prism
   function getcode(url,id){
     $.ajax({
        url: url,
        dataType: "text",
        success: function (data)
        {
          var encoded = data.replace(/[\u00A0-\u9999<>\&]/gim, function(i) {
                 return '&#'+i.charCodeAt(0)+';';
          });
          $(id).html(encoded); 
          Prism.highlightElement($(id)[0]);
        }
      });

  }

$(function() {
  
  getcode("code.js","#source-code");
  if ($('#grammar').length > 0) 
    getcode("../../data/haiku.yaml", "#grammar");
  
});  
