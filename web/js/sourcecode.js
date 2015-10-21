//displaying source code with syntax highlighting 

   function getcode(url,id){
         $.ajax({
        url: url,
        dataType: "text",
        success: function (data)
        {
          $(id).html(data);
          Prism.highlightElement($(id)[0]);
        }
      });
  }

$(function() {
  getcode("code.js","#source-code");
});  