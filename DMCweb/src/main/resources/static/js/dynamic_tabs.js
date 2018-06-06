function addStepFunction(tabId, funTreeList, urlSelectFunction) {
	console.log(tabId);
	console.log(funTreeList);
	console.log(urlSelectFunction);
	var tabHead = '\
    	<li class="fixed"> \
			<a data-toggle="tab" role="tab" href="#tab_ID_"> \
				Tab _ID_\
				<button class="close" type="button" title="Remove this page">×</button> \
			</a> \
		</li> \
    ';
	var tabContent = '\
		<div class="tab-pane fade" id="tab_ID_"> \
			<div class="row"> \
				<div class="col-sm-4" . \
					<div id="tree_ID_"></div> \
				</div> \
				<div class="col-sm-4" . \
					<div id="selected_ID_"></div> \
					<div id="details_ID_"></div> \
				</div> \
			</div> \
		</div> \
	';
	return function() {
		$('#tab-list').append( $(tabHead.replace(/_ID_/g, tabId)) );
		$('#tab-content').append( $(tabContent.replace(/_ID_/g, tabId)) );
		
		insertFunctionsTree( funTreeList, urlSelectFunction, tabId);
		tabId ++;
	}
}

