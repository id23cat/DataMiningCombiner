function insertFunctionsTree(treeData, actionUrl, step) {
	 $('#tree').treeview({
     	data: treeData,
     	onNodeSelected: function(event, node) {
     		$.ajax({
     			url: actionUrl + "/" + step,
     			type: "POST",
     			contentType : 'application/json; charset=utf-8',
     			data: JSON.stringify(node),
     			success: function(){
     				$("#selected").text(node.text + ": " +node.id + "; URL=" + actionUrl);
     			}
     		})
     	}
     });
}