/*переделать на jquery*/

function imgSourceChanged() {
	
	if (document.getElementById("from_pc").checked == true) {
		document.getElementById("img_to_process_url").value = null;
		document.getElementById("img_to_process_url").disabled = true;
		document.getElementById("img_to_process_file").disabled = false;
	}
	
	if (document.getElementById("by_url").checked == true) {
		document.getElementById("img_to_process_file").value = null;
		document.getElementById("img_to_process_url").disabled = false;
		document.getElementById("img_to_process_file").disabled = true;
	}
	
}

function tilesSourceChanged() {
	
	if (document.getElementById("template").checked == true) {
		document.getElementById("tiles_zip").value = null;
		document.getElementById("tiles_zip").disabled = true;
		document.getElementById("tiles_tag").disabled = false;
	}
	
	if (document.getElementById("upload").checked == true) {
		document.getElementById("tiles_tag").value = null;
		document.getElementById("tiles_zip").disabled = false;
		document.getElementById("tiles_tag").disabled = true;
	}
	
}