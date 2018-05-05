$(function(){
	$('div.productlist').each(function(){
		var $this = $(this), 
			$viewBox = $this.find('.j-view-box'),
			$btns = $this.find('[name=viewBtn]');
		
		$btns.click(function(){
			var $btn = $(this), view = $btn.attr('rel');
			
			$btns.each(function(){
				$(this).removeClass($(this).attr('value')+'_btnselected');
			});
			
			$btn.addClass(view+'_btnselected');
			$viewBox.removeClass('listview gridview textview').addClass(view);
		});
	});
	
	// Ajax链接
	$("a[data-ajax]").each(function(){
		$(this).click(function(event){
			var $this = $(this);
			var rel = $this.attr('data-ajax') || "mainContent";

			$("#"+rel).load($this.attr("href"));
			
			event.preventDefault();
		});
	});

});


function validateCallback(form, callback) {
	var $form = $(form);
	
	$.ajax({
		type: form.method || 'POST',
		url:$form.attr("action"),
		data:$form.serializeArray(),
		dataType:"json",
		cache: false,
		success: callback || ajaxDone
	});
	
	return false;
}

function iframeCallback(form, callback){
	var $form = $(form), $iframe = $("#callbackframe");
	if(!$form.valid()) {return false;}

	if ($iframe.size() == 0) {
		$iframe = $("<iframe id='callbackframe' name='callbackframe' src='about:blank' style='display:none'></iframe>").appendTo("body");
	}
	if(!form.ajax) {
		$form.append('<input type="hidden" name="ajax" value="1" />');
	}
	form.target = "callbackframe";
	
	_iframeResponse($iframe[0], callback || ajaxDone);
}
function _iframeResponse(iframe, callback){
	var $iframe = $(iframe), $document = $(document);
	
	$document.trigger("ajaxStart");
	
	$iframe.bind("load", function(event){
		$iframe.unbind("load");
		$document.trigger("ajaxStop");
		
		if (iframe.src == "javascript:'%3Chtml%3E%3C/html%3E';" || // For Safari
			iframe.src == "javascript:'<html></html>';") { // For FF, IE
			return;
		}

		var doc = iframe.contentDocument || iframe.document;

		// fixing Opera 9.26,10.00
		if (doc.readyState && doc.readyState != 'complete') return; 
		// fixing Opera 9.64
		if (doc.body && doc.body.innerHTML == "false") return;
	   
		var response;
		
		if (doc.XMLDocument) {
			// response is a xml document Internet Explorer property
			response = doc.XMLDocument;
		} else if (doc.body){
			try{
				response = $iframe.contents().find("body").text();
				response = jQuery.parseJSON(response);
			} catch (e){ // response is html document or plain text
				response = doc.body.innerHTML;
			}
		} else {
			// response is a xml document
			response = doc;
		}
		
		callback(response);
	});
}

function ajaxDone(json){
	if(json["statusCode"] == 300) {
		alert(json.message);
	} else {
		alert(json.message + " OK");
	};
}
